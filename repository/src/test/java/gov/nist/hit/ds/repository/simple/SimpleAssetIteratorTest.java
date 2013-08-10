package gov.nist.hit.ds.repository.simple;

import static org.junit.Assert.*;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.Type;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleAssetIterator;
import gov.nist.hit.ds.repository.simple.SimpleType;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleAssetIteratorTest {
	static Repository repos;
	
	// Create temp folder to be the External Cache
//	@Rule
//	public TemporaryFolder tempFolder = new TemporaryFolder()
	
	@BeforeClass
	static public void initialize() throws RepositoryException {
		
		RepositoryFactory fact = new RepositoryFactory();
		repos = fact.createRepository(
				"This is my repository",
				"Description",
				new SimpleType("simple"));
		
		repos.createAsset("My Site", "This is my site", new SimpleType("site"));

		repos.createAsset("My Site", "This is my simple", new SimpleType("simple"));
	}

	@Test
	public void allAssetsTest() throws RepositoryException {
		assertEquals(1, assetCount(new SimpleType("site")));
		assertEquals(1, assetCount(new SimpleType("simple")));
		assertEquals(2, assetCount(null));
	}
	
	int assetCount(Type type) throws RepositoryException {
		int count = 0;
		SimpleAssetIterator it = new SimpleAssetIterator(repos.getId(), type);
		
		while (it.hasNextAsset()) {
			count++;
			it.nextAsset();
		}
		return count;
	}
}
