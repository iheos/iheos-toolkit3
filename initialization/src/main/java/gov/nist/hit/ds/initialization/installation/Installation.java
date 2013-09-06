package gov.nist.hit.ds.initialization.installation;


import gov.nist.hit.ds.initialization.tkProps.TkLoader;
import gov.nist.hit.ds.initialization.tkProps.client.TkProps;
import gov.nist.hit.ds.utilities.io.Io;

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
	File warHome = null;
	String sep = File.separator;
	public TkProps tkProps;
	PropertyServiceManager propertyServiceMgr = null;
	static Logger logger = Logger.getLogger(Installation.class);
	static Installation me = null;
	File externalCache;
	String buildNumber;
	File toolkitPropertiesFile = null;

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
		if (warHome == null) {
			logger.fatal("WAR home direcotry is not initialized");
			throw new InitializationFailedException("WAR home directory is not initialized");
		}
		propertyServiceMgr = new PropertyServiceManager();

		loadBuildNumber();

		initializeExternalCache(new File(propertyServiceMgr.getToolkitProperties().get(PropertyServiceManager.EXTERNAL_CACHE)));
	}
	
	static public void reset() { me = null; }
	
	public Installation setToolkitPropertiesFile(File propsFile) {
		this.toolkitPropertiesFile = propsFile;
		return this;
	}

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
				throw new RuntimeException("Could not load toolkit.properties from <" + toolkitPropertiesFile + ">");
			}
		} else {
			logger.debug("Using class loader");
			ClassLoader cl = getClass().getClassLoader();
			URL url = cl.getResource("toolkit.properties");
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

	void initializeExternalCache(File externalCache) throws InitializationFailedException {
		this.externalCache = externalCache;
		logger.info("Initializing external cache to <" + externalCache + "> ");
		try {
			File f = externalCache; 
			if (f.exists()) {
				if (!f.isDirectory()) throw new InitializationFailedException("External Cache location <" + f + "> is not a directory");
				if (!f.canWrite()) throw new InitializationFailedException("External Cache location <" + f + "> is not writable");
			} else {
				f.mkdir();
				if (!f.exists()) throw new InitializationFailedException("Cannot create External Cache at <" + f + ">");
			}
			ExternalCacheManager ecMgr = new ExternalCacheManager(f);
			initializeRepository(ecMgr);
			initializeTkProps(ecMgr);
			initializeEnvironments(ecMgr);
			initializeSimDb(ecMgr);
			initializeActors(ecMgr);
		} catch (IOException e) {
			throw new InitializationFailedException("",e);
		}
	}

	void initializeRepository(ExternalCacheManager ecMgr) {
		File r = ecMgr.getRepositoryFile();
		if (!r.exists()) r.mkdir();
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

	static public Installation installation() {
		if (me == null)
			me = new Installation();
		return me;
	}	

	private Installation() {   }

	public PropertyManager getPropertyManager() {
		return new PropertyManager();
	}

	public File getWarHome() { 
		return warHome; 
	}

	public void setWarHome(File warHome) { 
		this.warHome = warHome; 
	}

	public void setExternalCache(File externalCache) throws InitializationFailedException {
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

	public File toolkitxFile() {
		return new File(Installation.installation().getWarHome() + sep + "toolkitx");
	}

	ExternalCacheManager getExternalCacheManager() {
		return new ExternalCacheManager(externalCache);
	}

	public File getDefaultCodesFile() throws IOException {
		File envFile =  getExternalCacheManager().getEnvironmentFile();
		String defaultEnvName = getPropertyManager().getPropertyMap().get(PropertyServiceManager.DEFAULT_ENVIRONMENT);
		File env = new File(envFile,defaultEnvName);
		return new File(env, "codes.xml");
	}

	public File getExternalCache() {
		return externalCache;
	}

}
