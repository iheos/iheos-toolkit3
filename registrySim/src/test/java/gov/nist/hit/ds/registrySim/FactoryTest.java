package gov.nist.hit.ds.registrySim;

import gov.nist.hit.ds.actorTransaction.AsyncType;
import gov.nist.hit.ds.actorTransaction.TlsType;
import gov.nist.hit.ds.initialization.Installation;
import gov.nist.hit.ds.registrySim.factory.DocumentRegistryActorFactory;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.client.Simulator;

import java.io.File;

import org.junit.Test;

public class FactoryTest {

	@Test
	public void factoryTest() {
		Installation.installation().setExternalCache(new File("/Users/bmajur/tmp/toolkit"));
		Installation.installation().setWarHome(new File("/Users/bmajur/tomcat1/webapps/xdstools2"));
		String simId = "123";
		DocumentRegistryActorFactory fact = new DocumentRegistryActorFactory();
		Simulator sim = fact.buildNewSimulator(new SimId(simId), 
				new TlsType[]  { TlsType.NOTLS }, 
				new AsyncType[] { AsyncType.SYNC });
		System.out.println(sim);
	}
}
