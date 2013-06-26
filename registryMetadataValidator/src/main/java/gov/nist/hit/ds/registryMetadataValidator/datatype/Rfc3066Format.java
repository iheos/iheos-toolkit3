package gov.nist.hit.ds.registryMetadataValidator.datatype;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;

/**
 * RFC 3066
 * @author bill
 * 
 * The syntax of this tag in ABNF [RFC 2234] is:
 *
 *   Language-Tag = Primary-subtag *( "-" Subtag )
 *
 *   Primary-subtag = 1*8ALPHA
 *
 *   Subtag = 1*8(ALPHA / DIGIT)
 *   
 */
public class Rfc3066Format extends FormatValidator {

	public Rfc3066Format(ErrorRecorder er, String context, String resource) {
		super(er, context, resource);
	}
	
	String errMsg = " - does not conform to RFC 3066 format";
	String xresource = "RFC 3066";
	
	static String digits = "1234567890";
	static String alphas = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public void validate(String input) {
		if (input == null || input.equals("")) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(errMsg, getResource(xresource + " - input is empty")), this);
			return;
		}
		String[] parts = input.split("-");
		if (parts[0].length() > 8) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(input + errMsg, getResource(xresource + " - Primary-subtag limited to 8 characters")), this);
		}
		if (!allAlphas(parts[0])) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(input + errMsg, getResource(xresource + " - Primary-subtag is not all alpha characters")), this);
		}
		if (parts.length == 1)
			return;
		
		if (parts[1].length() > 8) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(input + errMsg, getResource(xresource + " - Subtag limited to 8 characters")), this);
		}
		if (!allAlphasAndDigits(parts[1])) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(input + errMsg, getResource(xresource + " - Subtag is not all alpha or digit characters")), this);
		}
		
	}

	boolean allAlphas(String in) {
		for (int i=0; i<in.length(); i++) {
			char c = in.charAt(i);
			if (alphas.indexOf(c) == -1)
				return false;
		}
		return true;
	}
	boolean allAlphasAndDigits(String in) {
		for (int i=0; i<in.length(); i++) {
			char c = in.charAt(i);
			if (digits.indexOf(c) == -1 && alphas.indexOf(c) == -1)
				return false;
		}
		return true;
	}
}
