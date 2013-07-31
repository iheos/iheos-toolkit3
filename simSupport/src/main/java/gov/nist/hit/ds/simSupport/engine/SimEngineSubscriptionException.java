package gov.nist.hit.ds.simSupport.engine;

/**
 * Report an error attempting to match up Publishers and Subscribers in the SimEngine.
 * @author bmajur
 *
 */
public class SimEngineSubscriptionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SimEngineSubscriptionException(String msg) {
		super(msg);
	}

}
