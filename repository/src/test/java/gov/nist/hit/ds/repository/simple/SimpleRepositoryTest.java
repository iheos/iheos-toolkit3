package gov.nist.hit.ds.repository.simple;

import gov.nist.hit.ds.repository.api.ArtifactId;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositoryIterator;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.api.Type;
import gov.nist.hit.ds.repository.shared.id.SimpleTypeId;
import gov.nist.hit.ds.utilities.datatypes.Hl7Date;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SimpleRepositoryTest {

	static ArtifactId repId = null;
	
	
	@BeforeClass
	static public void initialize() throws RepositoryException {
		
		Repository rep = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL)).createRepository(
				"Repos "+ Math.random(),
				"Description",
				new SimpleType("simpleRepos", ""));
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
//		Type simpleType = new SimpleType("simpleRepos");
        Type simpleType = Configuration.getType(new SimpleTypeId("simpleRepos",SimpleType.REPOSITORY));
                Repository repos1 = fact.createRepository(
				"Repos "+ new Hl7Date().now(),
				"Description",
				simpleType);
		ArtifactId repId1 = repos1.getId();
		Type repType1 = repos1.getType();
		
		assertTrue("query for type simple should return a repository of type simple - got [" +
		    repType1.getDomain() + "] instead.", simpleType.isEqual(repType1));
		
		Repository repos2 = fact.createRepository(
				"Repos 2",
				"Description",
				simpleType);
		ArtifactId repId2 = repos2.getId();
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
	public void residentCreateReposTest() throws RepositoryException {
		
		try {
			new RepositoryFactory(Configuration.getRepositorySrc(Access.RO_RESIDENT))
			.createRepository(                           // Create on resident is bad
					"Resident repos",
					"Description",
					new SimpleType("simpleRepos", "no desc") 
					);
			
			
			fail("An exception should be thrown");
			
		} catch (RepositoryException re) {
			// This is expected
			;
		}
		
	}
	
	
	@Test
	public void badReposTypeTest() {
		try {
			new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL))
			.createRepository(                           
					"This is my repository",
					"Description",
					new SimpleType("XXX- BAD TYPE", "no desc")  // Type is bad
					);
			
			
			fail("An exception should have been thrown");
			
		} catch (RepositoryException re) {
			// This is expected
			;
		}
		
	}
	
	boolean findRepo(Type type, ArtifactId repIdToFind) throws RepositoryException {
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
