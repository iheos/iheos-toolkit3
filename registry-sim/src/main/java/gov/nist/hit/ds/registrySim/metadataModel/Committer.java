package gov.nist.hit.ds.registrySim.metadataModel;

import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.valSupport.client.ValidationContext;
import gov.nist.hit.ds.valSupport.engine.MessageValidatorEngine;
import gov.nist.hit.ds.valSupport.message.AbstractMessageValidator;

public class Committer extends AbstractMessageValidator {
	MetadataCollection delta;
	
	public Committer(ValidationContext vc, IAssertionGroup er, MetadataCollection delta) {
		super(vc, er);
		this.delta = delta;
	}

	// caller takes responsibility for locking

	public void run(MessageValidatorEngine mvc) {
		// merge in changes
		delta.mergeDelta(er);
		
	}

}
