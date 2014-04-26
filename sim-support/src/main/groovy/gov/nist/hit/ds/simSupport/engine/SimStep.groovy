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
        crosslink()
    }

	def setEvent(Event event) {
		this.event = event;
        crosslink()
	}

	def setAssertionGroup(AssertionGroup assertionGroup) {
		this.assertionGroup = assertionGroup
        crosslink()
	}

    void setSimComponent(SimComponent component) {
        this.simComponent = component
        crosslink()
    }
	
	SimComponent getSimComponent() { return simComponent }

    private def crosslink() {
        if (event && event.assertionGroup) assertionGroup = event.assertionGroup
        if (simComponent && event) simComponent.event = event
        if (simComponent && assertionGroup) simComponent.setAssertionGroup(assertionGroup)
        if (simComponent && assertionGroup) assertionGroup.validatorName = simComponent.class.name
        if (simComponent) name = simComponent.getName()
    }

    def fail(msg) { assertionGroup.fail(msg) }

    def hasInternalError() { assertionGroup.hasInternalError() }

    def getInternalError() { assertionGroup.getInternalError() }

    def internalError(msg) { assertionGroup.internalError(msg) }

	public String toString() { (name) ? name : super.toString() }
}
