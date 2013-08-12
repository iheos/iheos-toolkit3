package gov.nist.hit.ds.actorSim.servlet;

import gov.nist.hit.ds.actorSim.factory.ActorSimFactory;
import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.http.environment.Event;
import gov.nist.hit.ds.http.environment.HttpEnvironment;
import gov.nist.hit.ds.http.parser.HttpHeader;
import gov.nist.hit.ds.http.parser.HttpHeader.HttpHeaderParseException;
import gov.nist.hit.ds.http.parser.ParseException;
import gov.nist.hit.ds.initialization.Installation;
import gov.nist.hit.ds.simSupport.datatypes.SimEndpoint;
import gov.nist.hit.ds.simSupport.sim.SimDb;
import gov.nist.hit.ds.simSupport.validators.SimEndpointParser;
import gov.nist.hit.ds.soapSupport.core.FaultCode;
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment;
import gov.nist.hit.ds.soapSupport.core.SoapFault;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.utilities.io.Io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	File warHome;
	File simDbDir;
	String faultSent = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.config = config;

		// This assumes that the toolkit has been configured before this Servlet
		// is initialized.
		warHome = new File(config.getServletContext().getRealPath("/"));
		simDbDir = Installation.installation().propertyServiceManager().getSimDbDir();
		try {
			initSimEnvironment();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException("Error initializing Simulator Environment.", e);
		} 
	}
	
	/*
	 * Initialize the simulator environment.  
	 */
	public void initSimEnvironment() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		new ActorSimFactory().loadSims();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String uri  = request.getRequestURI().toLowerCase();

		// This is being set up early in case we need to generate a SOAPFault
		HttpEnvironment httpEnv = new HttpEnvironment().setResponse(response);
		SoapEnvironment soapEnv = new SoapEnvironment(httpEnv);

		// Parse endpoint to discover what simulator is the target of the request.
		SimEndpoint endpoint;
		try {
			endpoint = new SimEndpointParser().parse(uri);
		} catch (Exception e) {
			sendSoapFault(soapEnv, FaultCode.EndpointUnavailable, "Cannot parse endpoint <" + uri + "> " + e.getMessage());
			return;
		}

		handleSimulatorInputTransaction(request, soapEnv, endpoint);
	}

	public void handleSimulatorInputTransaction(HttpServletRequest request,
			SoapEnvironment soapEnv, SimEndpoint endpoint) {
		// DB space for this simulator - needed here so the request message can be logged
		//    also made available through a request attribute
		// TODO: the SimDb instance should be moved to the HttpEnvironment
		SimDb db;
		Event event;
		try {
			db = new SimDb(endpoint.getSimId());
			event = db.createEvent(ActorType.findActor(endpoint.getActor()), endpoint.getTransaction());
		} catch (Exception e) {
			logger.error("Internal error initializing simulator environment", e);
			sendSoapFault(soapEnv, FaultCode.Receiver, "Internal error initializing simulator environment: " + e.getMessage());
			return;
		} 
		soapEnv.getHttpEnvironment().setEventLog(event);
		request.setAttribute("Event", event); // SimServletFilter needs this to log response as it goes out on the wire

		// This makes the input message available to the SimChain
		try {
			logRequest(request, event, endpoint.getActor(), endpoint.getTransaction());
		} catch (Exception e) {
			logger.error("Internal error logging request message", e);
			sendSoapFault(soapEnv, FaultCode.Receiver, "Internal error logging request message: " + e.getMessage());
			return;
		} 
		
		// Find the correct SimChain and run it.  The SimChain selected by a combination of
		// Actor and Transaction codes.  These codes came from the endpoint used to contact this 
		// Servlet.
		try {
			new ActorSimFactory().run(endpoint.getActor() + "^" + endpoint.getTransaction());
		} catch (SoapFaultException sfe) {
			sendSoapFault(soapEnv, sfe);
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
			buf.append(name).append(": ").append(value).append("\r\n");
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
		event.putRequestHeader(buf.toString());
		event.putRequestBody(Io.getBytesFromInputStream(request.getInputStream()));
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
