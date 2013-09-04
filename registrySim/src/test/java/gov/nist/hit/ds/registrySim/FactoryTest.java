package gov.nist.hit.ds.registrySim;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.actorTransaction.AsyncType;
import gov.nist.hit.ds.actorTransaction.TlsType;
import gov.nist.hit.ds.initialization.installation.ExtendedPropertyManager;
import gov.nist.hit.ds.initialization.installation.InitializationFailedException;
import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.registrySim.factory.DocumentRegistryActorFactory;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.client.Simulator;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class FactoryTest {

	@Before
	public void init() throws InitializationFailedException {
		Installation.reset();
		Installation.installation().setExternalCache(new File("src/test/resources/external_cache"));
		
		File warHome = new File("src/test/resources/registry");
		Installation.installation().setWarHome(warHome);
		Installation.installation().setToolkitPropertiesFile(new File(warHome,"WEB-INF/toolkit.properties"));
		ExtendedPropertyManager.load(warHome);
	}
	
	@Test
	public void notlsSyncTest() {
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
		} catch (XdsInternalException e) {
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
		} catch (XdsInternalException e) {
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
		} catch (XdsInternalException e) {
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
