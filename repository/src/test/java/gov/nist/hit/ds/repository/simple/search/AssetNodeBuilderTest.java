package gov.nist.hit.ds.repository.simple.search;

import gov.nist.hit.ds.repository.api.ArtifactId;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.PropertyKey;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleType;
import gov.nist.hit.ds.repository.simple.search.client.AssetNode;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AssetNodeBuilderTest {

	static ArtifactId repId = null;
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
		
		Asset a = repos.createAsset("parent", "This is my site", new SimpleType("siteAsset"));
		a.setProperty(PropertyKey.DISPLAY_ORDER, "1");
		a.setMimeType("text/plain");
		a.updateContent("My Content".getBytes());
		ArtifactId assetId = a.getId();
		
		Asset a2 = repos.createAsset("child", "This is my site", new SimpleType("siteAsset"));
		a2.setProperty(PropertyKey.DISPLAY_ORDER, "1");
		a2.setMimeType("text/plain");		
		a2.updateContent("My Content".getBytes());
		ArtifactId assetId2 = a2.getId();
		
		a.addChild(a2);  // make a the parent of a2

		assertFalse(assetId.isEqual(assetId2));
		
		Asset a21 = repos.createAsset("grandchild1", "This is my site", new SimpleType("siteAsset"));
		a21.setProperty(PropertyKey.DISPLAY_ORDER, "1");
		a21.setMimeType("text/plain");		
		a21.updateContent("My Content".getBytes());
		// ArtifactId assetId21 = a21.getId();

		a2.addChild(a21);  // make a the parent of a21
		
	}

	@Test
	public void builderTest() {

		try {
			AssetNodeBuilder anb = new AssetNodeBuilder();
			List<AssetNode> tree = anb.build(repos, PropertyKey.CREATED_DATE);
			System.out.println(tree.toString());
			// Inspect the tree here 
			
			assertTrue("parent".equals(tree.get(0).getDisplayName()));
			
			AssetNode grandchild = tree.get(0).getChildren().get(0).getChildren().get(0);
			assertTrue("grandchild1".equals(grandchild.getDisplayName()));
			
			System.out.println("*** chain test ***");
			AssetNode chain = anb.getParentChain(repos, grandchild, true);
			chainTest(chain);
			
		} catch (Exception ex) {
			fail("builder test failed.");
		}
	}
	
	private void chainTest(AssetNode an) {
		for (AssetNode child : an.getChildren()) {
			System.out.println(child.getLocation());
			if (child.getChildren()!=null) {
				chainTest(child);
			}
		}
	
	}
}
