package gov.nist.hit.ds.simSupport.transaction;

import static org.junit.Assert.assertTrue;

import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.simSupport.loader.PropertyResourceFactory;

import java.io.IOException;

import gov.nist.hit.ds.toolkit.installation.InitializationFailedException;
import gov.nist.hit.ds.toolkit.installation.Installation;
import org.junit.Before;
import org.junit.Test;

public class ValidatorDefLoaderTest {

	@Before
	public void init() throws InitializationFailedException, IOException, RepositoryException {
		Installation.reset();
		Installation.installation().initialize();
		Configuration.configuration();
	}

	@Test
	public void loadTest() {
		PropertyResourceFactory l = new PropertyResourceFactory("footrans.properties");
		try {
			l.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			l.getProperties().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			assertTrue("b".equals(l.getProperties().getProperty("a")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
