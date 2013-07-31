package gov.nist.hit.ds.repository.simple;

import static org.junit.Assert.assertTrue;
import gov.nist.hit.ds.repository.api.Id;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositoryIterator;
import gov.nist.hit.ds.repository.api.Type;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleRepositoryTest {
	static File RootOfAllRepositories = new File("/temp/repositories");
	static Id repId = null;
	
	// Create temp folder to be the External Cache
//	@Rule
//	public TemporaryFolder tempFolder = new TemporaryFolder()
	
	@BeforeClass
	static public void initialize() throws RepositoryException {
		// skb fix later with EasyMock
		// ToDO
		//Installation.installation()
		//System.out.println (Installation.installation().tkProps.get("toolkit.servlet.context", "xdstools2"));
		
		new Configuration(RootOfAllRepositories);
		Repository rep = new RepositoryFactory().createRepository(
				"This is my repository",
				"Description",
				new SimpleType("simple", ""));
		repId = rep.getId();
	}
	
	@Test
	public void loadRepositoryTest() throws RepositoryException {
		RepositoryFactory repFact = new RepositoryFactory();
		repFact.getRepository(repId);
	}
	
	@Test
	public void repositoryIteratorTest1() throws RepositoryException {
		SimpleRepositoryIterator it = new SimpleRepositoryIterator();
		
		assertTrue (it.size() > 0);
		assertTrue (it.size() == it.remaining());
		assertTrue(it.hasNextRepository());
		it.nextRepository();
		assertTrue(it.size() == it.remaining() + 1);
	}
		
	@Test 
	public void repositoryIteratorTest2() throws RepositoryException {
		RepositoryFactory fact = new RepositoryFactory();
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
	
	boolean findRepo(Type type, Id repIdToFind) throws RepositoryException {
		RepositoryFactory fact = new RepositoryFactory();
		for (RepositoryIterator ri=fact.getRepositoriesByType(type); ri.hasNextRepository();) {
			Repository r = ri.nextRepository();
			if (repIdToFind.isEqual(r.getId())) {
				return true;
			}
		}
		return false;
	}
	
}
