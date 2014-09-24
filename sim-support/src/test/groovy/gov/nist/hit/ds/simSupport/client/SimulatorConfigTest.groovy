package gov.nist.hit.ds.simSupport.client

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.AsyncType
import gov.nist.hit.ds.actorTransaction.EndpointType
import gov.nist.hit.ds.actorTransaction.TlsType
import gov.nist.hit.ds.simSupport.client.configElementTypes.SimConfigElement
import gov.nist.hit.ds.simSupport.client.configElementTypes.EndpointSimConfigElement
import gov.nist.hit.ds.simSupport.endpoint.EndpointValue
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals

public class SimulatorConfigTest {

    static String config = '''
<ActorsTransactions>
    <transaction displayName="Stored Query" id="sq" code="sq" asyncCode="sq.as">
        <request action="urn:ihe:iti:2007:RegistryStoredQuery"/>
        <response action="urn:ihe:iti:2007:RegistryStoredQueryResponse"/>
    </transaction>
    <transaction displayName="Register" id="rb" code="rb" asyncCode="r.as">
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
    </transaction>
    <transaction displayName="Provide and Register" id="prb" code="prb" asyncCode="pr.as">
        <request action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse"/>
    </transaction>
    <transaction displayName="Retrieve" id="ret" code="ret" asyncCode="ret.as">
        <request action="urn:ihe:iti:2007:RetrieveDocumentSet"/>
        <response action="urn:ihe:iti:2007:RetrieveDocumentSetResponse"/>
        <property name="repositoryUniqueId" value=""/>
    </transaction>
    <transaction displayName="Update" id="update" code="update" asyncCode="update.as">
        <request action="urn:ihe:iti:2010:UpdateDocumentSet"/>
        <response action="urn:ihe:iti:2010:UpdateDocumentSetResponse"/>
    </transaction>
    <actor displayName="Document Registry" id="reg">
        <simFactoryClass class="gov.nist.hit.ds.registrySim.factories.DocumentRegistryActorFactory"/>
        <transaction id="rb"/>
        <transaction id="sq"/>
        <transaction id="update"/>
    </actor>
    <actor displayName="Document Repository" id="rep">
        <simFactoryClass class="gov.nist.hit.ds.registrySim.factory.DocumentRepositoryActorFactory"/>
        <transaction id="prb"/>
        <transaction id="ret"/>
    </actor>
</ActorsTransactions>
'''
    @Before
    void setup() {
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(config)
    }

    @Test
	public void findTest2() {
		ActorSimConfig sConfig = new ActorSimConfig(new ActorTransactionTypeFactory().getActorTypeIfAvailable("reg"));
		sConfig.
				add(new EndpointSimConfigElement(
						new EndpointType(
								new ActorTransactionTypeFactory().getTransactionTypeIfAvailable("rb"),
								TlsType.NOTLS,
								AsyncType.ASYNC
								),
								new EndpointValue("http://example.com/async"))
						);
		sConfig.add(new EndpointSimConfigElement(
								new EndpointType(
										new ActorTransactionTypeFactory().getTransactionTypeIfAvailable("rb"),
										TlsType.TLS,
										AsyncType.ASYNC
										),
										new EndpointValue("https://example.com/async"))
						);
		List<SimConfigElement> cEles = sConfig.findConfigs(
				[ new ActorTransactionTypeFactory().getTransactionTypeIfAvailable("rb") ],
				[  TlsType.TLS ],
				[ AsyncType.ASYNC ]);
		
		assertEquals("Number of TLS/ASYNC configs", 1, cEles.size());
				
	}

}
