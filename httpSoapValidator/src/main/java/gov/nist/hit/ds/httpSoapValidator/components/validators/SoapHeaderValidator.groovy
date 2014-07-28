package gov.nist.hit.ds.httpSoapValidator.components.validators
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ValidationFault
import gov.nist.hit.ds.soapSupport.FaultCode
import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.utilities.xml.XmlUtil
import org.apache.axiom.om.OMElement
import org.apache.axiom.om.OMNamespace

import javax.xml.namespace.QName

public class SoapHeaderValidator  extends ValComponentBase {
    SimHandle handle
    OMElement header;
    static final String wsaddresingNamespace = "http://www.w3.org/2005/08/addressing";
    String expectedAction = null;
    List<OMElement> messageId;
    List<OMElement> relatesTo;
    List<OMElement> to;
    List<OMElement> action;
    List<OMElement> from;
    List<OMElement> replyTo;
    List<OMElement> faultTo;

    SoapHeaderValidator(SimHandle handle, OMElement header) {
        super(handle.event)
        this.handle = handle
        this.header = header
        expectedAction = handle.transactionType.requestAction
    }

    @ValidationFault(id="WSAparse", msg="Parsing WSA header fields", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void parseWSAddressingFields() throws SoapFaultException {
		messageId = XmlUtil.childrenWithLocalName(header, "MessageID")
		relatesTo = XmlUtil.childrenWithLocalName(header, "RelatesTo")
		to = XmlUtil.childrenWithLocalName(header, "To")
		action = XmlUtil.childrenWithLocalName(header, "Action")
		from = XmlUtil.childrenWithLocalName(header, "From")
		replyTo = XmlUtil.childrenWithLocalName(header, "ReplyTo")
		faultTo = XmlUtil.childrenWithLocalName(header, "FaultTo")
	}

	@ValidationFault(id="WSA011", dependsOn="WSAparse", msg="Validate MessageId Namespace", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateMessageIdNamespace() throws SoapFaultException {
		if (messageId.size() == 0)
			return;
		OMElement ele = messageId.get(0);
		OMNamespace omns = ele.getNamespace();
		String nsuri = omns.getNamespaceURI();
		assertEquals(wsaddresingNamespace, nsuri);
	}

	@ValidationFault(id="WSA021", dependsOn="WSAparse", msg="Validate RelatesTo Namespace if present", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateRelatesToNamespace() throws SoapFaultException {
		if (relatesTo.size() == 0) { msg('Not present'); return }
		OMElement ele = relatesTo.get(0);
		OMNamespace omns = ele.getNamespace();
		String nsuri = omns.getNamespaceURI();
		assertEquals(wsaddresingNamespace, nsuri);
	}

	@ValidationFault(id="WSA030", dependsOn="WSAparse", msg="Validate To namespace if present", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateToNamespace() throws SoapFaultException {
		if (to.size() == 0)
			return;
		OMElement ele = to.get(0);
		OMNamespace omns = ele.getNamespace();
		String nsuri = omns.getNamespaceURI();
		assertEquals(wsaddresingNamespace, nsuri);
	}

	@ValidationFault(id="WSA290", dependsOn="WSAparse", msg="Validate Action Value", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateActionValue() throws SoapFaultException {
		if (action.size() == 0) { fail('Not present') }
		assertEquals(expectedAction, action.get(0).getText());
	}

	@ValidationFault(id="WSA040", dependsOn="WSAparse", msg="Validate Action Namespace", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateActionNamespace() throws SoapFaultException {
        if (action.size() == 0) { fail('Not present') }
		OMElement ele = action.get(0);
		OMNamespace omns = ele.getNamespace();
		String nsuri = omns.getNamespaceURI();
		assertEquals(wsaddresingNamespace, nsuri);
	}

	@ValidationFault(id="WSA050", dependsOn="WSAparse", msg="Validate From namespace if present", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateFromNamespace() throws SoapFaultException {
		if (from.size() == 0) { msg('Not present'); return }
		OMElement ele = from.get(0);
		OMNamespace omns = ele.getNamespace();
		String nsuri = omns.getNamespaceURI();
		assertEquals(wsaddresingNamespace, nsuri);
	}

	@ValidationFault(id="WSA060", dependsOn="WSAparse", msg="Validate ReplyTo namespace if present", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateReplyToNamespace() throws SoapFaultException {
		if (replyTo.size() == 0) { msg('Not present'); return }
		OMElement ele = replyTo.get(0);
		OMNamespace omns = ele.getNamespace();
		String nsuri = omns.getNamespaceURI();
		assertEquals(wsaddresingNamespace, nsuri);
	}

	@ValidationFault(id="WSA070", dependsOn="WSAparse", msg="Validate FaultTo namespace if present", faultCode=FaultCode.Sender, ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateFaultToNamespace() throws SoapFaultException {
		if (faultTo.size() == 0) { msg('Not present'); return }
		OMElement ele = faultTo.get(0);
		OMNamespace omns = ele.getNamespace();
		String nsuri = omns.getNamespaceURI();
		assertEquals(wsaddresingNamespace, nsuri);
	}

	@ValidationFault(id="WSA080", dependsOn="WSAparse", msg="Multiple WS-Addressing To headers are not allowed", faultCode=FaultCode.InvalidAddressingHeader, ref="http://www.w3.org/TR/ws-addr-core#msgaddrpropsinfoset")
	public void validateToRequired() throws SoapFaultException {
		assertFalse(to.size() > 1);
	}

	@ValidationFault(id="WSA090", dependsOn="WSAparse", msg="Multiple WS-Addressing From headers are not allowed", faultCode=FaultCode.InvalidAddressingHeader, ref="http://www.w3.org/TR/ws-addr-core#msgaddrpropsinfoset")
	public void validateFromRequired() throws SoapFaultException {
		assertFalse(from.size() > 1);
	}

	@ValidationFault(id="WSA100", dependsOn="WSAparse", msg="Multiple WS-Addressing ReplyTo headers are not allowed", faultCode=FaultCode.InvalidAddressingHeader, ref="http://www.w3.org/TR/ws-addr-core#msgaddrpropsinfoset")
	public void validateReplyToRequired() throws SoapFaultException {
		assertFalse(replyTo.size() > 1);
	}

	@ValidationFault(id="WSA110", dependsOn="WSAparse", msg="Multiple WS-Addressing FaultTo headers are not allowed", faultCode=FaultCode.InvalidAddressingHeader, ref="http://www.w3.org/TR/ws-addr-core#msgaddrpropsinfoset")
	public void validateFaultToRequired() throws SoapFaultException {
		assertFalse(faultTo.size() > 1);
	}

	@ValidationFault(id="WSA120", dependsOn="WSAparse", msg="A Single WS-Addressing Action header is required", faultCode=FaultCode.InvalidAddressingHeader, ref="http://www.w3.org/TR/ws-addr-core#msgaddrpropsinfoset")
	public void validateSingleActionRequired() throws SoapFaultException {
		assertEquals(1, action.size());
	}

	@ValidationFault(id="WSA130", dependsOn="WSAparse", msg="Multiple WS-Addressing MessageId headers are not allowed", faultCode=FaultCode.InvalidAddressingHeader, ref="http://www.w3.org/TR/ws-addr-core#msgaddrpropsinfoset")
	public void validateMessageIdRequired() throws SoapFaultException {
		assertFalse(messageId.size() > 1);
	}

	@ValidationFault(id="WSA140", dependsOn="WSAparse", msg="At least one WS-Addressing SOAP header element must have a soapenv:mustUnderstand=\"true\"", faultCode=FaultCode.MustUnderstand, ref="http://www.w3.org/TR/soap12-part0/#L4697")
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

	@ValidationFault(id="WSA150", dependsOn="WSAparse", msg="Validate ReplyTo must be an Endpoint Reference", faultCode=FaultCode.EndpointUnavailable, ref="http://www.w3.org/TR/ws-addr-core#abstractmaps")
	public void validateReplyToHttpStyle() throws SoapFaultException {
		validateEndpointReference(replyTo);
	}

	@ValidationFault(id="WSA160", dependsOn="WSAparse", msg="Validate From must be an Endpoint Reference", faultCode=FaultCode.EndpointUnavailable, ref="http://www.w3.org/TR/ws-addr-core#abstractmaps")
	public void validateFromHttpStyle() throws SoapFaultException {
		validateEndpointReference(from);
	}

	@ValidationFault(id="WSA170", dependsOn="WSAparse", msg="Validate FaultTo must be an Endpoint Reference", faultCode=FaultCode.EndpointUnavailable, ref="http://www.w3.org/TR/ws-addr-core#abstractmaps")
	public void validateFaultToHttpStyle() throws SoapFaultException {
		validateEndpointReference(faultTo);
	}

	/**********************************
	 * From Endpoint
	 **********************************/
	OMElement fromEndpoint = null;
	String fromEndpointValue = null;

	@ValidationFault(id="FromEndpointParse", dependsOn="WSAparse", msg="Parse From endpoint", faultCode=FaultCode.EndpointUnavailable, ref="http://www.w3.org/TR/ws-addr-core")
	public void parseFromEndpoint() throws SoapFaultException {
		fromEndpoint = getFirst(from);
		if (fromEndpoint != null)
			fromEndpointValue = fromEndpoint.getText();
	}

	@ValidationFault(id="WSA180", dependsOn="FromEndpointParse", msg="From endpoint element name must be Address if present", faultCode=FaultCode.EndpointUnavailable, ref="http://www.w3.org/TR/ws-addr-core")
	public void validateFromElementIsAddress() throws SoapFaultException {
		if (fromEndpoint == null) { msg('Not present'); return }
		assertEquals("Address", fromEndpoint.getLocalName());
	}

	@ValidationFault(id="WSA190", dependsOn="FromEndpointParse", msg="Parse From endpoint element namespace if present", faultCode=FaultCode.EndpointUnavailable, ref="http://www.w3.org/TR/ws-addr-core")
	public void validateFromElementNamespace() throws SoapFaultException {
		if (fromEndpoint == null) { msg('Not present'); return }
		assertEquals(wsaddresingNamespace, fromEndpoint.getNamespace().getNamespaceURI());
	}

	@ValidationFault(id="WSA200", dependsOn="FromEndpointParse", msg="From endpoint must have http prefix if present", faultCode=FaultCode.EndpointUnavailable, ref="http://www.w3.org/TR/ws-addr-core")
	public void validateFromValuePrefix() throws SoapFaultException {
		if (fromEndpointValue == null) { msg('Not present'); return }
        infoFound(fromEndpointValue)
		if (fromEndpointValue.startsWith("urn:"))
			return;
		assertTrue(fromEndpointValue.startsWith("http"));
	}

	/**********************************
	 * ReplyTo Endpoint
	 **********************************/
	OMElement replyToEndpoint = null;
	String replyToEndpointValue = null;

	@ValidationFault(id="ReplyToEndpointParse", dependsOn="WSAparse", msg="Parse ReplyTo endpoint if present", faultCode=FaultCode.EndpointUnavailable, ref="http://www.w3.org/TR/ws-addr-core")
	public void parseReplyToEndpoint() throws SoapFaultException {
		replyToEndpoint = getFirst(replyTo);
		if (replyToEndpoint != null)
			replyToEndpointValue = replyToEndpoint.getText();
        else msg('Not present')
	}

	@ValidationFault(id="WSA210", dependsOn="ReplyToEndpointParse", msg="ReplyTo endpoint element name must be Address if present", faultCode=FaultCode.EndpointUnavailable, ref="http://www.w3.org/TR/ws-addr-core")
	public void validateReplyToElementIsAddress() throws SoapFaultException {
		if (replyToEndpoint == null) { msg('Not present'); return }
		assertEquals("Address", replyToEndpoint.getLocalName());
	}

	@ValidationFault(id="WSA220", dependsOn="ReplyToEndpointParse", msg="Verify ReplyTo endpoint element namespace if present", faultCode=FaultCode.EndpointUnavailable, ref="http://www.w3.org/TR/ws-addr-core")
	public void validateReplyToElementNamespace() throws SoapFaultException {
		if (replyToEndpoint == null) { msg('Not present'); return }
		assertEquals(wsaddresingNamespace, replyToEndpoint.getNamespace().getNamespaceURI());
	}

	@ValidationFault(id="WSA230", dependsOn="ReplyToEndpointParse", msg="Verify ReplyTo endpoint value prefix if present", faultCode=FaultCode.EndpointUnavailable, ref="http://www.w3.org/TR/ws-addr-core")
	public void validateReplyToValuePrefix() throws SoapFaultException {
		if (replyToEndpointValue == null) { msg('Not present'); return }
        infoFound(replyToEndpoint)
		assertTrue(replyToEndpointValue.startsWith("http"));
	}

	/**********************************
	 * FaultTo Endpoint
	 **********************************/
	OMElement faultToEndpoint = null;
	String faultToEndpointValue = null;

	@ValidationFault(id="FaultToEndpointParse", dependsOn="WSAparse", msg="Parse FaultTo endpoint", faultCode=FaultCode.EndpointUnavailable, ref="http://www.w3.org/TR/ws-addr-core")
	public void parseFaultToEndpoint() throws SoapFaultException {
		faultToEndpoint = getFirst(faultTo);
		if (faultToEndpoint != null)
			faultToEndpointValue = faultToEndpoint.getText();
	}

	@ValidationFault(id="WSA240", dependsOn="FaultToEndpointParse", msg="FaultTo endpoint element name must be Address if present", faultCode=FaultCode.EndpointUnavailable, ref="http://www.w3.org/TR/ws-addr-core")
	public void validateFaultToElementIsAddress() throws SoapFaultException {
		if (faultToEndpoint == null) { msg('Not present'); return }
		assertEquals("Address", faultToEndpoint.getLocalName());
	}

	@ValidationFault(id="WSA250", dependsOn="FaultToEndpointParse", msg="Verify FaultTo endpoint element namespace if present", faultCode=FaultCode.EndpointUnavailable, ref="http://www.w3.org/TR/ws-addr-core")
	public void validateFaultToElementNamespace() throws SoapFaultException {
		if (faultToEndpoint == null) { msg('Not present'); return }
		assertEquals(wsaddresingNamespace, faultToEndpoint.getNamespace().getNamespaceURI());
	}

	@ValidationFault(id="WSA260", dependsOn="FaultToEndpointParse", msg="Verify FaultTo endpoint has http prefix if present", faultCode=FaultCode.EndpointUnavailable, ref="http://www.w3.org/TR/ws-addr-core")
	public void validateFaultToValuePrefix() throws SoapFaultException {
		if (faultToEndpointValue == null) { msg('Not present'); return }
        infoFound(faultToEndpoint)
		assertTrue(faultToEndpointValue.startsWith("http"));
	}

	/**********************************
	 * To Endpoint
	 **********************************/
	OMElement toEndpoint = null;
	String toEndpointValue = null;

	@ValidationFault(id="ToEndpointParse", dependsOn="WSAparse", msg="Parse To endpoint", faultCode=FaultCode.EndpointUnavailable, ref="http://www.w3.org/TR/ws-addr-core")
	public void parseToEndpoint() throws SoapFaultException {
		toEndpoint = getFirst(to);
		if (toEndpoint != null)
			toEndpointValue = toEndpoint.getText();
	}

	@ValidationFault(id="WSA270", dependsOn="ToEndpointParse", msg="Verify To endpoint has http prefix if present", faultCode=FaultCode.EndpointUnavailable, ref="http://www.w3.org/TR/ws-addr-core")
	public void validateToValuePrefix() throws SoapFaultException {
        if (toEndpointValue == null) { msg('Not present'); return }
        infoFound(toEndpointValue)
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

	boolean mustUnderstandValueOk(String value) {
		if ("1".equals(value)) return true;
		if ("true".equalsIgnoreCase("true")) return true;
		return false;
	}

	static QName addressQName = new QName("http://www.w3.org/2005/08/addressing", "Address");

	void validateEndpointReference(List<OMElement> eles) throws SoapFaultException {
		if (eles.isEmpty()) { msg('Not present'); return }
		OMElement endpointReference = eles.get(0);
		assertNotNullNoLog(endpointReference);
		@SuppressWarnings("unchecked")
		Iterator<OMElement> addresses = (Iterator<OMElement>) endpointReference.getChildrenWithName(addressQName);
		assertNotNullNoLog(addresses);
		boolean hasAddress = addresses.hasNext();
		assertTrueNoLog(hasAddress);
		OMElement address = addresses.next();
		boolean hasMultipleAddress = addresses.hasNext();
		assertTrueNoLog(!hasMultipleAddress);
		assertTrue(address.getText().startsWith("http"));
	}

    @Override
    public void run() throws SoapFaultException, RepositoryException {
        runValidationEngine()
    }

    @Override
    public boolean showOutputInLogs() {
        return false
    }
}
