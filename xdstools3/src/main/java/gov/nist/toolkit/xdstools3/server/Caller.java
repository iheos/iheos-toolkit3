package gov.nist.toolkit.xdstools3.server;

import java.io.Serializable;

/**
 * Singleton class that acts as a directory of all calls to server-side packages. All calls must go through this class in order to
 * develop and build easily on top of this application.
 * 
 * @author dazais
 *
 */
public class Caller implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6431109235310163158L;
	private static Caller instance = null;
	
	protected Caller(){
	}

	/**
	 * Singleton method. Use this method to gain access to the functionality in this class.
	 * @return
	 */
	public static Caller getInstance(){
		if (instance == null){
			instance = new Caller();
		}
		return instance;
	}

	// List here the calls to server-side packages
	public void logMeIn(String username, String password){
		System.out.println("Call to server successful");
	}
	
}
