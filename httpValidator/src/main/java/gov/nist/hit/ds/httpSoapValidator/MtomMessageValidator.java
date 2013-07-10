package gov.nist.hit.ds.httpSoapValidator;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.errorRecording.factories.ErrorRecorderBuilder;
import gov.nist.hit.ds.http.parser.HttpParseException;
import gov.nist.hit.ds.http.parser.HttpParserBa;
import gov.nist.hit.ds.http.parser.MultipartParserBa;
import gov.nist.hit.ds.http.parser.PartBa;
import gov.nist.hit.ds.simSupport.ValidationContext;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidator;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MtomMessageValidator extends MessageValidator {
	HttpParserBa headers;
	MessageValidatorEngine mvc;
//	RegistryValidationInterface rvi;
	byte[] bodyBytes;

	public MtomMessageValidator(ValidationContext vc, HttpParserBa headers, byte[] body, MessageValidatorEngine mvc) {
		super(vc);
		this.headers = headers;
		this.mvc = mvc;
		this.bodyBytes = body;
	}


	public void run(ErrorRecorder er, MessageValidatorEngine mvc) {
		this.er = er;
		headers.setErrorRecorder(er);
		try {
			

			HttpParserBa hp = headers;
			
			String body = new String(bodyBytes, hp.getCharset());
			hp.setBody(bodyBytes);
			hp.tryMultipart();
			MultipartParserBa mp = hp.getMultipartParser();
			

			er.detail("Multipart contains " + mp.getPartCount() + " parts");
			if (mp.getPartCount() == 0) {
				er.err(XdsErrorCode.Code.NoCode, new ErrorContext("Cannot continue parsing, no Parts found", ""), this);
				return;
			}
			
			List<String> partIds = new ArrayList<String>();
			for (int i=0; i<mp.getPartCount(); i++) {
				PartBa p = mp.getPart(i);
				partIds.add(p.getContentId());
			}
			er.detail("Part Content-IDs are " + partIds);
			
			
			PartBa startPart = mp.getStartPart();
			
			if (startPart != null)
				er.detail("Found start part - " + startPart.getContentId());
			else {
				er.err(XdsErrorCode.Code.NoCode, new ErrorContext("Start part [" + mp.getStartPartId() + "] not found", Mtom.XOP_example2), this);
				return;
			}
				
			vc.isSimpleSoap = false;

			// no actual validation, just saves Part list on validation stack so it can
			// be found by later steps that need it
			mvc.addMessageValidator("MultipartContainer", new MultipartContainer(vc, mp), er.buildNewErrorRecorder());

			
			er.detail("Scheduling validation of SOAP wrapper");
			MessageValidatorFactory.getValidatorContext(erBuilder, startPart.getBody(), mvc, "Validate SOAP", vc);

		} catch (UnsupportedEncodingException e) {
			er.err(XdsErrorCode.Code.NoCode, e);
		} catch (HttpParseException e) {
			er.err(XdsErrorCode.Code.NoCode, e);
		}

	}

}
