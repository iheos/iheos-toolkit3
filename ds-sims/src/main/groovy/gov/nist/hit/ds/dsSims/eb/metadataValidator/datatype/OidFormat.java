package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype;

import gov.nist.hit.ds.dsSims.eb.metadataValidator.field.ValidatorCommon;
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode;

public class OidFormat extends FormatValidator {

	public OidFormat(ErrorRecorder er, String context, String resource) {
		super(er, context, resource);
	}

	public void validate(String input) {
		if (!ValidatorCommon.is_oid(input, true))
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, context + ": " + input + " is not in OID format", this, getResource(null));
	}

}
