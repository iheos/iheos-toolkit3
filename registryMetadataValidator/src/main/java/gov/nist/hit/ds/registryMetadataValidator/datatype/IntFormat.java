package gov.nist.hit.ds.registryMetadataValidator.datatype;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.registryMetadataValidator.field.ValidatorCommon;

public class IntFormat extends FormatValidator {

	public IntFormat(ErrorRecorder er, String context, String resource) {
		super(er, context, resource);
	}

	public void validate(String input) {
		if (!ValidatorCommon.isInt(input)) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(context + ": " + input + " is not an integer", getResource(null)), this);
		}
		
	}

}
