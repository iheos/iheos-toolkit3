package gov.nist.hit.ds.repository.simple.index;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.initialization.Installation;
import gov.nist.hit.ds.repository.api.Id;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.IdFactory;
import gov.nist.hit.ds.repository.simple.SimpleId;
import gov.nist.hit.ds.repository.simple.index.db.DbIndexContainer;

import java.io.File;

import javax.servlet.ServletContext;

import org.junit.BeforeClass;
import org.junit.Test;

public class CreateContainerTest  {
	
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
	public void removeContainerTest() {
		System.out.println("Running testRemoveContainer...");
		DbIndexContainer dbc = new DbIndexContainer();
		try {
			if (dbc.doesIndexContainerExist())
				dbc.removeIndexContainer();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("removeContainer failed!");
		
		}
	}
	
	@Test
	public void createIndexContainerTest() {
		System.out.println("Running testIndexContainer...");
		DbIndexContainer dbc = new DbIndexContainer();
		try {
			if (!dbc.doesIndexContainerExist()) {
				dbc.createIndexContainer();
				System.out.println("New index container created.");
			}
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Index Container test Failed");
						
		}		
	}
	
	
	@Test
	public void purgeIndexTest() throws RepositoryException {
		System.out.println("Running testPurgeIndex...");
		
		DbIndexContainer dbc = new DbIndexContainer();

		try {
			if (dbc.doesIndexContainerExist()) {
				dbc.purge();
				assertTrue(dbc.getIndexCount()==0);
			}
		
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("purge failed");
						
		}
	}
	
//	@Test 
	public void expandContainerTest() {
		System.out.println("Running testExpandContainer...");
		
		DbIndexContainer dbc = new DbIndexContainer();
		
		try {
			dbc.expandContainer(new String[] {"a","b","c"}, "siteAsset");
		} catch (Exception e) {
			fail("test expand failed!");

		}
	}
	
//	@Test
	public void addIndexTest() throws RepositoryException {
		System.out.println("Running testAddIndex...");
		
		DbIndexContainer dbc = new DbIndexContainer();

		try {
			Id id = new IdFactory().getNewId();
			SimpleId sid = new SimpleId(id.getIdString());

			int insertRecords = 5;
			for (int cx=0; cx<insertRecords; cx++) {
				id = new IdFactory().getNewId();
				sid = new SimpleId(id.getIdString());

				dbc.addIndex("test", sid.getIdString(), "siteAsset","a","myvalue" + cx);
				
			}
			assertTrue(dbc.getIndexCount()==insertRecords);

			
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("add index failed");
		}
	}
	


//	@Test 
	public void indexColumnTest() {
		System.out.println("Running testCol...");
		
		DbIndexContainer dbc = new DbIndexContainer();
		
		try {
			dbc.expandContainer(new String[] {"x","b","z"}, "siteAsset");
			System.out.println (dbc.isIndexed(dbc.getColumn("siteAsset", "a")));
		} catch (Exception e) {
			fail("test expand failed!");
						
		}
	}
	
}