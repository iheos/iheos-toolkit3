package gov.nist.hit.ds.repository.simple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.AssetIterator;
import gov.nist.hit.ds.repository.api.Id;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.SimpleType;
import org.junit.Test;

public class SimpleAssetTest {
	

	@Test
	public void createAssetTest() throws RepositoryException {
		RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		Repository repos = fact.createRepository(
				"This is my repository",
				"Description",
				new SimpleType("site"));
		
		Asset a = repos.createAsset("My Site", "This is my site", new SimpleType("siteAsset"));
		Id assetId = a.getId();
		
		Asset a2 = repos.getAsset(assetId);
		
		Id assetId2 = a2.getId();
		
		assertTrue("created and retrieved asset id should be the same", assetId.isEqual(assetId2));
	}
	
	@Test
	public void assetPropertyTest() throws RepositoryException {
		RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		Repository repos = fact.createRepository(
				"This is my repository",
				"Description",
				new SimpleType("site"));
		
		Asset a = repos.createAsset("My Site", null, new SimpleType("siteAsset"));
		Id assetId = a.getId();		

		assertNotNull(a);
	}

	@Test
	public void contentTest() throws RepositoryException {
		RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		Repository repos = fact.createRepository(
				"This is my repository",
				"Description",
				new SimpleType("site"));
		
		String myContent = "My Content";
		
		Asset a = repos.createAsset("My Site", "This is my site", new SimpleType("siteAsset"));
		a.updateContent(myContent.getBytes());
		Id assetId = a.getId();
		
		Asset a2 = repos.getAsset(assetId);
		Id assetId2 = a2.getId();
		byte[] contentBytes = a2.getContent();
		
		assertNotNull(contentBytes);
		
		String myContent2 = new String(contentBytes);
		
		assertEquals("content does not match", myContent, myContent2);
		
		assertTrue("created and retrieved asset id should be the same", assetId.isEqual(assetId2));
	}
	
	@Test
	public void parentAssetTest() throws RepositoryException {
		RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		Repository repos = fact.createRepository(
				"This is my repository",
				"Description",
				new SimpleType("site"));
		
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
	public void getAssetTest() throws RepositoryException {
		RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		Repository repos = fact.createRepository(
				"This is my repository",
				"Description",
				new SimpleType("site"));
		
		Asset a = repos.createAsset("My Site", "This is my site", new SimpleType("siteAsset"));
		a.updateContent("My Content".getBytes());
		Id assetId = a.getId();

		Asset a2 = repos.getAsset(assetId);
		
		assertNotNull(a2);
		
		assertTrue(assetId.isEqual(a2.getId()));

	}

	@Test
	public void assetIteratorTest() throws RepositoryException {
		RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		Repository repos = fact.createRepository(
				"This is my repository",
				"Description",
				new SimpleType("site"));
		
		Asset a = repos.createAsset("My Site", "This is my site", new SimpleType("siteAsset"));
		a.updateContent("My Content".getBytes());
		
		Asset a2 = repos.createAsset("My Site", "This is my site", new SimpleType("siteAsset"));
		a2.updateContent("My Content".getBytes());

		AssetIterator ai = repos.getAssets();
		
		assertTrue(ai.hasNextAsset());
		Asset b1 = ai.nextAsset();
		assertNotNull(b1);
		
		assertTrue(ai.hasNextAsset());
		Asset b2 = ai.nextAsset();
		assertNotNull(b2);
		
		assertFalse(ai.hasNextAsset());

	}

}
