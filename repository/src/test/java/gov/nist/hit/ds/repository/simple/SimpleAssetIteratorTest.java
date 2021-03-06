package gov.nist.hit.ds.repository.simple;

import static org.junit.Assert.*;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.Type;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.SimpleAssetIterator;
import gov.nist.hit.ds.repository.simple.SimpleType;

import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleAssetIteratorTest {
	static Repository repos;
	
	// Create temp folder to be the External Cache
//	@Rule
//	public TemporaryFolder tempFolder = new TemporaryFolder()
	
	@BeforeClass
	static public void initialize() throws RepositoryException {
		
		RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		repos = fact.createRepository(
				"This is my repository",
				"Description",
				new SimpleType("simpleRepos"));
		
		repos.createAsset("My Site", "This is my site", new SimpleType("siteAsset"));

		repos.createAsset("My Site", "This is my simple", new SimpleType("simple"));
	}

	@Test
	public void allAssetsTest() throws RepositoryException {
		assertEquals(1, assetCount(new SimpleType("siteAsset")));
		assertEquals(1, assetCount(new SimpleType("simple")));
		assertEquals(2, assetCount(null));
	}
	
	int assetCount(Type type) throws RepositoryException {
		int count = 0;
		
		
		
		SimpleAssetIterator it = new SimpleAssetIterator(repos, type);
		
		while (it.hasNextAsset()) {
			count++;
			it.nextAsset();
		}
		return count;
	}
	
	@Test
	public void residentAssetIteratorTest() throws RepositoryException {

		SimpleRepository repos = new SimpleRepository(new SimpleId("testkit"));
		repos.setSource(Configuration.getRepositorySrc(Access.RO_RESIDENT));
		
		
		SimpleAssetIterator it = new SimpleAssetIterator(repos);
		
		assertTrue(it.hasNextAsset());
	}
}
