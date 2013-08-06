package gov.nist.hit.ds.repository.simple;

import gov.nist.hit.ds.initialization.Installation;
import gov.nist.hit.ds.repository.simple.index.CreateContainerTest;
import gov.nist.hit.ds.repository.simple.index.ExpandContainerTest;
import gov.nist.hit.ds.repository.simple.index.IndexableRepositoryTest;
import gov.nist.hit.ds.repository.simple.search.SearchTest;

import java.io.File;

import org.junit.ClassRule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({		
		CreateContainerTest.class
		,ExpandContainerTest.class
		,IndexableRepositoryTest.class
		,SimpleCreateAssetTest.class
		,SimpleAssetIteratorTest.class
		,SimpleAssetTest.class
		,SimpleNamedRepositoryTest.class
		,SimpleRepositoryIteratorTest.class
		,SimpleRepositoryTest.class
		,SimpleTextAssetTest.class
		,SimpleTypeIteratorTest.class
		,SearchTest.class
		} )
public class SimpleTestSuite {
	

	
	
	@ClassRule
	static public ExternalResource classRule = new ExternalResource()	{
		  String RepositoriesPath;  					// Repositories folder
		  public String getRepositoriesPath() {
			return RepositoriesPath;
		}

		public void setRepositoriesPath(String repositoriesPath) {
			RepositoriesPath = repositoriesPath;
		}

		public String getInstallationPath() {
			return InstallationPath;
		}

		public void setInstallationPath(String installationPath) {
			InstallationPath = installationPath;
		}

		public File getRepositoryRoot() {
			return RepositoryRoot;
		}

		public void setRepositoryRoot(File repositoryRoot) {
			RepositoryRoot = repositoryRoot;
		}

		String InstallationPath;						// Path containing the WEB-INF folder (for External_Cache)		
		  File RepositoryRoot; 
		
		@Override
		protected void before() throws Throwable {

			
			try {
				setInstallationPath(SimpleTestSuite.class.getClassLoader().getResource("Installation/").getFile());
				System.out.println(getInstallationPath());
				
			} catch (NullPointerException e) {
				throw new Exception("The resource folder path configuration seems to be incorrect!");
			}
			
			Installation.installation();
			Installation.installation().setWarHome(new File(getInstallationPath())); // This would be the WAR installation directory
			
			
			
			String externalCache = Installation.installation().propertyServiceManager()
										.getToolkitProperties().get("External_Cache");
			
			// Incorporate relative path for testing, this can be skipped for non-development environments
			externalCache = InstallationPath + externalCache;
			
			System.out.println(externalCache);
			
			Installation.installation().setExternalCache(new File(externalCache)); // Prefix only if externalCache is not expected to be absolute path, which in this case the EC_Dir is relative. 
			
			setRepositoriesPath(externalCache + "/repositories");
			setRepositoryRoot(new File(RepositoriesPath));
			
			new Configuration(getRepositoryRoot());			
			
		}
		
		@Override
		protected void after() {
			
		}

	};

}
