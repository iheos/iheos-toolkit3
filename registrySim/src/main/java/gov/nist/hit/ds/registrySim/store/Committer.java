package gov.nist.hit.ds.registrySim.store;

import gov.nist.toolkit.errorRecording.ErrorRecorder;
import gov.nist.toolkit.valSupport.client.ValidationContext;
import gov.nist.toolkit.valSupport.engine.MessageValidatorEngine;
import gov.nist.toolkit.valSupport.message.MessageValidator;

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
