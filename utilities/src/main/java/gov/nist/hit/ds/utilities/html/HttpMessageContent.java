package gov.nist.hit.ds.utilities.html;

public class HttpMessageContent {
	String header;
	byte[] body;
	
	public String getHeader() {
		return header;
	}
	public HttpMessageContent setHeader(String header) {
		this.header = header;
		return this;
	}
	public byte[] getBody() {
		return body;
	}
	public HttpMessageContent setBody(byte[] body) {
		this.body = body;
		return this;
	}
	
	
}
