package gov.nist.hit.ds.simSupport.engine;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;

/**
 * An abstract class that makes use of the SimComponent interface easier
 * by implementing most of the required methods leaving on the run
 * method to be implemented by the component class being built.
 * @author bmajur
 *
 */
public abstract class SimComponentBase implements SimComponent {
	public ErrorRecorder er;
	String name;
	String description;
	
	@Override
	public void setErrorRecorder(ErrorRecorder er) {
		this.er = er;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}


}
