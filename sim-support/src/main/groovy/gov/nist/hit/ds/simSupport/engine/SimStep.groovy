package gov.nist.hit.ds.simSupport.engine

import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.eventLog.Fault
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup
import gov.nist.hit.ds.soapSupport.FaultCode

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

    def setFault(Fault fault) { event.fault = fault }

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

    def fail(String msg) { assertionGroup.fail(msg) }

    def hasInternalError() { assertionGroup.hasInternalError() }

    def getInternalError() { assertionGroup.getInternalError() }

    def internalError(String msg) {
        Fault fault = new Fault()
        fault.faultCode = FaultCode.Receiver.toString()
        fault.faultMsg = msg
        event.setFault(fault)
        assertionGroup.internalError(msg)
    }

	public String toString() { (name) ? name : super.toString() }
}
