package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype;

import gov.nist.hit.ds.dsSims.eb.metadataValidator.field.ValidatorCommon;
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode;

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
