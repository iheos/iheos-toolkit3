package gov.nist.hit.ds.environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Environment {
	File externalCache;
	
	public Environment(File externalCache) {
		this.externalCache = externalCache;
	}
	
	File getEnvDir() {
		return new File(externalCache + "environment");
	}
	
	/**
	 * Get names of all installed Environments.
	 * @return
	 */
	public List<String> getInstalledEnvironments() {
		List<String> envs = new ArrayList<String>();
		File envDir = getEnvDir();
		File[] files = envDir.listFiles();
		if (files == null)
			return envs;
		for (int i=0; i<files.length; i++) {
			File file = files[i];
			if (file.isDirectory())
				envs.add(file.getName());
		}
		return envs;
	}
	
	public File getCodesFile(String environmentName) {
		return new File(getEnvDir(), environmentName);
	}
}
