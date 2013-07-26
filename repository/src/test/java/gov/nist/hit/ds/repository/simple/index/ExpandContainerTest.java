package gov.nist.hit.ds.repository.simple.index;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.initialization.Installation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.index.db.DbIndexContainer;

import java.io.File;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.junit.BeforeClass;
import org.junit.Test;

public class ExpandContainerTest {

	/*
	 * Important: The following system path variables need to verified manually before running the test.
	 * 
	 */

	static String RootPath = "/e/artrep_test_resources/"; 		// Root Path or the Test resources folder
	public static String RepositoriesPath;  					// Repositories folder
	static String InstallationPath = RootPath+"installation";	// Path containing the WEB-INF folder (for External_Cache)
	
	public static File RootOfAllRepositories; 
	static Installation inst = null;
	
	@BeforeClass
	static public void initialize() throws RepositoryException {
		
		// The MockServletContext is used for testing purposes only
		
		ServletContext sc = MockServletContext.getServletContext(InstallationPath); 
		
		Installation.installation(sc);
		
		String externalCache = Installation.installation().propertyServiceManager()
									.getToolkitProperties().get("External_Cache");
		System.out.println(externalCache);
		Installation.installation().setExternalCache(new File(sc.getRealPath(externalCache)));
		inst = Installation.installation();
		
		RepositoriesPath = externalCache + "/repositories";
		RootOfAllRepositories = new File(RepositoriesPath);
		
		new Configuration(RootOfAllRepositories);

	}

	
	@Test
	public void retrieveIndexPropertyTest() {		
		for (String s : DbIndexContainer.getIndexableProperties()) {
			System.out.println (s);
		}
		
	}
	
	//@Test
	public void expandColumnsTest() {
		DbIndexContainer dbc = new DbIndexContainer();
		

		try {
			ArrayList<String> iap = DbIndexContainer.getIndexableProperties();
			dbc.expandContainer(iap.toArray(new String[iap.size()]));
		
			for (String s : iap) {
				assertTrue(dbc.isIndexed(s));	
			}
			
		} catch (Exception e) {
			fail("test expand failed!");
						
		}

	}
	
}
