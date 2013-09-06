package gov.nist.hit.ds.repository.simple;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.repository.api.Id;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositoryIterator;
import gov.nist.hit.ds.repository.api.Type;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;

import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleRepositoryTest {

	static Id repId = null;
	
	
	@BeforeClass
	static public void initialize() throws RepositoryException {
		
		Repository rep = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL)).createRepository(
				"This is my repository",
				"Description",
				new SimpleType("simple", ""));
		repId = rep.getId();
	}
	
	@Test
	public void loadRepositoryTest() throws RepositoryException {
		RepositoryFactory repFact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		repFact.getRepository(repId);
	}
	
	@Test
	public void repositoryIteratorTest1() throws RepositoryException {
		SimpleRepositoryIterator it = new SimpleRepositoryIterator(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		
		assertTrue (it.size() > 0);
		assertTrue (it.size() == it.remaining());
		assertTrue(it.hasNextRepository());
		it.nextRepository();
		assertTrue(it.size() == it.remaining() + 1);
	}
		
	@Test 
	public void repositoryIteratorTest2() throws RepositoryException {
		RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		Type simpleType = new SimpleType("simple", "");
		Repository repos1 = fact.createRepository(
				"This is my repository",
				"Description",
				simpleType);
		Id repId1 = repos1.getId();
		Type repType1 = repos1.getType();
		
		assertTrue("query for type simple should return a repository of type simple - got [" +
		    repType1.getDomain() + "] instead.", simpleType.isEqual(repType1));
		
		Repository repos2 = fact.createRepository(
				"This is my repository",
				"Description",
				simpleType);
		Id repId2 = repos2.getId();
		Type repType2 = repos2.getType();
		
		assertTrue("query for type simple should return a repository of type simple - got [" +
		    repType2.getDomain() + "] instead.", simpleType.isEqual(repType2));

		boolean found = false;
		for (RepositoryIterator ri=fact.getRepositoriesByType(simpleType); ri.hasNextRepository();) {
			Repository r = ri.nextRepository();
			if (repId1.isEqual(r.getId())) {
				found = true;
				break;
			}
		}
		assertTrue("repId1 not found", found);
		
		found = false;
		for (RepositoryIterator ri=fact.getRepositoriesByType(simpleType); ri.hasNextRepository();) {
			Repository r = ri.nextRepository();
			if (repId2.isEqual(r.getId())) {
				found = true;
				break;
			}
		}
		assertTrue("repId2 not found", found);
	}
	
	@Test
	public void invalidReposTypeTest() throws RepositoryException {
		
		try {
			new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL))
			.createRepository(
					"This is my repository",
					"Description",
					new SimpleType("XXX", "") // This is bad
					);
			
			fail("An exception should be thrown");
			
		} catch (RepositoryException re) {
			// This is good			
			;
		}
		
		
		
	}	
	public void residentTest() throws RepositoryException {
		
		RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		
		fact.createRepository("new repository", "test", new SimpleType("simple", ""));
	}
	
	boolean findRepo(Type type, Id repIdToFind) throws RepositoryException {
		RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		for (RepositoryIterator ri=fact.getRepositoriesByType(type); ri.hasNextRepository();) {
			Repository r = ri.nextRepository();
			if (repIdToFind.isEqual(r.getId())) {
				return true;
			}
		}
		return false;
	}
	
}
