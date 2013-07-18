package gov.nist.hit.ds.simSupport.sims;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode.Code;
import gov.nist.hit.ds.simSupport.datatypes.Foo;
import gov.nist.hit.ds.simSupport.engine.SimComponent;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;

public class FooMakerError implements SimComponent{

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


	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setDescription(String description) {
		// TODO Auto-generated method stub
		
	}

}
