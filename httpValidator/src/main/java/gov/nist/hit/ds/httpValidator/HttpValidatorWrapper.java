package gov.nist.hit.ds.httpValidator;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.valSupport.message.MessageValidator;

public class HttpValidatorWrapper implements MessageValidator {
	ErrorRecorder er;
	
	@Override
	public void setErrorRecorder(ErrorRecorder er) {
		this.er = er;
	}

	@Override
	public ErrorRecorder getErrorRecorder() {
		return er;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
