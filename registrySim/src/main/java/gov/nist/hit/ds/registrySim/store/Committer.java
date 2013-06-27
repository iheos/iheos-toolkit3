package gov.nist.hit.ds.registrySim.store;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.valSupport.client.ValidationContext;
import gov.nist.hit.ds.valSupport.engine.MessageValidatorEngine;
import gov.nist.hit.ds.valSupport.message.MessageValidator;

public class Committer extends MessageValidator {
	MetadataCollection delta;
	
	public Committer(ValidationContext vc, ErrorRecorder er, MetadataCollection delta) {
		super(vc, er);
		this.delta = delta;
	}

	// caller takes responsibility for locking

	public void run(MessageValidatorEngine mvc) {
		// merge in changes
		delta.mergeDelta(er);
		
	}

}
