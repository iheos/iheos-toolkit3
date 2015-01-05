package gov.nist.hit.ds.testClient.metadataValidator.datatype;

import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.testClient.logging.ErrorRecorder;
import gov.nist.hit.ds.testClient.metadataValidator.field.ValidatorCommon;

public class IntFormat extends FormatValidator {

	public IntFormat(ErrorRecorder er, String context, String resource) {
		super(er, context, resource);
	}

	public void validate(String input) {
		if (!ValidatorCommon.isInt(input)) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, context + ": " + input + " is not an integer", this, getResource(null));
		}
		
	}

}
