package gov.nist.hit.ds.registrySim;

import gov.nist.hit.ds.initialization.installation.InitializationFailedException;
import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;

import java.io.File;
import java.io.IOException;

public class TestEnvironmentSetup {
	File RepositoriesPath;  					// Repositories folder
	File InstallationPath;						// Path containing the WEB-INF folder (for External_Cache)		
	File RepositoryRoot; 
	
	public TestEnvironmentSetup setup() throws InitializationFailedException, IOException, RepositoryException {
		new FactoryTest().init();
		//
		// Assets stored in temporary repositories will be cleaned up after test run
		// 
		setInstallationPath(new File("src/test/resources/Installation"));
		System.out.println(getInstallationPath());


//		Installation.installation().setWarHome(getInstallationPath()); // This would be the WAR installation directory

		File externalCache = new File("/Users/bill/tmp/external_cache");

		Installation.installation().setExternalCache(externalCache); // Prefix only if externalCache is not expected to be absolute path, which in this case the EC_Dir is relative. 

		setRepositoriesPath(new File(externalCache, "/repositories"));
		setRepositoryRoot(RepositoriesPath);

		try {
			Configuration.configuration();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}	
		return this;
	}

	public File getRepositoriesPath() {
		return RepositoriesPath;
	}

	public void setRepositoriesPath(File repositoriesPath) {
		RepositoriesPath = repositoriesPath;
	}

	public File getInstallationPath() {
		return InstallationPath;
	}

	public void setInstallationPath(File installationPath) {
		InstallationPath = installationPath;
	}

	public File getRepositoryRoot() {
		return RepositoryRoot;
	}

	public void setRepositoryRoot(File repositoryRoot) {
		RepositoryRoot = repositoryRoot;
	}


}
