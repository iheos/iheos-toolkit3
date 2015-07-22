package gov.nist.hit.ds.httpSoapValidator.validators
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.DependsOn
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Fault
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.hit.ds.soapSupport.FaultCode
import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.utilities.xml.Parse
import groovy.util.logging.Log4j
import org.apache.axiom.om.OMElement
import org.apache.axiom.om.OMNamespace


public class SoapMessageParser extends ValComponentBase {
    private static Logger log = Logger.getLogger(SoapMessageParser);
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
    @Fault(code=FaultCode.Sender)
    @Validation(id="SoapHTTPBodyPresent",  msg="HTTP Body Present", ref="??")
    public void bodyPresent() throws SoapFaultException {
        assertNotNull xmlMessage
    }

    @Fault(code=FaultCode.Sender)
    @DependsOn(ids=['SoapHTTPBodyPresent'])
    @Validation(id="SoapParseXML", msg="Parsing XML", ref="??")
    public void parseXML() throws SoapFaultException {
        defaultMsg()
        try {
            xml = Parse.parse_xml_string(xmlMessage)
        } catch (Exception e) {
            fail("XML Parse errors:\n${e.getMessage()}")
        }
    }

    @Fault(code=FaultCode.Sender)
    @DependsOn(ids=['SoapParseXML'])
    @Validation(id="SoapParseEnvelope", msg="Parse SOAP Envelope", ref="??")
    public void parseSOAPEnvelope() throws SoapFaultException {
        defaultMsg()
        try {
            parse()
        } catch (Exception e) {
            fail("SOAP Parse errors:\n${e.getMessage()}")
        }
    }

    @Fault(code=FaultCode.Sender)
    @DependsOn(ids=['SoapParseEnvelope'])
    @Validation(id="SoapVerifyEnvelope", msg="Top element must be Envelope", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
    public void verifySOAPEnvelopeElement() throws SoapFaultException { assertEquals('Envelope', root?.getLocalName()) }

    @Fault(code=FaultCode.Sender)
    @DependsOn(ids=['SoapParseEnvelope'])
    @Validation(id="SoapVerifyHeaderPresent", msg="Header must be present and be first child of Envelope", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
    public void verifySOAPHeaderElement() throws SoapFaultException { assertEquals('Header', header?.getLocalName()) }

    @Fault(code=FaultCode.Sender)
    @DependsOn(ids=['SoapParseEnvelope'])
    @Validation(id="SoapParserVerifyBodyPresent", msg="Body must be present, must be second child of Envelope", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
    public void verifySOAPBodyElement() throws SoapFaultException { assertEquals('Body', body?.getLocalName()) }

    @Fault(code=FaultCode.Sender)
    @DependsOn(ids=['SoapParseEnvelope'])
    @Validation(id="SoapVerifyPartCount", msg="Envelope must have 2 children", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
    public void validatePartCount() throws SoapFaultException { assertEquals(2, partCount) }

    @Fault(code=FaultCode.Sender)
    @DependsOn(ids=['SoapParseEnvelope'])
	@Validation(id="SoapEnvelopeNamespace", msg="Correct Envelope Namespace", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateSOAPEnvelopeNamespace() throws SoapFaultException {
		OMNamespace ns = root?.getNamespace();
		if (ns != null)
			assertEquals(soapNamespaceName, ns.getNamespaceURI());
		else
			fail(soapNamespaceName);
	}

    @Fault(code=FaultCode.Sender)
    @DependsOn(ids=['SoapParseEnvelope'])
	@Validation(id="SoapHeaderNamespace", msg="Correct Header Namespace", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateSOAPHeaderNamespace() throws SoapFaultException {
		OMNamespace ns = header?.getNamespace();
		if (ns != null)
			assertEquals(soapNamespaceName, ns.getNamespaceURI());
		else
			fail(soapNamespaceName);
	}

    @Fault(code=FaultCode.Sender)
    @DependsOn(ids=['SoapParseEnvelope'])
	@Validation(id="SoapBodyNamespace", msg="Correct Body Namespace", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
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


    @Override
    public void run() throws SoapFaultException, RepositoryException {
        runValidationEngine();
    }

    @Override
    public boolean showOutputInLogs() {
        return false;
    }


}
