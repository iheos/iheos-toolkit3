package gov.nist.hit.ds.repository.simple.index;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.index.db.DbIndexContainer;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

public class ExpandContainerTest {

	
	
	@Test
	public void retrieveIndexPropertyTest() {
		
		try {
			for (String s : DbIndexContainer.getIndexableProperties(Configuration.getRepositorySrc(Access.RW_EXTERNAL))) {
				System.out.println (s);
			}
		} catch (RepositoryException e) {			
			fail(e.toString());
		}
		
	}
	
	//@Test
	public void expandColumnsTest() {
		DbIndexContainer dbc = new DbIndexContainer();
		ConcurrentHashMap<String,String> columnMap = new ConcurrentHashMap<String,String>();

		try {
			ArrayList<String> iap = DbIndexContainer.getIndexableProperties(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
			dbc.expandContainer(iap.toArray(new String[iap.size()]), columnMap);
		
			for (String s : iap) {
				assertTrue(dbc.isIndexed(s));	
			}
			
		} catch (Exception e) {
			fail("test expand failed!" + e.toString());						
		}

	}
	
}
