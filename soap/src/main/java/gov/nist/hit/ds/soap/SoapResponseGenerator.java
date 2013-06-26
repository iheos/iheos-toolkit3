package gov.nist.hit.ds.soap;

import org.apache.axiom.om.OMElement;

public class SoapResponseGenerator {
	String action;
	String messageId;
	OMElement env = null;
	
	public SoapResponseGenerator(String responseWSAction, String messageId) {
		this.action = responseWSAction;
		this.messageId = messageId;
	}
	
	public SoapResponseGenerator addBody(OMElement body) {
		env = SoapUtil.buildSoapEnvelope();

		SoapUtil.attachSoapHeader(null, action, null, messageId, env);
		SoapUtil.attachSoapBody(body, env);

		return this;
	}
	
	public OMElement getEnvelope() {
		return env;
	}

}
