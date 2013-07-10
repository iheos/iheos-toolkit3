package gov.nist.hit.ds.simSupport;

public class SimFailureException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SimFailureException(String msg) {
		super(msg);
	}
	
	public SimFailureException(String msg, Exception e) {
		super(msg,e);
	}
	
}
