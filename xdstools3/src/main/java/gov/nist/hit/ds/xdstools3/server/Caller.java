package gov.nist.hit.ds.xdstools3.server;

import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.simSupport.api.ValidationApi;
import gov.nist.hit.ds.siteManagement.loader.Sites;
import gov.nist.hit.ds.toolkit.Toolkit;
import gov.nist.hit.ds.xdstools3.client.exceptions.ToolkitServerError;
import gov.nist.hit.ds.xdstools3.server.demo.ActorsCollectionsDataSamples;
import gov.nist.hit.ds.xdstools3.server.demo.TestDataHelper;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.HashMap;
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
    private final ValidationApi api = new ValidationApi();

    // Transaction name common to all MHD transactions
    private final String MHD_TRANSACTION_NAME = "pdb";

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


    // ------------------ Header app properties -------------------------

    public String getToolkitConnectathonEvent() {
        return Toolkit.getToolkitConnectathonEvent();
    }

    public String getToolkitVersion() {
        return Toolkit.getToolkitVersion();
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
        Toolkit.saveProperties(Toolkit.getToolkitPropertiesFile());
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
        //TODO missing the location of external cache
        String[] currentAdminParams = {Toolkit.getHost(), Toolkit.getPort(), Toolkit.getTlsPort(),
                "ext_cache_location", Toolkit.getDefaultEnvironmentName(), Toolkit.getGazelleConfigURL()};
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
    public String[] retrieveTestSessions() {
        // String[] sessions = {"Test session 1", "Test session 2"};   // test data
        try {
            logger.info("Retrieved the list of user session names.");
            return Toolkit.getUserSessions().toArray(new String[0]);
        }
        catch (NullPointerException e) {
            logger.info("No user session names are registered.");
            return new String[]{};
        }
    }

    /**
     * Registers a new session name and perpetuates the change to the back-end.
     * Retrieving the new list of sessions is done from inside the service
     * implementation ToolbarServiceImpl.
     * @param sessionName New session name entered by the user
     * @see gov.nist.hit.ds.xdstools3.server.RPCServices.ToolbarServiceImpl
     */
    public void addTestSession(String sessionName){
        Toolkit.addUserSession(sessionName);
    }

    /**
     * Deletes a user session and perpetuates the change to the back-end.
     * Retrieving the new list of sessions is done from inside the service
     * implementation ToolbarServiceImpl.
     * @param sessionName
     * @see gov.nist.hit.ds.xdstools3.server.RPCServices.ToolbarServiceImpl
     */
    public void deleteTestSession(String sessionName) {
        Toolkit.deleteUserSession(sessionName);
    }



    // --------------------- Actors and Collections ---------------------

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
     * @param messageType the type of MHD message being uploaded
     * @param filecontent MHD message contents
     *
     * @return an Asset (= validation result) as handled by the repository / LogBrowser
     */
    public AssetNode validateMHDMessage(String messageType, String filecontent) throws ToolkitServerError {
        //if (messageType == "Submit") {
        AssetNode validationResult = api.validateRequest(MHD_TRANSACTION_NAME, filecontent);
        logger.info("Received AssetNode with parameters AssetId: " + validationResult.getAssetId()
                +", AssetType: "+ validationResult.getType()
        +", RepositoryId: "+ validationResult.getRepId());
        return validationResult;
        //} // end submit
        // else throw new unsupportedmessagetypeexception
    }


    /**
     * Converts a MHD file into an XDS file
     * @param uploadedFileContents the contents as a String of the MHD file uploaded by the user
     * @param location the location of the file. This is the "rootDirPath" parameter that originates from the
     *                 upload servlet. I do not know if we need this. -Diane
     * @return
     */
    public AssetNode convertMHDtoXDS(String location, String uploadedFileContents) {
        //TODO
        // This is a test implementation that saves the uploaded file and displays it for the user
        // inside a popup window. This should go away when the actual conversion is linked from the backend.
        AssetNode validationResult = api.validateRequest(MHD_TRANSACTION_NAME, uploadedFileContents);
        logger.info("Received AssetNode with parameters AssetId: " + validationResult.getAssetId()
                +", AssetType: "+ validationResult.getType()
                +", RepositoryId: "+ validationResult.getRepId());
        return validationResult;
//        return saveTempFileService.saveAsXMLFile(uploadedFileContents);
    }



    // ----------------------------- Submit Test Data -------------------------------

    /**
     * Retrieves all the available test data sets for a given type of test data.
     * This is for now used in the Submit Test Data tab.
     * @param testDataType type of test data
     * @return all the matching test data sets
     */
    public Map<String,String> retrieveTestDataSet(String testDataType) {
        return TestDataHelper.instance.getTestDataSet();
    }


    // ----------------------------- Query & Retrieve -------------------------------

    public AssetNode findDocuments(String pid, boolean tls, boolean saml, boolean onDemand, String endpointID) throws ToolkitServerError {
        // create class to hold all parameters
        // TODO insert API call for Find Documents
        return null;
    }


    // --------------------------------- Sites / Actors / Endpoints ----------------------------------

    /**
     * Retrieve the full list of sites with their data
     * @return
     */
    public Sites retrieveSites() {
        return null;
    }

    public Map<String, String> retrieveSiteAttributes() {
        // TODO needs real API
        Map<String, String> test = new HashMap<>();
        test.put("pidfeedhost", "https://ihexds.nist.gov");
        test.put("pidfeedport", "12081");
        test.put("rtls", "https://ihexds.nist.gov:12081/tf6/services/xdsregistryb");
        test.put("rnotls", "http://ihexds.nist.gov:12080/tf6/services/xdsregistryb-notls");
        test.put("sqtls", "https://ihexds.nist.gov");
        test.put("sqnotls", "https://ihexds.nist.gov");
        test.put("updatetls", "https://ihexds.nist.gov");
        test.put("updateNotls", "https://ihexds.nist.gov");
        test.put("mpqtls", "https://ihexds.nist.gov");
        test.put("mpqNotls", "https://ihexds.nist.gov");
        test.put("repouuid", "https://ihexds.nist.gov");
        test.put("retrievetls", "https://ihexds.nist.gov");
        test.put("retrievenotls", "https://ihexds.nist.gov");
        test.put("pnrtls", "https://ihexds.nist.gov");
        test.put("pnrnotls", "https://ihexds.nist.gov");
        test.put("docsrcretrievetls", "https://ihexds.nist.gov");
        test.put("docsrcretrievenotls", "https://ihexds.nist.gov");
        test.put("srcreporetrievetls", "https://ihexds.nist.gov");
        test.put("srcreporetrievenotls", "https://ihexds.nist.gov");
        test.put("xdrpnrtls", "https://ihexds.nist.gov");
        test.put("xdrpnrnotls", "https://ihexds.nist.gov");
        test.put("hcid", "https://ihexds.nist.gov");
        test.put("xcqtls", "https://ihexds.nist.gov");
        test.put("xcqnotls", "https://ihexds.nist.gov");
        test.put("xcrtls", "https://ihexds.nist.gov");
        test.put("xcrnotls", "https://ihexds.nist.gov");
        test.put("xcpdtls", "https://ihexds.nist.gov");
        test.put("xcpdnotls", "https://ihexds.nist.gov");
        test.put("igqtls", "https://ihexds.nist.gov");
        test.put("igqnotls", "https://ihexds.nist.gov");
        test.put("igrtls", "https://ihexds.nist.gov");
        test.put("igrnotls", "https://ihexds.nist.gov");

        return test;
    }

}


