package gov.nist.hit.ds.toolkit.installation;

import gov.nist.hit.ds.toolkit.Toolkit;
import gov.nist.hit.ds.utilities.io.Io;
import gov.nist.hit.ds.utilities.string.StringUtil;
import gov.nist.hit.ds.xdsException.ExceptionUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Singleton that represents the local installation.
 * @author bmajur
 *
 */
public class Installation {
	static public final String TOOLKIT_PROPERTIES = "toolkit.properties";
	
	String sep = File.separator;
//	public TkProps tkProps;
	PropertyServiceManager propertyServiceMgr = null;
	static Logger logger = Logger.getLogger(Installation.class);
	static Installation me = null;
	public File externalCache;
	String buildNumber;
	File toolkitPropertiesFile = null;
	boolean initialized = false;

    static public Installation installation()   {
        if (me == null) me = new Installation();
        return me;
    }

    private Installation() {   }

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
	public void initialize() throws InitializationFailedException, IOException {
		if (initialized)
			return;
        initialized = true;
        Toolkit.initialize();

		propertyServiceMgr = new PropertyServiceManager();

		loadBuildNumber();

		if (externalCache == null) {
			String externalCacheString = propertyServiceMgr.getToolkitProperties().get(PropertyServiceManager.EXTERNAL_CACHE);
			if (externalCacheString == null || externalCacheString.equals(""))
				throw new InitializationFailedException("externalCache location not configured or empty in toolkit.properties");
            logger.debug("raw external_cache string is <" + externalCacheString + ">");
			if (!externalCacheString.startsWith("/")) {
				// relative path - probably means a unit test is running - use class loader to locate
				// The class loader will not find directories!! So we must find a fixed file in the external_cache.
				// That file is tk_props.txt
				// This means any external_cache that is located via class loaded must be initialized with a tk_props.txt file.
				// The file can be empty.


				ClassLoader cl = getClass().getClassLoader();
                String tkPropsLoc = externalCacheString + "/" + "tk_props.txt";
				URL url = cl.getResource(tkPropsLoc); // Use of a standard backslash in URL works best. A File.separator mixed with URL causes a problem for Windows.
				if (url == null) {
                    url = cl.getResource("external-cache/tk_props.txt");
                    if (url == null) {
                        String msg = "Cannot load tk_props.txt from <" + tkPropsLoc + "> or classpath.";
                        logger.fatal(msg);
                        throw new InitializationFailedException(msg);
                    }
				}


				String ecString = StringUtil.removePrefix(url.toString(), "file:");
				logger.info("Loading external cache from <" + ecString + ">");
				externalCache = new File(ecString);
				externalCache = externalCache.getParentFile();  // remove tk_props.txt from path
			} else {
				externalCache = new File(externalCacheString);
			}
            logger.debug("External Cache location is <" + externalCache + ">");
		}


        initializeExternalCache(externalCache);
	}
	
	void initializeExternalCache(File externalCache) throws InitializationFailedException {
		this.externalCache = externalCache;
		logger.info("Initializing external cache to <" + externalCache + "> ");
			File f = externalCache;
			if (f.exists()) {
				if (!f.isDirectory()) throw new InitializationFailedException("External Cache location <" + f + "> is not a directory");
				if (!f.canWrite()) throw new InitializationFailedException("External Cache location <" + f + "> is not writable");
			} else {
				throw new InitializationFailedException("External Cache directory <" + externalCache + "> does not exist; it is specified by the following toolkit.properties file: <" + getToolkitPropertiesFile().toString() + ">. If the application is running in a development environment, please edit the toolkit.properties file in the src/main/resources instead of the path shown in the previous statement.");
			}
			initializeRepository();
			initializeEnvironments();
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
			try {
				is = Io.getInputStreamFromFile(toolkitPropertiesFile);
			} catch (FileNotFoundException e) {
				throw new RuntimeException("Could not load " + TOOLKIT_PROPERTIES + " from <" + toolkitPropertiesFile + ">");
			}
		} else {
			logger.debug("Using class loader");
			ClassLoader cl = getClass().getClassLoader();
			URL url = cl.getResource(TOOLKIT_PROPERTIES);
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

	void initializeRepository() {
		File r = Toolkit.externalRepositoryFile();
		if (!r.exists()) r.mkdir();
	}

	void initializeEnvironments() {
		File e = Toolkit.environmentsFile();
		if (!e.exists()) e.mkdir();
	}

	public PropertyManager getPropertyManager() {
		return new PropertyManager();
	}

	public void setExternalCache(File externalCache) throws InitializationFailedException {
        ExceptionUtil.here("setting external cache");
		initializeExternalCache(externalCache);
	}

	public PropertyServiceManager propertyServiceManager() {
		if (propertyServiceMgr == null)
			propertyServiceMgr = new PropertyServiceManager();
		return propertyServiceMgr;
	}

	public File getExternalCache() {
		return externalCache;
	}

}
