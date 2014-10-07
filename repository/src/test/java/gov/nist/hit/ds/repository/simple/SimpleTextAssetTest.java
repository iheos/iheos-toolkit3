package gov.nist.hit.ds.repository.simple;

import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SimpleTextAssetTest {
	
	@Test
	public void contentTest() throws RepositoryException {
		RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		Repository repos = fact.createNamedRepository(
                "ContentTest",
                "Description",
                new SimpleType("site"),
                "ContentTest");
		
		String testString = "My Content";
		
		Asset a = repos.createAsset("My Site", "This is my site", new SimpleType("siteAsset"));
        
		a.setContent(testString.getBytes(), "text/plain");

		byte[] contentBytes = a.getContent();
		String mimeType = a.getMimeType();
		if (mimeType != null && mimeType.equals("text/plain")) {
			String contentStr = new String(contentBytes);
			assertTrue(testString.equals(contentStr));
		} else {
			fail("mimeType should be set and equal to test/plain");
		}

        testString = "new updated content";
        a.updateContent(testString.getBytes());

        if (!testString.equals(new String(a.getContent()))) {
            fail("update content failed");
        }

        a.setMimeType(null); // Will not set the property
        /*
        try {
            a.setMimeType(null);
            fail("Should not be able to set a null mimeType");
        } catch (RepositoryException re) {
            // good
        }
        */


        File oldContentFile = a.getContentFile();

        a.setMimeType("text/json");

        if(oldContentFile!=null && oldContentFile.exists()) {
            fail("file was not removed when mimeType changed");
        }

        testString = "final text update by setContent";
        a.setContent(testString.getBytes(), "text/plain");

        if (!testString.equals(new String(a.getContent()))) {
            fail("setContent failed");
        }


        testString = "{}";
        a.setContent(testString.getBytes(), "text/json");

        if (!testString.equals(new String(a.getContent()))) {
            fail("setContent text/json failed");
        }

    }
}
