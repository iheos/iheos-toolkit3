package gov.nist.hit.ds.simSupport.test.sims;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode.Code;
import gov.nist.hit.ds.simSupport.engine.ValSim;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.simSupport.test.datatypes.Foo;

public class FooMakerError implements ValSim{

	Foo foo;
	ErrorRecorder er;

	public Foo getFoo() {
		return foo;
	}


	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}


	@Override
	public void setErrorRecorder(ErrorRecorder er) {
		this.er = er;
	}


	@Override
	public void run(MessageValidatorEngine mve) {
		try {
			throw new Exception("FooMakerError");
		} catch (Exception e) {
			er.err(Code.NoCode, e);
		}
	}

}
