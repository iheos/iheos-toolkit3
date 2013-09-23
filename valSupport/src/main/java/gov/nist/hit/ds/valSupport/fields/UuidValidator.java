package gov.nist.hit.ds.valSupport.fields;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;

public class UuidValidator {
	IAssertionGroup er;
	String rawMsgPrefix = "Validating UUID format of "; 
	String msgPrefix;
	
	public UuidValidator(IAssertionGroup er, String errorMsgPrefix) {
		this.er = er;
		if (errorMsgPrefix != null) 
			rawMsgPrefix = errorMsgPrefix;
	}
	
	
	boolean allHexDigits(String hex, String errorPrefix) {
		String d = "0123456789abcdef";
		
		for (int i=0; i<hex.length(); i++) {
			char digit = hex.charAt(i);
			if (d.indexOf(digit) > -1)
				continue;
			if (d.indexOf(String.valueOf(digit).toLowerCase()) > -1) {
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(errorPrefix + " - hex digits must be lower case, found - " + digit, null), this);
				return false;
			} else {
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(errorPrefix + " - non-hex digit found - " + digit, null), this);
				return false;
			}
		}
		return true;
	}
	
	// validate UUID format
	public void validateUUID(String uuid) {
		msgPrefix = rawMsgPrefix + uuid;
		
		if (!uuid.startsWith("urn:uuid:")) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(msgPrefix + " - does not have urn:uuid: prefix", null), this);
			return;
		}
		
		String content = uuid.substring(9);
		String[] parts = content.split("-");
		
		if (parts.length != 5) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(msgPrefix + " - does not have 5 hex-digit groups separated by the - character", null), this);
			return;
		}

		String part;
		
		part = parts[0];
		if (part.length() != 8)
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(msgPrefix + " - first hex character group must have 8 digits", null), this);
		allHexDigits(part, msgPrefix);
		
		part = parts[1];
		if (part.length() != 4)
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(msgPrefix + " - second hex character group must have 4 digits", null), this);
		allHexDigits(part, msgPrefix);
		
		part = parts[2];
		if (part.length() != 4)
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(msgPrefix + " - third hex character group must have 4 digits", null), this);
		allHexDigits(part, msgPrefix);
		
		part = parts[3];
		if (part.length() != 4)
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(msgPrefix + " - fourth hex character group must have 4 digits", null), this);
		allHexDigits(part, msgPrefix);
		
		part = parts[4];
		if (part.length() != 12)
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(msgPrefix + " - fifth hex character group must have 12 digits", null), this);
		allHexDigits(part, msgPrefix);
		
	}
	

}
