package gov.nist.toolkit.valsupport.fields;

import gov.nist.toolkit.valsupport.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class UuidValidator {
	String rawMsgPrefix = "Validating UUID format of "; 
	String msgPrefix;
	List<String> errs = new ArrayList<String>();
	
	public UuidValidator(String errorMsgPrefix) {
		if (errorMsgPrefix != null) 
			rawMsgPrefix = errorMsgPrefix;
	}
	
	
	// validate UUID format
	public void validateUUID(String uuid) throws ValidationException {
		msgPrefix = rawMsgPrefix + uuid;
		
		if (!uuid.startsWith("urn:uuid:")) {
			errs.add(msgPrefix + " - does not have urn:uuid: prefix");
			throw new ValidationException(errs);
		}
		
		String content = uuid.substring(9);
		String[] parts = content.split("-");
		
		if (parts.length != 5) {
			errs.add(msgPrefix + " - does not have 5 hex-digit groups separated by the - character");
			throw new ValidationException(errs);
		}

		String part;
		
		part = parts[0];
		if (part.length() != 8)
			errs.add(msgPrefix + " - first hex character group must have 8 digits");
		allHexDigits(part, msgPrefix);
		
		part = parts[1];
		if (part.length() != 4)
			errs.add(msgPrefix + " - second hex character group must have 4 digits");
		allHexDigits(part, msgPrefix);
		
		part = parts[2];
		if (part.length() != 4)
			errs.add(msgPrefix + " - third hex character group must have 4 digits");
		allHexDigits(part, msgPrefix);
		
		part = parts[3];
		if (part.length() != 4)
			errs.add(msgPrefix + " - fourth hex character group must have 4 digits");
		allHexDigits(part, msgPrefix);
		
		part = parts[4];
		if (part.length() != 12)
			errs.add(msgPrefix + " - fifth hex character group must have 12 digits");
		allHexDigits(part, msgPrefix);
		
		if (errs.size() != 0)
			throw new ValidationException(errs);
	}
	
	private boolean allHexDigits(String hex, String errorPrefix) {
		String d = "0123456789abcdef";
		
		for (int i=0; i<hex.length(); i++) {
			char digit = hex.charAt(i);
			if (d.indexOf(digit) > -1)
				continue;
			if (d.indexOf(String.valueOf(digit).toLowerCase()) > -1) {
				errs.add(errorPrefix + " - hex digits must be lower case, found - " + digit);
				return false;
			} else {
				errs.add(errorPrefix + " - non-hex digit found - " + digit);
				return false;
			}
		}
		return true;
	}

}
