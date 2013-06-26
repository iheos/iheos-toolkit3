package gov.nist.hit.ds.valSupport.message;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.factories.ErrorRecorderBuilder;
import gov.nist.hit.ds.valSupport.client.ValidationContext;
import gov.nist.hit.ds.valSupport.engine.MessageValidatorEngine;

/**
 * Abstract class that all valiators are based on. It's primary
 * significance is forcing validators to support the run method so they
 * can be called when their turn comes on the validation queue.
 * @author bill
 *
 */
abstract public class MessageValidator {
	protected ValidationContext vc; 
	public ErrorRecorder er;
	
	
	abstract public void run(MessageValidatorEngine mvc);

	public MessageValidator(ValidationContext vc, ErrorRecorder er) {
		this.vc = vc;
		this.er = er;
	}
	
	public MessageValidator(ValidationContext vc, ErrorRecorderBuilder erBuilder) {
		this.vc = vc;
		this.er = erBuilder.buildNewErrorRecorder();
	}
	
	public ErrorRecorder getErrorRecorder() {
		return er;
	}
	
	public boolean isSuccess() {
		return er.hasErrors();	
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("\n\tValidationContext : ")
		.append(this.getClass().getName())
		.append(" : ")
		.append(vc.toString());
		
		return buf.toString();
	}
	

}
