package gov.nist.hit.ds.repository.simple;

import static org.junit.Assert.assertTrue;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;

import org.junit.Test;

public class SimpleRepositoryIteratorTest {
	


	@Test
	public void repositoryIteratorTest() throws RepositoryException {
		
		new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL)).createRepository(
				"Repos "+ Math.random(),
				"Description",
				new SimpleType("simpleRepos", ""));
		
		SimpleRepositoryIterator it = new SimpleRepositoryIterator(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		
		assertTrue (it.size() > 0);
		assertTrue (it.size() == it.remaining());
		assertTrue(it.hasNextRepository());
		it.nextRepository();
		assertTrue(it.size() == it.remaining() + 1);
	}
		
	@Test
	public void residentReposIteratorTest() throws RepositoryException {
		
	SimpleRepositoryIterator it = new SimpleRepositoryIterator(Configuration.getRepositorySrc(Access.RO_RESIDENT));
		
		assertTrue (it.size() > 0);
		assertTrue (it.size() == it.remaining());
		assertTrue(it.hasNextRepository());
		it.nextRepository();
		assertTrue(it.size() == it.remaining() + 1);
	
	}
	
}
