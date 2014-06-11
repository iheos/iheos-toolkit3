package gov.nist.hit.ds.repository.simple;

import gov.nist.hit.ds.repository.api.ArtifactId;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.PropertyKey;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class SimpleNamedRepositoryTest {

	static ArtifactId repId = null;
	static Repository repos;

	@BeforeClass
	static public void initialize() throws RepositoryException {
		


		repos = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL)).createNamedRepository(
				"SimpleNamedRepositoryTest", // displayName
				"Description",
				new SimpleType("simpleRepos", "no description"),
				"SimpleNamedRepositoryTest");
		repId = repos.getId();
	}
	
	
	@Test
	public void loadRepositoryTest() throws RepositoryException {
		RepositoryFactory repFact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		repFact.getRepository(repId);
	}
	
	@Test
	public void getAssetByIdTest() throws RepositoryException {
		Asset a = repos.createNamedAsset("My Site", "This is my site", new SimpleType("siteAsset"), "mysite"); // should be in root path
		ArtifactId assetId = a.getId();
		
		Asset a2 = repos.getAsset(assetId);
		
		ArtifactId assetId2 = a2.getId();
		
		assertTrue("created and retrieved asset id should be the same", assetId.isEqual(assetId2));
	}

	
	@Test
	public void findChildByAssetIdTest() throws RepositoryException {
		
		Asset a1 = repos.createNamedAsset("Parent", "This is my site", new SimpleType("siteAsset"), "My Parent Id"); 
		a1.setProperty(PropertyKey.DISPLAY_ORDER, "1");
		a1.setMimeType("text/plain");
		a1.updateContent("My Content".getBytes());
		ArtifactId assetId1 = a1.getId();
		
		Asset a2 = repos.createNamedAsset("Child", "This is my site", new SimpleType("siteAsset"), "My Child Id");
		a1.setProperty(PropertyKey.DISPLAY_ORDER, "1");
		a2.setMimeType("text/plain");		
		a2.updateContent("My Content".getBytes());
		ArtifactId assetId2 = a2.getId();
		
		a1.addAsset(a2);  // Add a child to the parent
		
		assertFalse(assetId1.isEqual(assetId2));
		
		Asset found = repos.getAsset(assetId2); // Find asset that is nested by Id 
		
		assertTrue("created and retrieved asset id should be the same", assetId2.isEqual(found.getId()));		
		
	}

    /*
    @Test
    public void findByIdDeepScan() throws RepositoryException {
        RepositoryFactory reposFact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
        ArtifactId id = new SimpleId("transactions-cap");

        Repository repos = null;
        try {
            repos = reposFact.getRepository(id);
        } catch (RepositoryException re) {
           fail(re.toString());
        }
        Asset ioHeader = repos.getAsset(new SimpleId("2014_03_14_23_50_09_89_105156956128474_io"));

        assertNotNull(ioHeader);

    } */
	
	@Test
	public void getBadIdTest() throws RepositoryException {
		
			ArtifactId badId = new SimpleId("Bad");
			Asset notFound = null;
			try {
				notFound = repos.getAsset(badId);	
				if (notFound!=null) {
					fail("should not be found");
				}
			} catch (RepositoryException ex) {
				// good
				assertTrue(ex.getMessage().startsWith((RepositoryException.ASSET_NOT_FOUND)));
			}
			  
			
		
	}
}
