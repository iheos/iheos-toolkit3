package gov.nist.hit.ds.httpSoap.validators
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Fault
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.hit.ds.soapSupport.FaultCode
import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.utilities.xml.Parse
import groovy.util.logging.Log4j
import org.apache.axiom.om.OMElement
import org.apache.axiom.om.OMNamespace

@Log4j
public class SoapMessageValidator extends ValComponentBase {
	String soapNamespaceName = "http://www.w3.org/2003/05/soap-envelope";
    String xmlMessage
    OMElement xml
	OMElement root = null;
	OMElement header = null;
	OMElement body = null;
	int partCount;

    SimHandle handle
    SoapMessageValidator parser

    SoapMessageValidator(SimHandle handle, String msg) {
        super(handle.event)
        this.handle = handle
        this.xmlMessage = msg
    }

    // TODO:  This needs a Do-This-First annotation similar to dependsOn.  It should testRun first because we label it so not because it has no dependencies.
    @Fault(code=FaultCode.Sender)
    @Validation(id="Soap010",  msg="HTTP Body Present", ref="??")
    public void Soap010() throws SoapFaultException {
        assertNotNull xmlMessage
        if (!xmlMessage) quit()
    }

    @Validation(id="Soap011",  msg="XML starts with", ref="??")
    public void Soap011() throws SoapFaultException { infoFound(xmlMessage.substring(0, 15).trim()) }

    @Fault(code=FaultCode.Sender)
    @Validation(id="Soap020", msg="Parsing XML", ref="??")
    public void parseXML() throws SoapFaultException {
        try {
            xml = Parse.parse_xml_string(xmlMessage)
        } catch (Exception e) {
            fail("XML Parse errors: ${e.getMessage()}")
            quit()
        }
    }

    @Fault(code=FaultCode.Sender)
    @Validation(id="Soap030", msg="Parse SOAP Envelope", ref="??")
    public void parseSOAPEnvelope() throws SoapFaultException {
        try {
            parse()
        } catch (Exception e) {
            fail("SOAP Parse errors: ${e.getMessage()}")
            quit()
        }
    }

    @Fault(code=FaultCode.Sender)
    @Validation(id="Soap040", msg="Top element must be Envelope", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
    public void verifySOAPEnvelopeElement() throws SoapFaultException { assertEquals('Envelope', root?.getLocalName()) }

    @Fault(code=FaultCode.Sender)
    @Validation(id="Soap050", msg="Header must be present and be first child of Envelope", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
    public void verifySOAPHeaderElement() throws SoapFaultException { assertEquals('Header', header?.getLocalName()) }

    @Fault(code=FaultCode.Sender)
    @Validation(id="Soap060", msg="Body must be present, must be second child of Envelope", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
    public void verifySOAPBodyElement() throws SoapFaultException { assertEquals('Body', body?.getLocalName()) }

    @Fault(code=FaultCode.Sender)
    @Validation(id="Soap070", msg="Envelope must have 2 children", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
    public void validatePartCount() throws SoapFaultException { assertEquals(2, partCount) }

    @Fault(code=FaultCode.Sender)
	@Validation(id="Soap080", msg="Correct Envelope Namespace", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateSOAPEnvelopeNamespace() throws SoapFaultException {
		OMNamespace ns = root?.getNamespace();
		if (ns != null)
			assertEquals(soapNamespaceName, ns.getNamespaceURI());
		else
			fail(soapNamespaceName);
	}

    @Fault(code=FaultCode.Sender)
	@Validation(id="Soap090", msg="Correct Header Namespace", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateSOAPHeaderNamespace() throws SoapFaultException {
		OMNamespace ns = header?.getNamespace();
		if (ns != null)
			assertEquals(soapNamespaceName, ns.getNamespaceURI());
		else
			fail(soapNamespaceName);
	}

    @Fault(code=FaultCode.Sender)
	@Validation(id="Soap100", msg="Correct Body Namespace", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateSOAPBodyNamespace() throws SoapFaultException {
		OMNamespace ns = body?.getNamespace();
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


}
