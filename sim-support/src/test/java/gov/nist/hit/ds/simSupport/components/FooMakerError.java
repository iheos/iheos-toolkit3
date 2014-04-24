package gov.nist.hit.ds.simSupport.components;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.simSupport.engine.SimComponent;
import gov.nist.hit.ds.simSupport.v2compatibility.MessageValidatorEngine;

public class FooMakerError implements SimComponent{

	Foo foo;
	AssertionGroup er;
	Event event;

	public Foo getFoo() {
		return foo;
	}


	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}


	@Override
	public void setAssertionGroup(AssertionGroup er) {
		this.er = er;
	}


	@Override
	public void run(MessageValidatorEngine mve) {
		try {
			throw new Exception("FooMakerError");
		} catch (Exception e) {
//			er.err(XdsErrorCode.Code.NoCode, e);
		}
	}


	@Override
	public String getDescription() {
		return null;
	}


	@Override
	public void setName(String name) {
	}


	@Override
	public void setDescription(String description) {
	}


	@Override
	public void setEvent(Event event) {
		this.event = event;
	}


	@Override
	public boolean showOutputInLogs() {
		return false;
	}

}
