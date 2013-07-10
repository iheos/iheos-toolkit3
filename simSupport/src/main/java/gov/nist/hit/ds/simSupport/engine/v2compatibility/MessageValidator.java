package gov.nist.hit.ds.simSupport.engine.v2compatibility;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.simSupport.ValidationContext;
import gov.nist.hit.ds.simSupport.engine.ValSim;


/**
 * This class exists to provide backwards compatibility with version 2.
 * Its use should be temporary, ending when all services are transitioned.
 * @author bmajur
 *
 */
abstract public class MessageValidator implements ValSim {
	public ValidationContext vc;
	public ErrorRecorder er;
	String name;  // a descriptive name for debugging displays
	
	abstract public void run(ErrorRecorder er, MessageValidatorEngine mve);

	public MessageValidator(ValidationContext vc) {
		this.vc = vc;
	}
	
	public ErrorRecorder getErrorRecorder() {
		return er;
	}

	@Override
	public void setErrorRecorder(ErrorRecorder er) {
		this.er = er;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public MessageValidator setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Called by SimEngine to execute the Simulator logic. The
	 * SimEngine only know this via the ValSim interface.
	 */
	@Override
	public void run(MessageValidatorEngine mve) {
		run(er, mve);
	}

}
