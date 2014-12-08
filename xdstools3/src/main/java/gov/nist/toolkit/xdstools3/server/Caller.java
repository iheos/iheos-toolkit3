package gov.nist.toolkit.xdstools3.server;

import gov.nist.hit.ds.toolkit.Toolkit;
import gov.nist.toolkit.xdstools3.server.RPCServices.SaveTempFileService;
import gov.nist.toolkit.xdstools3.server.demo.ActorsCollectionsDataSamples;
import gov.nist.toolkit.xdstools3.server.demo.TestDataHelper;
import org.apache.log4j.Logger;

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

	private static final long serialVersionUID = -6431109235310163158L;
	private static Caller instance = null;
    private final static Logger logger = Logger.getLogger(Caller.class.getName());
    private final SaveTempFileService saveTempFileService = new SaveTempFileService();

	protected Caller(){
	}

	/**
	 * Singleton method. Use this method to gain access to the functionality in this class.
	 * @return Caller instance
	 */
	public static Caller getInstance(){
		if (instance == null){
			instance = new Caller();
		}
		return instance;
	}



    // ------------------ Administrator functionality --------------------
    /**
     * Login as admin
     * @param password the admin password
     */
    public boolean logMeIn(String password){
        String pwd = Toolkit.getPassword();
        if (password.equals(pwd)){
            logger.info("User logged in as admin.");
            return true;
        }
        return false;
    }

    /**
     * Saves the application settings as defined by the administrator(s)
     * @param settings Table of settings: host, port, tls_port, cache, environment, gazelleURL
     */
    public void saveAdminSettings(String[] settings){
        // TODO save admin settings to back-end
        System.out.println("Admin settings were saved");
    }

    /**
     * Changes the administrator password.
     * @param oldPassword
     * @param newPassword
     */
    public void saveAdminPassword(String oldPassword, String newPassword){
        // TODO save admin password to back-end if oldPassword is correct
        System.out.println("Admin password was saved");
    }

    /**
     * Retrieves the Toolkit / Admin settings from the back-end
     * @return Table of settings: host, port, tls_port, external cache location, default environment, gazelle URL
     */
    public String[] retrieveAdminSettings(){
        String[] currentAdminParams = {Toolkit.getHost(), Toolkit.getPort(), Toolkit.getTlsPort(),
                "C://ext_cache", Toolkit.getDefaultEnvironmentName(), Toolkit.getGazelleConfigURL()};
        //String[] test = {"http://nist1", "90800", "90801", "C://ext_cache", "NA2015", "http://gazelle.net"}; // test data
        logger.info("Retrieved from back-end (API Toolkit) the following parameters: "
                     + "host, port, TLS port, external cache location, default environment, gazelle URL.");
        return currentAdminParams;
    }


    // --------------------- Sessions and Environments ---------------------

    /**
     * Sets the list of environments
     * @return the list of available environments
     */
   public String[] retrieveEnvironments(){
       String[] envs = Toolkit.getEnvironmentNames().toArray(new String[0]);
       logger.info("Retrieved the list of environments.");
       // String[] envs = {"NA2014", "EURO2011", "EURO2012", "NwHIN"}; // test data
       return envs;
   }

    /**
     * Sets the environment selected by the user
     */
    public void setEnvironment(String envName) {
        Toolkit.setCurrentEnvironmentName(envName);
        logger.info("Environment '" + envName + "' was set.");
    }

    /**
     * Sets the list of test sessions
     * @return the list of test sessions
     */
    public String[] retrieveTestSessions(){
        String[] sessions = {"Test session 1", "Test session 2"}; // test data
        //logger.info("Retrieved the list of sessions.");
        return sessions;
    }

    /**
     * Registers a new session name and update the client-side data
     * @param sessionName New session name entered by the user
     */
    public String[] addTestSession(String sessionName){
        String[] sessions = {"Test session 1", "Test session 2", sessionName}; // test data
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




    // ----------------------------- MHD -------------------------------

    /**
     * Calls validation on an MHD message
     * @param messageType type of mhd message
     * @param filecontent mhd message itself
     *
     * @return
     */
    public String validateMHDMessage(String messageType, String filecontent) {
        /* TODO Implementation using toolkitServices.getSession().getLastUpload() to get the file uploaded
          (Change method prototype if required)*/
        return "Response for "+messageType+" validation.";
    }

    /**
     * Method that retrieves test data set
     * @param testDataType
     * @return
     */
    public Map<String,String> retrieveTestDataSet(String testDataType) {
        return TestDataHelper.instance.getTestDataSet();
    }

    /**
     * Method that converts a MHD file into an XDS file
     * @param uploadedFileContents the contents as a String of the MHD file uploaded by the user
     * @param location the location of the file. This is the "rootDirPath" parameter that originates from the
     *                 upload servlet. I do not know if we need this. -Diane
     * @return
     */
    public String convertMHDtoXDS(String location, String uploadedFileContents) {
        // This is a test implementation that saves the uploaded file and displays it for the user
        // inside a popup window. This should go away when the actual conversion is linked from the backend.
        return saveTempFileService.saveAsXMLFile(uploadedFileContents);
    }
}
