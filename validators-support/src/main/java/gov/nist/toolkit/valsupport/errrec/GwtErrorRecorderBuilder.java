package gov.nist.toolkit.valsupport.errrec;


import gov.nist.hit.ds.eventLog.errorRecording.factories.ErrorRecorderBuilder;

public class GwtErrorRecorderBuilder implements ErrorRecorderBuilder {

	public GwtErrorRecorder buildNewErrorRecorder() {
		GwtErrorRecorder rec =  new GwtErrorRecorder();
		rec.errorRecorderBuilder = this;
		return rec;
	}

}
