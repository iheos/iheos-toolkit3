package gov.nist.hit.ds.simSupport.sims;

import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.simSupport.datatypes.Bar;
import gov.nist.hit.ds.simSupport.engine.SimComponent;
import gov.nist.hit.ds.simSupport.v2compatibility.MessageValidatorEngine;

public class BarUser implements SimComponent {
	Event event;
	
	public void setBar(Bar b) {
	}

	@Override
	public void setAssertionGroup(AssertionGroup er) {
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void run(MessageValidatorEngine mve) {
		System.out.println("BarUser injectAll");
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