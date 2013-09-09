package gov.nist.hit.ds.repository.simple;


import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.index.CreateContainerTest;
import gov.nist.hit.ds.repository.simple.index.ExpandContainerTest;
import gov.nist.hit.ds.repository.simple.search.SearchTest;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.ClassRule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * 
 * @author Sunil.Bhaskarla
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({		
		CreateContainerTest.class
		,ExpandContainerTest.class
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


		
		@Override
		protected void before() throws Throwable {
			
			File warHome = new File(getClass().getClassLoader().getResource(SimpleTestSuite.class.getSimpleName()).getFile());
			
			Installation.installation();
			Installation.installation().setWarHome(warHome); // This would be the WAR installation directory
			
			
			// Test assets created will be automatically removed after the test
			File externalCache = new File(warHome.toString() + "/Testing/Test_environment");
						
			Installation.installation().setExternalCache(externalCache); // Use a prefix here when externalCache is not expected to be absolute path, which in this case the EC_Dir is relative. 
			
			Configuration.configuration();
						
		}
		
		@Override
		protected void after() {
			
			try {
				System.out.println("Clearing test data folder...");
				 FileUtils.cleanDirectory(Configuration.getRepositoriesDataDir(Configuration.getRepositorySrc(Access.RW_EXTERNAL)));
				 System.out.println("done.");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (RepositoryException e) {
				e.printStackTrace();
			}
		 
		}
		

	};

}
