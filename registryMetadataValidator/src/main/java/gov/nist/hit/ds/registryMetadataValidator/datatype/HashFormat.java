package gov.nist.hit.ds.registryMetadataValidator.datatype;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;

public class HashFormat extends FormatValidator {

	public HashFormat(ErrorRecorder er, String context, String resource) {
		super(er, context, resource);
	}

	public void validate(String input) {
		if (!UuidFormat.isHexString(input)) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(context + ": " + input + " is not in hex format", getResource("ITI TF-3: Table 4.1-3 (SHA1)")), this);
		}
	}

}
