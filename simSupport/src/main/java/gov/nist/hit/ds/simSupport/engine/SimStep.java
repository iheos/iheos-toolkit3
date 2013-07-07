package gov.nist.hit.ds.simSupport.engine;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;

/**
 * Define a single simulator step. Do not initialize
 * the ErrorRecorder, it is initialized by sim engine.
 * @author bmajur
 *
 */
public class SimStep {
	String name;
	ErrorRecorder er;  // set by engine
	ValSim valsim;
	
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
	public ValSim getValSim() {
		// link to ErrorRecorder here since we
		// don't know the ordering of setter calls
		valsim.setErrorRecorder(er);
		return valsim;
	}
	public SimStep setValSim(ValSim valsim) {
		this.valsim = valsim;
		return this;
	}
	
	
	
	
	
}
