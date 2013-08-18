package gov.nist.hit.ds.http.environment;

import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

/**
 * Internal to simulator operation, an instance of this class represents the 
 * request message and the environment for generating a response message.
 * @author bill
 *
 */
public class HttpEnvironment {
	HttpServletResponse response = null;
	OutputStream os = null;
	Object eventLog;
	
	public HttpServletResponse getResponse() {
		return response;
	}
	public HttpEnvironment setResponse(HttpServletResponse response) {
		this.response = response;
		return this;
	}
	
	public OutputStream getOutputStream() throws Exception {
		if (os == null) {
			if (response == null)
				throw new Exception("SoapEnvironment: Cannot retrieve outputstream - no HttpServletResponse object is registered");
			os = response.getOutputStream();
		}
		return os;
	}

	public Object getEventLog() { return eventLog; }
	
	public HttpEnvironment setEventLog(Object eventLog) { this.eventLog = eventLog; return this; }

}
