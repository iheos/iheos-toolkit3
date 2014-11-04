package gov.nist.hit.ds.simSupport.components;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.simSupport.validationEngine.ValComponent;

public class FooUser implements ValComponent {
	Event event;
	
	public void setFoo(Foo f) {
	}

	public void setAssertionGroup(AssertionGroup er) {
	}

	
	public String getName() {
		return this.getClass().getSimpleName();
	}

	public void run() {
		
	}

	public String getDescription() {
		return null;
	}

	public void setName(String name) {
	}

	public void setDescription(String description) {
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public boolean showOutputInLogs() {
		return false;
	}

}
