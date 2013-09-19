package gov.nist.hit.ds.simSupport.engine;

import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;

/**
 * Define a single simulator step. Do not initialize
 * the ErrorRecorder, it is initialized by sim engine.
 * @author bmajur
 *
 */
public class SimStep {
	String name = null;
	AssertionGroup ag = null;  // set by engine
	SimComponent simComponent = null;
	boolean ran = false;
	
	public String getName() {
		return name;
	}
	
	public SimStep setName(String name) {
		this.name = name;
		return this;
	}
	
	public AssertionGroup getAssertionGroup() {
		return ag;
	}
	
	public SimStep setAssertionGroup(AssertionGroup er) {
		this.ag = er;
		simComponent.setErrorRecorder(er);
		return this;
	}
	
	public SimComponent getSimComponent() {
		// link to ErrorRecorder here since we
		// don't know the ordering of setter calls
		simComponent.setErrorRecorder(ag);
		return simComponent;
	}
	
	public SimStep setSimComponent(SimComponent valsim) {
		this.simComponent = valsim;
		return this;
	}
	
	public boolean hasRan() {
		return ran;
	}
	
	public boolean hasRan(boolean hasRan) {
		ran = hasRan;
		return ran;
	}
	
	public String toString() {
		if (name != null)
			return name;
		else
			return super.toString();
	}
}
