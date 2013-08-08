package gov.nist.hit.ds.registrySim;

import static org.junit.Assert.*;
import gov.nist.hit.ds.actorTransaction.AsyncType;
import gov.nist.hit.ds.actorTransaction.TlsType;
import gov.nist.hit.ds.initialization.Installation;
import gov.nist.hit.ds.registrySim.factory.DocumentRegistryActorFactory;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.client.Simulator;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class FactoryTest {

	@Test
	public void notlsSyncTest() {
		Installation.installation().setExternalCache(new File("src/test/resources/external_cache"));
		Installation.installation().setWarHome(new File("src/test/resources/registry"));
		String simId = "123";
		DocumentRegistryActorFactory fact = new DocumentRegistryActorFactory();
		Simulator sim = null;
		try {
			sim = fact.buildNewSimulator(new SimId(simId), 
					new TlsType[]  { TlsType.NOTLS }, 
					new AsyncType[] { AsyncType.SYNC });
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		System.out.println(sim);
		
		assertFalse(sim.getConfig(0).hasExpired());
		
		// 3 endpoints, name, metadataupdate, extrametadata, codes filename
		assertTrue(sim.getConfig(0).getEditable().size() == 7);
		// creationTime
		assertTrue(sim.getConfig(0).getFixed().size() == 1);
	}


	@Test
	public void tlsNotlsSyncTest() {
		Installation.installation().setExternalCache(new File("src/test/resources/external_cache"));
		Installation.installation().setWarHome(new File("src/test/resources/registry"));
		String simId = "123";
		DocumentRegistryActorFactory fact = new DocumentRegistryActorFactory();
		Simulator sim = null;
		try {
			sim = fact.buildNewSimulator(new SimId(simId), 
					new TlsType[]  { TlsType.TLS, TlsType.NOTLS }, 
					new AsyncType[] { AsyncType.SYNC });
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		System.out.println(sim);
		
		assertFalse(sim.getConfig(0).hasExpired());
		
		// 6 endpoints, name, metadataupdate, extrametadata, codes filename
		assertTrue(sim.getConfig(0).getEditable().size() == 10);
		// creationTime
		assertTrue(sim.getConfig(0).getFixed().size() == 1);
	}

	@Test
	public void tlsNotlsSyncNoSyncTest() {
		Installation.installation().setExternalCache(new File("src/test/resources/external_cache"));
		Installation.installation().setWarHome(new File("src/test/resources/registry"));
		String simId = "123";
		DocumentRegistryActorFactory fact = new DocumentRegistryActorFactory();
		Simulator sim = null;
		try {
			sim = fact.buildNewSimulator(new SimId(simId), 
					new TlsType[]  { TlsType.TLS, TlsType.NOTLS }, 
					new AsyncType[] { AsyncType.ASYNC, AsyncType.SYNC });
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		System.out.println(sim);
		
		assertFalse(sim.getConfig(0).hasExpired());
		
		// 12 endpoints, name, metadataupdate, extrametadata, codes filename
		assertTrue(sim.getConfig(0).getEditable().size() == 16);
		// creationTime
		assertTrue(sim.getConfig(0).getFixed().size() == 1);
	}
}
