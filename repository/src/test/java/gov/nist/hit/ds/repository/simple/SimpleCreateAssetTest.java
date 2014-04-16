package gov.nist.hit.ds.repository.simple;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.utilities.xdsException.ExceptionUtil;

import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleCreateAssetTest {
	static Repository repos;


	@BeforeClass
	static public void initialize() throws RepositoryException {
		
		RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		repos = fact.createRepository(
				"Repos "+ (int)(Math.random() * 10),
				"Description",
				new SimpleType("simpleRepos"));
	}

	@Test
	public void createUniqueIdsTest() {
		try {
			Asset a = repos.createAsset("My Deletable Site", "This is my site", new SimpleType("siteAsset"));
			assertFalse(repos.getId().isEqual(a.getId()));
		} catch (RepositoryException e) {
			fail(ExceptionUtil.exception_details(e));
		}
	}

	@Test
	public void createNamedAssetTest() {
		String name = "mine";
		try {
			Asset a = repos.createNamedAsset("My Deletable Site", "This is my site", new SimpleType("siteAsset"), name);
			assertFalse(repos.getId().isEqual(a.getId()));
		} catch (RepositoryException e) {
			fail(ExceptionUtil.exception_details(e));
		}
	}

	@Test
	public void createBadlyNamedAssetTest() {
		String name = "repository";
		try {
			repos.createNamedAsset("My Deletable Site", "This is my site", new SimpleType("siteAsset"), name);
			fail("Should have thrown exception");
		} catch (RepositoryException e) {
			// good!
		}
	}
	
	@Test
	public void createNamedAssetTestDuplicate() {
		String name = "mine";
		try {
			repos.createNamedAsset("My Deletable Site", "This is my site", new SimpleType("siteAsset"), name);
			//fail("Should have thrown exception");
		} catch (RepositoryException e) {
			e.toString();
			fail("Should not have thrown exception because the of automatic name id increment");
		}
	}
	
	@Test
	public void createSafeNamedAssetTest() {
		String name = "My Deletable $ite!";
		try {
			repos.createNamedAsset(name, "This is my site", new SimpleType("siteAsset"), name);
		} catch (RepositoryException e) {
			e.toString();
			fail("Should NOT have thrown exception becuase the name should be have been converted to a safe name");
		}
	}

}
