package gov.nist.hit.ds.repository.simple.search;

import static org.junit.Assert.assertFalse;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.Id;
import gov.nist.hit.ds.repository.api.PropertyKey;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleType;
import gov.nist.hit.ds.repository.simple.search.client.AssetNode;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.fail;

public class AssetNodeBuilderTest {

	static Id repId = null;
	static Repository repos = null;
	
	@BeforeClass
	static public void initialize() throws RepositoryException {
		
		repos = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL)).createNamedRepository(
				"Node builder test",
				"Description",
				new SimpleType("simpleRepos"),
				"Node builder test"
				);
		repId = repos.getId();
	}
	

	@Test
	public void parentAssetTest() throws RepositoryException {
		
		Asset a = repos.createAsset("My Site", "This is my site", new SimpleType("siteAsset"));
		a.updateContent("My Content".getBytes());
		Id assetId = a.getId();
		
		Asset a2 = repos.createAsset("My Site", "This is my site", new SimpleType("siteAsset"));
		a2.updateContent("My Content".getBytes());
		Id assetId2 = a2.getId();
		
		a.addAsset(assetId2);  // make a the parent of a2
		
		assertFalse(assetId.isEqual(assetId2));
	}

	@Test
	public void builderTest() {

		try {
			AssetNodeBuilder anb = new AssetNodeBuilder();
			List<AssetNode> tree = anb.build(repos, PropertyKey.CREATED_DATE);
			System.out.print(tree.toString());
			// Inspect the tree here though it was manually checked few times
		} catch (Exception ex) {
			fail("builder test failed.");
		}
	}
}
