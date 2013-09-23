package gov.nist.hit.ds.registryMetadataValidator.datatype;

import gov.nist.hit.ds.errorRecording.IAssertionGroup;

import org.apache.axiom.om.OMElement;


public class AnyFormat extends FormatValidator {

	public AnyFormat(IAssertionGroup er, String context, String resource) {
		super(er, context, resource);
	}

	public void validate(String input) {
	}
	
	public void validate(OMElement input) {
	}

}
