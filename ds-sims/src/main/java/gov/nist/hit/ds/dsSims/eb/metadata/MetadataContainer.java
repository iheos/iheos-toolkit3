package gov.nist.hit.ds.dsSims.eb.metadata;

import gov.nist.hit.ds.dsSims.eb.MessageValidator;
import gov.nist.hit.ds.dsSims.eb.client.ValidationContext;
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.simSupport.v2compatibility.MessageValidatorEngine;

/**
 * Empty shell of a validator that holds a copy of the pre-parsed 
 * metadata so other validators can find it later.
 * @author bill
 *   
 */
public class MetadataContainer extends MessageValidator {
	Metadata m;
	
	public MetadataContainer(ValidationContext vc, Metadata m) {
		super(vc);
		this.m = m;
	}

	public void run(ErrorRecorder er, MessageValidatorEngine mvc) {
		this.er = er;
		
	}
	
	public Metadata getMetadata() {
		return m;
	}

}
