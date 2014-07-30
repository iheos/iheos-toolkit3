package gov.nist.hit.ds.simSupport.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import gov.nist.hit.ds.actorTransaction.*;

import java.util.List;

import org.junit.Test;

public class SimulatorConfigTest {


	@Test
	public void findTest2() {
		ActorSimConfig sConfig = new ActorSimConfig(ActorTypeFactory.find("registry"));
		sConfig.
				add(new EndpointActorSimConfigElement(
						new EndpointLabel(
								TransactionTypeFactory.find("register"),
								TlsType.NOTLS,
								AsyncType.ASYNC
								),
								"http://example.com/async")
						);
		sConfig.add(new EndpointActorSimConfigElement(
								new EndpointLabel(
										TransactionTypeFactory.find("register"),
										TlsType.TLS,
										AsyncType.ASYNC
										),
										"https://example.com/async")
						);
		List<AbstractActorSimConfigElement> cEles = sConfig.findConfigs(
				new TransactionType[] { TransactionTypeFactory.find("register") },
				new TlsType[] { TlsType.TLS }, 
				new AsyncType[] { AsyncType.ASYNC});
		
		assertEquals("Number of TLS/ASYNC configs", 1, cEles.size());
				
	}

}
