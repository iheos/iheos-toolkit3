package gov.nist.toolkit.xdstools3.server;

import gov.nist.toolkit.xdstools3.server.demo.ActorsCollectionsDataSamples;

import java.io.Serializable;
import java.util.Map;

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

        // TODO compare login with server-side admin credentials
        System.out.println("Test successful: you are logged in.");
	}


    // --------------------- Sessions and Environments ---------------------

    /**
     * Sets the list of environments
     * @return the list of available environments
     */
   public String[] retrieveEnvironments(){
     // String[] envs = Toolkit.getEnvironmentNames().toArray(new String[0]);
      String[] envs = {"NA2014", "EURO2011", "EURO2012", "NwHIN"};
       return envs;
   }

    /**
     * Sets the environment selected by the user
     */
    public void setEnvironment(String envName) {
        // FIXME NA2014 or the top item from the drop-down needs to be set as default environment in back-end.
        System.out.println("Test successful: Environment " + envName + " was set.");
    }

    /**
     * Sets the list of test sessions
     * @return the list of test sessions
     */
    public String[]  retrieveTestSessions(){
        String[] sessions = {"Test session 1", "Test session 2"};
        return sessions;
    }

    /**
     * Registers a new session name and update the client-side data
     * @param sessionName New session name entered by the user
     */
    public String[] addTestSession(String sessionName){
        String[] sessions = {"Test session 1", "Test session 2", sessionName};
        // this test data gives wrong behavior (can stay as is for now)
        System.out.println("Test successful: A new click or new session name was registered");
        return sessions;
    }



    // --------------------- Actors and Collectons ---------------------

    public Map<String, String> getCollectionNames(String collectionSetName) throws Exception  {
        return ActorsCollectionsDataSamples.instance.getCollectionNames(collectionSetName);
    }

    public Map<String, String> getCollection(String collectionSetName, String collectionName) {
        return ActorsCollectionsDataSamples.instance.getCollection(collectionSetName,collectionName);
    }

    public String getTestReadme(String test) {
        return ActorsCollectionsDataSamples.instance.getTestReadme(test);
    }


}
