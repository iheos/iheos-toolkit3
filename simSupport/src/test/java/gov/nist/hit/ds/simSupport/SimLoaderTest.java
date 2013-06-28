package gov.nist.hit.ds.simSupport;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class SimLoaderTest {

	@Test
	public void loadRegTest() {
		try {
			GenericSim gs = new SimLoader("dummy").getSimulator();
			assertTrue(gs.getClass().getName().endsWith("DummySim"));
			assertTrue("running".equals(gs.getState()));
		} catch (SimConfigurationException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} catch (InstantiationException e) {
			e.printStackTrace();
			fail();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			fail();
		}
	}
}
