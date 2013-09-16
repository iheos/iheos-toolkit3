package gov.nist.hit.ds.registrySim;

import static org.junit.Assert.fail;
import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.initialization.installation.ExtendedPropertyManager;
import gov.nist.hit.ds.initialization.installation.InitializationFailedException;
import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.simSupport.factory.SimulatorFactory;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class FactoryTest {

	/**
	 * This initialization is used by other tests.
	 * @throws InitializationFailedException
	 */
	@Before
	public void init() throws InitializationFailedException {
		Installation.reset();
		Installation.installation().setExternalCache(new File("src/test/resources/external_cache"));

		File warHome = new File("src/test/resources/registry");
		Installation.installation().setWarHome(warHome);
		Installation.installation().setToolkitPropertiesFile(new File(warHome,"WEB-INF/toolkit.properties"));
		ExtendedPropertyManager.load(warHome);
		
		try {
			Configuration.configuration();
		} catch (RepositoryException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void buildTest() {
		try {
			SimulatorFactory simFactory = new SimulatorFactory().buildSimulator();
			simFactory.addActorSim(ActorType.REGISTRY);
			simFactory.save();
			simFactory.getSimulator();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} 
	}

}
