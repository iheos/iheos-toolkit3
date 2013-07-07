package gov.nist.hit.ds.simSupport.test.sims;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.simSupport.engine.ValSim;
import gov.nist.hit.ds.simSupport.test.datatypes.Foo;

public class FooUser implements ValSim {

	public void setFoo(Foo f) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setErrorRecorder(ErrorRecorder er) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

}
