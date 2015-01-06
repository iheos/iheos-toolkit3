package gov.nist.hit.ds.toolkit.environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Environment {
	String name;
    File environmentsFile;
	
	public Environment(File environmentsFile, String name) {
        this.name = name;
        this.environmentsFile = environmentsFile;
	}

    File environmentDir() { return new File(environmentsFile, name); }

    public File getCodesFile() {
        return new File(environmentDir(), "codes.xml");
    }

    public String getName() { return name; }

	
	/**
	 * Get names of all installed Environments. Should be called only
     * through Toolkit()
	 * @return
	 */
	static public List<String> getInstalledEnvironments(File environmentsFile) {
		List<String> envs = new ArrayList<String>();
		File[] files = environmentsFile.listFiles();
		if (files == null)
			return envs;
		for (int i=0; i<files.length; i++) {
			File file = files[i];
			if (file.isDirectory())
				envs.add(file.getName());
		}
		return envs;
	}
	
}
