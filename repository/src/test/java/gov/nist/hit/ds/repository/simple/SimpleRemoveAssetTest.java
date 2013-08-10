package gov.nist.hit.ds.repository.simple;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.xdsException.ExceptionUtil;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleRemoveAssetTest {

	static Repository repos;
	static Asset a;


	@BeforeClass
	static public void initialize() throws RepositoryException {

		RepositoryFactory fact = new RepositoryFactory();
		repos = fact.createRepository(
				"This is my repository",
				"Description",
				new SimpleType("simple"));

		a = repos.createAsset("My Deletable Site", "This is my site", new SimpleType("site"));
		a.updateContent("My Content".getBytes());
	}

	@Test
	public void removeAssetTest() {
		Asset a2 = null;
		try {
			a2 = repos.getAsset(a.getId());
		} catch (RepositoryException e) {
			fail("Failed to load newly created asset");
		}
		assertNotNull(a2);

		try {
			repos.deleteAsset(a.getId());
		} catch (RepositoryException e) {
			fail(ExceptionUtil.exception_details(e));
		}

		a2 = null;
		try {
			a2 = repos.getAsset(a.getId());
			fail("Deleted asset still is loadable");
		} catch (RepositoryException e) {
		}
		assertNull(a2);

		try {
			repos.deleteAsset(a.getId());
			fail("Delete asset should have thrown an exception");
		} catch (RepositoryException e) {
		}
	}

}
