package gov.nist.hit.ds.simSupport.engine;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;

/**
 * Identifies a class as a valsim. Two methods are required, setErrorRecorder 
 * and run.
 * @author bmajur
 *
 */
public interface ValSim {
	void setErrorRecorder(ErrorRecorder er);
	void run();
}
