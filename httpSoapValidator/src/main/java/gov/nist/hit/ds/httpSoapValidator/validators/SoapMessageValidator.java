package gov.nist.hit.ds.httpSoapValidator.validators;

import gov.nist.hit.ds.httpSoapValidator.datatypes.SoapMessage;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.annotations.Inject;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.core.ValidationFault;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.soapSupport.soapFault.FaultCode;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

public class SoapMessageValidator extends SimComponentBase{
	String soapNamespaceName = "http://www.w3.org/2003/05/soap-envelope";
	OMElement root = null;
	OMElement header = null;
	OMElement body = null;
	int partCount;

	@Inject
	public void setSoapMessage(SoapMessage message) {
		this.header = message.getHeader();
		this.body = message.getBody();
		this.root = message.getRoot();
		this.partCount = message.getPartCount();
	}

	@ValidationFault(id="SOAP001", msg="Top element must be Envelope", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateSOAPEnvelopePresent() throws SoapFaultException {
		boolean ok = assertNotNull(root);
		if (ok)
			assertEquals("Envelope", root.getLocalName());
	}

	@ValidationFault(id="SOAP002", dependsOn="SOAP001", msg="Envelope Namespace", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateSOAPEnvelopeNamespace() throws SoapFaultException {
		OMNamespace ns = root.getNamespace();
		if (ns != null)
			assertEquals(soapNamespaceName, ns.getNamespaceURI());
		else
			fail(soapNamespaceName);
	}

	@ValidationFault(id="SOAP003", msg="Envelope must have 2 childern", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validatePartCount() throws SoapFaultException {
		assertEquals(2, partCount);
	}

	@ValidationFault(id="SOAP004", msg="Header must be present, must be first child of Envelope", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateHeaderPresent() throws SoapFaultException {
		boolean ok = assertNotNull(header);
		if (ok)
			assertEquals("Header", header.getLocalName());
	}

	@ValidationFault(id="SOAP005", msg="Header Namespace", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateSOAPHeaderNamespace() throws SoapFaultException {
		boolean ok = assertNotNull(root);
		if (ok) {
			OMNamespace ns = header.getNamespace();
			if (ns != null)
				assertEquals(soapNamespaceName, ns.getNamespaceURI());
			else
				fail(soapNamespaceName);
		}
	}

	@ValidationFault(id="SOAP006", msg="Body must be present, must be second child of Envelope", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateBodyPresent() throws SoapFaultException {
		boolean ok = assertNotNull(body);
		if (ok)
			assertEquals("Body", body.getLocalName());
	}

	@ValidationFault(id="SOAP007", msg="Body Namespace", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateSOAPBodyNamespace() throws SoapFaultException {
		boolean ok = assertNotNull(root);
		if (ok) {
			OMNamespace ns = body.getNamespace();
			if (ns != null)
				assertEquals(soapNamespaceName, ns.getNamespaceURI());
			else
				fail(soapNamespaceName);
		}
	}

	@Override
	public void run(MessageValidatorEngine mve) throws SoapFaultException, RepositoryException {
		runValidationEngine();
	}


}
