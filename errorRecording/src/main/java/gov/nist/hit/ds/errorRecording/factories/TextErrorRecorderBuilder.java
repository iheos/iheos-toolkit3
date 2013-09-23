package gov.nist.hit.ds.errorRecording.factories;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.TextErrorRecorder;

public class TextErrorRecorderBuilder implements ErrorRecorderBuilder {

	public ErrorRecorder buildNewErrorRecorder() {
		TextErrorRecorder rec =  new TextErrorRecorder();
		rec.errorRecorderBuilder = this;
		return rec;
	}

}
