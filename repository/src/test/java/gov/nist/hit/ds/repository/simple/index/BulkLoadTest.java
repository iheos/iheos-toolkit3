package gov.nist.hit.ds.repository.simple.index;

import gov.nist.hit.ds.repository.api.ArtifactId;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.repository.shared.PropertyKey;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleType;
import gov.nist.hit.ds.repository.simple.search.AssetNodeBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BulkLoadTest {

	static ArtifactId repId = null;
	static Repository repos = null;

	static final int ASSETS_TO_CREATE = 10 * 5; // Multiple of 10
	

	private class PopThread implements Runnable {
		private int start=1;
		private int assetsToCreate = 10;
		private Repository reposT = null;
		
		public PopThread(int start, int assetsToCreate)  {
			this.start = start;
			this.assetsToCreate = assetsToCreate;
		}
		
		@Override
		public void run() {
			ArtifactId testId = null;
			try {
				
				reposT = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL)).createNamedRepository(
						"Bulk load test  FLAT" + ASSETS_TO_CREATE,
						"Description",
						new SimpleType("simpleRepos"),
						"Bulk_FLAT_" + start + "_" + assetsToCreate
						);
				repId = reposT.getId();		
				
				int end = start + assetsToCreate;
				for (int cx=start; cx < end; cx++) {
					Asset a = reposT.createAsset("asset-"+cx, "This is my site", new SimpleType("siteAsset"));
					a.setProperty(PropertyKey.DISPLAY_ORDER, ""+cx);
					a.setMimeType("text/plain");
					a.updateContent("My Content".getBytes());
					
					assertNotNull(a.getId());
					
					if (cx==start) {
						testId = a.getId();		
					}				
				}
				
				Asset found = reposT.getAsset(testId); // Find asset that is nested by Id 
				
				assertTrue("created and retrieved asset id should be the same", testId.isEqual(found.getId()));
				
				bulk02CountTest(reposT, assetsToCreate);
				
			} catch (Exception ex) {
				fail(ex.toString());
			}


		}
	}
	
	/**
	 * 
	 * @throws RepositoryException
	 */
	
	@BeforeClass
	static public void initialize() throws RepositoryException {
	
		repos = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL)).createNamedRepository(
				"Bulk load test  FLAT" + ASSETS_TO_CREATE,
				"Description",
				new SimpleType("simpleRepos"),
				"Bulk-FLAT" + ASSETS_TO_CREATE
				);
		repId = repos.getId();		
		
		popRepos2();
		bulk02CountTest(repos,ASSETS_TO_CREATE);
		
	}
	
	@Test
	public void popTest() {
		ExecutorService svc = Executors.newCachedThreadPool();
		
		for (int cx=0; cx<ASSETS_TO_CREATE; cx+=10) {
			svc.execute(new Thread(new PopThread(cx,10)));			
		}
	
		svc.shutdown();
		boolean svcStatus = false;
		try {
			svcStatus = svc.awaitTermination(15,TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			fail("svc termination failed!");
		} 
		
		System.out.print("indexer svc exit status: " + svcStatus);

	}
	
	private static void popRepos2() throws RepositoryException {
		ArtifactId testId = null;
		for (int cx=0; cx < ASSETS_TO_CREATE; cx++) {

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
	
	
	private static void bulk02CountTest(Repository repos, int ct) {

		try {
			AssetNodeBuilder anb = new AssetNodeBuilder();
			List<AssetNode> tree = anb.build(repos, PropertyKey.CREATED_DATE);
			System.out.println(tree.toString());
			// Inspect the tree here 
			
			assertTrue(tree.size()==ct);			
			
		} catch (Exception ex) {
			fail("builder test failed.");
		}
	}

		
}
