package gov.nist.hit.ds.simSupport.datatypes.client;

import static org.junit.Assert.*;
import gov.nist.hit.ds.actorTransaction.AsyncType;
import gov.nist.hit.ds.actorTransaction.EndpointLabel;
import gov.nist.hit.ds.actorTransaction.TlsType;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.simSupport.client.ParamType;
import gov.nist.hit.ds.simSupport.client.SimulatorConfig;
import gov.nist.hit.ds.simSupport.client.SimulatorConfigElement;

import java.util.List;

import org.junit.Test;

public class SimulatorConfigTest {

	@Test
	public void findTest() {
		SimulatorConfig sConfig = new SimulatorConfig().
				add(new SimulatorConfigElement(
						TransactionType.REGISTER.getName(),
						ParamType.ENDPOINT,
						new EndpointLabel(
								TransactionType.REGISTER,
								TlsType.TLS,
								AsyncType.ASYNC
								).get())
						);
		List<SimulatorConfigElement> cEles = sConfig.findConfigs(
				new TransactionType[] { TransactionType.REGISTER }, 
				new TlsType[] { TlsType.TLS }, 
				new AsyncType[] { AsyncType.ASYNC});
		
		assertTrue(cEles.size() == 1);
				
	}

	@Test
	public void findTest2() {
		SimulatorConfig sConfig = new SimulatorConfig().
				add(new SimulatorConfigElement(
						TransactionType.REGISTER.getName(),
						ParamType.ENDPOINT,
						new EndpointLabel(
								TransactionType.REGISTER,
								TlsType.NOTLS,
								AsyncType.ASYNC
								).get())
						).add(new SimulatorConfigElement(
								TransactionType.REGISTER.getName(),
								ParamType.ENDPOINT,
								new EndpointLabel(
										TransactionType.REGISTER,
										TlsType.TLS,
										AsyncType.ASYNC
										).get())
						);
		List<SimulatorConfigElement> cEles = sConfig.findConfigs(
				new TransactionType[] { TransactionType.REGISTER }, 
				new TlsType[] { TlsType.TLS }, 
				new AsyncType[] { AsyncType.ASYNC});
		
		assertTrue(cEles.size() == 1);
				
	}

}
