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
		if (response == null)
			throw new Exception("SoapEnvironment: Cannot retrieve outputstream - no HttpServletResponse object is registered");
		return response.getOutputStream();
	}


	/**
	 * This is managed as type Object to avoid type dependency loops.
	 * @return
	 */
	public Object getEventLog() { return eventLog; }

	/**
	 * This is managed as type Object to avoid type dependency loops.
	 * @param eventLog
	 * @return
	 */
	public HttpEnvironment setEventLog(Object eventLog) { this.eventLog = eventLog; return this; }

}
