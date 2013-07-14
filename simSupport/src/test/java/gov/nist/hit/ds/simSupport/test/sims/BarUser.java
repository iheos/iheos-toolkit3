package gov.nist.hit.ds.simSupport.test.sims;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.simSupport.engine.SimComponent;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.simSupport.test.datatypes.Bar;

public class BarUser implements SimComponent {

	public void setBar(Bar b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setErrorRecorder(ErrorRecorder er) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void run(MessageValidatorEngine mve) {
		System.out.println("BarUser run");
	}
}
