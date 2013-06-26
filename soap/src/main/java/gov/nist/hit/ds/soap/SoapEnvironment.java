package gov.nist.hit.ds.soap;

import javax.servlet.http.HttpServletResponse;

public class SoapEnvironment {
	boolean multipart;
	String requestAction;
	String responseAction;
	String messageId;
	HttpServletResponse response;

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
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
}
