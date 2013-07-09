package gov.nist.hit.ds.soap.core;

import gov.nist.hit.ds.http.parser.HttpEnvironment;

import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

/**
 * Keep track of SOAP/HTTP environment so response can be
 * properly formatted.
 * 
 * TODO:  response content type needs to be set
 * @author bill
 *
 */
public class SoapEnvironment {
	boolean multipart = false;
	String requestAction = null;
	String responseAction = null;
	String messageId = null;
	HttpEnvironment httpEnv;
	
	public SoapEnvironment(HttpEnvironment httpEnv) {
		this.httpEnv = httpEnv;
	}
	public HttpEnvironment getHttpEnvironment() {
		return httpEnv;
	}

	public boolean isMultipart() {
		return multipart;
	}
	public void setMultipart(boolean multipart) {
		this.multipart = multipart;
	}
	public String getRequestAction() {
		return requestAction;
	}
	public void setRequestAction(String requestAction) {
		this.requestAction = requestAction;
	}
	public String getResponseAction() {
		return responseAction;
	}
	public void setResponseAction(String responseAction) {
		this.responseAction = responseAction;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public HttpServletResponse getResponse() {
		return httpEnv.getResponse();
	}
	public OutputStream getOutputStream() throws Exception {
		return httpEnv.getOutputStream();
	}
}
