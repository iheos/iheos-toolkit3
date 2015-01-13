package gov.nist.hit.ds.xdsExceptions;

public class NoSessionException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NoSessionException(String msg) {
		super(msg);
	}

	public NoSessionException(String msg, Exception e) {
		super(msg, e);
	}

}
