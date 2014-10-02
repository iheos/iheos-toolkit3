package gov.nist.hit.ds.httpSoapValidator.datatypes;

import org.apache.axiom.om.OMElement;

/**
 * Holder for the SOAP Body, the XML child of the 
 * SOAPBody element.
 * @author bmajur
 *
 */
public class SoapBody {
	OMElement body;

	public OMElement getBody() {
		return body;
	}

	public SoapBody setBody(OMElement body) {
		this.body = body;
		return this;
	}
}
