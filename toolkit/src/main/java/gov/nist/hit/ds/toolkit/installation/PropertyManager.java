package gov.nist.hit.ds.toolkit.installation;

import gov.nist.hit.ds.toolkit.Toolkit;
import gov.nist.hit.ds.utilities.io.Io;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyManager {

	static Logger logger = Logger.getLogger(PropertyManager.class);

	//	String propFile;
	Properties toolkitProperties = null;

	public void update(Map<String, String> props) throws Exception {
		if (toolkitProperties == null)
			toolkitProperties = new Properties();
		for (String key : props.keySet()) {
			String value = props.get(key);
			validateProperty(key, value);
			toolkitProperties.put(key, value);
			save(props);
		}
	}

	void validateProperty(String name, String value) throws Exception {
		if (name == null)
			throw new Exception("Property with name null not allowed");
		if (name.equals("Admin_password")) {
			if (value == null || value.equals(""))
				throw new Exception("Empty password not allowed");
		}
		else if (name.equals("Actors_file")) {
			File f = new File(value);
			if (f.exists() && f.canWrite())
				return;
			File dir = f.getParentFile();
			dir.mkdirs();
			if (!dir.exists())
				throw new Exception("Cannot create directory for actors.xml file: " + dir.toString());
			if (!f.exists()) {
				f.createNewFile();
				if (f.exists() && f.canWrite()) {
					f.delete();
					return;
				}
				f.delete();
			}
			throw new Exception("Cannot create actors.xml file at: " + f);
		}
		else if (name.equals("Simulator_database_directory")) {
			File f = new File(value);
			f.mkdirs();
			if (!f.exists())
				throw new Exception("Cannot create Message_database_directory " + value);
		}
	}

    @Deprecated
	public String getPassword() {
		loadProperties();
		return (String) toolkitProperties.get("Admin_password");
	}

	/**
	 * Save admin password
	 * @param newPassword new password entered by the user
	 */
	public void savePassword(String newPassword) throws IOException {
		toolkitProperties.setProperty("Admin_password", newPassword);
		saveProperties(Toolkit.getToolkitPropertiesFile());
	}

    @Deprecated
	public String getToolkitHost() {
		loadProperties();
		return (String) toolkitProperties.get("Toolkit_Host");
	}

    @Deprecated
	public String getToolkitPort() {
		loadProperties();
		return (String) toolkitProperties.get("Toolkit_Port");
	}

    @Deprecated
	public String getToolkitTlsPort()  {
		loadProperties();
		return (String) toolkitProperties.get("Toolkit_TLS_Port");
	}

    @Deprecated
	public String getToolkitGazelleConfigURL()  {
		loadProperties();
		return (String) toolkitProperties.get("Gazelle_Config_URL");
	}

    @Deprecated
	public String getExternalCache()  {
		loadProperties();
		String cache = (String) toolkitProperties.get("External_Cache");
		System.setProperty("External_Cache", cache);
		return cache;
	}

    @Deprecated
	public boolean isUseActorsFile()  {
		loadProperties();
		String use = (String) toolkitProperties.get("Use_Actors_File");
		if (use == null)
			return true;
		return "true".compareToIgnoreCase(use) == 0;
	}

    @Deprecated
	public String getDefaultAssigningAuthority()  {
		loadProperties();
		return (String) toolkitProperties.get("PatientID_Assigning_Authority");
	}

    @Deprecated
	public String getDefaultEnvironmentName()  {
		loadProperties();
		return (String) toolkitProperties.get("Default_Environment");
	}

	@Deprecated
	public String getCurrentEnvironmentName()  {
		String cache = getExternalCache();
		File currentFile = new File(cache + File.separator + "environment" + File.separator + "current");
		String currentName = null;
		try {
			currentName = Io.stringFromFile(currentFile).trim();
		} catch (IOException e) {}
		return currentName;
	}

	@Deprecated
	public void setCurrentEnvironmentName(String name) throws IOException  {
		String cache = getExternalCache();

		File currentFile = new File(cache + File.separator + "environment" + File.separator + "current");
		Io.stringToFile(currentFile, name);
	}

    @Deprecated
	public String getToolkitEnableAllCiphers()  {
		loadProperties();
		return (String) toolkitProperties.getProperty("Enable_all_ciphers");
	}

	public String getToolkitAppSubtitle() {
		loadProperties();
		return (String) toolkitProperties.getProperty("Toolkit_App_Subtitle");
	}

	public String getToolkitVersion() {
		loadProperties();
		return (String) toolkitProperties.getProperty("Toolkit_version");
	}

    @Deprecated
	public void save(Map<String, String> props) throws IOException  {
		saveProperties();
	}

    @Deprecated
	public void loadProperties()  {
		if (toolkitProperties != null) {
			return;
		}
		toolkitProperties = new Properties();
		try {
			toolkitProperties.load(Installation.installation().getToolkitProperties());
		} catch (IOException e) {
			logger.fatal("Could not load toolkit properties.");
			throw new RuntimeException("Could not load toolkit properties.");
		}
	}

    public void loadProperties(File toolkitPropertiesFile) {
        toolkitProperties = new Properties();
        try {
            toolkitProperties.load(Io.getInputStreamFromFile(toolkitPropertiesFile));
        } catch (IOException e) {
            logger.fatal("Could not load toolkit properties from <" + toolkitPropertiesFile + ">");
            throw new RuntimeException("Could not load toolkit properties from <" + toolkitPropertiesFile + ">");
        }
    }

    @Deprecated
	public void saveProperties() throws IOException {
		saveProperties(toolkitProperties, Installation.installation().getToolkitPropertiesFile());
	}

    public void saveProperties(File file) {
        try {
            saveProperties(toolkitProperties, file);
        } catch (IOException e) {
            logger.fatal("Could not save toolkit properties to <" + file + ">");
            throw new RuntimeException("Could not save toolkit properties to <" + file + ">");
        }
    }

    @Deprecated
	public void saveProperties(Properties props, File file) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			props.store(fos, "");
			fos.flush();
		}
		finally {
			fos.close();
		}
	}

    @Deprecated
	public Map<String, String> getPropertyMap()  {
		loadProperties();
		Map<String, String> props = new HashMap<String, String>();
		for (Object keyObj : toolkitProperties.keySet()) {
			String key = (String) keyObj;
			String value = toolkitProperties.getProperty(key);
			props.put(key, value);
		}
		return props;
	}

}
