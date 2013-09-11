package gov.nist.hit.ds.simSupport.engine.v2compatibility;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.simSupport.engine.SimComponent;
import gov.nist.hit.ds.simSupport.loader.ValidationContext;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;


/**
 * This class exists to provide backwards compatibility with version 2.
 * Its use should be temporary, ending when all services are transitioned.
 * @author bmajur
 *
 */
abstract public class MessageValidator implements SimComponent {
	public ValidationContext vc;
	public ErrorRecorder er;
	String name;  // a descriptive name for debugging displays
	
	abstract public void run(ErrorRecorder er, MessageValidatorEngine mve) throws SoapFaultException;

	public MessageValidator(ValidationContext vc) {
		this.vc = vc;
	}
	
	public MessageValidator() {
		
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
	
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Called by SimEngine to execute the Simulator logic. The
	 * SimEngine only know this via the ValSim interface.
	 */
	@Override
	public void run(MessageValidatorEngine mve) throws SoapFaultException {
		run(er, mve);
	}

}
