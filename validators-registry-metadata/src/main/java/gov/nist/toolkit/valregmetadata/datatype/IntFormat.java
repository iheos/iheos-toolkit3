package gov.nist.toolkit.valregmetadata.datatype;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.toolkit.valregmetadata.field.ValidatorCommon;

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
