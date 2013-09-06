package gov.nist.hit.ds.simSupport.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.AsyncType;
import gov.nist.hit.ds.actorTransaction.EndpointLabel;
import gov.nist.hit.ds.actorTransaction.TlsType;
import gov.nist.hit.ds.actorTransaction.TransactionType;

import java.util.List;

import org.junit.Test;

public class SimulatorConfigTest {


	@Test
	public void findTest2() {
		ActorSimConfig sConfig = new ActorSimConfig(ActorType.REGISTRY);
		sConfig.
				add(new EndpointActorSimConfigElement(
						new EndpointLabel(
								TransactionType.REGISTER,
								TlsType.NOTLS,
								AsyncType.ASYNC
								),
								"http://example.com/async")
						);
		sConfig.add(new EndpointActorSimConfigElement(
								new EndpointLabel(
										TransactionType.REGISTER,
										TlsType.TLS,
										AsyncType.ASYNC
										),
										"https://example.com/async")
						);
		List<AbstractActorSimConfigElement> cEles = sConfig.findConfigs(
				new TransactionType[] { TransactionType.REGISTER }, 
				new TlsType[] { TlsType.TLS }, 
				new AsyncType[] { AsyncType.ASYNC});
		
		assertEquals("Number of TLS/ASYNC configs", 1, cEles.size());
				
	}

}
