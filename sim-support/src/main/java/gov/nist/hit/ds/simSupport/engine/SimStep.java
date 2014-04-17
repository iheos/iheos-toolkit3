package gov.nist.hit.ds.simSupport.engine;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;

/**
 * Define a single simulator step. Do not installRepositoryLinkage
 * the ErrorRecorder, it is initialized by sim engine.
 * @author bmajur
 *
 */
public class SimStep {
	String name = null;
	AssertionGroup assertionGroup = null;  // set by engine
	Event event = null;        // set by engine
	SimComponent simComponent = null;

	public String getName() {
		return name;
	}
	
	public SimStep setName(String name) {
		this.name = name;
		return this;
	}
	
	public Event getEvent() {
		return event;
	}

	public SimStep setEvent(Event event) {
		this.event = event;
		simComponent.setEvent(event);
		return this;
	}

	public AssertionGroup getAssertionGroup() {
		return assertionGroup;
	}
	
	public SimStep setAssertionGroup(AssertionGroup er) {
		this.assertionGroup = er;
		simComponent.setAssertionGroup(er);
		return this;
	}
	
	public SimComponent getSimComponent() {
		// link to ErrorRecorder here since we
		// don't know the ordering of setter calls
		simComponent.setAssertionGroup(assertionGroup);
		return simComponent;
	}
	
	public SimStep setSimComponent(SimComponent valsim) {
		this.simComponent = valsim;
		return this;
	}
	
	public String toString() {
		if (name != null)
			return name;
		else
			return super.toString();
	}
}
