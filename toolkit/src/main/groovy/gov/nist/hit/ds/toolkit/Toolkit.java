package gov.nist.hit.ds.toolkit;

import gov.nist.hit.ds.toolkit.environment.Environment;
import gov.nist.hit.ds.toolkit.environment.UserSession;
import gov.nist.hit.ds.toolkit.installation.Installation;
import gov.nist.hit.ds.toolkit.installation.PropertyManager;
import gov.nist.hit.ds.xdsException.ExceptionUtil;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * This is the starting point to find all file-based resources in toolkit.
 *
 *
 * Created by bmajur on 3/14/14.
 */
public class Toolkit {
    private static File toolkitPropertiesFile = null;
    private static File warRootFile = null;
    private static File externalCacheFile = null;
    private static PropertyManager propertyManager;
    private static boolean initialized = false;
    private static boolean testEnv = false;
    static Logger logger = Logger.getLogger(Toolkit.class);

    private static void initializeStatus() {
        logger.info("Toolkit initialization:");
        logger.info("...properties file at " + toolkitPropertiesFile);
        logger.info("...war file at " + warRootFile);
        logger.info("...external cache at " + externalCacheFile);
        logger.info("...property manager is " + ((propertyManager==null) ? "null" : "not null"));
        logger.info("...test environment is " + testEnv);
        logger.info("...initialized is " + initialized);
    }
    /**
     * At RUNTIME:
     * toolkit.properties resides at WAR-INF/toolkit.properties
     * So, the war file base is toolkit.properities/../..
     * This is needed to find file based resources stored in the war file that
     * are not on the CLASSPATH so they cannot be found via the normal
     * getResource() calls.
     *
     * At UNIT TEST TIME:
     * this will find toolkit.properties at ${moduleName}/test/resources/toolkit.properties
     * If your unit tests are sensitive to the contents of toolkit.properties (your module
     * needs to have more than one toolkit.properties file to support the breadth
     * of unit testing it does)
     */

    static public void initialize() {
        try {
            initialize1();
            initializeStatus();
        } catch (Throwable t) {
            logger.fatal("Initialization failed: " + ExceptionUtil.exception_details(t));
            initializeStatus();
        }
    }

    static private void initialize1() {
        if (initialized) return;
        initialized = true;
//        try {
//            Installation.installation().initialize();
//        } catch (IOException e) {
//            logger.fatal(e);
//        }
        URL resource = new Toolkit().getClass().getResource("/toolkit.properties");
        if (resource == null) {
            logger.fatal("Cannot load toolkit.properties");
            throw new RuntimeException("Cannot load toolkit.properties");
        }
        else {
            logger.info("toolkit.properties loaded from <" + resource + ">");
            toolkitPropertiesFile = new File(resource.getFile());
            // if a unit test is currently running the the relative location of
            // the warRoot will be different. This conditional checks for this.
            // If this is a test then set warRootFile to the resource directory within
            // thee test directory.
            if (toolkitPropertiesFile.getParent().endsWith("test-classes")) {
                warRootFile = toolkitPropertiesFile.getParentFile().getParentFile();
                logger.info("Test configuration");
                testEnv = true;
            }
            else if (toolkitPropertiesFile.getParent().endsWith("classes") /*&&
                toolkitPropertiesFile.getParentFile().getParent().endsWith("WAR") */) {
                    // production environment
                    warRootFile = toolkitPropertiesFile.getParentFile().getParentFile().getParentFile();
                logger.info("Production configuration");
            }
            else
                warRootFile = null;
            logger.debug("warRoot set to <" + warRootFile + ">");

            propertyManager = new PropertyManager();
            propertyManager.loadProperties(toolkitPropertiesFile);

//            try { Installation.installation().initialize(); }
//            catch (RuntimeException e) { throw e; }
//            catch (Exception e) { throw new RuntimeException(e); }
            if (warRootFile == null) {
                String msg = "Location of WAR file root not initialized";
                logger.fatal(msg);
                throw new RuntimeException(msg);
            }

            try { Installation.installation().initialize(); }
            catch (RuntimeException e) { throw e; }
            catch (Exception e) { throw new RuntimeException(e); }

            if ( externalCacheFile == null) {
                externalCacheFile = Installation.installation().externalCache;
                if ( externalCacheFile == null) {
                    String msg = "Location of External Cache not initialized";
                    logger.fatal(msg);
                    throw new RuntimeException(msg);
                }
            }
            try {
                externalRepositoriesTypesFile().mkdirs();
                if (internalRepositoriesTypesFile().exists())
                    FileUtils.copyDirectory(internalRepositoriesTypesFile(), externalRepositoriesTypesFile());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * This should only be used to support unit testing and then only if absolutely
     * necessary.  You have been warned.
     * @param file
     */
    static public void setToolkitPropertiesFile(File file) {
        toolkitPropertiesFile = file;
    }

    /**
     * This should only be used to support unit testing and then only if absolutely
     * necessary.  You have been warned.
     * @param file
     */
    static public void setWarRootFile(File file) {
        warRootFile = file;
    }

    /**
     * Accessor functions that reveal locations of critical resources in toolkit
     * @return
     */
    static public File getToolkitPropertiesFile() { return toolkitPropertiesFile; }
    static public File warRootFile() { return warRootFile; }

    /**
     * WAR:
     * schema
     * testkit
     * xdstest
     * toolkit.properties (hidden)
     */
    /**
     * EC:
     * actors
     * environment
     * repository
     * tk_props (hidden)
     * testLogCache
     */


    static private File toolkitxFile() {
        if (testEnv)
            return new File(warRootFile, "test-classes/toolkitx");
        return new File(warRootFile, "toolkitx");
    }

    static public File schemaFile() { return new File(toolkitxFile(), "schema"); }
    static public File testkitFile() { return new File(toolkitxFile(), "testkit"); }
    static public File xdstestFile() { return new File(toolkitxFile(), "xdstest"); }
    static public File testLogFile() {
        return new File(externalCacheFile(), "TestLogCache");
    }

    static public String getPassword() { return propertyManager.getPassword(); }
    static public String getHost() { return propertyManager.getToolkitHost(); }
    static public String getPort() { return propertyManager.getToolkitPort(); }
    static public String getTlsPort() { return propertyManager.getToolkitTlsPort(); }
    static public String getGazelleConfigURL() { return propertyManager.getToolkitGazelleConfigURL(); }
    static public File externalCacheFile() { return new File(propertyManager.getExternalCache()); }
    static public boolean useActorsFile() { return propertyManager.isUseActorsFile(); }
    static public String getDefaultAssigningAuthority() { return propertyManager.getDefaultAssigningAuthority(); }
    static public String getEnableAllCiphers() { return propertyManager.getToolkitEnableAllCiphers(); }
    static public void saveProperties(File file) { propertyManager.saveProperties(file);}

    // Environments
    static public String getCurrentEnvironmentName() { return propertyManager.getCurrentEnvironmentName(); }
    static public String getToolkitAppSubtitle(){ return propertyManager.getToolkitAppSubtitle();}
    static public String getToolkitVersion(){ return propertyManager.getToolkitVersion();}
    static public void setCurrentEnvironmentName(String name) {
        try {
            propertyManager.setCurrentEnvironmentName(name);
        } catch (IOException e) {
            logger.fatal("setCurrentEnvironmentName failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    static public File environmentsFile() { return new File(externalCacheFile(), "environment"); }
    static public List<String> getEnvironmentNames() { return Environment.getInstalledEnvironments(environmentsFile()); }
    static public Environment getEnvironment(String name) {  return new Environment(environmentsFile(), name); }
    static public String getDefaultEnvironmentName() {
        // If the default environment coded in toolkit.properties does not exist then fall back
        // to the more basic default called "default" which is installed when toolkit
        // is initialized.
        String name = propertyManager.getDefaultEnvironmentName();
        if (new Environment(environmentsFile(), name).exists())
            return name;
        return "default";
    }

    // User sessions
    static public List<String> getUserSessions() { return new UserSession(externalCacheFile()).names(); }
    static public void addUserSession(String name) { new UserSession(externalCacheFile()).add(name); }
    static public void deleteUserSession(String name) { new UserSession(externalCacheFile()).delete(name); }


    // Repository
    static public File externalRepositoryFile() { return new File(externalCacheFile(), "repositories"); }
    static public File externalRepositoriesTypesFile() { return new File(externalRepositoryFile(), "types"); }
    static public File internalRepositoriesTypesFile() { return new File(new File(toolkitxFile(), "repositories"), "types"); }

}
