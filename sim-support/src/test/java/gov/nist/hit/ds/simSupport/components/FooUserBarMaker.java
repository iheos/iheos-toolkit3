package gov.nist.hit.ds.simSupport.components;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.simSupport.annotations.SimComponentInject;
import gov.nist.hit.ds.simSupport.engine.SimComponent;

public class FooUserBarMaker implements SimComponent {
	Foo foo;
	Event event;
	
	@SimComponentInject
	public void setFoo(Foo f) {
		this.foo = f;
	}

	public Bar getBar() {
		return new Bar("Created in FooUserBarMaker");
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
		System.out.println("FooUserBarMaker running - foo is " + foo.getValue());
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
