package gov.nist.hit.ds.testClient.metadataValidator.datatype;

import gov.nist.hit.ds.testClient.logging.ErrorRecorder;
import org.apache.axiom.om.OMElement;

public class AnyFormat extends FormatValidator {

	public AnyFormat(ErrorRecorder er, String context, String resource) {
		super(er, context, resource);
	}

	public void validate(String input) {
	}
	
	public void validate(OMElement input) {
	}

}
