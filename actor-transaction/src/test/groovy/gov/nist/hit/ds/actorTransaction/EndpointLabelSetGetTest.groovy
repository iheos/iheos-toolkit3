package gov.nist.hit.ds.actorTransaction;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EndpointLabelSetGetTest {

	TransactionType register;
    ActorType actorType;

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
        <property name="repositoryUniqueId" value="1.2.3.4"/>
    </actor>
</ActorsTransactions>
'''
    @Before
    void setup() {
        ActorTransactionTypeFactory.clear()
        new ActorTransactionTypeFactory().load(config)
        actorType = new ActorTransactionTypeFactory().getActorTypeIfAvailable("reg");
        register = new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb')
    }

	@Test
	public void setGetTest()  {
		EndpointLabel label = new EndpointLabel(actorType, "REGISTER");

		assertEquals("", register, label.getTransType());
		
		label.setTransType(register);
		assertEquals("", register, label.getTransType());
		
		label.setAsync(true);
		assertEquals("", AsyncType.ASYNC, label.getAsyncType());
	
		label.setAsync(false);
		assertEquals("", AsyncType.SYNC, label.getAsyncType());
	
		label.setTls(true);
		assertEquals("", TlsType.TLS, label.getTlsType());

		label.setTls(false);
		assertEquals("", TlsType.NOTLS, label.getTlsType());
}
	
}
