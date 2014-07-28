package gov.nist.hit.ds.dsSims.metadataValidator.datatype

import gov.nist.hit.ds.dsSims.metadataValidator.field.ValidatorCommon
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode;


public class CxFormat extends FormatValidator {

	public CxFormat(ErrorRecorder er, String context, String resource) {
		super(er, context, resource);
	}

	public void validate(String input) {
		String error = ValidatorCommon.validate_CX_datatype(input);
		if (error != null) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, context + ": " + input + " : " + error, this, getResource(null));
		}
	}

}
