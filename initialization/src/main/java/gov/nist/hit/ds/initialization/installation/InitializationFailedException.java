package gov.nist.hit.ds.initialization.installation;

public class InitializationFailedException extends Exception {

	private static final long serialVersionUID = 4525592414022786089L;

	public InitializationFailedException(String message) {
		super(message);
	}

	public InitializationFailedException(String message, Exception e) {
		super(message, e);
	}
}
