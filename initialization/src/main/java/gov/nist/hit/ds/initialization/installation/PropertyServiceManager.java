package gov.nist.hit.ds.initialization.installation;

import gov.nist.hit.ds.utilities.io.Io;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

public class PropertyServiceManager {
	static public final String TOOLKIT_TLS_PORT = "Toolkit_TLS_Port";
	static public final String TOOLKIT_PORT = "Toolkit_Port";
	static public final String DEFAULT_ENVIRONMENT = "Default_Environment";
	static public final String ENABLE_ALL_CIPHERS = "Enable_all_ciphers";
	static public final String ADMIN_PASSWORD = "Admin_password";
	static public final String USE_ACTORS_FILE = "Use_Actors_File";
	static public final String GAZELLE_CONFIG_FILE = "Gazelle_Config_URL";
	static public final String EXTERNAL_CACHE = "External_Cache";
	static public final String TOOLKIT_HOST = "Toolkit_Host";

	PropertyManager propertyManager = null;
	//	File warHome = null;

	static Logger logger = Logger.getLogger(PropertyServiceManager.class);

	public File getActorsDirName()  {
		File f = new File(getPropertyManager().getExternalCache() + File.separator + "actors");
		f.mkdirs();
		return f;
	}

	// isRead - is the actors file about to be read? (as opposed to written)
	public File configuredActorsFile(boolean isRead)  {
		File loc = getActorsFileName();
		if (isRead) {
			if (!loc.canRead())
				return null;
			return loc;
		}
		try {
			if (!isRead && (loc.canWrite() || loc.createNewFile()))
				return loc;
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	public String getAdminPassword()  {
		return getPropertyManager().getPassword();
	}

	public String getToolkitHost()  {
		return getPropertyManager().getToolkitHost();
	}

	public String getToolkitPort()  {
		return getPropertyManager().getToolkitPort();
	}

	public String getToolkitTlsPort()  {
		return getPropertyManager().getToolkitTlsPort();
	}

	public String getToolkitEnableAllCiphers()  {
		return getPropertyManager().getToolkitEnableAllCiphers();
	}

	public File getActorsFileName()  {
		return new File(getPropertyManager().getExternalCache() + File.separator + "actors.xml");
	}

	public File getSimDbDir()  {
		File f = new File(getPropertyManager().getExternalCache() + File.separator + "simdb");
		f.mkdirs();
		return f;
	}

	public String getDefaultEnvironmentName()  {
		return getPropertyManager().getDefaultEnvironmentName();
	}

	public String getDefaultAssigningAuthority()  {
		return getPropertyManager().getDefaultAssigningAuthority();
	}

	public PropertyManager getPropertyManager() {
		loadPropertyManager();
		return propertyManager;
	}

	public void loadPropertyManager() {
		if (propertyManager != null)
			return;

		propertyManager = new PropertyManager();

		// This removes the dependency that 
		// gov.nist.registry.common2.xml.SchemaValidation
		// has on port 9080
		// Schema references will be made directly through the file system and not
		// via "system" references (via a URI)
		//		System.setProperty("XDSSchemaDir", warHome + File.separator + "toolkitx" + File.separator + "schema");
	}


	public File getTestLogCache()  {
		String testLogCache = getPropertyManager().getExternalCache() + File.separator + "TestLogCache";
		File f;

		//		// internal is obsolete
		//		if ("internal".equals(testLogCache)) {
		//			testLogCache = getWarHome() + "SessionCache" + 
		//					File.separator +
		//					getSession().getId() + File.separator + 
		//					"TestLog";
		//			f = new File(testLogCache);
		//			f.mkdirs();
		//			return f;
		//		}

		f = new File(testLogCache);
		f.mkdirs();

		if (!( f.exists() && f.isDirectory() && f.canWrite()  )) {
			String msg = "Cannot access Test Log Cache [" + testLogCache + "] - either it doesn't exist, isn't a directory or isn't writable";
			logger.warn(msg);
			throw new RuntimeException(msg);
		}

		return f;
	}

	public String getAttributeValue(String username, String attName) throws Exception {
		return Io.stringFromFile(getAttributeFile(username, attName));
	}

	public void setAttributeValue(String username, String attName, String attValue) throws Exception {
		Io.stringToFile(getAttributeFile(username, attName), attValue);
	}

	File getAttributeFile(String username, String attName) throws Exception {
		return new File(getAttributeCache(username) + File.separator + attName + ".txt");
	}

	File getAttributeCache(String username) throws Exception {
		String attributeCache = getPropertyManager().getExternalCache() + File.separator + "Attributes" + File.separator + username;
		File f = new File(attributeCache);

		if (!( f.exists() && f.isDirectory() && f.canWrite()  )) {
			String msg = "Cannot access Attribute Cache [" + attributeCache + "] - either it doesn't exist, isn't a directory or isn't writable";
			logger.fatal(msg);
			throw new Exception(msg);
		}
		return f;
	}

	public Map<String, String> getToolkitProperties()  {
		return getPropertyManager().getPropertyMap();
	}

	public boolean reloadPropertyFile() {
		propertyManager = null;
		getPropertyManager();
		return true;
	}


	public boolean isTestLogCachePrivate() {
		return true;
		//		String testLogCache = getPropertyManager().getExternalCache() + File.separator + "TestLogCache";
		//		return !"internal".equals(testLogCache);
	}

	public String getImplementationVersion() {
		return Installation.installation().getBuildNumber();
	}

	//	public boolean isGazelleConfigFeedEnabled() {
	//		logger.debug(": " + "isGazelleConfigFeedEnabled");
	//		return SiteServiceManager.getSiteServiceManager().useGazelleConfigFeed(session);
	//	}

	//	public String getInitParameter(String parmName) {
	//		logger.debug(getSessionIdIfAvailable() + ": " + "getInitParameter(" + parmName + ")");
	//		return tsi.servletContext().getInitParameter(parmName);
	//	}

	//	public List<String> getEnvironmentNames() {
	//		logger.debug(": " + "getEnvironmentNames()");
	//		return getSession().getEnvironmentNames();
	//	}

	/**
	 * @throws IOException 
	 * Set environment name for current session
	 * @param name
	 * @throws
	 */
	//	public void setEnvironment(String name)  {
	//		logger.debug(": " + "setEnvironment(" + name + ")");
	//		getSession().setEnvironment(name);
	//	}

	//	public String getCurrentEnvironment() {
	//		return getPropertyManager().getCurrentEnvironmentName();
	//	}

	public String getDefaultEnvironment()  {
		return getPropertyManager().getDefaultEnvironmentName();
	}

	//	public void setSessionProperties(Map<String, String> props) {
	//		logger.debug(": " + "setSessionProperties()");
	//		Session s = getSession();
	//		if (s == null)
	//			return;
	//		s.setSessionProperties(props);
	//	}
}
