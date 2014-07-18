package gov.nist.hit.ds.httpSoapValidator.parsers
import gov.nist.hit.ds.httpSoapValidator.datatypes.SoapMessage
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ValidationFault
import gov.nist.hit.ds.soapSupport.FaultCode
import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.utilities.datatypes.RequiredOptional
import gov.nist.hit.ds.utilities.xml.Parse
import org.apache.axiom.om.OMElement
/**
 * Parse SOAP message and make parts available through the SoapMessage object.
 * @author bmajur
 *
 */

// Redo based on gov.nist.toolkit.valregmsg.message.SoapMessageValidator
public class SoapMessageParser extends ValComponentBase {
	OMElement xml
	String xmlMessage
	OMElement header = null
	OMElement body = null
	OMElement root
	int partCount

    SoapMessageParser(SimHandle handle, String msg) {
        super(handle.event)
        xmlMessage = msg
    }

	public SoapMessage getSoapMessage() {
		return new SoapMessage().
				setHeader(header).
				setBody(body).
				setRoot(root).
				setPartCount(partCount);
	}

    @ValidationFault(id="SoapParserXML", required=RequiredOptional.R, msg="Parse XML", faultCode=FaultCode.Sender, ref="??")
    public void parseXML() throws SoapFaultException {
        try {
            infoFound('Parsing XML')
            xml = Parse.parse_xml_string(xmlMessage)
        } catch (Exception e) {
            fail("XML Parse errors:\n${e.getMessage()}")
        }
    }

    @ValidationFault(id="SoapParserVerifyEnvelope", required=RequiredOptional.R, msg="Verify Envelope element", faultCode=FaultCode.Sender, ref="??", dependsOn='SoapParserXML')
    public void verifySOAPEnvelopeElement() throws SoapFaultException {
        infoFound('Verify Envelope element')
        assertEquals('Envelope', xml.getLocalName())
    }

    @ValidationFault(id="SoapParserParseEnvelope", required=RequiredOptional.R, msg="Parse SOAP Envelope", faultCode=FaultCode.Sender, ref="??", dependsOn='SoapParserXML')
    public void parseSOAPEnvelope() throws SoapFaultException {
        try {
            infoFound('Parsing SOAP Message')
            parse()
        } catch (Exception e) {
            fail("SOAP Parse errors:\n${e.getMessage()}")
        }
    }

    @ValidationFault(id="SoapParserVerifyHeader", required=RequiredOptional.R, msg="Verify Soap Header element", faultCode=FaultCode.Sender, ref="??", dependsOn='SoapParserParseEnvelope')
    public void verifySOAPHeaderElement() throws SoapFaultException {
        infoFound('Verify Soap Header element')
        assertEquals('Header', header.getLocalName())
    }

    @ValidationFault(id="SoapParserVerifyBody", required=RequiredOptional.R, msg="Verify Soap Body element", faultCode=FaultCode.Sender, ref="??", dependsOn='SoapParserParseEnvelope')
    public void verifySOAPBodyElement() throws SoapFaultException {
        infoFound('Verify Soap Bddy element')
        assertEquals('Body', body.getLocalName())
    }


    @Override
    public void run() throws SoapFaultException, RepositoryException {
        runValidationEngine();
    }

    public void parse() {
		root = xml;
		if (root != null) {
			Iterator<?> partsIterator = root.getChildElements();

			partCount = 0;
			if (partsIterator.hasNext()) {
				partCount++;
				header = (OMElement) partsIterator.next();
				if (partsIterator.hasNext()) {
					partCount++;
					body = (OMElement) partsIterator.next();
					partCount += countParts(partsIterator);
				}
			}
		}
	}

	int countParts(Iterator<?> it) {
		int cnt = 0;
		while (it.hasNext()) {
			it.next();
			cnt++;
		}
		return cnt;
	}

	@Override
	public boolean showOutputInLogs() {
		return true
	}

}
