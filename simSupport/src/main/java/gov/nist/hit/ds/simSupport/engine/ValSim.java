package gov.nist.hit.ds.simSupport.engine;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;

/**
 * Identifies a class as a valsim. Two methods are required, setErrorRecorder 
 * and run.
 * @author bmajur
 *
 */
public interface ValSim {
	void setErrorRecorder(ErrorRecorder er);
	String getName();
	void run(MessageValidatorEngine mve);
}
