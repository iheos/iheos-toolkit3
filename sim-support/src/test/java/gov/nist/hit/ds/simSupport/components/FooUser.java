package gov.nist.hit.ds.simSupport.components;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.simSupport.validationEngine.ValComponent;

public class FooUser implements ValComponent {
	Event event;
	
	public void setFoo(Foo f) {
	}

	@Override
	public void setAssertionGroup(AssertionGroup er) {
	}

	
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void run() {
		
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
