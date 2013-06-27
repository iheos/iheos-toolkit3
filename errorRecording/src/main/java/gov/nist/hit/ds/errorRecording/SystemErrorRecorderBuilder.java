package gov.nist.hit.ds.errorRecording;

import gov.nist.hit.ds.errorRecording.factories.ErrorRecorderBuilder;


public class SystemErrorRecorderBuilder implements ErrorRecorderBuilder {

	public SystemErrorRecorder buildNewErrorRecorder() {
		SystemErrorRecorder rec =  new SystemErrorRecorder();
		rec.errorRecorderBuilder = this;
		return rec;
	}

}
