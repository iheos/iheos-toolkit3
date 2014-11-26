package gov.nist.hit.ds.initialization.installation;

import gov.nist.hit.ds.initialization.environment.Environment;

import java.io.File;

public class ExternalCacheManager {
	File externalCache;
	
	public ExternalCacheManager(File externalCache) {
		this.externalCache = externalCache;
	}
	
	public File getRepositoryFile() {
		return new File(externalCache, "repositories");
	}
	
	public File getTkPropsFile() {
		return new File(externalCache, "tk_props.txt");
	}
	
	public File getEnvironmentFile() {
		return new File(externalCache, "environment");
	}
	
	public File getSimDbFile() {
		return new File(externalCache, "simdb");
	}
	
	public File getActorsFile() {
		return new File(externalCache, "actors.xml");
	}
	
	public File getActorsDir() {
		return new File(externalCache, "actors");
	}

    public Environment getEnvironment(){return new Environment(getEnvironmentFile());}

}
