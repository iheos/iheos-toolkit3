package gov.nist.hit.ds.initialization;

import static org.junit.Assert.fail;
import gov.nist.hit.ds.initialization.installation.InitializationFailedException;
import gov.nist.hit.ds.initialization.installation.Installation;

import java.io.IOException;

import org.junit.Test;

public class BadInitializationTest {

	@Test
	public void badInitializationTest()  {
		try {
			Installation.installation().initialize();
		} catch (InitializationFailedException e) {
			return;
		}
		fail();
	}

}
