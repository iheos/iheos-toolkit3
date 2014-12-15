package gov.nist.hit.ds.simulatorIntegrationTest.registrySim;

import gov.nist.hit.ds.actorTransaction.ActorTypeFactory;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.client.Simulator;
import gov.nist.hit.ds.simSupport.factory.SimulatorFactory;
import gov.nist.hit.ds.siteManagement.client.Site;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class RegistrySimBuildRestoreIT {
	@Before
	public void before() {
		Installation.reset();
		Installation.installation().initialize();
		Configuration.configuration();
	}
	
	// Build sim, save it, restore it
	@Test
	public void buildSaveRestoreTest() {
		SimulatorFactory fact = new SimulatorFactory();
		fact.initializeSimulator();
		fact.addActorSim(ActorTypeFactory.find("registry"));
		
		Simulator sim = fact.getSimulator();
		Site site = fact.getSite();
		
		SimulatorFactory.save(sim);
		SimulatorFactory.save(site);
		
		SimId simId = sim.getSimId();
		Simulator sim2 = SimulatorFactory.load(simId);
		
		assertTrue(simId.equals(sim2.getSimId()));
	}
}
