package gov.nist.hit.ds.repository.simple;


import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.index.BulkLoadTest;
import gov.nist.hit.ds.repository.simple.index.CreateContainerTest;
import gov.nist.hit.ds.repository.simple.index.ExpandContainerTest;
import gov.nist.hit.ds.repository.simple.search.AssetNodeBuilderTest;
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
		,AssetNodeBuilderTest.class
		,BulkLoadTest.class
		} )
public class SimpleTestSuite {
	
	@ClassRule
	static public ExternalResource classRule = new ExternalResource()	{
		
		@Override
		protected void before() throws Throwable {
			
			// Setup application-wide singletons
			
			Installation.reset();
			Installation.installation().initialize(); 			
 			
 			// Test assets created will be automatically removed after the test suite run is complete
 						 			
			Configuration.configuration();
			
			
			File dataDir = Configuration.getRepositoriesDataDir(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
			
			if (dataDir.exists()) {
			 System.out.println("Clearing TEST data before testing...");
			 FileUtils.cleanDirectory(dataDir);
			}

		}
		
		@Override
		protected void after() {
			
//			try {
//				System.out.println("Clearing TEST data folder after testing...");
//				 FileUtils.cleanDirectory(Configuration.getRepositoriesDataDir(Configuration.getRepositorySrc(Access.RW_EXTERNAL)));
//				 System.out.println("done.");
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (RepositoryException e) {
//				e.printStackTrace();
//			}
		 
		}
		

	};

}
