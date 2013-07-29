package gov.nist.hit.ds.httpSoapValidator.datatypes;

import org.apache.axiom.om.OMElement;

public class SoapMessage {
	OMElement header;
	OMElement body;
	
	public OMElement getHeader() {
		return header;
	}
	public SoapMessage setHeader(OMElement header) {
		this.header = header;
		return this;
	}
	public OMElement getBody() {
		return body;
	}
	public SoapMessage setBody(OMElement body) {
		this.body = body;
		return this;
	}
	
	
}
