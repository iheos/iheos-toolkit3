package gov.nist.hit.ds.httpSoapValidator.validators;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode.Code;
import gov.nist.hit.ds.httpSoapValidator.datatypes.SoapMessage;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.annotations.Inject;
import gov.nist.hit.ds.simSupport.engine.annotations.ValidatorParameter;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment;
import gov.nist.hit.ds.soapSupport.core.ValidationFault;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.soapSupport.soapFault.FaultCode;
import gov.nist.hit.ds.utilities.xml.XmlUtil;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

public class SoapHeaderValidator   extends SimComponentBase {
	OMElement header;
	static final String wsaddresingNamespace = "http://www.w3.org/2005/08/addressing";
	static final String wsaddressingRef = "http://www.w3.org/TR/ws-addr-core/";
	SoapEnvironment soapEnvironment;
	String expectedAction = null;
	List<OMElement> messageId;
	List<OMElement> relatesTo;
	List<OMElement> to;
	List<OMElement> action;
	List<OMElement> from;
	List<OMElement> replyTo;
	List<OMElement> faultTo;

	@ValidatorParameter
	public SoapHeaderValidator setExpectedWsAction(String expectedAction) {
		this.expectedAction = expectedAction;
		return this;
	}

	@Inject
	public void setSoapMessage(SoapMessage soapMessage) {
		this.header = soapMessage.getHeader();
	}

	@Inject
	public SoapHeaderValidator setSoapEnvironment(SoapEnvironment soapEnvironment) {
		this.soapEnvironment = soapEnvironment;
		expectedAction = soapEnvironment.getExpectedRequestAction();
		return this;
	}

	@Override
	public void run(MessageValidatorEngine mve) throws SoapFaultException {
		validateWSAddressing();
		validateWSAction();
	}

	void validateWSAction() throws SoapFaultException {
		if (expectedAction == null) {
			ag.warning(
					Code.NoCode, 
					new ErrorContext("WS-Action not validated - no expected value is configured"), 
					this.getClass().getName());
			return;
		} 
		if (!expectedAction.equals(soapEnvironment.getRequestAction()))
			throw new SoapFaultException(
					ag,
					FaultCode.ActionNotSupported,
					new ErrorContext("Expected WS-Action >" + expectedAction + "> not equal to WS-Action found in message <" + soapEnvironment.getRequestAction() + ">"));
		ag.detail("WS-Action is <" + soapEnvironment.getRequestAction() + ">");
	}

	@ValidationFault(id="WSAparse", msg="Parsing WSA header fields", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void parseWSAddressingFields() throws SoapFaultException {
		assertNotNull(header);
		messageId = XmlUtil.childrenWithLocalName(header, "MessageID");
		relatesTo = XmlUtil.childrenWithLocalName(header, "RelatesTo");
		to = XmlUtil.childrenWithLocalName(header, "To");
		action = XmlUtil.childrenWithLocalName(header, "Action");
		from = XmlUtil.childrenWithLocalName(header, "From");
		replyTo = XmlUtil.childrenWithLocalName(header, "ReplyTo");
		faultTo = XmlUtil.childrenWithLocalName(header, "FaultTo");
	}

	@ValidationFault(id="WSA001", dependsOn="WSAparse", msg="Validate MessageId Namespace", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateMessageIdNamespace() throws SoapFaultException {
		if (messageId.size() == 0)
			return;
		assertEquals(1, messageId.size());
		OMElement ele = messageId.get(0);
		OMNamespace omns = ele.getNamespace();
		String nsuri = omns.getNamespaceURI();
		assertEquals(wsaddresingNamespace, nsuri);
	}

	@ValidationFault(id="WSA002", dependsOn="WSAparse", msg="Validate RelatesTo Namespace", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateRelatesToNamespace() throws SoapFaultException {
		if (relatesTo.size() == 0)
			return;
		assertEquals(1, relatesTo.size());
		OMElement ele = relatesTo.get(0);
		OMNamespace omns = ele.getNamespace();
		String nsuri = omns.getNamespaceURI();
		assertEquals(wsaddresingNamespace, nsuri);
	}

	@ValidationFault(id="WSA003", dependsOn="WSAparse", msg="Validate To Namespace", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateToNamespace() throws SoapFaultException {
		if (to.size() == 0)
			return;
		assertEquals(1, to.size());
		OMElement ele = to.get(0);
		OMNamespace omns = ele.getNamespace();
		String nsuri = omns.getNamespaceURI();
		assertEquals(wsaddresingNamespace, nsuri);
	}

	@ValidationFault(id="WSA028", dependsOn="WSAparse", msg="Validate Action Present", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateActionPresent() throws SoapFaultException {
		assertNotNull(action);
		assertEquals(1, action.size());
	}

	@ValidationFault(id="WSA029", dependsOn="WSA028", msg="Validate Action Value", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateActionValue() throws SoapFaultException {
		assertEquals(expectedAction, action.get(0).getText());
	}

	@ValidationFault(id="WSA004", dependsOn="WSAparse", msg="Validate Action Namespace", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateActionNamespace() throws SoapFaultException {
		if (action.size() == 0)
			return;
		assertEquals(1, action.size());
		OMElement ele = action.get(0);
		OMNamespace omns = ele.getNamespace();
		String nsuri = omns.getNamespaceURI();
		assertEquals(wsaddresingNamespace, nsuri);
	}

	@ValidationFault(id="WSA005", dependsOn="WSAparse", msg="Validate From Namespace", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateFromNamespace() throws SoapFaultException {
		if (from.size() == 0)
			return;
		assertEquals(1, from.size());
		OMElement ele = from.get(0);
		OMNamespace omns = ele.getNamespace();
		String nsuri = omns.getNamespaceURI();
		assertEquals(wsaddresingNamespace, nsuri);
	}

	@ValidationFault(id="WSA006", dependsOn="WSAparse", msg="Validate ReplyTo Namespace", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateReplyToNamespace() throws SoapFaultException {
		if (replyTo.size() == 0)
			return;
		assertEquals(1, replyTo.size());
		OMElement ele = replyTo.get(0);
		OMNamespace omns = ele.getNamespace();
		String nsuri = omns.getNamespaceURI();
		assertEquals(wsaddresingNamespace, nsuri);
	}

	@ValidationFault(id="WSA007", dependsOn="WSAparse", msg="Validate FaultTo Namespace", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateFaultToNamespace() throws SoapFaultException {
		if (faultTo.size() == 0)
			return;
		assertEquals(1, faultTo.size());
		OMElement ele = faultTo.get(0);
		OMNamespace omns = ele.getNamespace();
		String nsuri = omns.getNamespaceURI();
		assertEquals(wsaddresingNamespace, nsuri);
	}

	@ValidationFault(id="WSA008", dependsOn="WSAparse", msg="Multiple WS-Addressing To headers are not allowed", faultCode=FaultCode.InvalidAddressingHeader, ref=wsaddressingRef + "#msgaddrpropsinfoset")
	public void validateToRequired() throws SoapFaultException {
		assertFalse(to.size() > 1);
	}
	
	@ValidationFault(id="WSA009", dependsOn="WSAparse", msg="Multiple WS-Addressing From headers are not allowed", faultCode=FaultCode.InvalidAddressingHeader, ref=wsaddressingRef + "#msgaddrpropsinfoset")
	public void validateFromRequired() throws SoapFaultException {
		assertFalse(from.size() > 1);
	}
	
	@ValidationFault(id="WSA010", dependsOn="WSAparse", msg="Multiple WS-Addressing ReplyTo headers are not allowed", faultCode=FaultCode.InvalidAddressingHeader, ref=wsaddressingRef + "#msgaddrpropsinfoset")
	public void validateReplyToRequired() throws SoapFaultException {
		assertFalse(replyTo.size() > 1);
	}
	
	@ValidationFault(id="WSA011", dependsOn="WSAparse", msg="Multiple WS-Addressing FaultTo headers are not allowed", faultCode=FaultCode.InvalidAddressingHeader, ref=wsaddressingRef + "#msgaddrpropsinfoset")
	public void validateFaultToRequired() throws SoapFaultException {
		assertFalse(faultTo.size() > 1);
	}
	
	@ValidationFault(id="WSA012", dependsOn="WSAparse", msg="A Single WS-Addressing Action header is required", faultCode=FaultCode.InvalidAddressingHeader, ref=wsaddressingRef + "#msgaddrpropsinfoset")
	public void validateSingleActionRequired() throws SoapFaultException {
		assertEquals(1, action.size());
	}
	
	@ValidationFault(id="WSA013", dependsOn="WSAparse", msg="Multiple WS-Addressing MessageId headers are not allowed", faultCode=FaultCode.InvalidAddressingHeader, ref=wsaddressingRef + "#msgaddrpropsinfoset")
	public void validateMessageIdRequired() throws SoapFaultException {
		assertFalse(messageId.size() > 1);
	}
	
	@ValidationFault(id="WSA014", dependsOn="WSAparse", msg="At least one WS-Addressing SOAP header element must have a soapenv:mustUnderstand=\"true\"", faultCode=FaultCode.MustUnderstand, ref="http://www.w3.org/TR/soap12-part0/#L4697")
	public void validateMustUnderstand() throws SoapFaultException {
		List<OMElement> hdrs = new ArrayList<OMElement> ();
		hdrs.addAll(messageId);
		hdrs.addAll(relatesTo);
		hdrs.addAll(to);
		hdrs.addAll(action);
		hdrs.addAll(from);
		hdrs.addAll(replyTo);
		hdrs.addAll(faultTo);

		boolean mufound = false;
		for (OMElement hdr : hdrs) {
			String mu = hdr.getAttributeValue(new QName("http://www.w3.org/2003/05/soap-envelope","mustUnderstand"));
			if (mu == null)
				continue;
			mufound = true;
			if (mustUnderstandValueOk(mu)) {
				mufound = true;
				break;
			}
		}
		assertTrue(mufound);
	}
	
	@ValidationFault(id="WSA015", dependsOn="WSAparse", msg="Validate ReplyTo is HTTP style endpoint", faultCode=FaultCode.EndpointUnavailable, ref=wsaddressingRef)
	public void validateReplyToHttpStyle() throws SoapFaultException {
		endpointCheckHttpStyle(replyTo);
	}
	
	@ValidationFault(id="WSA016", dependsOn="WSAparse", msg="Validate From is HTTP style endpoint", faultCode=FaultCode.EndpointUnavailable, ref=wsaddressingRef)
	public void validateFromHttpStyle() throws SoapFaultException {
		endpointCheckHttpStyle(from);
	}
	
	@ValidationFault(id="WSA017", dependsOn="WSAparse", msg="Validate FaultTo is HTTP style endpoint", faultCode=FaultCode.EndpointUnavailable, ref=wsaddressingRef)
	public void validateFaultToHttpStyle() throws SoapFaultException {
		endpointCheckHttpStyle(faultTo);
	}
	
	/**********************************
	 * From Endpoint
	 **********************************/
	OMElement fromEndpoint = null;
	String fromEndpointValue = null;
	
	@ValidationFault(id="FromEndpointParse", dependsOn="WSAparse", msg="Parse From endpoint", faultCode=FaultCode.EndpointUnavailable, ref=wsaddressingRef)
	public void parseFromEndpoint() throws SoapFaultException {
		fromEndpoint = getFirst(from);
		if (fromEndpoint != null)
			fromEndpointValue = fromEndpoint.getText();
	}

	@ValidationFault(id="WSA018", dependsOn="FromEndpointParse", msg="From endpoint element name must be Address", faultCode=FaultCode.EndpointUnavailable, ref=wsaddressingRef)
	public void validateFromElementIsAddress() throws SoapFaultException {
		if (fromEndpoint == null)
			return;
		assertEquals("Address", fromEndpoint.getLocalName());
	}

	@ValidationFault(id="WSA019", dependsOn="FromEndpointParse", msg="From endpoint element namespace", faultCode=FaultCode.EndpointUnavailable, ref=wsaddressingRef)
	public void validateFromElementNamespace() throws SoapFaultException {
		if (fromEndpoint == null)
			return;
		assertEquals(wsaddresingNamespace, fromEndpoint.getNamespace().getNamespaceURI());
	}

	@ValidationFault(id="WSA020", dependsOn="FromEndpointParse", msg="From endpoint must have http prefix", faultCode=FaultCode.EndpointUnavailable, ref=wsaddressingRef)
	public void validateFromValuePrefix() throws SoapFaultException {
		if (fromEndpointValue == null)
			return;
		if (fromEndpointValue.startsWith("urn:"))
			return;
		assertTrue(fromEndpointValue.startsWith("http"));
	}

	/**********************************
	 * ReplyTo Endpoint
	 **********************************/
	OMElement replyToEndpoint = null;
	String replyToEndpointValue = null;
	
	@ValidationFault(id="ReplyToEndpointParse", dependsOn="WSAparse", msg="Parse ReplyTo endpoint", faultCode=FaultCode.EndpointUnavailable, ref=wsaddressingRef)
	public void parseReplyToEndpoint() throws SoapFaultException {
		replyToEndpoint = getFirst(replyTo);
		if (replyToEndpoint != null)
			replyToEndpointValue = replyToEndpoint.getText();
	}

	@ValidationFault(id="WSA021", dependsOn="ReplyToEndpointParse", msg="ReplyTo endpoint element name must be Address", faultCode=FaultCode.EndpointUnavailable, ref=wsaddressingRef)
	public void validateReplyToElementIsAddress() throws SoapFaultException {
		if (replyToEndpoint == null)
			return;
		assertEquals("Address", replyToEndpoint.getLocalName());
	}

	@ValidationFault(id="WSA022", dependsOn="ReplyToEndpointParse", msg="ReplyTo endpoint element namespace", faultCode=FaultCode.EndpointUnavailable, ref=wsaddressingRef)
	public void validateReplyToElementNamespace() throws SoapFaultException {
		if (replyToEndpoint == null)
			return;
		assertEquals(wsaddresingNamespace, replyToEndpoint.getNamespace().getNamespaceURI());
	}

	@ValidationFault(id="WSA023", dependsOn="ReplyToEndpointParse", msg="ReplyTo endpoint value prefix", faultCode=FaultCode.EndpointUnavailable, ref=wsaddressingRef)
	public void validateReplyToValuePrefix() throws SoapFaultException {
		if (replyToEndpointValue == null)
			return;
		assertTrue(replyToEndpointValue.startsWith("http"));
	}

	/**********************************
	 * FaultTo Endpoint
	 **********************************/
	OMElement faultToEndpoint = null;
	String faultToEndpointValue = null;
	
	@ValidationFault(id="FaultToEndpointParse", dependsOn="WSAparse", msg="Parse FaultTo endpoint", faultCode=FaultCode.EndpointUnavailable, ref=wsaddressingRef)
	public void parseFaultToEndpoint() throws SoapFaultException {
		faultToEndpoint = getFirst(faultTo);
		if (faultToEndpoint != null)
			faultToEndpointValue = faultToEndpoint.getText();
	}

	@ValidationFault(id="WSA024", dependsOn="FaultToEndpointParse", msg="FaultTo endpoint element name must be Address", faultCode=FaultCode.EndpointUnavailable, ref=wsaddressingRef)
	public void validateFaultToElementIsAddress() throws SoapFaultException {
		if (faultToEndpoint == null)
			return;
		assertEquals("Address", faultToEndpoint.getLocalName());
	}

	@ValidationFault(id="WSA025", dependsOn="FaultToEndpointParse", msg="FaultTo endpoint element namespace", faultCode=FaultCode.EndpointUnavailable, ref=wsaddressingRef)
	public void validateFaultToElementNamespace() throws SoapFaultException {
		if (faultToEndpoint == null)
			return;
		assertEquals(wsaddresingNamespace, faultToEndpoint.getNamespace().getNamespaceURI());
	}

	@ValidationFault(id="WSA026", dependsOn="FaultToEndpointParse", msg="FaultTo endpoint value prefix", faultCode=FaultCode.EndpointUnavailable, ref=wsaddressingRef)
	public void validateFaultToValuePrefix() throws SoapFaultException {
		if (faultToEndpointValue == null)
			return;
		assertTrue(faultToEndpointValue.startsWith("http"));
	}

	/**********************************
	 * To Endpoint
	 **********************************/
	OMElement toEndpoint = null;
	String toEndpointValue = null;
	
	@ValidationFault(id="ToEndpointParse", dependsOn="WSAparse", msg="Parse To endpoint", faultCode=FaultCode.EndpointUnavailable, ref=wsaddressingRef)
	public void parseToEndpoint() throws SoapFaultException {
		toEndpoint = getFirst(to);
		if (toEndpoint != null)
			toEndpointValue = toEndpoint.getText();
	}

	@ValidationFault(id="WSA027", dependsOn="ToEndpointParse", msg="To endpoint value prefix", faultCode=FaultCode.EndpointUnavailable, ref=wsaddressingRef)
	public void validateToValuePrefix() throws SoapFaultException {
		if (toEndpointValue == null)
			return;
		assertTrue(toEndpointValue.startsWith("http"));
	}
	
	/**********************************************/

	OMElement getFirst(List<OMElement> eles) {
		for (OMElement ele : eles) {
			OMElement first = ele.getFirstElement();
			return first;
		}		
		return null;
	}
	
	void validateWSAddressing() throws SoapFaultException {
		if (header == null)
			return;

		validationEngine.run();
		
		if (action != null && action.size() > 0) {
			OMElement aEle = action.get(0);
			soapEnvironment.setRequestAction(aEle.getText());
		}

		if (messageId != null && messageId.size() > 0) {
			OMElement mid = messageId.get(0);
			soapEnvironment.setMessageId(mid.getText());
		}
	}

	boolean mustUnderstandValueOk(String value) {
		if ("1".equals(value)) return true;
		if ("true".equalsIgnoreCase("true")) return true;
		return false;
	}

	void endpointCheckHttpStyle(List<OMElement> eles) throws SoapFaultException {
		for (OMElement ele : eles) {
			OMElement first = ele.getFirstElement();
			assertNotNull(first);
		}
	}
}
