package gov.nist.hit.ds.simServlet;

import gov.nist.hit.ds.actorSimFactory.ActorSimFactory;
import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.http.environment.HttpEnvironment;
import gov.nist.hit.ds.http.parser.HttpHeader.HttpHeaderParseException;
import gov.nist.hit.ds.http.parser.ParseException;
import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.simSupport.datatypes.SimEndpoint;
import gov.nist.hit.ds.simSupport.engine.SimChainLoaderException;
import gov.nist.hit.ds.simSupport.engine.SimEngineException;
import gov.nist.hit.ds.simSupport.event.EventBuilder;
import gov.nist.hit.ds.simSupport.validators.SimEndpointParser;
import gov.nist.hit.ds.soapSupport.core.Endpoint;
import gov.nist.hit.ds.soapSupport.core.FaultCode;
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment;
import gov.nist.hit.ds.soapSupport.core.SoapFault;
import gov.nist.hit.ds.soapSupport.core.SoapResponseGenerator;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.utilities.io.Io;
import gov.nist.hit.ds.utilities.xml.Parse;
import gov.nist.hit.ds.xdsException.ExceptionUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * Servlet to service simulator input transactions.
 * @author bill
 *
 */
public class SimServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(SimServlet.class);
	ServletConfig config;
	File simDbDir;
	String faultSent = null;
	boolean initialized = false;
	Event event = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.config = config;
		BasicConfigurator.configure();
		logger.info("SimServlet initializing");

		// This should do all the primary initialization in case it was not done elsewhere
		try {
//			File warHome = new File(config.getServletContext().getRealPath("/"));
//			Installation.installation().setWarHome(warHome);
			Installation.installation().initialize();
			logger.info("External Cache is found at <" + Installation.installation().getExternalCache().toString() + ">");
		} catch (Exception e) {
			logger.fatal("Error initializing the toolkit.\n" + ExceptionUtil.exception_details(e));
			return;
			//			throw new ServletException("Error initializing the toolkit.", e);
		}
		// This assumes that the toolkit has been configured before this Servlet
		// is initialized.
		try {
			simDbDir = Installation.installation().propertyServiceManager().getSimDbDir();
			initSimEnvironment();
		} catch (Exception e) {
			logger.fatal("Error initializing Simulator Environment.\n" + ExceptionUtil.exception_details(e));
			return;
			//			throw new ServletException("Error initializing Simulator Environment.", e);
		} 
		try {
			initRepositoryEnvironment();
		} catch (Exception e) {
			logger.fatal("Error initializing Repository Environment.\n" + ExceptionUtil.exception_details(e));
			return;
			//			throw new ServletException("Error initializing Repository Environment.", e);
		}
		initialized = true;
	}

	/**
	 * Initialize the simulator environment. Called by init() and unit tests.  
	 * @throws SimEngineException 
	 */
	public void initSimEnvironment() throws FileNotFoundException, SecurityException, IllegalArgumentException, IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, SimChainLoaderException, SimEngineException {
		new ActorSimFactory().loadSims();
	}

	public void initRepositoryEnvironment() throws RepositoryException {
		Configuration.configuration();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String uri  = request.getRequestURI().toLowerCase();

		// This is being set up early in case we need to generate a SOAPFault
		HttpEnvironment httpEnv = new HttpEnvironment().setResponse(response);
		SoapEnvironment soapEnv = new SoapEnvironment(httpEnv);

		if (!initialized) {
			sendSoapFault(soapEnv, FaultCode.Receiver, "Toolkit service did not initialize correctly. Check the log files for details.");
			return;
		}

		// Parse endpoint to discover what simulator is the target of the request.
		SimEndpoint simEndpoint = parseEndpoint(uri, soapEnv);
		if (simEndpoint == null)
			return;

		event = buildEvent(soapEnv, simEndpoint);
		if (event == null)
			return;

		TransactionType transType = TransactionType.find(simEndpoint.getTransaction());
		if (transType == null) {
			sendSoapFault(soapEnv, FaultCode.Sender, "Unknown transaction code [" + simEndpoint.getTransaction() + "]");
			return;
		}
		soapEnv.setExpectedRequestAction(transType.getRequestAction());
		soapEnv.setResponseAction(transType.getResponseAction());

		handleSimulatorInputTransaction(request, soapEnv, simEndpoint, new Endpoint().setEndpoint(uri), event);

		returnEventResponse(httpEnv, soapEnv); 
	}

	SimEndpoint parseEndpoint(String uri, SoapEnvironment soapEnv) {
		SimEndpoint simEndpoint;
		try {
			simEndpoint = new SimEndpointParser().parse(uri);
		} catch (Exception e) {
			sendSoapFault(soapEnv, FaultCode.EndpointUnavailable, "Cannot parse endpoint <" + uri + "> " + e.getMessage());
			simEndpoint = null;
		}
		return simEndpoint;
	}

	void returnEventResponse(HttpEnvironment httpEnv,
			SoapEnvironment soapEnv) {
		try {
			Event event = (Event)httpEnv.getEventLog();
			String responseBodyString = event.getInOutMessages().getResponse();
			SoapResponseGenerator soapGen = new SoapResponseGenerator(soapEnv, Parse.parse_xml_string(responseBodyString));
			String responseString = soapGen.send();
			event.getInOutMessages().putResponse(responseString);
		} catch (Exception e) {
			logger.error("Error generating response: \n" + ExceptionUtil.exception_details(e));
			sendSoapFault(
					soapEnv, 
					FaultCode.Receiver,
					"Error generating response: \n" + ExceptionUtil.exception_details(e));
		}
	}

	public void handleSimulatorInputTransaction(HttpServletRequest request,
			SoapEnvironment soapEnv, SimEndpoint simEndpoint, Endpoint endpoint, Event event) {

		soapEnv.getHttpEnvironment().setEventLog(event);
		soapEnv.setEndpoint(endpoint);
		request.setAttribute("Event", event); // SimServletFilter needs this to log response as it goes out on the wire

		// Make the input message available to the SimChain
		try {
			logRequest(request, event, simEndpoint.getActor(), simEndpoint.getTransaction());
		} catch (Exception e) {
			logger.error("Internal error logging request message", e);
			sendSoapFault(soapEnv, FaultCode.Receiver, "Internal error logging request message: " + e.getMessage());
			return;
		} 

		// Find the correct SimChain and run it.  The SimChain selected by a combination of
		// Actor and Transaction codes.  These codes came from the endpoint used to contact this 
		// Servlet.
		try {
			new ActorSimFactory().run(simEndpoint.getActor() + "^" + simEndpoint.getTransaction(), soapEnv, event);
		} catch (SoapFaultException sfe) {
			logger.info("SOAPFault: " + sfe.getMessage());
			sendSoapFault(soapEnv, sfe);
		} catch (Exception e) {
			logger.error("SOAPFault: " + ExceptionUtil.exception_details(e));
			sendSoapFault(
					soapEnv,
					new SoapFaultException(
							null,
							FaultCode.Receiver,
							"Problem with SimChain definition for Actor (" + simEndpoint.getActor() 
							+ ") and Transaction (" + simEndpoint.getTransaction() + ") " +
							ExceptionUtil.exception_details(e)
							)
					);
		} catch (Throwable e) {
			logger.error("SOAPFault: " + ExceptionUtil.exception_details(e));
			sendSoapFault(
					soapEnv,
					new SoapFaultException(
							null,
							FaultCode.Receiver,
							"Problem with SimChain definition for Actor (" + simEndpoint.getActor() 
							+ ") and Transaction (" + simEndpoint.getTransaction() + ") " +
							ExceptionUtil.exception_details(e)
							)
					);
		}
	}

	private Event buildEvent(SoapEnvironment soapEnv, SimEndpoint simEndpoint) {
		Event event = null;
		try {
			event = new EventBuilder().buildEvent(simEndpoint.getSimId(), ActorType.findActor(simEndpoint.getActor()).getShortName(), simEndpoint.getTransaction());
//			SimDb db = new SimDb(simEndpoint.getSimId());
//			SimId simId = simEndpoint.getSimId();
//			RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
//			Repository repos = fact.createNamedRepository(
//					"Event_Repository", 
//					"Event Repository", 
//					new SimpleType("simEventRepository"),               // repository type
//					ActorType.findActor(simEndpoint.getActor()).getShortName() + "-" + simId    // repository name
//					);
//			Asset eventAsset = repos.createAsset(
//					db.nowAsFilenameBase(), 
//					simEndpoint.getTransaction() + " Event", 
//					new SimpleType("simEvent"));
//			event = new Event(eventAsset);
		} catch (Exception e) {
			logger.error("Internal error initializing simulator environment", e);
			sendSoapFault(soapEnv, FaultCode.Receiver, "Internal error initializing simulator environment: " + e.getMessage());
		}
		return event;
	}
	

	/**
	 * Log the incoming request in the SimDb.
	 * @param request - HttpRequest
	 * @param db - SimDb section created for this event
	 * @param actor - actor parsed from endpoint
	 * @param transaction - transaction parsed from endpoint
	 * @return reference to this event in SimDb
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws HttpHeaderParseException
	 * @throws ParseException
	 * @throws RepositoryException 
	 */
	void logRequest(HttpServletRequest request, Event event, String actor, String transaction)
			throws FileNotFoundException, IOException, HttpHeaderParseException, ParseException, RepositoryException {
		StringBuffer buf = new StringBuffer();
		Map<String, String> headers = new HashMap<String, String>();

		buf.append(request.getMethod() + " " + request.getRequestURI() + " " + request.getProtocol() + "\r\n");
		for (@SuppressWarnings("unchecked")
		Enumeration<String> en=request.getHeaderNames(); en.hasMoreElements(); ) {
			String name = en.nextElement();
			String value = request.getHeader(name);
			if (name.equals("Transfer-Encoding"))
				continue;  // log will not include transfer encoding so don't include this
			headers.put(name.toLowerCase(), value);
			buf.append(name).append(": ").append(value).append("\r\n");
		}

		buf.append("\r\n");

		// Log the request header and body in the SimDb event
		if (event!=null) {
			event.getInOutMessages().putRequestHeader(buf.toString());
			event.getInOutMessages().putRequestBody(Io.getBytesFromInputStream(request.getInputStream()));
		}
	}

	void sendSoapFault(SoapEnvironment soapEnv, FaultCode faultCode, String reason) {
		sendSoapFault(
				soapEnv, 					
				new SoapFaultException(
						null,      // No error recorder established to allow capture to logs
						faultCode,
						new ErrorContext(reason)));
		try {
			if (event != null)
				event.getFault().add(getFaultSent());
		} catch (RepositoryException e) {
			logger.error(e);
		}
	}

	void sendSoapFault(SoapEnvironment soapEnv, SoapFaultException sfe) {

		try {
			faultSent = new SoapFault(soapEnv, sfe).send();
		}  catch (Exception ex) {
			logger.error("Error sending SOAPFault", ex);
		}
	}

	/**
	 * This is only used by unit tests to determine if a soap fault was returned.
	 * @return
	 */
	public String getFaultSent() { return faultSent; }

}
