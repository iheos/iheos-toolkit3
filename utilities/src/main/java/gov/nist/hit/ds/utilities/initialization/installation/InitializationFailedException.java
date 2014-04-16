package gov.nist.hit.ds.utilities.initialization.installation;

import gov.nist.hit.ds.utilities.xdsException.ToolkitRuntimeException;

public class InitializationFailedException extends ToolkitRuntimeException {

	private static final long serialVersionUID = 4525592414022786089L;

	public InitializationFailedException(String message) {
		super(message);
	}

	public InitializationFailedException(String message, Exception e) {
		super(message, e);
	}
}
