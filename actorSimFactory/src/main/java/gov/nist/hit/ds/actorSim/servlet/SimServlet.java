package gov.nist.hit.ds.actorSim.servlet;

import gov.nist.hit.ds.http.parser.HttpHeader;
import gov.nist.hit.ds.http.parser.HttpHeader.HttpHeaderParseException;
import gov.nist.hit.ds.http.parser.ParseException;
import gov.nist.hit.ds.initialization.Installation;
import gov.nist.hit.ds.simSupport.client.NoSimException;
import gov.nist.hit.ds.simSupport.datatypes.SimEndPoint;
import gov.nist.hit.ds.simSupport.sim.SimDb;
import gov.nist.hit.ds.simSupport.validators.SimEndpointParser;
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

public class SimServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(SimServlet.class);
	ServletConfig config;
	File warHome;
	File simDbDir;  // = "/Users/bill/tmp/xdstools2/simdb";

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.config = config;
		
		warHome = new File(config.getServletContext().getRealPath("/"));
		simDbDir = Installation.installation().propertyServiceManager().getSimDbDir();
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String uri  = request.getRequestURI().toLowerCase();
		SimEndPoint endpoint;
		try {
			endpoint = new SimEndpointParser().parse(uri);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		// DB space for this simulator
		SimDb db;
		try {
			db = new SimDb(endpoint.getSimId(), endpoint.getActor(), endpoint.getTransaction());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (NoSimException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		request.setAttribute("SimDb", db);

		try {
			logRequest(request, db, endpoint.getActor(), endpoint.getTransaction());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HttpHeaderParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	void logRequest(HttpServletRequest request, SimDb db, String actor, String transaction)
	throws FileNotFoundException, IOException, HttpHeaderParseException, ParseException {
		StringBuffer buf = new StringBuffer();
		Map<String, String> headers = new HashMap<String, String>();
		String contentType;
		HttpHeader contentTypeHeader;
		String bodyCharset;
		byte[] bodyBytes;
		String body;

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
		contentTypeHeader = new HttpHeader("content-type: " + ctype);
		bodyCharset = contentTypeHeader.getParam("charset");
		contentType = contentTypeHeader.getValue();

		if (bodyCharset == null || bodyCharset.equals(""))
			bodyCharset = "UTF-8";

		buf.append("\r\n");


		db.putRequestHeaderFile(buf.toString().getBytes());

		db.putRequestBodyFile(Io.getBytesFromInputStream(request.getInputStream()));

	}

//	void sendSoapFault(HttpServletResponse response, String message) {
//		try {
//			SoapFault sf = new SoapFault(SoapFault.FaultCodes.Sender, message);
//			SimCommon c = new SimCommon(response);
//			OMElement faultEle = sf.getXML();
//			OMElement soapEnv = c.wrapResponseInSoapEnvelope(faultEle);
//			c.sendHttpResponse(soapEnv, SimCommon.getUnconnectedErrorRecorder(), false);
//		} catch (Exception e) {
//			logger.error(ExceptionUtil.exception_details(e));
//		}
//	}

}
