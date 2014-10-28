package gov.nist.hit.ds.simSupport.components;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.simSupport.validationEngine.ValComponent;

public class FooMakerNoOutput implements ValComponent {
	Foo foo;
	Event event;

	public String getName() {
		return this.getClass().getSimpleName();
	}


	public void setAssertionGroup(AssertionGroup er) {
	}


	public void run() {
		foo = new Foo("Fubar");
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

    public void setParm(String value) {}
}
