package gov.nist.hit.ds.simSupport.engine;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;

/**
 * Define a single simulator step. Do not initialize
 * the ErrorRecorder, it is initialized by sim engine.
 * @author bmajur
 *
 */
public class SimStep {
	String name = null;
	ErrorRecorder er = null;  // set by engine
	SimElement valsim = null;
	boolean ran = false;
	
	public String getName() {
		return name;
	}
	public SimStep setName(String name) {
		this.name = name;
		return this;
	}
	public ErrorRecorder getErrorRecorder() {
		return er;
	}
	public SimStep setErrorRecorder(ErrorRecorder er) {
		this.er = er;
		return this;
	}
	public SimElement getValSim() {
		// link to ErrorRecorder here since we
		// don't know the ordering of setter calls
		valsim.setErrorRecorder(er);
		return valsim;
	}
	public SimStep setValSim(SimElement valsim) {
		this.valsim = valsim;
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
