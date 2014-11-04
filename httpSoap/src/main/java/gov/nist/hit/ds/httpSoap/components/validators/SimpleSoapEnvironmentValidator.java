package gov.nist.hit.ds.httpSoap.components.validators;

import gov.nist.hit.ds.http.parser.HttpParserBa;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase;
import gov.nist.hit.ds.soapSupport.SoapFaultException;

/**
 * Validate SIMPLE SOAP message. The input (an HTTP stream) has already been parsed
 * and the headers are in a HttpParserBa class and the body in a byte[]. This 
 * validator only evaluates the HTTP headers. Validation of the body is passed
 * off to MessageValidatorFactory.
 * @author bill
 *
 */
public class SimpleSoapEnvironmentValidator extends ValComponentBase {
	HttpParserBa hparser;
//	MessageValidatorEngine mvc;
	byte[] bodyBytes;
	String charset = null;

    public SimpleSoapEnvironmentValidator() { super(null); }

    @Override
    public void run() throws SoapFaultException, RepositoryException {

    }

    @Override
    public boolean showOutputInLogs() {
        return false;
    }

//	@SimComponentInject
//	public SimpleSoapEnvironmentValidator setHttpParser(HttpParserBa parser) {
//		this.hparser = parser;
//		return this;
//	}
//
//	@Override
//	public void run(MessageValidatorEngine mve) throws SoapFaultException, RepositoryException {
//		if (hparser.isMultipart())
//			throw new SoapFaultException(
//					ag,
//					FaultCode.Sender,
//					new ErrorContext("Expecting SIMPLE SOAP - multipart format indicates MTOM instead"));
//		bodyBytes = hparser.getBody();
//		String contentTypeString = hparser.getHttpMessage().getHeader("content-type");
//		HttpHeader contentTypeHeader = null;
//		try {
//			contentTypeHeader = new HttpHeader(contentTypeString);
//		} catch (ParseException e) {
//			throw new SoapFaultException(
//					ag,
//					FaultCode.Sender,
//					new ErrorContext(
//							"Error parsing content-type header - <" + contentTypeString + ">"));
//		}
//		String contentTypeValue = contentTypeHeader.getValue();
//		if (contentTypeValue == null) contentTypeValue = "";
//		if (!"application/soap+xml".equals(contentTypeValue.toLowerCase()))
//			throw new SoapFaultException(
//					ag,
//					FaultCode.Sender,
//					new ErrorContext(
//							"Content-Type header must have value application/soap+xml - found instead " + contentTypeValue,
//							"http://www.w3.org/TR/soap12-part0 - Section 4.1.2"));
//
//
//		event.addArtifact("Content-Type", contentTypeValue);
//
//		charset = contentTypeHeader.getParam("charset");
//		if (charset == null || charset.equals("")) {
//			charset = "UTF-8";
//			ag.addAssertion(new Assertion().setStatus(AssertionStatus.INFO).setExpected("-").setFound("-").setMsg("No message CharSet found in Content-Type header, assuming " + charset));
////			ag.detail("No message CharSet found in Content-Type header, assuming " + charset);
//		} else {
//			ag.addAssertion(new Assertion().setStatus(AssertionStatus.INFO).setExpected("-").setFound(charset).setMsg("Message CharSet"));
////			ag.detail("Message CharSet is " + charset);
//		}
//
//	}
//
//	public XmlText getXmlText() {
//		return new XmlText().setXml(new String(bodyBytes));
//	}
//
//	@Override
//	public boolean showOutputInLogs() {
//		return true;
//	}

}
