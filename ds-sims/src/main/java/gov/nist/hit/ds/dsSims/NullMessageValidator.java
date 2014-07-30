package gov.nist.hit.ds.dsSims;

import gov.nist.hit.ds.dsSims.client.ValidationContext;
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.simSupport.v2compatibility.MessageValidatorEngine;

/**
 * A validator with and empty run method.  Used as a place to hang
 * an isolated ErrorRecorder.
 * @author bill
 *
 */
public class NullMessageValidator extends MessageValidator {
	
	public NullMessageValidator(ValidationContext vc) {
		super(vc);
	}

	public void run(ErrorRecorder er, MessageValidatorEngine mvc) {
		this.er = er;
		
	}

}
