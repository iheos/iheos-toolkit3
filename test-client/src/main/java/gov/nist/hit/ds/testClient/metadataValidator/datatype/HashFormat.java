package gov.nist.hit.ds.testClient.metadataValidator.datatype;

import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.testClient.logging.ErrorRecorder;

public class HashFormat extends FormatValidator {

	public HashFormat(ErrorRecorder er, String context, String resource) {
		super(er, context, resource);
	}

	public void validate(String input) {
		if (!UuidFormat.isHexString(input)) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, context + ": " + input + " is not in hex format", this, getResource("ITI TF-3: Table 4.1-3 (SHA1)"));
		}
	}

}
