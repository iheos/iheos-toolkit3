package gov.nist.hit.ds.repository.simple.index;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import gov.nist.hit.ds.repository.api.Id;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.IdFactory;
import gov.nist.hit.ds.repository.simple.SimpleId;
import gov.nist.hit.ds.repository.simple.index.db.DbIndexContainer;

import org.junit.Test;

public class CreateContainerTest  {
	

	
	@Test
	public void removeContainerTest() {
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
	
	@Test
	public void createIndexContainerTest() {
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
		ConcurrentHashMap<String,String> columnMap = new ConcurrentHashMap<String,String>();
		try {
			dbc.expandContainer(new String[] {"a","b","c"}, columnMap);
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
		ConcurrentHashMap<String,String> columnMap = new ConcurrentHashMap<String,String>();
		try {
			dbc.expandContainer(new String[] {"x","b","z"},  columnMap);
			System.out.println (dbc.isIndexed(dbc.getColumn("siteAsset", "a")));
		} catch (Exception e) {
			fail("test expand failed!");
						
		}
	}
	
}