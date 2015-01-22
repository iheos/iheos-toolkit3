package gov.nist.hit.ds.testClient.metadataValidator.datatype;

import gov.nist.hit.ds.testClient.logging.ErrorRecorder;
import gov.nist.hit.ds.testClient.logging.XdsErrorCode;
import org.apache.axiom.om.OMElement;

public abstract class FormatValidator {
	ErrorRecorder er;
	String context;
	String resource;
	
	public FormatValidator(ErrorRecorder er, String context, String resource) {
		this.er = er;
		this.context = context;
		this.resource = resource;
	}
	
	public void validate(String input) throws FormatValidatorCalledIncorrectlyException {
		throw new FormatValidatorCalledIncorrectlyException("Called incorrectly");
	}
	
	public void validate(OMElement input) throws FormatValidatorCalledIncorrectlyException {
		throw new FormatValidatorCalledIncorrectlyException("Called incorrectly");
	}
	
	protected String getResource(String specialResource) {
		if (resource == null)
			return specialResource;
		else if (specialResource == null)
			return resource;
		else
			return resource + " and " + specialResource;

	}
	
	protected void err(String input, String error, String additionalResource) {
		er.err(XdsErrorCode.Code.XDSRegistryMetadataError, context + ": " + input + " : " + error, this, getResource(additionalResource));
	}
}