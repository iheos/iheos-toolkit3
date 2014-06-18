package gov.nist.hit.ds.valSupport.message;

import gov.nist.hit.ds.errorRecording.IAssertionGroup;

/**
 * A message validator is the basic interface for both validators
 * and simulator components.
 * @author bmajur
 *
 */
public interface MessageValidator {
	void setErrorRecorder(IAssertionGroup er);
	IAssertionGroup getErrorRecorder();
	void run();
}
