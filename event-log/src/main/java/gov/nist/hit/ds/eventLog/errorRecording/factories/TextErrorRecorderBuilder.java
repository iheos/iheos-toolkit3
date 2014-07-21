package gov.nist.hit.ds.eventLog.errorRecording.factories;

import gov.nist.hit.ds.eventLog.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.eventLog.errorRecording.TextErrorRecorder;

public class TextErrorRecorderBuilder implements ErrorRecorderBuilder {

	public IAssertionGroup buildNewErrorRecorder() {
		TextErrorRecorder rec =  new TextErrorRecorder();
		rec.errorRecorderBuilder = this;
		return rec;
	}

}
