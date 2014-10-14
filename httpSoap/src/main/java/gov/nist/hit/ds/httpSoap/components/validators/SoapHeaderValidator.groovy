package gov.nist.hit.ds.httpSoap.components.validators
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.DependsOn
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Fault
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
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

    @Fault(code=FaultCode.Sender)
    @Validation(id="WSAparse", msg="Parsing WSA header fields", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void parseWSAddressingFields() throws SoapFaultException {
		messageId = XmlUtil.childrenWithLocalName(header, "MessageID")
		relatesTo = XmlUtil.childrenWithLocalName(header, "RelatesTo")
		to = XmlUtil.childrenWithLocalName(header, "To")
		action = XmlUtil.childrenWithLocalName(header, "Action")
		from = XmlUtil.childrenWithLocalName(header, "From")
		replyTo = XmlUtil.childrenWithLocalName(header, "ReplyTo")
		faultTo = XmlUtil.childrenWithLocalName(header, "FaultTo")
	}

    @DependsOn(ids=["WSAparse"])
    @Fault(code=FaultCode.Sender)
	@Validation(id="WSA011", msg="Validate MessageId Namespace", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateMessageIdNamespace() throws SoapFaultException {
		if (messageId.size() == 0)
			return;
		OMElement ele = messageId.get(0);
		OMNamespace omns = ele.getNamespace();
		String nsuri = omns.getNamespaceURI();
		assertEquals(wsaddresingNamespace, nsuri);
	}

    @DependsOn(ids=["WSAparse"])
    @Fault(code=FaultCode.Sender)
	@Validation(id="WSA021", msg="Validate RelatesTo Namespace if present", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateRelatesToNamespace() throws SoapFaultException {
		if (relatesTo.size() == 0) { msg('Not present'); return }
		OMElement ele = relatesTo.get(0);
		OMNamespace omns = ele.getNamespace();
		String nsuri = omns.getNamespaceURI();
		assertEquals(wsaddresingNamespace, nsuri);
	}

    @DependsOn(ids=["WSAparse"])
    @Fault(code=FaultCode.Sender)
	@Validation(id="WSA030", msg="Validate To namespace if present", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateToNamespace() throws SoapFaultException {
		if (to.size() == 0)
			return;
		OMElement ele = to.get(0);
		OMNamespace omns = ele.getNamespace();
		String nsuri = omns.getNamespaceURI();
		assertEquals(wsaddresingNamespace, nsuri);
	}

    @DependsOn(ids=["WSAparse"])
    @Fault(code=FaultCode.Sender)
	@Validation(id="WSA290", msg="Validate Action Value", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateActionValue() throws SoapFaultException {
		if (action.size() == 0) { fail('Not present') }
		assertEquals(expectedAction, action.get(0).getText());
	}

    @DependsOn(ids=["WSAparse"])
    @Fault(code=FaultCode.Sender)
	@Validation(id="WSA040", msg="Validate Action Namespace", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateActionNamespace() throws SoapFaultException {
        if (action.size() == 0) { fail('Not present') }
		OMElement ele = action.get(0);
		OMNamespace omns = ele.getNamespace();
		String nsuri = omns.getNamespaceURI();
		assertEquals(wsaddresingNamespace, nsuri);
	}

    @DependsOn(ids=["WSAparse"])
    @Fault(code=FaultCode.Sender)
	@Validation(id="WSA050", msg="Validate From namespace if present", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateFromNamespace() throws SoapFaultException {
		if (from.size() == 0) { msg('Not present'); return }
		OMElement ele = from.get(0);
		OMNamespace omns = ele.getNamespace();
		String nsuri = omns.getNamespaceURI();
		assertEquals(wsaddresingNamespace, nsuri);
	}

    @DependsOn(ids=["WSAparse"])
    @Fault(code=FaultCode.Sender)
	@Validation(id="WSA060", msg="Validate ReplyTo namespace if present", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateReplyToNamespace() throws SoapFaultException {
		if (replyTo.size() == 0) { msg('Not present'); return }
		OMElement ele = replyTo.get(0);
		OMNamespace omns = ele.getNamespace();
		String nsuri = omns.getNamespaceURI();
		assertEquals(wsaddresingNamespace, nsuri);
	}

    @DependsOn(ids=["WSAparse"])
    @Fault(code=FaultCode.Sender)
	@Validation(id="WSA070", msg="Validate FaultTo namespace if present", ref="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv")
	public void validateFaultToNamespace() throws SoapFaultException {
		if (faultTo.size() == 0) { msg('Not present'); return }
		OMElement ele = faultTo.get(0);
		OMNamespace omns = ele.getNamespace();
		String nsuri = omns.getNamespaceURI();
		assertEquals(wsaddresingNamespace, nsuri);
	}

    @DependsOn(ids=["WSAparse"])
    @Fault(code=FaultCode.InvalidAddressingHeader)
	@Validation(id="WSA080", msg="Multiple WS-Addressing To headers are not allowed", ref="http://www.w3.org/TR/ws-addr-core#msgaddrpropsinfoset")
	public void validateToRequired() throws SoapFaultException {
		assertFalse(to.size() > 1);
	}

    @DependsOn(ids=["WSAparse"])
    @Fault(code=FaultCode.InvalidAddressingHeader)
	@Validation(id="WSA090", msg="Multiple WS-Addressing From headers are not allowed", ref="http://www.w3.org/TR/ws-addr-core#msgaddrpropsinfoset")
	public void validateFromRequired() throws SoapFaultException {
		assertFalse(from.size() > 1);
	}

    @DependsOn(ids=["WSAparse"])
    @Fault(code=FaultCode.InvalidAddressingHeader)
	@Validation(id="WSA100", msg="Multiple WS-Addressing ReplyTo headers are not allowed", ref="http://www.w3.org/TR/ws-addr-core#msgaddrpropsinfoset")
	public void validateReplyToRequired() throws SoapFaultException {
		assertFalse(replyTo.size() > 1);
	}

    @DependsOn(ids=["WSAparse"])
    @Fault(code=FaultCode.InvalidAddressingHeader)
	@Validation(id="WSA110", msg="Multiple WS-Addressing FaultTo headers are not allowed", ref="http://www.w3.org/TR/ws-addr-core#msgaddrpropsinfoset")
	public void validateFaultToRequired() throws SoapFaultException {
		assertFalse(faultTo.size() > 1);
	}

    @DependsOn(ids=["WSAparse"])
    @Fault(code=FaultCode.InvalidAddressingHeader)
	@Validation(id="WSA120", msg="A Single WS-Addressing Action header is required", ref="http://www.w3.org/TR/ws-addr-core#msgaddrpropsinfoset")
	public void validateSingleActionRequired() throws SoapFaultException {
		assertEquals(1, action.size());
	}

    @DependsOn(ids=["WSAparse"])
    @Fault(code=FaultCode.InvalidAddressingHeader)
	@Validation(id="WSA130", msg="Multiple WS-Addressing MessageId headers are not allowed", ref="http://www.w3.org/TR/ws-addr-core#msgaddrpropsinfoset")
	public void validateMessageIdRequired() throws SoapFaultException {
		assertFalse(messageId.size() > 1);
	}

    @DependsOn(ids=["WSAparse"])
    @Fault(code=FaultCode.MustUnderstand)
	@Validation(id="WSA140", msg="At least one WS-Addressing SOAP header element must have a soapenv:mustUnderstand=\"true\"", ref="http://www.w3.org/TR/soap12-part0/#L4697")
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

    @DependsOn(ids=["WSAparse"])
    @Fault(code=FaultCode.EndpointUnavailable)
	@Validation(id="WSA150", msg="Validate ReplyTo must be an Endpoint Reference", ref="http://www.w3.org/TR/ws-addr-core#abstractmaps")
	public void validateReplyToHttpStyle() throws SoapFaultException {
		validateEndpointReference(replyTo);
	}

    @DependsOn(ids=["WSAparse"])
    @Fault(code=FaultCode.EndpointUnavailable)
	@Validation(id="WSA160", msg="Validate From must be an Endpoint Reference", ref="http://www.w3.org/TR/ws-addr-core#abstractmaps")
	public void validateFromHttpStyle() throws SoapFaultException {
		validateEndpointReference(from);
	}

    @DependsOn(ids=["WSAparse"])
    @Fault(code=FaultCode.EndpointUnavailable)
	@Validation(id="WSA170", msg="Validate FaultTo must be an Endpoint Reference", ref="http://www.w3.org/TR/ws-addr-core#abstractmaps")
	public void validateFaultToHttpStyle() throws SoapFaultException {
		validateEndpointReference(faultTo);
	}

	/**********************************
	 * From Endpoint
	 **********************************/
	OMElement fromEndpoint = null;
	String fromEndpointValue = null;

    @DependsOn(ids=["WSAparse"])
    @Fault(code=FaultCode.EndpointUnavailable)
	@Validation(id="FromEndpointParse", msg="Parse From endpoint", ref="http://www.w3.org/TR/ws-addr-core")
	public void parseFromEndpoint() throws SoapFaultException {
		fromEndpoint = getFirst(from);
		if (fromEndpoint != null)
			fromEndpointValue = fromEndpoint.getText();
	}

    @DependsOn(ids=["FromEndpointParse"])
    @Fault(code=FaultCode.EndpointUnavailable)
	@Validation(id="WSA180",  msg="From endpoint element displayName must be Address if present", ref="http://www.w3.org/TR/ws-addr-core")
	public void validateFromElementIsAddress() throws SoapFaultException {
		if (fromEndpoint == null) { msg('Not present'); return }
		assertEquals("Address", fromEndpoint.getLocalName());
	}

    @DependsOn(ids=["FromEndpointParse"])
    @Fault(code=FaultCode.EndpointUnavailable)
	@Validation(id="WSA190", msg="Parse From endpoint element namespace if present", ref="http://www.w3.org/TR/ws-addr-core")
	public void validateFromElementNamespace() throws SoapFaultException {
		if (fromEndpoint == null) { msg('Not present'); return }
		assertEquals(wsaddresingNamespace, fromEndpoint.getNamespace().getNamespaceURI());
	}

    @DependsOn(ids=["FromEndpointParse"])
    @Fault(code=FaultCode.EndpointUnavailable)
	@Validation(id="WSA200", msg="From endpoint must have http prefix if present", ref="http://www.w3.org/TR/ws-addr-core")
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

    @DependsOn(ids=["WSAparse"])
    @Fault(code=FaultCode.EndpointUnavailable)
	@Validation(id="ReplyToEndpointParse", msg="Parse ReplyTo endpoint if present", ref="http://www.w3.org/TR/ws-addr-core")
	public void parseReplyToEndpoint() throws SoapFaultException {
		replyToEndpoint = getFirst(replyTo);
		if (replyToEndpoint != null)
			replyToEndpointValue = replyToEndpoint.getText();
        else msg('Not present')
	}

    @DependsOn(ids=["ReplyToEndpointParse"])
    @Fault(code=FaultCode.EndpointUnavailable)
	@Validation(id="WSA210", msg="ReplyTo endpoint element displayName must be Address if present", ref="http://www.w3.org/TR/ws-addr-core")
	public void validateReplyToElementIsAddress() throws SoapFaultException {
		if (replyToEndpoint == null) { msg('Not present'); return }
		assertEquals("Address", replyToEndpoint.getLocalName());
	}

    @DependsOn(ids=["ReplyToEndpointParse"])
    @Fault(code=FaultCode.EndpointUnavailable)
	@Validation(id="WSA220", msg="Verify ReplyTo endpoint element namespace if present", ref="http://www.w3.org/TR/ws-addr-core")
	public void validateReplyToElementNamespace() throws SoapFaultException {
		if (replyToEndpoint == null) { msg('Not present'); return }
		assertEquals(wsaddresingNamespace, replyToEndpoint.getNamespace().getNamespaceURI());
	}

    @DependsOn(ids=["ReplyToEndpointParse"])
    @Fault(code=FaultCode.EndpointUnavailable)
	@Validation(id="WSA230", msg="Verify ReplyTo endpoint value prefix if present", ref="http://www.w3.org/TR/ws-addr-core")
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

    @DependsOn(ids=["WSAparse"])
    @Fault(code=FaultCode.EndpointUnavailable)
	@Validation(id="FaultToEndpointParse", msg="Parse FaultTo endpoint", ref="http://www.w3.org/TR/ws-addr-core")
	public void parseFaultToEndpoint() throws SoapFaultException {
		faultToEndpoint = getFirst(faultTo);
		if (faultToEndpoint != null)
			faultToEndpointValue = faultToEndpoint.getText();
	}

    @DependsOn(ids=["FaultToEndpointParse"])
    @Fault(code=FaultCode.EndpointUnavailable)
	@Validation(id="WSA240", msg="FaultTo endpoint element displayName must be Address if present", ref="http://www.w3.org/TR/ws-addr-core")
	public void validateFaultToElementIsAddress() throws SoapFaultException {
		if (faultToEndpoint == null) { msg('Not present'); return }
		assertEquals("Address", faultToEndpoint.getLocalName());
	}

    @DependsOn(ids=["FaultToEndpointParse"])
    @Fault(code=FaultCode.EndpointUnavailable)
	@Validation(id="WSA250", msg="Verify FaultTo endpoint element namespace if present", ref="http://www.w3.org/TR/ws-addr-core")
	public void validateFaultToElementNamespace() throws SoapFaultException {
		if (faultToEndpoint == null) { msg('Not present'); return }
		assertEquals(wsaddresingNamespace, faultToEndpoint.getNamespace().getNamespaceURI());
	}

    @DependsOn(ids=["FaultToEndpointParse"])
    @Fault(code=FaultCode.EndpointUnavailable)
	@Validation(id="WSA260", msg="Verify FaultTo endpoint has http prefix if present", ref="http://www.w3.org/TR/ws-addr-core")
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

    @DependsOn(ids=["WSAparse"])
    @Fault(code=FaultCode.EndpointUnavailable)
	@Validation(id="ToEndpointParse", msg="Parse To endpoint", ref="http://www.w3.org/TR/ws-addr-core")
	public void parseToEndpoint() throws SoapFaultException {
		toEndpoint = getFirst(to);
		if (toEndpoint != null)
			toEndpointValue = toEndpoint.getText();
	}

    @DependsOn(ids=["ToEndpointParse"])
    @Fault(code=FaultCode.EndpointUnavailable)
	@Validation(id="WSA270", msg="Verify To endpoint has http prefix if present", ref="http://www.w3.org/TR/ws-addr-core")
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
