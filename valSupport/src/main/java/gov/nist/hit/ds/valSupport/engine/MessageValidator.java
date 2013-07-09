package gov.nist.hit.ds.valSupport.engine;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.valSupport.client.ValidationContext;

import org.apache.axiom.om.OMElement;

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
	
	
	abstract public void run(ErrorRecorder er, MessageValidatorEngine mvc);

	public MessageValidator(ValidationContext vc) {
		this.vc = vc;
	}
	
	public ErrorRecorder getErrorRecorder() {
		return er;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("\n\tValidationContext: ").append(vc.toString());
		
		return buf.toString();
	}
}
