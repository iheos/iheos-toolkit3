package gov.nist.hit.ds.simSupport.engine;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.Assertion;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.eventLog.assertion.AssertionStatus;
import gov.nist.hit.ds.soapSupport.core.ValidationFault;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;


/**
 * An abstract class that makes use of the SimComponent interface easier
 * by implementing most of the required methods leaving on the run
 * method to be implemented by the component class being built.
 * @author bmajur
 *
 */
public abstract class SimComponentBase implements SimComponent {
	public AssertionGroup ag;
	public Event event;
	String name;
	String description;
	public ValidationEngine validationEngine;

	public SimComponentBase() {
		validationEngine = new ValidationEngine(this);
	}

	@Override
	public void setEvent(Event event) {
		this.event = event;
	}

	@Override
	public void setAssertionGroup(AssertionGroup er) {
		this.ag = er;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	public void assertEquals(String expected, String found, String msg) {
		ag.assertEquals(expected, found, msg);
	}

	public void assertEquals(int expected, int found, String msg) throws SoapFaultException {
		Assertion a = ag.assertEquals(expected, found, msg);
		if (validationEngine.valFault != null) {
			ValidationFault vf = validationEngine.valFault;
			a.
			setId(vf.id()).
			setReference(vf.ref()).
			setCode(vf.faultCode().toString());
			if (a.getStatus() == AssertionStatus.ERROR) {
				a.setStatus(AssertionStatus.FAULT);
				throw new SoapFaultException(
						ag,
						vf.faultCode(),
						new ErrorContext(a.getMsg(), vf.ref())
						);
			}
		}
	}


}
