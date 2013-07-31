package gov.nist.hit.ds.httpSoapValidator.validators;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode.Code;
import gov.nist.hit.ds.httpSoapValidator.datatypes.MetadataLevel;
import gov.nist.hit.ds.httpSoapValidator.datatypes.SoapMessage;
import gov.nist.hit.ds.simSupport.engine.Inject;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.core.FaultCodes;
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.utilities.xml.XmlUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

public class SoapHeaderValidator extends SimComponentBase {
	OMElement header;
	MetadataLevel level = MetadataLevel.XDS;  // default
	static final String wsaddresingNamespace = "http://www.w3.org/2005/08/addressing";
	static final String wsaddressingRef = "http://www.w3.org/TR/ws-addr-core/";
	SoapEnvironment soapEnvironment;
	String expectedAction;

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
		return this;
	}
	
	public MetadataLevel getMetadataLevel() {
		return level;
	}

	@Override
	public void run(MessageValidatorEngine mve) throws SoapFaultException {
		validateMetadataLevel();
		validateWSAddressing();
		validateWSAction();
	}

	void validateWSAction() throws SoapFaultException {
		if (expectedAction == null) {
			er.warning(
					Code.NoCode, 
					new ErrorContext("WS-Action not validated - no expected value is configured"), 
					this.getClass().getName());
			return;
		} 
		if (!expectedAction.equals(soapEnvironment.getRequestAction()))
			throw new SoapFaultException(
					er,
					FaultCodes.ActionNotSupported,
					new ErrorContext("Expected WS-Action >" + expectedAction + "> not equal to WS-Action found in message <" + soapEnvironment.getRequestAction() + ">"));
		er.detail("WS-Action is <" + soapEnvironment.getRequestAction() + ">");
	}

	void validateWSAddressing() throws SoapFaultException {
		if (header == null)
			return;
		er.challenge("WS-Addressing");
		List<OMElement> messageId = XmlUtil.childrenWithLocalName(header, "MessageID");
		List<OMElement> relatesTo = XmlUtil.childrenWithLocalName(header, "RelatesTo");
		List<OMElement> to = XmlUtil.childrenWithLocalName(header, "To");
		List<OMElement> action = XmlUtil.childrenWithLocalName(header, "Action");
		List<OMElement> from = XmlUtil.childrenWithLocalName(header, "From");
		List<OMElement> replyTo = XmlUtil.childrenWithLocalName(header, "ReplyTo");
		List<OMElement> faultTo = XmlUtil.childrenWithLocalName(header, "FaultTo");

		// check for namespace
		validateNamespace(messageId, wsaddresingNamespace);
		validateNamespace(relatesTo, wsaddresingNamespace);
		validateNamespace(to,        wsaddresingNamespace);
		validateNamespace(action,    wsaddresingNamespace);
		validateNamespace(from,      wsaddresingNamespace);
		validateNamespace(replyTo,   wsaddresingNamespace);
		validateNamespace(faultTo,   wsaddresingNamespace);

		// check for repeating and required elements
		// this does not take async into consideration
		if (to.size() > 1)
			invalidAddressingHeader("Multiple WS-Addressing To headers are not allowed",wsaddressingRef + "#msgaddrpropsinfoset");
		if (from.size() > 1)
			invalidAddressingHeader("Multiple WS-Addressing From headers are not allowed",wsaddressingRef + "#msgaddrpropsinfoset");
		if (replyTo.size() > 1)
			invalidAddressingHeader("Multiple WS-Addressing ReplyTo headers are not allowed",wsaddressingRef + "#msgaddrpropsinfoset");
		if (faultTo.size() > 1)
			invalidAddressingHeader("Multiple WS-Addressing FaultTo headers are not allowed",wsaddressingRef + "#msgaddrpropsinfoset");
		if (action.size() == 0)
			invalidAddressingHeader("WS-Addressing Action header is required",wsaddressingRef + "#msgaddrpropsinfoset");
		if (action.size() > 1)
			invalidAddressingHeader("Multiple WS-Addressing Action headers are not allowed",wsaddressingRef + "#msgaddrpropsinfoset");
		if (messageId.size() > 	1) 
			invalidAddressingHeader("Multiple WS-Addressing MessageID headers are not allowed",wsaddressingRef + "#msgaddrpropsinfoset");

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
			//			er.detail("The WS-Addressing SOAP header " + hdr.getLocalName() + " has value other than \"1\""/*,"ITI TF-2x: V.3.2.2"*/);
		}
		if (!mufound) {
			String msg = "At least one WS-Addressing SOAP header element must have a soapenv:mustUnderstand attribute soapenv:mustUnderstand with value of logical true";
			String ref = "http://www.w3.org/TR/soap12-part0/#L4697"; 
			er.detail("Taken from the above reference:");
			er.detail("In the SOAP 1.2 infoset-based description, the env:mustUnderstand attribute in header elements takes the (logical) value \"true\" or \"false\", whereas in SOAP 1.1 they are the literal value \"1\" or \"0\" respectively.");
			er.detail("This validation accepts 1 or 0 or any capitalization of true or false");
			mustUnderstandError(msg, ref);
		}

		//		if (action.size() > 0) {
		//			OMElement a = action.get(0);
		//			String mu = a.getAttributeValue(MetadataSupport.must_understand_qname);
		//			if (!"1".equals(mu))
		//				er.err("The WS-Action SOAP header element must have attribute wsa:mustUnderstand=\"1\"","ITI TF-2x: V.3.2.2");
		//		}

		// check for endpoint format
		endpointCheck(replyTo, false);
		endpointCheck(from, true);
		endpointCheck(faultTo, false);

		// check for simple http style endpoint
		httpCheck(to);

		// return WSAction
		soapEnvironment.setRequestAction(null);

		if (action.size() > 0) {
			OMElement aEle = action.get(0);
			soapEnvironment.setRequestAction(aEle.getText());
		}

		if (messageId.size() > 0) {
			OMElement mid = messageId.get(0);
			soapEnvironment.setMessageId(mid.getText());
		}

	}

	void httpCheck(List<OMElement> eles) throws SoapFaultException {
		for (OMElement ele : eles) {
			String value = ele.getText();
			if (!value.startsWith("http")) {
				String msg = "Value of " + ele.getLocalName() + " must be http endpoint - found instead " + value; 
				throw new SoapFaultException(
						er,
						FaultCodes.EndpointUnavailable, 
						new ErrorContext(msg, wsaddressingRef).toString());
			}
		}
	}

	boolean mustUnderstandValueOk(String value) {
		if ("1".equals(value)) return true;
		if ("true".equalsIgnoreCase("true")) return true;
		return false;
	}

	void endpointCheck(List<OMElement> eles, boolean anyURIOk) throws SoapFaultException {
		for (OMElement ele : eles) {
			OMElement first = ele.getFirstElement();
			if (first == null) {
				String msg = "Validating contents of " + ele.getLocalName() + ": " + "not HTTP style endpoint"; 
				throw new SoapFaultException(er, FaultCodes.EndpointUnavailable, new ErrorContext(msg, wsaddressingRef));

			} else {
				String valError = validateEndpoint(first, anyURIOk);
				if (valError != null) {
					String msg = "Validating contents of " + ele.getLocalName() + ": " + valError; 
					throw new SoapFaultException(er, FaultCodes.EndpointUnavailable, new ErrorContext(msg, wsaddressingRef));
				}
			}
		}
	}

	String validateEndpoint(OMElement endpoint, boolean anyURIOk) {
		if (endpoint == null)
			return "null value";
		if (!endpoint.getLocalName().equals("Address"))
			return "found " + endpoint.getLocalName() + " but expected Address";
		if (!endpoint.getNamespace().getNamespaceURI().equals(wsaddresingNamespace))
			return "found namespace" + endpoint.getNamespace().getNamespaceURI() + " but expected " + wsaddresingNamespace;
		String value = endpoint.getText();

		if (anyURIOk && value.startsWith("urn:"))
			return null;

		if (!value.startsWith("http"))
			return "not HTTP style endpoint";
		return null;
	}

	void validateNamespace(List<OMElement> eles, String namespace) throws SoapFaultException {
		for (OMElement ele : eles) {
			OMNamespace omns = ele.getNamespace();
			String nsuri = omns.getNamespaceURI();
			if (!namespace.equals(nsuri)) {
				String msg = "Namespace on element " + ele.getLocalName() + " must be " +
						namespace + " - found instead " + nsuri;
				throw new SoapFaultException(er, FaultCodes.Sender, new ErrorContext(msg, wsaddressingRef).toString());
			}
		}
	}

	void invalidAddressingHeader(String msg, String ref) throws SoapFaultException {
		throw new SoapFaultException(
				er,
				FaultCodes.InvalidAddressingHeader,
				new ErrorContext(msg, ref)
				);
	}

	void mustUnderstandError(String msg, String ref) throws SoapFaultException {
		throw new SoapFaultException(
				er,
				FaultCodes.MustUnderstand,
				new ErrorContext(msg, ref)
				);
	}

	void validateMetadataLevel() throws SoapFaultException {
		Iterator<?> children = header.getChildElements();
		OMElement metadataLevelEle = null;
		while (children.hasNext()) {
			OMElement child = (OMElement) children.next();
			if ("metadata-level".equals(child.getLocalName())) {
				metadataLevelEle = child;
				break;
			}
		}
		if (metadataLevelEle != null) { 
			if (!"urn:direct:addressing".equals(metadataLevelEle.getNamespace().getNamespaceURI()))
				throw new SoapFaultException(
						er,
						FaultCodes.Sender, 
						new ErrorContext(
								"Namespace on metadata-level header element must be " + "urn:direct:addressing", 
								"http://wiki.directproject.org/file/view/2011-03-09%20PDF%20-%20XDR%20and%20XDM%20for%20Direct%20Messaging%20Specification_FINAL.pdf:6.1.1 SOAP Headers"));
			String levelTxt = metadataLevelEle.getText();
			if ("minimal".equals(levelTxt))
				level = MetadataLevel.MINIMAL;
			else if ("XDS".equals(levelTxt))
				level = MetadataLevel.XDS;
			else
				throw new SoapFaultException(
						er,
						FaultCodes.Sender, 
						new ErrorContext(
								"metadata-level must be <minimal> or <XDS>.", 
								"http://wiki.directproject.org/file/view/2011-03-09%20PDF%20-%20XDR%20and%20XDM%20for%20Direct%20Messaging%20Specification_FINAL.pdf:6.1.1 SOAP Headers"));
		}
		er.challenge("Direct");
		er.detail("metadata-level set to <" + level + ">");
	}

}
