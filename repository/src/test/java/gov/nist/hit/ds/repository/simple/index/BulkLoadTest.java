package gov.nist.hit.ds.repository.simple.index;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.Id;
import gov.nist.hit.ds.repository.api.PropertyKey;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleType;
import gov.nist.hit.ds.repository.simple.search.AssetNodeBuilder;
import gov.nist.hit.ds.repository.simple.search.client.AssetNode;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class BulkLoadTest {

	static Id repId = null;
	static Repository repos = null;

	static final int ASSETS_TO_TEST = 100;
	
	@BeforeClass
	static public void initialize() throws RepositoryException {
	
		repos = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL)).createNamedRepository(
				"Bulk load test  FLAT-" + ASSETS_TO_TEST,
				"Description",
				new SimpleType("simpleRepos"),
				"Bulk-FLAT1000"
				);
		repId = repos.getId();
	}
	
	
	@Test
	public void bulk1CreateTest() throws RepositoryException {
		
		
		for (int cx=0; cx < ASSETS_TO_TEST; cx++) {
			Asset a = repos.createAsset("asset-"+cx, "This is my site", new SimpleType("siteAsset"));
			a.setProperty(PropertyKey.DISPLAY_ORDER, ""+cx);
			a.setMimeType("text/plain");
			a.updateContent("My Content".getBytes());
			
			assertNotNull(a.getId());
						
		}				
	}
	
	@Test
	public void bulk2CountTest() {

		try {
			AssetNodeBuilder anb = new AssetNodeBuilder();
			List<AssetNode> tree = anb.build(repos, PropertyKey.CREATED_DATE);
			System.out.print(tree.toString());
			// Inspect the tree here 
			
			assertTrue(tree.size()==ASSETS_TO_TEST);
			
			
		} catch (Exception ex) {
			fail("builder test failed.");
		}
	}

	@Test
	public void bulk3CountTest() {

		try {
			AssetNodeBuilder anb = new AssetNodeBuilder();
			List<AssetNode> tree = anb.build(repos, PropertyKey.CREATED_DATE);
			System.out.print(tree.toString());
			// Inspect the tree here 
			
			assertTrue(tree.size()==ASSETS_TO_TEST);
			
			
		} catch (Exception ex) {
			fail("builder test failed.");
		}
	}
}
