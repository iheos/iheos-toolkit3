package gov.nist.hit.ds.repository.simple;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;

import org.junit.Test;

public class SimpleTextAssetTest {
	
	@Test
	public void contentTest() throws RepositoryException {
		RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		Repository repos = fact.createRepository(
				"Text content repository",
				"Description",
				new SimpleType("site"));
		
		String myContent = "My Content";
		
		Asset a = repos.createAsset("My Site", "This is my site", new SimpleType("siteAsset"));
		a.setContent(myContent, "text/plain");

		byte[] contentBytes = a.getContent();
		String mimeType = a.getMimeType();
		if (mimeType != null && mimeType.startsWith("text")) {
			String contentStr = new String(contentBytes);
			assertTrue(myContent.equals(contentStr));
		} else {
			fail("mimeType should be set and equal to test/plain");
		}
		
	}
}
