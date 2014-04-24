package gov.nist.hit.ds.repository.simple.index;

import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.index.db.DbIndexContainer;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

		try {
			ArrayList<String> iap = DbIndexContainer.getIndexableProperties(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
			dbc.expandContainer(iap.toArray(new String[iap.size()]));
		
			for (String s : iap) {
				dbc.getIndexedColumn(s);
			}
			
		} catch (Exception e) {
			fail("test expand failed!" + e.toString());						
		}

	}
	
}
