package gov.nist.hit.ds.eventLog.errorRecording.factories;

import gov.nist.hit.ds.eventLog.errorRecording.IAssertionGroup;

/**
 * Interface for classes that are factories that build ErrorRecorders
 * @author bill
 *
 */
public interface ErrorRecorderBuilder {

	public IAssertionGroup buildNewErrorRecorder();
}
