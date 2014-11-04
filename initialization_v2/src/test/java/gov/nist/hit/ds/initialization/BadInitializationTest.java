package gov.nist.hit.ds.initialization;

import gov.nist.hit.ds.initialization.installation.InitializationFailedException;
import gov.nist.hit.ds.initialization.installation.Installation;
import org.junit.Test;

import static junit.framework.Assert.fail;

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
