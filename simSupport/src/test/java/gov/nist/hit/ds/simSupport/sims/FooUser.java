package gov.nist.hit.ds.simSupport.sims;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.simSupport.datatypes.Foo;
import gov.nist.hit.ds.simSupport.engine.SimComponent;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;

public class FooUser implements SimComponent {
	Event event;
	
	public void setFoo(Foo f) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setAssertionGroup(AssertionGroup er) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void run(MessageValidatorEngine mve) {
		
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDescription(String description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEvent(Event event) {
		this.event = event;
	}

}
