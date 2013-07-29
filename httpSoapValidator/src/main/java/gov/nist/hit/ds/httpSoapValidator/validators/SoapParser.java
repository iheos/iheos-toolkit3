package gov.nist.hit.ds.httpSoapValidator.validators;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.httpSoapValidator.datatypes.SoapMessage;
import gov.nist.hit.ds.simSupport.engine.Inject;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.core.FaultCodes;
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.xmlValidator.XmlMessage;

import java.util.Iterator;

import org.apache.axiom.om.OMElement;

public class SoapParser extends SimComponentBase {
	OMElement xml;
	XmlMessage xmlMessage;
	String soapNamespaceName = "http://www.w3.org/2003/05/soap-envelope";
	OMElement header = null;
	OMElement body = null;

	@Inject
	public SoapParser setXML(XmlMessage xmlMessage) {
		this.xmlMessage = xmlMessage;
		return this;
	}
	
	public SoapMessage getSoapMessage() {
		return new SoapMessage().setHeader(header).setBody(body);
	}

	@Override
	public void run(MessageValidatorEngine mve) throws SoapFaultException {
		OMElement root = xmlMessage.getXml();
		if (!"Envelope".equals(root.getLocalName()))
			throw new SoapFaultException(
					er,
					FaultCodes.Sender,
					new ErrorContext("Top SOAP XML element must be <Envelope>", 
							"http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenv"));
		if (!soapNamespaceName.equals(root.getNamespace().getNamespaceURI()))
			throw new SoapFaultException(
					er, 
					FaultCodes.DataEncodingUnknown,
					new ErrorContext(
							"SOAP Envelope namespace must be <" + soapNamespaceName + "> found <" + root.getNamespace().getNamespaceURI()  + "> instead", 
							"http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenvelope"));
		Iterator<?> children = root.getChildElements();
		while (children.hasNext()) {
			OMElement ele = (OMElement) children.next();
			if ("Header".equals(ele.getLocalName())) {
				if (header != null)
					throw new SoapFaultException(
							er,
							FaultCodes.Sender,
							new ErrorContext(
									"SOAP Envelope contains multiple <Header> elements.", 
									"http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenvelope"));
				header = ele;
				if (!soapNamespaceName.equals(header.getNamespace().getNamespaceURI()))
					throw new SoapFaultException(
							er, 
							FaultCodes.DataEncodingUnknown,
							new ErrorContext(
									"SOAP Header namespace must be <" + soapNamespaceName + "> found <" + header.getNamespace().getNamespaceURI()  + "> instead", 
									"http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenvelope"));
			}
			if ("Body".equals(ele.getLocalName())) {
				if (body != null)
					throw new SoapFaultException(
							er,
							FaultCodes.Sender,
							new ErrorContext(
									"SOAP Envelope contains multiple <Body> elements.", 
									"http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenvelope"));
				body = ele;
				if (!soapNamespaceName.equals(body.getNamespace().getNamespaceURI()))
					throw new SoapFaultException(
							er, 
							FaultCodes.DataEncodingUnknown,
							new ErrorContext(
									"SOAP Body namespace must be <" + soapNamespaceName + "> found <" + body.getNamespace().getNamespaceURI()  + "> instead", 
									"http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenvelope"));
			}
		}
		if (header == null)
			throw new SoapFaultException(
					er,
					FaultCodes.Sender,
					new ErrorContext(
							"SOAP Header is required - not found.", 
							"ITI-TF3x:V3.2.2"));
		if (body == null)
			throw new SoapFaultException(
					er,
					FaultCodes.Sender,
					new ErrorContext(
							"SOAP Body is required - not found.", 
							"http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapenvelope"));			
	}

}
