package gov.nist.hit.ds.httpSoapValidator.validators

import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ValidationFault
import gov.nist.hit.ds.soapSupport.FaultCode
import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.utilities.datatypes.RequiredOptional
import gov.nist.hit.ds.utilities.xml.Parse
import groovy.util.logging.Log4j
import org.apache.axiom.om.OMElement
import org.apache.axiom.om.OMNamespace

@Log4j
public class SoapMessageParser extends ValComponentBase {
	String soapNamespaceName = "http://www.w3.org/2003/05/soap-envelope";
    String xmlMessage
    OMElement xml
	OMElement root = null;
	OMElement header = null;
	OMElement body = null;
	int partCount;

    SimHandle handle
    SoapMessageParser parser

    SoapMessageParser(SimHandle handle, String msg) {
        super(handle.event)
        this.handle = handle
        this.xmlMessage = msg
    }

    // TODO:  This needs a Do-This-First annotation similar to dependsOn.  It should run first because we label it so not because it has no dependencies.
    @ValidationFault(id="SoapHTTPBodyPresent", required=RequiredOptional.R, msg="HTTP Body Present", faultCode=FaultCode.Sender, ref="??")
    public void bodyPresent() throws SoapFaultException {
        assertNotNull xmlMessage
    }

    @ValidationFault(id="SoapParseXML", dependsOn='SoapHTTPBodyPresent', required=RequiredOptional.R, msg="Parsing XML", faultCode=FaultCode.Sender, ref="??")
    public void parseXML() throws SoapFaultException {
        defaultMsg()
        try {
            xml = Parse.parse_xml_string(xmlMessage)
        } catch (Exception e) {
            fail("XML Parse errors:\n${e.getMessage()}")
        }
    }

    @ValidationFault(id="SoapParseEnvelope", dependsOn='SoapParserXML', required=RequiredOptional.R, msg="Parse SOAP Envelope", faultCode=FaultCode.Sender, ref="??")
    public void parseSOAPEnvelope() throws SoapFaultException {
        defaultMsg()
        try {
            parse()
        } catch (Exception e) {
            fail("SOAP Parse errors:\n${e.getMessage()}")
        }
    }

    @ValidationFault(id="SoapVerifyEnvelope", dependsOn='SoapParseEnvelope', msg="Top element must be Envelope", required=RequiredOptional.R, faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
    public void verifySOAPEnvelopeElement() throws SoapFaultException { assertEquals('Envelope', root?.getLocalName()) }

    @ValidationFault(id="SoapVerifyHeaderPresent", dependsOn='SoapParseEnvelope', required=RequiredOptional.R, msg="Header must be present and be first child of Envelope", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
    public void verifySOAPHeaderElement() throws SoapFaultException { assertEquals('Header', header?.getLocalName()) }

    @ValidationFault(id="SoapParserVerifyBodyPresent", dependsOn='SoapParseEnvelope', required=RequiredOptional.R, msg="Body must be present, must be second child of Envelope", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
    public void verifySOAPBodyElement() throws SoapFaultException { assertEquals('Body', body?.getLocalName()) }

    @ValidationFault(id="SoapVerifyPartCount", dependsOn='SoapParseEnvelope', msg="Envelope must have 2 children", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
    public void validatePartCount() throws SoapFaultException { assertEquals(2, partCount) }

	@ValidationFault(id="SoapEnvelopeNamespace", dependsOn="SOAP010", msg="Correct Envelope Namespace", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateSOAPEnvelopeNamespace() throws SoapFaultException {
		OMNamespace ns = root.getNamespace();
		if (ns != null)
			assertEquals(soapNamespaceName, ns.getNamespaceURI());
		else
			fail(soapNamespaceName);
	}

	@ValidationFault(id="SoapHeaderNamespace", dependsOn="SoapParseEnvelope", msg="Correct Header Namespace", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateSOAPHeaderNamespace() throws SoapFaultException {
		OMNamespace ns = header.getNamespace();
		if (ns != null)
			assertEquals(soapNamespaceName, ns.getNamespaceURI());
		else
			fail(soapNamespaceName);
	}

	@ValidationFault(id="SoapBodyNamespace", dependsOn="SoapParseEnvelope", msg="Correct Body Namespace", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateSOAPBodyNamespace() throws SoapFaultException {
		OMNamespace ns = body.getNamespace();
		if (ns != null)
			assertEquals(soapNamespaceName, ns.getNamespaceURI());
		else
			fail(soapNamespaceName);
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
                    // look for extra parts
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
    public void run() throws SoapFaultException, RepositoryException {
        runValidationEngine();
    }

    @Override
    public boolean showOutputInLogs() {
        return false;
    }


}
