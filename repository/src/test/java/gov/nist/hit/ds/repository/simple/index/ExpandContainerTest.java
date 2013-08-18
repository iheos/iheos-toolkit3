package gov.nist.hit.ds.repository.simple.index;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.index.db.DbIndexContainer;

import java.io.File;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.junit.BeforeClass;
import org.junit.Test;

public class ExpandContainerTest {

	
	
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
