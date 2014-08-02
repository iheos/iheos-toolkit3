package gov.nist.hit.ds.repository.simple.index;


import gov.nist.hit.ds.repository.api.ArtifactId;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.IdFactory;
import gov.nist.hit.ds.repository.simple.SimpleId;
import gov.nist.hit.ds.repository.simple.index.db.DbIndexContainer;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CreateContainerTest  {

    @BeforeClass
    static public void initialize() throws RepositoryException {

            removeContainer();
            createIndexContainer();

    }


	static public void removeContainer() {
		System.out.println("Running testRemoveContainer...");
		DbIndexContainer dbc = new DbIndexContainer();
		try {
			if (dbc.doesIndexContainerExist())
				dbc.removeIndexContainer();
		} catch (RepositoryException e) {
			e.printStackTrace();
			fail("removeContainer failed!");

		}
	}


	static private void createIndexContainer() {
		System.out.println("Running testIndexContainer...");
		DbIndexContainer dbc = new DbIndexContainer();
		try {
			if (!dbc.doesIndexContainerExist()) {
				dbc.createIndexContainer();
			}
		} catch (RepositoryException e) {
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
			e.printStackTrace();
			fail("purge failed");
						
		}
	}
	
//	@Test 
	public void expandContainerTest() {
		System.out.println("Running testExpandContainer...");
		
		DbIndexContainer dbc = new DbIndexContainer();

		try {
			dbc.expandContainer(new String[] {"a","b","c"});
		} catch (Exception e) {
			fail("test expand failed!");

		}
	}
	
//	@Test
	public void addIndexTest() throws RepositoryException {
		System.out.println("Running testAddIndex...");
		
		DbIndexContainer dbc = new DbIndexContainer();

		try {
			ArtifactId id = new IdFactory().getNewId();
			SimpleId sid = new SimpleId(id.getIdString());

			int insertRecords = 5;
			for (int cx=0; cx<insertRecords; cx++) {
				id = new IdFactory().getNewId();
				sid = new SimpleId(id.getIdString());

				dbc.addIndex("test", sid.getIdString(), "siteAsset", "/bogus", "a","myvalue" + cx);
				
			}
			assertTrue(dbc.getIndexCount()==insertRecords);

			
		} catch (RepositoryException e) {
			e.printStackTrace();
			fail("add index failed");
		}
	}
	


//	@Test 
	public void indexColumnTest() {
		System.out.println("Running testCol...");
		
		DbIndexContainer dbc = new DbIndexContainer();

		try {
			dbc.expandContainer(new String[] {"x","b","z"});
			System.out.println(dbc.getIndexedColumn(dbc.getColumn("siteAsset", "a")));
		} catch (Exception e) {
			fail("test expand failed!");
		}
	}
	
}