package gov.nist.hit.ds.httpSoapValidator.validators;


import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.http.parser.HttpParseException;
import gov.nist.hit.ds.http.parser.HttpParserBa;
import gov.nist.hit.ds.http.parser.ParseException;
import gov.nist.hit.ds.simSupport.engine.Inject;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.core.FaultCodes;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.utilities.html.HttpMessageContent;
import gov.nist.hit.ds.utilities.string.StringUtil;


/**
 * Validate HTTP message.  Launches either MtomMessageValidator or SimpleSoapMessageValidator as appropriate.
 * @author bill
 *   
 */
public class HttpMessageValidator extends SimComponentBase {
	// Either header and body OR hparser are initialized by the constructor
	String header = null;
	byte[] body;
	HttpParserBa hparser = null;
//	ErrorRecorderBuilder erBuilder;
	MessageValidatorEngine mvc;
//	RegistryValidationInterface rvi;
	boolean isMultipartRequired = false;
	boolean multipartConfigured = false;

	public HttpMessageValidator() {
		setName(getClass().getSimpleName());
	}
	
	public HttpParserBa getHttpParser() {
		return hparser;
	}
	
	public HttpMessageValidator setMultipartRequired(String value) {
		isMultipartRequired = StringUtil.asBoolean(value);
		multipartConfigured = true;
		return this;
	}

	@Inject
	public HttpMessageValidator setMessageContent(HttpMessageContent content) {
		this.header = content.getHeader();
		this.body = content.getBody();
		return this;
	}
	
	@Override
	public void run(MessageValidatorEngine mve) throws SoapFaultException {
//		if (!multipartConfigured)
//			throw new SoapFaultException(
//					er,
//					FaultCodes.Receiver,
//					new ErrorContext("HttpMessageValidator not configured - SIMPLE SOAP or MTOM must be selected"));
//		try {
//			er.challenge("Parsing HTTP message");
//			
//			if (header != null)
//				hparser = new HttpParserBa(header.getBytes()); // since this is an exploratory parse, don't pass er
//			else
//				body = hparser.getBody();
//			
//			
//			
//		} catch (HttpParseException e) {
//			er.err(vc.getBasicErrorCode(), e);
//		} catch (ParseException e) {
//			er.err(vc.getBasicErrorCode(), e);
//		}
	}

	void run(ErrorRecorder er, MessageValidatorEngine mvc) {
//		this.er = er;
//		
//		try {
//			er.challenge("Parsing HTTP message");
//			
//			if (header != null)
//				hparser = new HttpParserBa(header.getBytes()); // since this is an exploratory parse, don't pass er
//			else
//				body = hparser.getBody();
//			
//			hparser.setErrorRecorder(er);
//			if (hparser.isMultipart()) {
//				if (!isMultipartRequired)
//					throw new SoapFaultException(
//							er,
//							FaultTypes.);
//				if (vc.isValid() && vc.requiresSimpleSoap()) 
//					er.err(vc.getBasicErrorCode(), new ErrorContext("Requested message type requires SIMPLE SOAP format message - MTOM format found", "ITI TF Volumes 2a and 2b"), this);
//				else
//					er.detail("Message is Multipart format");
//				er.detail("Scheduling MTOM parser");
//				mvc.addMessageValidator("Validate MTOM", new MtomMessageValidator(vc, hparser, body, mvc));
//			} else {
//				boolean val = vc.isValid(); 
//				boolean mt = vc.requiresMtom();
//				if (!val && mt)
//					er.err(vc.getBasicErrorCode(), new ErrorContext("Invalid message format: " + vc, "ITI TF Volumes 2a and 2b"), this);
//				if (mt)
//					er.err(vc.getBasicErrorCode(), new ErrorContext("Request Message is SIMPLE SOAP but MTOM is required", "ITI TF Volumes 2a and 2b"), this);
//				else
//					er.detail("Request Message is SIMPLE SOAP format");
//				er.detail("Scheduling SIMPLE SOAP Message Validator");
//				mvc.addMessageValidator("SimpleSoapMessageValidator", new SimpleSoapMessageValidator(vc, hparser, body, mvc));
////				SimpleSoapMessageValidator val = new SimpleSoapMessageValidator(vc, hparser, body, erBuilder, mvc);
////				val.run(er);
//			}
//		} catch (HttpParseException e) {
//			er.err(vc.getBasicErrorCode(), e);
//		} catch (ParseException e) {
//			er.err(vc.getBasicErrorCode(), e);
//		}

	}


}
