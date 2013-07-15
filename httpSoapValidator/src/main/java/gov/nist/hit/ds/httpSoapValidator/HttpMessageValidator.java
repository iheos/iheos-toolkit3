package gov.nist.hit.ds.httpSoapValidator;


import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.http.parser.HttpParseException;
import gov.nist.hit.ds.http.parser.HttpParserBa;
import gov.nist.hit.ds.http.parser.ParseException;
import gov.nist.hit.ds.simSupport.ValidationContext;
import gov.nist.hit.ds.simSupport.engine.Inject;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidator;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.utilities.html.HttpMessageContent;


/**
 * Validate HTTP message.  Launches either MtomMessageValidator or SimpleSoapMessageValidator as appropriate.
 * @author bill
 *   
 */
public class HttpMessageValidator extends MessageValidator {
	// Either header and body OR hparser are initialized by the constructor
	String header = null;
	byte[] body;
	HttpParserBa hparser = null;
//	ErrorRecorderBuilder erBuilder;
	MessageValidatorEngine mvc;
//	RegistryValidationInterface rvi;

	public HttpMessageValidator() {
		setName(getClass().getSimpleName());
	}
	
	@Inject
	public void setMessageValidatorEngine(MessageValidatorEngine mvc) {
		this.mvc = mvc;
	}
	
	@Inject 
	public void setValidationContext(ValidationContext vc) {
		this.vc = vc;
	}

//	public HttpMessageValidator(ValidationContext vc, HttpParserBa hparser, MessageValidatorEngine mvc) {
//		super(vc);
//		this.hparser = hparser;
//		this.mvc = mvc;
//	}

	@Inject
	public HttpMessageValidator setMessageContent(HttpMessageContent content) {
		this.header = content.getHeader();
		this.body = content.getBody();
		return this;
	}
		
	public void run(ErrorRecorder er, MessageValidatorEngine mvc) {
		this.er = er;
		
		try {
			er.challenge("Parsing HTTP message");
			
			if (header != null)
				hparser = new HttpParserBa(header.getBytes()); // since this is an exploratory parse, don't pass er
			else
				body = hparser.getBody();
			
			hparser.setErrorRecorder(er);
			if (hparser.isMultipart()) {
				if (vc.isValid() && vc.requiresSimpleSoap()) 
					er.err(vc.getBasicErrorCode(), new ErrorContext("Requested message type requires SIMPLE SOAP format message - MTOM format found", "ITI TF Volumes 2a and 2b"), this);
				else
					er.detail("Message is Multipart format");
				er.detail("Scheduling MTOM parser");
				mvc.addMessageValidator("Validate MTOM", new MtomMessageValidator(vc, hparser, body, mvc));
			} else {
				boolean val = vc.isValid(); 
				boolean mt = vc.requiresMtom();
				if (!val && mt)
					er.err(vc.getBasicErrorCode(), new ErrorContext("Invalid message format: " + vc, "ITI TF Volumes 2a and 2b"), this);
				if (mt)
					er.err(vc.getBasicErrorCode(), new ErrorContext("Request Message is SIMPLE SOAP but MTOM is required", "ITI TF Volumes 2a and 2b"), this);
				else
					er.detail("Request Message is SIMPLE SOAP format");
				er.detail("Scheduling SIMPLE SOAP Message Validator");
				mvc.addMessageValidator("SimpleSoapMessageValidator", new SimpleSoapMessageValidator(vc, hparser, body, mvc));
//				SimpleSoapMessageValidator val = new SimpleSoapMessageValidator(vc, hparser, body, erBuilder, mvc);
//				val.run(er);
			}
		} catch (HttpParseException e) {
			er.err(vc.getBasicErrorCode(), e);
		} catch (ParseException e) {
			er.err(vc.getBasicErrorCode(), e);
		}

	}

	@Override
	public String getDescription() {
		return "Parse HTTP message and schedule SIMPLE SOAP or MTOM parser";
	}

}
