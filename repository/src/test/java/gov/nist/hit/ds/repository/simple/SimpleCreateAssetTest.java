package gov.nist.hit.ds.repository.simple;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.xdsException.ExceptionUtil;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleCreateAssetTest {
	static File RootOfAllRepositories = new File("/temp/repositories");  // /Users/bmajur/tmp/repositories
	static Repository repos;

	// Create temp folder to be the External Cache
	//	@Rule
	//	public TemporaryFolder tempFolder = new TemporaryFolder()

	@BeforeClass
	static public void initialize() throws RepositoryException {
		new Configuration(RootOfAllRepositories);
		RepositoryFactory fact = new RepositoryFactory();
		repos = fact.createRepository(
				"This is my repository",
				"Description",
				new SimpleType("simple"));
	}

	@Test
	public void createUniqueIdsTest() {
		try {
			Asset a = repos.createAsset("My Deletable Site", "This is my site", new SimpleType("site"));
			assertFalse(repos.getId().isEqual(a.getId()));
		} catch (RepositoryException e) {
			fail(ExceptionUtil.exception_details(e));
		}
	}

	@Test
	public void createNamedAssetTest() {
		String name = "mine";
		try {
			Asset a = repos.createNamedAsset("My Deletable Site", "This is my site", new SimpleType("site"), name);
			assertFalse(repos.getId().isEqual(a.getId()));
		} catch (RepositoryException e) {
			fail(ExceptionUtil.exception_details(e));
		}
	}

	@Test
	public void createBadlyNamedAssetTest() {
		String name = "repository";
		try {
			repos.createNamedAsset("My Deletable Site", "This is my site", new SimpleType("site"), name);
			fail("Should have thrown exception");
		} catch (RepositoryException e) {
			// good!
		}
	}
}
