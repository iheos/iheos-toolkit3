package gov.nist.hit.ds.toolkit;

import static org.junit.Assert.fail;

import java.io.IOException;

import gov.nist.hit.ds.toolkit.installation.InitializationFailedException;
import gov.nist.hit.ds.toolkit.installation.Installation;
import org.junit.Test;

public class BadInitializationTest {

	@Test
	public void badInitializationTest()  {
		try {
			Installation.installation().initialize();
		} catch (InitializationFailedException e) {
			return;
		} catch (IOException e) {
			fail();
		}
		fail();
	}

}
