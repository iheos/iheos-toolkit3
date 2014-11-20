package gov.nist.hit.ds.initialization.installation;


import gov.nist.hit.ds.initialization.tkProps.TkLoader;
import gov.nist.hit.ds.initialization.tkProps.client.TkProps;
import gov.nist.hit.ds.utilities.io.Io;
import gov.nist.hit.ds.utilities.string.StringUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * Singleton that represents the local installation.
 * @author bmajur
 *
 */
public class Installation {
	static public final String TOOLKIT_PROPERTIES = "toolkit.properties";
	
	String sep = File.separator;
	public TkProps tkProps;
	PropertyServiceManager propertyServiceMgr = null;
	static Logger logger = Logger.getLogger(Installation.class);
	static Installation me = null;
	File externalCache;
	String buildNumber;
	File toolkitPropertiesFile = null;
	boolean initialized = false;

	/**
	 * Initialize the installation.  This is called by a servlet initializiation
	 * since that happens when the app is loaded. Before that first call,
	 * warHome must be set or nothing else will work.  If this parameter
	 * is wrong then toolkit is dead.
	 * 
	 * It must be called 
	 * after toolkit.properties is altered to force the reload.
	 * @throws InitializationFailedException 
	 * @throws IOException 
	 */
	public void initialize() throws InitializationFailedException {
		if (initialized == true)
			return;

		propertyServiceMgr = new PropertyServiceManager();

		loadBuildNumber();

		if (externalCache == null) {
			String externalCacheString = propertyServiceMgr.getToolkitProperties().get(PropertyServiceManager.EXTERNAL_CACHE);
			if (externalCacheString == null || externalCacheString.equals(""))
				throw new InitializationFailedException("externalCache location not configured or empty in toolkit.properties");
			if (!externalCacheString.startsWith("/")) {
				// relative path - probably means a unit test is running - use class loader to locate
				// The class loader will not find directories!! So we must find a fixed file in the external_cache.
				// That file is tk_props.txt
				// This means any external_cache that is located via class loaded must be initialized with a tk_props.txt file.
				// The file can be empty.
				ClassLoader cl = getClass().getClassLoader();
				URL url = cl.getResource(externalCacheString + "/" + "tk_props.txt"); // Use of a standard backslash in URL works best. A File.separator mixed with URL causes a problem for Windows.
				if (url == null) {
					String msg = "External Cache not present. Configured location is <" + externalCacheString + ">";
					logger.fatal(msg);
					throw new InitializationFailedException(msg);
				}
				String ecString = StringUtil.removePrefix(url.toString(), "file:");
				logger.info("Loading external cache from <" + ecString + ">");
				externalCache = new File(ecString);
				externalCache = externalCache.getParentFile();  // remove tk_props.txt from path
			} else {
				externalCache = new File(externalCacheString);
			}
		}

		initializeExternalCache(externalCache);

		initialized = true;
	}
	
	void initializeExternalCache(File externalCache) throws InitializationFailedException {
		this.externalCache = externalCache;
		logger.info("Initializing external cache to <" + externalCache.getAbsolutePath() + "> ");
		try {
			File f = externalCache; 
			if (f.exists()) {
				if (!f.isDirectory()) throw new InitializationFailedException("External Cache location <" + f + "> is not a directory");
				if (!f.canWrite()) throw new InitializationFailedException("External Cache location <" + f + "> is not writable");
			} else {
				throw new InitializationFailedException("External Cache directory <" + externalCache + "> does not exist");
			}
			ExternalCacheManager ecMgr = new ExternalCacheManager(f);
			initializeRepository(ecMgr);
			initializeTkProps(ecMgr);
			initializeEnvironments(ecMgr);
			initializeSimDb(ecMgr);
			initializeActors(ecMgr);
		} catch (IOException e) {
			throw new InitializationFailedException("External Cache Failed",e);
		}
		logger.info("External Cache initialization complete");
	}



	static public void reset() { me = null; }

	// should always use default location - root of class loader
	//	public Installation setToolkitPropertiesFile(File propsFile) {
	//		this.toolkitPropertiesFile = propsFile;
	//		return this;
	//	}

	public InputStream getToolkitProperties() { 
		logger.debug("Installation#getToolkitProperties");
		InputStream is = null;
		String from = null;
		if (toolkitPropertiesFile != null) {
			logger.debug("loading from file designator: " + toolkitPropertiesFile.toString());
			from = toolkitPropertiesFile.toString();
			logger.info("toolkit.properties loaded from <" + from + ">");
			try {
				is = Io.getInputStreamFromFile(toolkitPropertiesFile);
			} catch (FileNotFoundException e) {
				throw new RuntimeException("Could not load " + TOOLKIT_PROPERTIES + " from <" + toolkitPropertiesFile + ">");
			}
		} else {
			logger.debug("Using class loader");
			ClassLoader cl = getClass().getClassLoader();
			URL url = cl.getResource(TOOLKIT_PROPERTIES);
			logger.info("toolkit.properties loaded from <" + url + ">");
			if (url == null) {
				throw new RuntimeException("Could not load " + TOOLKIT_PROPERTIES + " file via the class loader");
			}
			from = url.toExternalForm();
			logger.debug("loading toolkit.properties from <" + url.toString());
			is = cl.getResourceAsStream("toolkit.properties");
		}
		if (is == null) {
			logger.debug("InputStream is null");
			throw new RuntimeException("Could not load toolkit.properties");
		} 
		logger.debug("InputStream is not null");
		return is;
	}

	public File getToolkitPropertiesFile() {
		URL url = getClass().getClassLoader().getResource("toolkit.properties");
		logger.info("toolkit.properties loaded from <" + url + ">");
		return new File(url.getFile());
	}

	public File getExtendedPropertiesFile() {
		URL url = getClass().getClassLoader().getResource("extended.properties");
		if (url == null)
			return null;
		return new File(url.getFile());
	}

	public File getToolkitxFile() {
		URL url = getClass().getClassLoader().getResource("toolkitx");
		return new File(url.getFile());
	}

	public String getBuildNumber() { return buildNumber; }

	private void loadBuildNumber() {
		InputStream is = getClass().getClassLoader().getResourceAsStream("build.num");
		if (is == null) {
			buildNumber = "Unknown";
			return;
		}
		try {
			byte[] ba = new byte[200];
			int count;
			count = is.read(ba);
			if (count < 1)
				buildNumber = "Unknown";
			else 
				buildNumber = new String(ba);
		} catch (IOException e) {
			buildNumber = "Unknown";
		}
	}

	void initializeRepository(ExternalCacheManager ecMgr) {
		File r = ecMgr.getRepositoryFile();
		if (!r.exists()) r.mkdir();
		File types = new File(r, "types");
		if (!types.exists()) types.mkdir();
	}

	void initializeTkProps(ExternalCacheManager ecMgr) throws IOException, InitializationFailedException {
		File r = ecMgr.getTkPropsFile();
		if (!r.exists())
			r.createNewFile();
		tkProps = TkLoader.tkProps(ecMgr.getTkPropsFile()); 
	}

	void initializeEnvironments(ExternalCacheManager ecMgr) {
		File e = ecMgr.getEnvironmentFile();
		if (!e.exists()) e.mkdir();
	}

	void initializeSimDb(ExternalCacheManager ecMgr) {
		File e = ecMgr.getSimDbFile();
		if (!e.exists()) e.mkdir();
	}

	void initializeActors(ExternalCacheManager ecMgr) {

	}

	public File getRepositoryRoot() {
		return getExternalCacheManager().getRepositoryFile();
	}

	static public Installation installation()   {
		if (me == null)
			me = new Installation();
		return me;
	}	

	private Installation() {   }

	public PropertyManager getPropertyManager() {
		return new PropertyManager();
	}

	public void setExternalCache(File externalCache) throws InitializationFailedException {
		propertyServiceManager().getPropertyManager().setExternalCache(externalCache.toString());
		initializeExternalCache(externalCache);
	}

	public PropertyServiceManager propertyServiceManager() {
		if (propertyServiceMgr == null)
			propertyServiceMgr = new PropertyServiceManager();
		return propertyServiceMgr;
	}

	public File simDbFile()  {
		return propertyServiceManager().getSimDbDir();
	}

	ExternalCacheManager getExternalCacheManager() {
		return new ExternalCacheManager(externalCache);
	}

	public File getDefaultCodesFile()  {
		File envFile =  getExternalCacheManager().getEnvironmentFile();
		String defaultEnvName = getPropertyManager().getPropertyMap().get(PropertyServiceManager.DEFAULT_ENVIRONMENT);
		File env = new File(envFile,defaultEnvName);
		return new File(env, "codes.xml");
	}

	public File getExternalCache() {
		return externalCache;
	}

}
