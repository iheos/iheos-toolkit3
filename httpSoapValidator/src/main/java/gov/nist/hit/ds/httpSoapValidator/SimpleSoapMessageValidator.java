package gov.nist.hit.ds.httpSoapValidator;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.http.parser.HttpHeader;
import gov.nist.hit.ds.http.parser.HttpParserBa;
import gov.nist.hit.ds.http.parser.ParseException;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidator;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.simSupport.transaction.ValidationContext;
import gov.nist.hit.ds.utilities.xml.XmlText;

/**
 * Validate SIMPLE SOAP message. The input (an HTTP stream) has already been parsed
 * and the headers are in a HttpParserBa class and the body in a byte[]. This 
 * validator only evaluates the HTTP headers. Validation of the body is passed
 * off to MessageValidatorFactory.
 * @author bill
 *
 */
public class SimpleSoapMessageValidator extends MessageValidator {
	HttpParserBa hparser;
	MessageValidatorEngine mvc;
	byte[] bodyBytes;
	String charset = null;
//	RegistryValidationInterface rvi;

	public SimpleSoapMessageValidator(ValidationContext vc, HttpParserBa hparser, byte[] body, MessageValidatorEngine mvc) {
		super(vc);
		this.hparser = hparser;
		this.mvc = mvc;
		this.bodyBytes = body;
		setName(getClass().getSimpleName());
	}

	public void run(ErrorRecorder er, MessageValidatorEngine mve) {
		this.er = er;
		String contentTypeString = hparser.getHttpMessage().getHeader("content-type");
		try {
			HttpHeader contentTypeHeader = new HttpHeader(contentTypeString);
			String contentTypeValue = contentTypeHeader.getValue();
			if (contentTypeValue == null) contentTypeValue = "";
			if (!"application/soap+xml".equals(contentTypeValue.toLowerCase()))
				err("Content-Type header must have value application/soap+xml - found instead " + contentTypeValue,"http://www.w3.org/TR/soap12-part0 - Section 4.1.2");
			else
				er.detail("Content-Type is " + contentTypeValue);

			charset = contentTypeHeader.getParam("charset");
			if (charset == null || charset.equals("")) {
				charset = "UTF-8";
				er.detail("No message CharSet found in Content-Type header, assuming " + charset);
			} else {
				er.detail("Message CharSet is " + charset);
			}

//			String body = new String(bodyBytes, charset);
			vc.isSimpleSoap = true;
			vc.hasSoap = true;
			
//			er.detail("Scheduling validation of SOAP wrapper");
//			MessageValidatorFactory.getValidatorContext(erBuilder, bodyBytes, mve, "Validate SOAP", vc);

		} catch (ParseException e) {
			err(e);
//		} catch (UnsupportedEncodingException e) {
//			err(e);
		}
	}
	
	public XmlText getXmlText() {
		return new XmlText().setXml(new String(bodyBytes));
	}
	
	void err(String msg, String ref) {
		er.err(XdsErrorCode.Code.NoCode, new ErrorContext(msg, ref), this);
	}
	
	void err(Exception e) {
		er.err(XdsErrorCode.Code.NoCode, e);
	}

	@Override
	public String getDescription() {
		return "Verify details of the SIMPLE SOAP HTTP header";
	}

	@Override
	public void setDescription(String description) {
		// TODO Auto-generated method stub
		
	}



}
