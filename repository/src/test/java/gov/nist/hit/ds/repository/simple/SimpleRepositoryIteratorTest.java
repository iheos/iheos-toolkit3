package gov.nist.hit.ds.repository.simple;

import static org.junit.Assert.assertTrue;
import gov.nist.hit.ds.initialization.Installation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleRepositoryIterator;
import gov.nist.hit.ds.repository.simple.SimpleType;
import gov.nist.hit.ds.repository.simple.index.MockServletContext;

import java.io.File;

import javax.servlet.ServletContext;

import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleRepositoryIteratorTest {
	
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
		new RepositoryFactory().createRepository(
				"This is my repository",
				"Description",
				new SimpleType("simple", ""));
	}
	

	@Test
	public void repositoryIteratorTest() throws RepositoryException {
		SimpleRepositoryIterator it = new SimpleRepositoryIterator();
		
		assertTrue (it.size() > 0);
		assertTrue (it.size() == it.remaining());
		assertTrue(it.hasNextRepository());
		it.nextRepository();
		assertTrue(it.size() == it.remaining() + 1);
	}
		

}
