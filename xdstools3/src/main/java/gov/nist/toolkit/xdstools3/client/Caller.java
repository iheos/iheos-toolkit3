package gov.nist.toolkit.xdstools3.client;

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

	// ---- List here the calls to backend packages ----

    /**
     * Login as admin
     * @param username the admin username
     * @param password the admin password
     */
	public void logMeIn(String username, String password){
		System.out.println("Call to server successful");
	}

    /**
     * Set the list of environments
     * @return the list of available environments
     */
   public String[] retrieveEnvironments(){
        return null;
   }

    /**
     * Set the list of test sessions
     * @return the list of test sessions
     */
    public String[]  retrieveTestSessions(){
        return null;
    }

}
