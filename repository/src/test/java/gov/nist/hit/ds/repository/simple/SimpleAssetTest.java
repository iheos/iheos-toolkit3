package gov.nist.hit.ds.repository.simple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.AssetIterator;
import gov.nist.hit.ds.repository.api.ArtifactId;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;

import org.junit.Test;

public class SimpleAssetTest {
	

	@Test
	public void createAssetTest() throws RepositoryException {
		RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		Repository repos = fact.createRepository(
				"Repos " + (int)(Math.random() * 10) ,
				"Description",
				new SimpleType("site"));
		
		Asset a = repos.createAsset("My Site", "This is my site", new SimpleType("siteAsset"));
		
		
		Asset a2 = repos.getAssetByPath(a.getPropFile());
		
		ArtifactId assetId2 = a2.getId();
		
		assertTrue("created and retrieved asset id should be the same", a.getId().isEqual(assetId2));
	}
	
	@Test
	public void assetPropertyTest() throws RepositoryException {
		RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		Repository repos = fact.createRepository(
				"Repos "+ (int)(Math.random() * 10),
				"Description",
				new SimpleType("site"));
		
		Asset a = repos.createAsset("My Site", null, new SimpleType("siteAsset"));
		// Id assetId = a.getId();		

		assertNotNull(a);
	}

	@Test
	public void contentTest() throws RepositoryException {
		RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));		
		Repository repos = fact.createNamedRepository("contentTest", "test content methods", new SimpleType("site"), "contentTest"); 
		
		String myContent = "My Content";
		
		Asset a = repos.createAsset("My Site", "This is my site", new SimpleType("siteAsset"));
		a.setMimeType("text/plain");
		a.updateContent(myContent.getBytes());
		ArtifactId assetId = a.getId();
		
		Asset a2 = repos.getAssetByPath(a.getPropFile());
		ArtifactId assetId2 = a2.getId();
		byte[] contentBytes = a2.getContent();
		
		assertNotNull(contentBytes);
		
		String myContent2 = new String(contentBytes);
		
		assertEquals("content does not match", myContent, myContent2);
		
		assertTrue("created and retrieved asset id should be the same", assetId.isEqual(assetId2));
		
		
		File oldContent = a.getContentFile();
		assertTrue(oldContent.exists());
		
		// Update mimeType on content test
		a.setMimeType("text/json");
		
		String jsonStr = 
				"{\r\n" + 
				"	\"artifacts\" : {\r\n" + 
				"		\"Request Header\" : {\r\n" + 
				"			\"id\" : \"a001ff\",\r\n" + 
				"			\"description\" : \"Request Header\",\r\n" + 
				"			\"displayOrder\" : 1,\r\n" + 
				"			\"mimeType\" : \"text/xml\"\r\n" + 
				"		},\r\n" + 
				"		\"Request Body\" : {\r\n" + 
				"			\"id\" : \"baa299\",\r\n" + 
				"			\"description\" : \"Request Body\",\r\n" + 
				"			\"displayOrder\" : 2,\r\n" + 
				"			\"mimeType\" : \"text/xml\"\r\n" + 
				"		}\r\n" + 
				"	}\r\n" + 
				"}";
		a.updateContent(jsonStr.getBytes());
		
		assertTrue(a.getContentFile().toString().endsWith("json"));
		assertTrue(a.getContentFile().exists());
		assertTrue(!oldContent.exists());
		
	}
	
	@Test
	public void parentAssetTest() throws RepositoryException {
		// RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		

		Repository repos = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL)).createNamedRepository(
				"parentAssetTest",
				"Description",
				new SimpleType("site"),
				"parentAssetTest"
				);
		
		Asset a = repos.createAsset("My Site", "This is my site", new SimpleType("siteAsset"));
		a.setMimeType("text/plain");
		a.updateContent("My Content".getBytes());
		ArtifactId assetId = a.getId();
		
		Asset a2 = repos.createAsset("My Site", "This is my site", new SimpleType("siteAsset"));
		a2.setMimeType("text/plain");
		a2.updateContent("My Content".getBytes());
		ArtifactId assetId2 = a2.getId();
		
		a.addAsset(a2);  // make a the parent of a2
		
		assertFalse(assetId.isEqual(assetId2));
		
		Asset a3 = repos.createAsset("My Site", "This is my third site", new SimpleType("siteAsset"));
		a3.setMimeType("text/plain");
		a3.updateContent("My Third Content".getBytes());
		ArtifactId assetId3 = a3.getId();
		
		a2.addAsset(a3);  // make a the parent of a3
		
		assertFalse(assetId3.isEqual(assetId2));

	}
	
	@Test
	public void getAssetTest() throws RepositoryException {
		RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		Repository repos = fact.createRepository(
				"Repos " 
		+ (int)(Math.random() * 10),
				"Description",
				new SimpleType("site"));
		
		Asset a = repos.createAsset("My Site", "This is my site", new SimpleType("siteAsset"));
		a.updateContent("My Content".getBytes());
		ArtifactId assetId = a.getId();

		Asset a2 = repos.getAssetByPath(a.getPropFile());
		
		assertNotNull(a2);
		
		assertTrue(assetId.isEqual(a2.getId()));

	}

	@Test
	public void assetIteratorTest() throws RepositoryException {
		RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		Repository repos = fact.createRepository(
				"Repos " + (int)(Math.random() * 10),
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
