package gov.nist.hit.ds.errorRecording.factories;

import gov.nist.hit.ds.errorRecording.IAssertionGroup;

/**
 * Interface for classes that are factories that build ErrorRecorders
 * @author bill
 *
 */
public interface ErrorRecorderBuilder {

	public IAssertionGroup buildNewErrorRecorder();
}
