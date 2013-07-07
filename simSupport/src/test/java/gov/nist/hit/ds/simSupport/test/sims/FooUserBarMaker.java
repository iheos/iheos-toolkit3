package gov.nist.hit.ds.simSupport.test.sims;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.simSupport.engine.ValSim;
import gov.nist.hit.ds.simSupport.test.datatypes.Bar;
import gov.nist.hit.ds.simSupport.test.datatypes.Foo;

public class FooUserBarMaker implements ValSim {
	Foo foo;
	
	public void setFoo(Foo f) {
		this.foo = f;
	}

	public Bar getBar() {
		// TODO Auto-generated method stub
		return new Bar("Created in FooUserBarMaker");
	}

	@Override
	public void setErrorRecorder(ErrorRecorder er) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		System.out.println("FooUserBarMaker running - foo is " + foo.getValue());
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

}
