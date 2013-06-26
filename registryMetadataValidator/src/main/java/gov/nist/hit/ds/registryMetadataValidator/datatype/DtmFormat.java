package gov.nist.hit.ds.registryMetadataValidator.datatype;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.registryMetadataValidator.field.ValidatorCommon;

public class DtmFormat extends FormatValidator {

	public DtmFormat(ErrorRecorder er, String context, String resource) {
		super(er, context, resource);
	}

	public void validate(String input) {
		int size = input.length();
		if (!(size == 4 || size == 6 || size == 8 || size == 10 || size == 12 || size == 14))
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(context + ": " + input + " is not in HL7 V2 DateTime format: it has an invalid number of characters", getResource("ITI TF-3: Table 4.1-3")), this);
		if (!ValidatorCommon.isInt(input) )
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(context + ": " + input + " is not in HL7 V2 DateTime format: all characters must be digits", getResource("ITI TF-3: Table 4.1-3")), this);
	}

}
