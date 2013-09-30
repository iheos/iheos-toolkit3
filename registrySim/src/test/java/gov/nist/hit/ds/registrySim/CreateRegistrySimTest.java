package gov.nist.hit.ds.registrySim;

import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.AsyncType;
import gov.nist.hit.ds.actorTransaction.TlsType;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.client.Simulator;
import gov.nist.hit.ds.simSupport.factory.SimulatorFactory;
import gov.nist.hit.ds.simSupport.sim.SimDb;

import org.junit.Test;

/**
 * Creates a Registry Sim for outide testing.
 * Register transaction entpoint is:
 * 	http://localhost:8080/xdstools3/sim/123/reg/r.b
 * @author bill
 *
 */
public class CreateRegistrySimTest {

	@Test
	public void setupTest() throws Exception  {
		SimDb simDb = null;
		try {
			Installation.reset();

			//		File externalCache = new File("/Users/bill/tmp/external_cache");
			//		Installation.installation().setExternalCache(externalCache); 
			//
			Installation.installation().initialize();
			Configuration.configuration();

			// Create a new Registry simulator with non-TLS and sync inputs only
			String simIdString = "123";
			SimId simId = new SimId(simIdString);
			Simulator sim = null;
			SimulatorFactory simFactory = new SimulatorFactory().buildSimulator(simId);
			simFactory.addActorSim(ActorType.REGISTRY);
			simDb = simFactory.save();
			sim = simFactory.getSimulator();

			// verify endpoint was created
			String endpointString = sim.getEndpoint(TransactionType.REGISTER, TlsType.NOTLS, AsyncType.SYNC);
			System.out.println("Register endpoint is " + endpointString);
		} finally {
			if (simDb != null) {
				simDb.delete();
				simDb = null;
			}
		}
	}

}
