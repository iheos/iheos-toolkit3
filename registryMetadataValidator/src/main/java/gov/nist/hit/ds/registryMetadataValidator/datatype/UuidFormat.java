package gov.nist.hit.ds.registryMetadataValidator.datatype;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;

public class UuidFormat extends FormatValidator {

	public UuidFormat(ErrorRecorder er, String context, String resource) {
		super(er, context, resource);
	}

	public void validate(String input) {
		if (!isUuid(input)) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(context + ": " + input + " is not in UUID format", getResource("ITI TF-3: Table 4.1-3 and section 4.1.12.3")), this);
		}
	}

	// example: urn:uuid:488705e6-91e6-47f8-b567-8c06f8472e74
	boolean isUuid(String value)  {
		if (value == null)
			return false;
		if (!value.startsWith("urn:uuid:"))
			return false;
		
		if (value.length() != 45) 
			return false;
		
		String rest;
		rest = value.substring(9);
		
		if (!isLCHexString(rest.substring(0, 8)))
			return false;
		rest = rest.substring(8);
		
		if (!(rest.charAt(0) == '-'))
			return false;
		rest = rest.substring(1);

		if (!isLCHexString(rest.substring(0, 4)))
			return false;
		rest = rest.substring(4);
		
		if (!(rest.charAt(0) == '-'))
			return false;
		
		rest = rest.substring(1);
		if (!isLCHexString(rest.substring(0, 4)))
			return false;
		rest = rest.substring(4);
				
		if (!(rest.charAt(0) == '-'))
			return false;
		
		rest = rest.substring(1);
		if (!isLCHexString(rest.substring(0, 4)))
			return false;
		rest = rest.substring(4);
				
		if (!(rest.charAt(0) == '-'))
			return false;
		
		rest = rest.substring(1);
		if (!isLCHexString(rest.substring(0, 12)))
			return false;
		return true;
	}
	
	static public boolean isLCHexString(String value) {
		for (int i=0; i<value.length(); i++) {
			char c = value.charAt(i);
			switch (c) {
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '0':
			case 'a':
			case 'b':
			case 'c':
			case 'd':
			case 'e':
			case 'f':
				continue;
			default:
				return false;
			}
		}
		return true;
	}

	static public boolean isHexString(String value) {
		for (int i=0; i<value.length(); i++) {
			char c = value.charAt(i);
			switch (c) {
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '0':
			case 'a':
			case 'b':
			case 'c':
			case 'd':
			case 'e':
			case 'f':
			case 'A':
			case 'B':
			case 'C':
			case 'D':
			case 'E':
			case 'F':
				continue;
			default:
				return false;
			}
		}
		return true;
	}

}
