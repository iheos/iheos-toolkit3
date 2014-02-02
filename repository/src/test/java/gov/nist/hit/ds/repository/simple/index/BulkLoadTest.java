package gov.nist.hit.ds.repository.simple.index;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.ArtifactId;
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

	static ArtifactId repId = null;
	static Repository repos = null;

	static final int ASSETS_TO_TEST = 100;
	
	
	/**
	 * 
	 * @throws RepositoryException
	 */
	
	@BeforeClass
	static public void initialize() throws RepositoryException {
	
		repos = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL)).createNamedRepository(
				"Bulk load test  FLAT" + ASSETS_TO_TEST,
				"Description",
				new SimpleType("simpleRepos"),
				"Bulk-FLAT" + ASSETS_TO_TEST
				);
		repId = repos.getId();
		
		ArtifactId testId = null;
		for (int cx=0; cx < ASSETS_TO_TEST; cx++) {

			Asset a = repos.createAsset("asset-"+cx, "This is my site", new SimpleType("siteAsset"));
			a.setProperty(PropertyKey.DISPLAY_ORDER, ""+cx);
			a.setMimeType("text/plain");
			a.updateContent("My Content".getBytes());
			
			assertNotNull(a.getId());
			
			if (cx==0) {
				testId = a.getId();		
			}
		}	
		
		Asset found = repos.getAsset(testId); // Find asset that is nested by Id 
		
		assertTrue("created and retrieved asset id should be the same", testId.isEqual(found.getId()));
		
		
	}
	
	@Test
	public void bulk02CountTest() {

		try {
			AssetNodeBuilder anb = new AssetNodeBuilder();
			List<AssetNode> tree = anb.build(repos, PropertyKey.CREATED_DATE);
			System.out.println(tree.toString());
			// Inspect the tree here 
			
			assertTrue(tree.size()==ASSETS_TO_TEST);			
			
		} catch (Exception ex) {
			fail("builder test failed.");
		}
	}

		
}
