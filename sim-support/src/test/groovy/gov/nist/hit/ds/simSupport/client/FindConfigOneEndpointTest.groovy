package gov.nist.hit.ds.simSupport.client

import gov.nist.hit.ds.actorTransaction.*
import gov.nist.hit.ds.simSupport.client.configElementTypes.TransactionSimConfigElement
import gov.nist.hit.ds.simSupport.client.configElementTypes.SimConfigElement
import gov.nist.hit.ds.simSupport.client.configElementTypes.TransactionSimConfigElement
import gov.nist.hit.ds.simSupport.endpoint.EndpointValue
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals

public class FindConfigOneEndpointTest {
	ActorSimConfig sConfig;
    ActorType actorType;
	List<SimConfigElement> cEles;


    static String config = '''
<ActorsTransactions>
    <transaction name="Stored Query" id="sq" code="sq" asyncCode="sq.as">
        <request action="urn:ihe:iti:2007:RegistryStoredQuery"/>
        <response action="urn:ihe:iti:2007:RegistryStoredQueryResponse"/>
        <implClass value="unused"/>
    </transaction>
    <transaction name="Register" id="rb" code="rb" asyncCode="r.as">
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
        <implClass value="unused"/>
    </transaction>
    <transaction name="Provide and Register" id="prb" code="prb" asyncCode="pr.as">
        <request action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse"/>
        <implClass value="unused"/>
    </transaction>
    <transaction name="Update" id="update" code="update" asyncCode="update.as">
        <request action="urn:ihe:iti:2010:UpdateDocumentSet"/>
        <response action="urn:ihe:iti:2010:UpdateDocumentSetResponse"/>
        <implClass value="unused"/>
    </transaction>
    <actor name="Document Registry" id="reg">
        <simFactoryClass class="gov.nist.hit.ds.registrySim.factories.DocumentRegistryActorFactory"/>
        <transaction id="rb"/>
        <transaction id="sq"/>
        <transaction id="update"/>
    </actor>
    <actor name="Document Repository" id="rep">
        <simFactoryClass class="gov.nist.hit.ds.registrySim.factory.DocumentRepositoryActorFactory"/>
        <transaction id="prb"/>
        <property displayName="repositoryUniqueId" value="1.2.3.4"/>
    </actor>
</ActorsTransactions>
'''
    @Before
    void setup() {
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(config)
        startUp()
    }

	public void startUp() {
		sConfig = new ActorSimConfig(new ActorTransactionTypeFactory().getActorTypeIfAvailable("reg")).
				add(
						new TransactionSimConfigElement(
								new EndpointType(
										new ActorTransactionTypeFactory().getTransactionTypeIfAvailable("rb"),
										TlsType.TLS,
										AsyncType.ASYNC
										),
										new EndpointValue("https://example.com/async")
								)
						);
        actorType = new ActorTransactionTypeFactory().getActorTypeIfAvailable("reg");
        cEles = sConfig.findConfigs(
                [ actorType.find("rb") ],
                [ TlsType.TLS ],
                [ AsyncType.ASYNC, AsyncType.SYNC ]);
	}
	
	@Test
	public void verifyTestdata() {
		assertEquals(sConfig.getActorType(), new ActorTransactionTypeFactory().getActorTypeIfAvailable("reg"));
		assertEquals("ActorSimConfig size", 1, sConfig.elements.size());
	}
	
	@Test
	public void anyAnyTest() {
		cEles = sConfig.findConfigs(
                [ new ActorTransactionTypeFactory().getTransactionTypeIfAvailable("rb") ],
				[ TlsType.TLS, TlsType.NOTLS ],
				[ AsyncType.ASYNC, AsyncType.SYNC ]);
		assertEquals("findConfig any TLS any ASYNC", 1, cEles.size());
	}

	@Test
	public void tlsAnyTest() {
		cEles = sConfig.findConfigs(
                [ new ActorTransactionTypeFactory().getTransactionTypeIfAvailable("rb") ],
				[ TlsType.TLS ],
				[ AsyncType.ASYNC, AsyncType.SYNC ]);
		assertEquals("findConfig any ASYNC", 1, cEles.size());
	}
	
	@Test
	public void anyAsyncTest() {
		cEles = sConfig.findConfigs(
                [ new ActorTransactionTypeFactory().getTransactionTypeIfAvailable("rb") ],
				[ TlsType.TLS, TlsType.NOTLS ],
				[ AsyncType.ASYNC ]);
		assertEquals("findConfig any TLS size", 1, cEles.size());
	}
	
	@Test
	public void tlsAsyncTest() {
		cEles = sConfig.findConfigs(
                [ new ActorTransactionTypeFactory().getTransactionTypeIfAvailable("rb") ],
				[ TlsType.TLS ],
				[ AsyncType.ASYNC ]);
		assertEquals("findConfig size", 1, cEles.size());
	}
	
	@Test
	public void noTlsAnyTest() {
		cEles = sConfig.findConfigs(
                [ new ActorTransactionTypeFactory().getTransactionTypeIfAvailable("rb") ],
				[ TlsType.NOTLS ],
				[ AsyncType.ASYNC, AsyncType.SYNC ]);
		assertEquals("findConfig any no TLS any ASYNC size", 0, cEles.size());
	}
	
	@Test
	public void anySyncTest() {
		cEles = sConfig.findConfigs(
                [ new ActorTransactionTypeFactory().getTransactionTypeIfAvailable("rb") ],
				[ TlsType.TLS, TlsType.NOTLS ],
				[ AsyncType.SYNC ]);
		assertEquals("findConfig any TLS SYNC size", 0, cEles.size());
	}

}
