package gov.nist.hit.ds.soap;

import gov.nist.hit.ds.http.HttpResponseGenerator;
import gov.nist.hit.ds.utilities.xml.OMFormatter;

import org.apache.axiom.om.OMElement;

/**
 * Generate SOAP response messages.
 * TODO: Multipart generation.
 * @author bill
 *
 */
public class SoapResponseGenerator {
	SoapEnvironment senv;
	OMElement envelope = null;
	OMElement body;
	
	public SoapResponseGenerator(SoapEnvironment senv, OMElement body) {
		this.senv = senv;
		this.body = body;
	}
	
	public OMElement getEnvelope() {
		if (envelope == null) {
			envelope = SoapUtil.buildSoapEnvelope();

			SoapUtil.attachSoapHeader(null, senv.getResponseAction(), null, senv.getMessageId(), envelope);
			SoapUtil.attachSoapBody(body, envelope);
		}
		return envelope;
	}
	
	public void send() throws Exception {
		if (senv.multipart)
			throw new Exception("SoapResponseGenerator#send: cannot generate multipart responses yet");
		HttpResponseGenerator resp = new HttpResponseGenerator(senv.getHttpEnvironment());
		resp.sendResponse(new OMFormatter(getEnvelope()).toString());
	}

}
