package gov.nist.hit.ds.httpSoapValidator.components.validators;

import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase;
import gov.nist.hit.ds.soapSupport.SoapFaultException;
import org.apache.axiom.om.OMElement;

public class SoapMessageValidator extends ValComponentBase {
	String soapNamespaceName = "http://www.w3.org/2003/05/soap-envelope";
	OMElement root = null;
	OMElement header = null;
	OMElement body = null;
	int partCount;

    @Override
    public void run() throws SoapFaultException, RepositoryException {

    }

    @Override
    public boolean showOutputInLogs() {
        return false;
    }

//	public void setSoapMessage(SoapMessage message) {
//		this.header = message.getHeader();
//		this.body = message.getBody();
//		this.root = message.getRoot();
//		this.partCount = message.getPartCount();
//	}
//
//	@ValidationFault(id="SOAP010", msg="Top element present", faultCode= FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
//	public void validateRoot() throws SoapFaultException {
//		assertNotNull(root);
//	}
//
//	@ValidationFault(id="SOAP011", dependsOn="SOAP010", msg="Top element must be Envelope", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
//	public void validateSOAPEnvelopePresent() throws SoapFaultException {
//		assertEquals("Envelope", root.getLocalName());
//	}
//
//	@ValidationFault(id="SOAP020", dependsOn="SOAP010", msg="Envelope Namespace correct", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
//	public void validateSOAPEnvelopeNamespace() throws SoapFaultException {
//		OMNamespace ns = root.getNamespace();
//		if (ns != null)
//			assertEquals(soapNamespaceName, ns.getNamespaceURI());
//		else
//			fail(soapNamespaceName);
//	}
//
//	@ValidationFault(id="SOAP030", msg="Envelope must have 2 childern", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
//	public void validatePartCount() throws SoapFaultException {
//		assertEquals(2, partCount);
//	}
//
//	@ValidationFault(id="SOAP040", msg="Header must be present and be first child of Envelope", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
//	public void validateHeaderPresent() throws SoapFaultException {
//		if (header == null)
//			assertNotNull(header);
//		else
//			assertEquals("Header", header.getLocalName());
//	}
//
//	@ValidationFault(id="SOAP050", dependsOn="SOAP010", msg="Header Namespace", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
//	public void validateSOAPHeaderNamespace() throws SoapFaultException {
//		OMNamespace ns = header.getNamespace();
//		if (ns != null)
//			assertEquals(soapNamespaceName, ns.getNamespaceURI());
//		else
//			fail(soapNamespaceName);
//	}
//
//	@ValidationFault(id="SOAP060", dependsOn="SOAP010", msg="Body must be present, must be second child of Envelope", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
//	public void validateBodyPresent() throws SoapFaultException {
//		if (body == null)
//			assertNotNull(body);
//		else
//			assertEquals("Body", body.getLocalName());
//	}
//
//	@ValidationFault(id="SOAP070", dependsOn="SOAP060", msg="Body Namespace", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
//	public void validateSOAPBodyNamespace() throws SoapFaultException {
//		OMNamespace ns = body.getNamespace();
//		if (ns != null)
//			assertEquals(soapNamespaceName, ns.getNamespaceURI());
//		else
//			fail(soapNamespaceName);
//	}
//
//	@Override
//	public void run(MessageValidatorEngine mve) throws SoapFaultException, RepositoryException {
//		runValidationEngine();
//	}
//
//    @Override
//    public void run() throws SoapFaultException, RepositoryException {
//
//    }
//
//    @Override
//	public boolean showOutputInLogs() {
//		return true;
//	}


}
