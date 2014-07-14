package gov.nist.hit.ds.repository.simple;

import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.PropertyKey;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.xdsException.ExceptionUtil;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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


    @Test
    public void testSameChildNamedAssetInDifferentTopLevelAsset() throws RepositoryException {

        Repository repos = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL)).createNamedRepository(
                "childWithSameName",
                "Description",
                new SimpleType("site"),
                "childWithSameName"
        );

        Asset p1 = repos.createNamedAsset("Parent1","",new SimpleType("siteAsset"), "Parent1");
        Asset c1 = repos.createNamedAsset("Child Display Name","",new SimpleType("siteAsset"), "Child");
        c1.setContent("child text content","text/plain");
        p1.addChild(c1);

        Asset p2 = repos.createNamedAsset("Parent2","",new SimpleType("siteAsset"), "Parent2");
        Asset c2 = repos.createNamedAsset("Child Display Name","",new SimpleType("siteAsset"), "Child");
        String c2Desc =  "Child under Parent2";
        c2.setProperty(PropertyKey.DESCRIPTION,c2Desc);
        p2.addChild(c2);

        assertTrue(p1.getChildByName("Child").getName().equals("Child"));

        assertTrue(p2.getChildByName("Child").getName().equals("Child"));


    }


    @Test
    public void testGrandChildNamedAsset() throws RepositoryException {
        Repository repos = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL)).createNamedRepository(
                "grandChildTest",
                "Description",
                new SimpleType("site"),
                "grandChildTest"
        );

        Asset p3 = repos.createNamedAsset("Parent3","",new SimpleType("siteAsset"), "Parent3");
        String c3DisplayName = "Child Display Name";
        Asset c3 = repos.createNamedAsset(c3DisplayName,"",new SimpleType("siteAsset"), "Child");
        p3.addChild(c3);


        Asset gc3 = repos.createNamedAsset("GrandChild Display Name","",new SimpleType("siteAsset"), "GrandChild");
        String gc3Desc =  "GrandChild under Parent2";
        gc3.setProperty(PropertyKey.DESCRIPTION,gc3Desc);
        c3.addChild(gc3);

        assertTrue(p3.getChildByName("Child").getProperty(PropertyKey.DISPLAY_NAME).equals(c3DisplayName));

        assertTrue(p3.getChildByName("GrandChild").getProperty(PropertyKey.DESCRIPTION).equals(gc3Desc));

        // Test repository root level getChildByName
        assertTrue(repos.getChildByName("GrandChild").getName().equals("GrandChild"));
        assertTrue(repos.getChildByName("Parent3").getName().equals("Parent3"));

        gc3.deleteAsset(); // Leaf asset delete

        p3.deleteAsset(); // Full parent folder delete

    }


    @Test
    public void testReposDelete() throws RepositoryException {
        Repository repos = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL)).createNamedRepository(
                "reposDelete",
                "Description",
                new SimpleType("site"),
                "reposDelete"
        );

        Asset p3 = repos.createNamedAsset("Parent3","",new SimpleType("siteAsset"), "Parent3");
        Asset c3 = repos.createNamedAsset("Child Display Name","",new SimpleType("siteAsset"), "Child");
        p3.addChild(c3);


        repos.delete();

    }

}