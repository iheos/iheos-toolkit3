package gov.nist.hit.ds.eventLog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gov.nist.hit.ds.eventLog.testSupport.TestEnvironmentSetup;
import gov.nist.hit.ds.initialization.installation.InitializationFailedException;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.simple.SimpleType;

import org.junit.Before;
import org.junit.Test;

public class CreateEventTest {
	
	@Before
	public void setup() throws InitializationFailedException {
		new TestEnvironmentSetup().setup();
	}
	
	@Test
	public void openSameRepository() throws RepositoryException {
		RepositoryFactory fact = new RepositoryFactory();
		Repository repos = fact.createNamedRepository("foo", "my foo", new SimpleType("eventLog"), "BillFoo"); 
		Repository repos2 = fact.createNamedRepository("foo", "my foo", new SimpleType("eventLog"), "BillFoo");
		assertEquals("Must return same repository", repos.getId().getIdString(), repos2.getId().getIdString());
		
		Asset a = repos.createNamedAsset("myasset", "fo", new SimpleType("event"), "MyAsset");
	}

	@Test
	public void createTest() throws RepositoryException {
		Repository repos;
		
		RepositoryFactory fact = new RepositoryFactory();
		
		repos = fact.createRepository(
				"This is a simple repository",
				"Description - the most basic repository",
				new SimpleType("eventLog"));
		
		assertNotNull(repos);
		
		Asset a = repos.createAsset("My Site", "This is my site", new SimpleType("event"));
		a.updateContent("basic string - text stream - content\ntest 1", "text/*");
				
		assertNotNull(a);
		
		

	}
}
