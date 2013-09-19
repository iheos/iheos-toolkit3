package gov.nist.hit.ds.registrySim;

import static org.junit.Assert.fail;
import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.initialization.installation.InitializationFailedException;
import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.simSupport.factory.SimulatorFactory;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class FactoryTest {

	/**
	 * This initialization is used by other tests.
	 * @throws InitializationFailedException
	 * @throws IOException 
	 * @throws RepositoryException 
	 */
	@Before
	public void init() throws InitializationFailedException, IOException, RepositoryException {
		Installation.reset();
		Installation.installation().initialize();
		Configuration.configuration();
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
