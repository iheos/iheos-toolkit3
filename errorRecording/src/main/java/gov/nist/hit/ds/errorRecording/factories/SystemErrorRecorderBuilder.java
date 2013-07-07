package gov.nist.hit.ds.errorRecording.factories;

import gov.nist.hit.ds.errorRecording.SystemErrorRecorder;


public class SystemErrorRecorderBuilder implements ErrorRecorderBuilder {

	public SystemErrorRecorder buildNewErrorRecorder() {
		SystemErrorRecorder rec =  new SystemErrorRecorder();
		rec.setErrorRecorderBuilder(this);
		return rec;
	}

}
