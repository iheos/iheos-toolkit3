package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype

import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import org.apache.axiom.om.OMElement

public abstract class FormatValidator {
    ValComponentBase base

	public FormatValidator(ValComponentBase base) {
		this.base = base;
	}
	
	public void validate(String input) {
        base.fail('FormatValidatorCalledIncorrectly')
	}
	
	public void validate(OMElement input) throws FormatValidatorCalledIncorrectlyException {
        base.fail('FormatValidatorCalledIncorrectly')
	}
	
//	protected String getResource(String specialResource) {
//		if (resource == null)
//			return specialResource;
//		else if (specialResource == null)
//			return resource;
//		else
//			return resource + " and " + specialResource;
//
//	}
//
//	protected void err(String input, String error, String additionalResource) {
//		er.err(XdsErrorCode.Code.XDSRegistryMetadataError, context + ": " + input + " : " + error, this, getResource(additionalResource));
//	}
}
