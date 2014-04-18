package gov.nist.hit.ds.simSupport.client;

import static org.junit.Assert.assertEquals;

import gov.nist.hit.ds.actorTransaction.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FindConfigOneEndpointTest {
	ActorSimConfig sConfig;
    ActorType actorType = ActorTypeFactory.find("registry");
	List<AbstractActorSimConfigElement> cEles = sConfig.findConfigs(
            new TransactionType[] { actorType.find("register") },
            new TlsType[] { TlsType.TLS},
            new AsyncType[] { AsyncType.ASYNC, AsyncType.SYNC});
	
	@Before
	public void startUp() {
		sConfig = new ActorSimConfig(ActorTypeFactory.find("registry")).
				add(
						new EndpointActorSimConfigElement(
								new EndpointLabel(
										TransactionTypeFactory.find("register"),
										TlsType.TLS,
										AsyncType.ASYNC
										),
										"https://example.com/async"
								
								)
						);
	}
	
	@Test
	public void verifyTestdata() {
		assertEquals(sConfig.getActorType(), ActorTypeFactory.find("registry"));
		assertEquals("ActorSimConfig size", 1, sConfig.elements.size());
	}
	
	@Test
	public void anyAnyTest() {
		cEles = sConfig.findConfigs(
				new TransactionType[] { TransactionTypeFactory.find("register") },
				new TlsType[] { TlsType.TLS, TlsType.NOTLS}, 
				new AsyncType[] { AsyncType.ASYNC, AsyncType.SYNC});
		assertEquals("findConfig any TLS any ASYNC size", 1, cEles.size());
	}

	@Test
	public void tlsAnyTest() {
		cEles = sConfig.findConfigs(
				new TransactionType[] { TransactionTypeFactory.find("register") },
				new TlsType[] { TlsType.TLS}, 
				new AsyncType[] { AsyncType.ASYNC, AsyncType.SYNC});
		assertEquals("findConfig any ASYNC size", 1, cEles.size());
	}
	
	@Test
	public void anyAsyncTest() {
		cEles = sConfig.findConfigs(
				new TransactionType[] { TransactionTypeFactory.find("register") },
				new TlsType[] { TlsType.TLS, TlsType.NOTLS}, 
				new AsyncType[] { AsyncType.ASYNC});
		assertEquals("findConfig any TLS size", 1, cEles.size());
	}
	
	@Test
	public void tlsAsyncTest() {
		cEles = sConfig.findConfigs(
				new TransactionType[] { TransactionTypeFactory.find("register") },
				new TlsType[] { TlsType.TLS }, 
				new AsyncType[] { AsyncType.ASYNC});
		assertEquals("findConfig size", 1, cEles.size());
	}
	
	@Test
	public void noTlsAnyTest() {
		cEles = sConfig.findConfigs(
				new TransactionType[] { TransactionTypeFactory.find("register") },
				new TlsType[] { TlsType.NOTLS}, 
				new AsyncType[] { AsyncType.ASYNC, AsyncType.SYNC});
		assertEquals("findConfig any no TLS any ASYNC size", 0, cEles.size());
	}
	
	@Test
	public void anySyncTest() {
		cEles = sConfig.findConfigs(
				new TransactionType[] { TransactionTypeFactory.find("register") },
				new TlsType[] { TlsType.TLS, TlsType.NOTLS}, 
				new AsyncType[] { AsyncType.SYNC});
		assertEquals("findConfig any TLS SYNC size", 0, cEles.size());
	}

}
