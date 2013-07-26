package gov.nist.hit.ds.repository.simple;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.Id;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;

import org.junit.Test;

public class SimpleTextAssetTest {
	
	@Test
	public void contentTest() throws RepositoryException {
		RepositoryFactory fact = new RepositoryFactory();
		Repository repos = fact.createRepository(
				"This is my repository",
				"Description",
				new SimpleType("site"));
		
		String myContent = "My Content";
		
		Asset a = repos.createAsset("My Site", "This is my site", new SimpleType("site"));
		a.updateContent(myContent, "text/plain");
		Id assetId = a.getId();
		
		Asset a2 = repos.getAsset(assetId);
		// Id assetId2 = a2.getId();
		byte[] contentBytes = a2.getContent();
		String mimeType = a2.getMimeType();
		if (mimeType != null && mimeType.startsWith("text")) {
			String contentStr = new String(contentBytes);
			assertTrue(myContent.equals(contentStr));
		} else {
			fail("mimeType should be set and equal to test/plain");
		}
		
	}
}
