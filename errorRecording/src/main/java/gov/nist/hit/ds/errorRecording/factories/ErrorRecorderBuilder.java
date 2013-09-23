package gov.nist.hit.ds.errorRecording.factories;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;


/**
 * Interface for classes that are factories that build ErrorRecorders
 * @author bill
 *
 */
public interface ErrorRecorderBuilder {

	public ErrorRecorder buildNewErrorRecorder();
}
