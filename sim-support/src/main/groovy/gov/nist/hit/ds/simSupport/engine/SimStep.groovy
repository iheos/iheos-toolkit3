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
    boolean completed = false;

    def init(Event event) {
        if (this.event) return
        this.event = event
        assertionGroup = event.assertionGroup
        assertionGroup.validatorName = simComponent.class.name
    }

	public SimStep setEvent(Event event) {
		this.event = event;
		simComponent.setEvent(event);
		return this;
	}

	public SimStep setAssertionGroup(AssertionGroup assertionGroup) {
		this.assertionGroup = assertionGroup
		simComponent.setAssertionGroup(assertionGroup)
		return this
	}
	
	public SimComponent getSimComponent() {
		// link to ErrorRecorder here since we
		// don't know the ordering of setter calls
		simComponent.setAssertionGroup(assertionGroup)
		return simComponent
	}

	public String toString() { (name) ? name : super.toString() }
}
