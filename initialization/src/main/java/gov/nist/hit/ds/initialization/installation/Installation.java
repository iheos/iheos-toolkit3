package gov.nist.hit.ds.initialization.installation;


import gov.nist.hit.ds.initialization.environment.Environment;
import gov.nist.hit.ds.initialization.tkProps.TkLoader;
import gov.nist.hit.ds.initialization.tkProps.client.TkProps;

import java.io.File;
import java.io.IOException;

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

	/**
	 * Initialize the installation.  This is called by a servlet initializiation
	 * since that happens when the app is loaded. Before that first call,
	 * warHome must be set or nothing else will work.  If this parameter
	 * is wrong then toolkit is dead.
	 * 
	 * It must be called 
	 * after toolkit.properties is altered to force the reload.
	 * @throws InitializationFailedException 
	 */
	public void initialize() throws InitializationFailedException {
		if (warHome == null) {
			logger.fatal("WAR home direcotry is not initialized");
			throw new InitializationFailedException("WAR home direcotry is not initialized");
		}
		propertyServiceMgr = new PropertyServiceManager(warHome);
		initializeExternalCache(new File(propertyServiceMgr.getToolkitProperties().get(PropertyServiceManager.EXTERNAL_CACHE)));
	}

	void initializeExternalCache(File externalCache) throws InitializationFailedException {
		this.externalCache = externalCache;
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

	static public Installation installation() {
		if (me == null)
			me = new Installation();
		return me;
	}	

	private Installation() {   }

	public PropertyManager getPropertyManager() {
		return new PropertyManager(warHome + File.separator + "WEB-INF" + File.separator + "toolkit.properties");
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
			propertyServiceMgr = new PropertyServiceManager(warHome);
		return propertyServiceMgr;
	}

	public File simDbFile() {
		return propertyServiceManager().getSimDbDir();
	}

	public File toolkitxFile() {
		return new File(Installation.installation().getWarHome() + sep + "toolkitx");
	}

	ExternalCacheManager getExternalCacheManager() {
		return new ExternalCacheManager(externalCache);
	}

	public File getDefaultCodesFile() {
		File envFile =  getExternalCacheManager().getEnvironmentFile();
		String defaultEnvName = getPropertyManager().getPropertyMap().get(PropertyServiceManager.DEFAULT_ENVIRONMENT);
		File env = new File(envFile,defaultEnvName);
		return new File(env, "codes.xml");
	}

}
