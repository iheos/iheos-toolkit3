package gov.nist.hit.ds.simSupport.datatypes.client;

import static org.junit.Assert.*;
import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.AsyncType;
import gov.nist.hit.ds.actorTransaction.EndpointLabel;
import gov.nist.hit.ds.actorTransaction.TlsType;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.simSupport.client.ParamType;
import gov.nist.hit.ds.simSupport.client.ActorSimConfig;
import gov.nist.hit.ds.simSupport.client.ActorSimConfigElement;

import java.util.List;

import org.junit.Test;

public class SimulatorConfigTest {

	@Test
	public void findTest() {
		ActorSimConfig sConfig = new ActorSimConfig(ActorType.REGISTRY).
				add(new ActorSimConfigElement(
						TransactionType.REGISTER.getName(),
						ParamType.ENDPOINT,
						new EndpointLabel(
								TransactionType.REGISTER,
								TlsType.TLS,
								AsyncType.ASYNC
								).get())
						);
		List<ActorSimConfigElement> cEles = sConfig.findConfigs(
				new TransactionType[] { TransactionType.REGISTER }, 
				new TlsType[] { TlsType.TLS }, 
				new AsyncType[] { AsyncType.ASYNC});
		
		assertTrue(cEles.size() == 1);
				
	}

	@Test
	public void findTest2() {
		ActorSimConfig sConfig = new ActorSimConfig(ActorType.REGISTRY).
				add(new ActorSimConfigElement(
						TransactionType.REGISTER.getName(),
						ParamType.ENDPOINT,
						new EndpointLabel(
								TransactionType.REGISTER,
								TlsType.NOTLS,
								AsyncType.ASYNC
								).get())
						).add(new ActorSimConfigElement(
								TransactionType.REGISTER.getName(),
								ParamType.ENDPOINT,
								new EndpointLabel(
										TransactionType.REGISTER,
										TlsType.TLS,
										AsyncType.ASYNC
										).get())
						);
		List<ActorSimConfigElement> cEles = sConfig.findConfigs(
				new TransactionType[] { TransactionType.REGISTER }, 
				new TlsType[] { TlsType.TLS }, 
				new AsyncType[] { AsyncType.ASYNC});
		
		assertTrue(cEles.size() == 1);
				
	}

}
