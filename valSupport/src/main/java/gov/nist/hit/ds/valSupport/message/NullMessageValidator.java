package gov.nist.hit.ds.valSupport.message;

import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.valSupport.client.ValidationContext;
import gov.nist.hit.ds.valSupport.engine.MessageValidatorEngine;

/**
 * A validator with and empty run method.  Used as a place to hang
 * an isolated ErrorRecorder.
 * @author bill
 *
 */
public class NullMessageValidator extends AbstractMessageValidator {
	
	public NullMessageValidator(ValidationContext vc, IAssertionGroup er) {
		super(vc, er);
	}

	@Override
	public void run(MessageValidatorEngine mvc) {
		// TODO Auto-generated method stub
		
	}

}
