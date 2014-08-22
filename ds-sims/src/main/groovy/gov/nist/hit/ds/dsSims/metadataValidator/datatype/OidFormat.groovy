package gov.nist.hit.ds.dsSims.metadataValidator.datatype
import gov.nist.hit.ds.dsSims.metadataValidator.field.ValidatorCommon
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase

public class OidFormat extends FormatValidator {

	public OidFormat(ValComponentBase base, String attName) {
		super(base);
	}

	public void validate(String input) {
		if (!ValidatorCommon.is_oid(input, true))
            base.fail('OID Format of ' + attName, input)
//			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, context + ": " + input + " is not in OID format", this, getResource(null));
	}

}
