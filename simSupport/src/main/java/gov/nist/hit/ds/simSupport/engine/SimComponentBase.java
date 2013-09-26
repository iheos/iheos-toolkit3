package gov.nist.hit.ds.simSupport.engine;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.Assertion;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.eventLog.assertion.AssertionStatus;
import gov.nist.hit.ds.eventLog.assertion.annotations.Validation;
import gov.nist.hit.ds.simSupport.validationEngine.ValidationEngine;
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
	
	/******************************************
	 * 
	 * Cooperate with ValidationEngine
	 * @throws SoapFaultException 
	 * 
	 */
	
	public boolean fail(String expected) throws SoapFaultException {
		Assertion a = ag.fail(expected);
		recordAssertion(a);
		return !a.failed();
	}

	public boolean assertEquals(String expected, String found) throws SoapFaultException {
		Assertion a = ag.assertEquals(expected, found);
		recordAssertion(a);
		return !a.failed();
	}

	public boolean assertEquals(int expected, int found) throws SoapFaultException {
		Assertion a = ag.assertEquals(expected, found);
		recordAssertion(a);
		return !a.failed();
	}
	
	public boolean assertNotNull(Object value) throws SoapFaultException {
		Assertion a = ag.assertNotNull(value);
		recordAssertion(a);
		return !a.failed();
	}

	void recordAssertion(Assertion a) throws SoapFaultException {
		if (validationEngine.validationFaultAnnotation != null) {
			ValidationFault vf = validationEngine.validationFaultAnnotation;
			a.
			setId(vf.id()).
			setMsg(vf.msg()).
			setReference(vf.ref()).
			setCode(vf.faultCode().toString());
			if (a.getStatus() == AssertionStatus.ERROR) {
				a.setStatus(AssertionStatus.FAULT);
				throw new SoapFaultException(
						ag,
						vf.faultCode(),
						new ErrorContext(a.getMsg(), Assertion.buildSemiDivided(vf.ref()))
						);
			}
		}
		if (validationEngine.validationAnnotation != null) {
			Validation vf = validationEngine.validationAnnotation;
			String id = vf.id();
			a.
			setId(id).
			setMsg(vf.msg()).
			setReference(vf.ref());
		}
	}

}
