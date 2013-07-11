package gov.nist.hit.ds.simSupport.test.sims;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.simSupport.engine.SimElement;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.simSupport.test.datatypes.Bar;
import gov.nist.hit.ds.simSupport.test.datatypes.Foo;

public class FooUserBarMaker implements SimElement {
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
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void run(MessageValidatorEngine mve) {
		System.out.println("FooUserBarMaker running - foo is " + foo.getValue());
	}

}
