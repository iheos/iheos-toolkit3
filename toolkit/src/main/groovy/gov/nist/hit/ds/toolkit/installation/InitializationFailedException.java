package gov.nist.hit.ds.toolkit.installation;

import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;

public class InitializationFailedException extends ToolkitRuntimeException {

	private static final long serialVersionUID = 4525592414022786089L;

	public InitializationFailedException(String message) {
		super(message);
	}

	public InitializationFailedException(String message, Exception e) {
		super(message, e);
	}
}
