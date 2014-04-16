package gov.nist.hit.ds.errorRecording.factories;

import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.SystemErrorRecorder;


public class SystemErrorRecorderBuilder implements ErrorRecorderBuilder {

	public IAssertionGroup buildNewErrorRecorder() {
		SystemErrorRecorder rec =  new SystemErrorRecorder();
		rec.setErrorRecorderBuilder(this);
		return rec;
	}

}
