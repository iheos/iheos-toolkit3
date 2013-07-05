package gov.nist.hit.ds.simSupport.test.sims;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.simSupport.engine.ValSim;
import gov.nist.hit.ds.simSupport.test.datatypes.Bar;

public class BarUser implements ValSim {

	public void setBar(Bar b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setErrorRecorder(ErrorRecorder er) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		System.out.println("BarUser run");
	}

}
