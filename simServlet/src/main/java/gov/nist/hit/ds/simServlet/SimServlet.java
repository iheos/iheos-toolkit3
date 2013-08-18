package gov.nist.hit.ds.simServlet;

import gov.nist.hit.ds.actorSimFactory.ActorSimFactory;
import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.http.environment.HttpEnvironment;
import gov.nist.hit.ds.http.parser.HttpHeader;
import gov.nist.hit.ds.http.parser.HttpHeader.HttpHeaderParseException;
import gov.nist.hit.ds.http.parser.ParseException;
import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.simSupport.datatypes.SimEndpoint;
import gov.nist.hit.ds.simSupport.engine.SimChainLoaderException;
import gov.nist.hit.ds.simSupport.engine.SimEngineSubscriptionException;
import gov.nist.hit.ds.simSupport.sim.SimDb;
import gov.nist.hit.ds.simSupport.validators.SimEndpointParser;
import gov.nist.hit.ds.soapSupport.core.Endpoint;
import gov.nist.hit.ds.soapSupport.core.FaultCode;
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment;
import gov.nist.hit.ds.soapSupport.core.SoapFault;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.utilities.io.Io;
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

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.config = config;
		logger.info("SimServlet initialized");
		
		// This assumes that the toolkit has been configured before this Servlet
		// is initialized.
		File warHome = new File(config.getServletContext().getRealPath("/"));
		Installation.installation().setWarHome(warHome);
		simDbDir = Installation.installation().propertyServiceManager().getSimDbDir();
		try {
			initSimEnvironment();
		} catch (Exception e) {
			logger.fatal("Error initializing Simulator Environment.\n" + ExceptionUtil.exception_details(e));
			throw new ServletException("Error initializing Simulator Environment.", e);
		} 
		try {
			initRepositoryEnvironment();
		} catch (RepositoryException e) {
			logger.fatal("Error initializing Repository Environment.\n" + ExceptionUtil.exception_details(e));
			throw new ServletException("Error initializing Repository Environment.", e);
		}
	}
	
	/**
	 * Initialize the simulator environment. Called by init() and unit tests.  
	 */
	public void initSimEnvironment() throws FileNotFoundException, SecurityException, IllegalArgumentException, IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, SimEngineSubscriptionException, SimChainLoaderException {
		new ActorSimFactory().loadSims();
	}
	
	public void initRepositoryEnvironment() throws RepositoryException {
			new Configuration(Installation.installation().getRepositoryRoot());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String uri  = request.getRequestURI().toLowerCase();

		// This is being set up early in case we need to generate a SOAPFault
		HttpEnvironment httpEnv = new HttpEnvironment().setResponse(response);
		SoapEnvironment soapEnv = new SoapEnvironment(httpEnv);

		// Parse endpoint to discover what simulator is the target of the request.
		SimEndpoint simEndpoint;
		try {
			simEndpoint = new SimEndpointParser().parse(uri);
		} catch (Exception e) {
			sendSoapFault(soapEnv, FaultCode.EndpointUnavailable, "Cannot parse endpoint <" + uri + "> " + e.getMessage());
			return;
		}

		handleSimulatorInputTransaction(request, soapEnv, simEndpoint, new Endpoint().setEndpoint(uri));
	}

	public void handleSimulatorInputTransaction(HttpServletRequest request,
			SoapEnvironment soapEnv, SimEndpoint simEndpoint, Endpoint endpoint) {
		// DB space for this simulator - needed here so the request message can be logged
		//    also made available through a request attribute
		// TODO: the SimDb instance should be moved to the HttpEnvironment
		SimDb db;
		Event event;
		try {
			db = new SimDb(simEndpoint.getSimId());
			event = (Event) db.createEvent(ActorType.findActor(simEndpoint.getActor()), simEndpoint.getTransaction());
		} catch (Exception e) {
			logger.error("Internal error initializing simulator environment", e);
			sendSoapFault(soapEnv, FaultCode.Receiver, "Internal error initializing simulator environment: " + e.getMessage());
			return;
		} 
		soapEnv.getHttpEnvironment().setEventLog(event);
		soapEnv.setEndpoint(endpoint);
		request.setAttribute("Event", event); // SimServletFilter needs this to log response as it goes out on the wire

		// This makes the input message available to the SimChain
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
			new ActorSimFactory().run(simEndpoint.getActor() + "^" + simEndpoint.getTransaction(), soapEnv);
		} catch (SoapFaultException sfe) {
			sendSoapFault(soapEnv, sfe);
		} catch (SimEngineSubscriptionException e) {
			sendSoapFault(
					soapEnv,
					new SoapFaultException(
							null,
							FaultCode.Receiver,
							"Problem with SimChain definition for Actor <" + simEndpoint.getActor() 
							+ "> and Transaction <" + simEndpoint.getTransaction() + "> " +
									e.getMessage()
							)
					);
		}
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
	 */
	void logRequest(HttpServletRequest request, Event event, String actor, String transaction)
			throws FileNotFoundException, IOException, HttpHeaderParseException, ParseException {
		StringBuffer buf = new StringBuffer();
		Map<String, String> headers = new HashMap<String, String>();
		HttpHeader contentTypeHeader;
		String bodyCharset;

		buf.append(request.getMethod() + " " + request.getRequestURI() + " " + request.getProtocol() + "\r\n");
		for (@SuppressWarnings("unchecked")
		Enumeration<String> en=request.getHeaderNames(); en.hasMoreElements(); ) {
			String name = en.nextElement();
			String value = request.getHeader(name);
			if (name.equals("Transfer-Encoding"))
				continue;  // log will not include transfer encoding so don't include this
			headers.put(name.toLowerCase(), value);
			buf.append(value).append("\r\n");
		}
		//		bodyCharset = request.getCharacterEncoding();
		String ctype = headers.get("content-type");
		if (ctype == null || ctype.equals(""))
			throw new IOException("Content-Type header not found");
		contentTypeHeader = new HttpHeader(ctype);
		bodyCharset = contentTypeHeader.getParam("charset");

		if (bodyCharset == null || bodyCharset.equals(""))
			bodyCharset = "UTF-8";

		buf.append("\r\n");

		// Log the request header and body in the SimDb event
		event.getInOutMessages().putRequestHeader(buf.toString());
		event.getInOutMessages().putRequestBody(Io.getBytesFromInputStream(request.getInputStream()));
	}

	void sendSoapFault(SoapEnvironment soapEnv, FaultCode faultCode, String reason) {
		sendSoapFault(
				soapEnv, 					
				new SoapFaultException(
						null,      // No error recorder established to allow capture to logs
						faultCode,
						new ErrorContext(reason)));
	}

	void sendSoapFault(SoapEnvironment soapEnv, SoapFaultException sfe) {
		try {
			faultSent = new SoapFault(soapEnv, sfe).send();
		}  catch (Exception ex) {
			logger.error("Error sending SOAPFault", ex);
		}
	}
	
	public String getFaultSent() { return faultSent; }

}
