package gov.nist.toolkit.utilities.initialization;

import gov.nist.hit.ds.utilities.initialization.installation.InitializationFailedException;
import gov.nist.hit.ds.utilities.initialization.installation.Installation;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.fail;

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
