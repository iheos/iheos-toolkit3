package gov.nist.hit.ds.repository.simple;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nist.hit.ds.initialization.Installation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.Type;
import gov.nist.hit.ds.repository.api.TypeIterator;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleTypeIterator;
import gov.nist.hit.ds.repository.simple.index.MockServletContext;

import java.io.File;

import javax.servlet.ServletContext;

import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleTypeIteratorTest {
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
	public void simpleTypeIteratorTest() throws RepositoryException {
		TypeIterator it = new SimpleTypeIterator();
		assertTrue("initially the iterator should have hasNextType() return true", it.hasNextType());
		
		Type nextType = it.nextType();
		
		assertNotNull("nextType() should return an object, not null", nextType);
		
		Type nextType2 = it.nextType();
		
		assertFalse("Two returned types should not be equal", nextType.isEqual(nextType2));
	}
	
	@Test
	public void managerIteratorTest() throws RepositoryException {
		TypeIterator ti = new RepositoryFactory().getRepositoryTypes();
		assertTrue("initially the iterator should have hasNextType() return true", ti.hasNextType());
		
		Type nextType = ti.nextType();
		
		assertNotNull("nextType() should return an object, not null", nextType);
		
		Type nextType2 = ti.nextType();
		
		assertFalse("Two returned types should not be equal", nextType.isEqual(nextType2));
	}
	
	
}
