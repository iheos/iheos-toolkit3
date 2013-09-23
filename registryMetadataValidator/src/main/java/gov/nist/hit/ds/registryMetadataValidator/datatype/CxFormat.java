package gov.nist.hit.ds.registryMetadataValidator.datatype;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.registryMetadataValidator.field.ValidatorCommon;

public class CxFormat extends FormatValidator {

	public CxFormat(IAssertionGroup er, String context, String resource) {
		super(er, context, resource);
	}

	public void validate(String input) {
		String error = ValidatorCommon.validate_CX_datatype(input);		
		if (error != null) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(context + ": " + input + " : " + error, getResource(null))  , this);
		}
	}

}
