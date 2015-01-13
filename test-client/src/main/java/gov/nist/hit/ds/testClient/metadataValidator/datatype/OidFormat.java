package gov.nist.hit.ds.testClient.metadataValidator.datatype;

import gov.nist.hit.ds.testClient.logging.ErrorRecorder;
import gov.nist.hit.ds.testClient.logging.XdsErrorCode;
import gov.nist.hit.ds.testClient.metadataValidator.field.ValidatorCommon;

public class OidFormat extends FormatValidator {

	public OidFormat(ErrorRecorder er, String context, String resource) {
		super(er, context, resource);
	}

	public void validate(String input) {
		if (!ValidatorCommon.is_oid(input, true))
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, context + ": " + input + " is not in OID format", this, getResource(null));
	}

}
