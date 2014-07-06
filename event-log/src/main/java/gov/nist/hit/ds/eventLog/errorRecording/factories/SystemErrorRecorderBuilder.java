package gov.nist.hit.ds.eventLog.errorRecording.factories;

import gov.nist.hit.ds.eventLog.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.eventLog.errorRecording.SystemErrorRecorder;


public class SystemErrorRecorderBuilder implements ErrorRecorderBuilder {

	public IAssertionGroup buildNewErrorRecorder() {
		SystemErrorRecorder rec =  new SystemErrorRecorder();
		rec.setErrorRecorderBuilder(this);
		return rec;
	}

}
