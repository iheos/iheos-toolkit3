package gov.nist.hit.ds.soapSupport.core;

import gov.nist.hit.ds.utilities.xml.OMFormatter;
import gov.nist.hit.ds.xdsException.XMLParserException;
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

    public SoapResponseGenerator(SoapEnvironment senv, String _body) throws XMLParserException {
        this.senv = senv;
        this.body = gov.nist.hit.ds.utilities.xml.Parse.parse_xml_string(_body);
    }


    public OMElement getEnvelope() {
		if (envelope == null) {
			envelope = SoapUtil.buildSoapEnvelope();

			SoapUtil.attachSoapHeader(null, senv.getResponseAction(), null, senv.getMessageId(), envelope);
			SoapUtil.attachSoapBody(body, envelope);
		}
		return envelope;
	}

    public String getEnvelopeAsString() {
        return new OMFormatter(getEnvelope()).toString();
    }
	
}
