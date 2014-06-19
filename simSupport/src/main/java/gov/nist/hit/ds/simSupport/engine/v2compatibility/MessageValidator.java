package gov.nist.hit.ds.simSupport.engine.v2compatibility;

import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
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
	public IAssertionGroup er;
	String name;  // a descriptive name for debugging displays
	Event event;
	
	abstract public void run(IAssertionGroup er, MessageValidatorEngine mve) throws SoapFaultException;

	public MessageValidator(ValidationContext vc) {
		this.vc = vc;
	}
	
	public MessageValidator() {
		
	}
	
	public IAssertionGroup getErrorRecorder() {
		return er;
	}

	@Override
	public void setAssertionGroup(AssertionGroup er) {
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
	
	public void setEvent(Event event) {
		this.event = event;
	}

}
