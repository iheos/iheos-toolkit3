package gov.nist.hit.ds.actorTransaction;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EndpointLabelSetGetTest {

	TransactionType register;
    ActorType actorType;

    static String config = '''
<ActorsTransactions>
    <transaction name="Stored Query" code="sq" asyncCode="sq.as" id="sq">
        <request action="urn:ihe:iti:2007:RegistryStoredQuery"/>
        <response action="urn:ihe:iti:2007:RegistryStoredQueryResponse"/>
        <implClass value="unused"/>
    </transaction>
    <transaction name="Register" code="rb" asyncCode="r.as" id="rb">
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
        <implClass value="unused"/>
    </transaction>
    <transaction name="Provide and Register" code="prb" asyncCode="pr.as" id="prb">
        <request action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse"/>
        <implClass value="unused"/>
    </transaction>
    <transaction name="Update" code="update" asyncCode="update.as" id="update">
        <request action="urn:ihe:iti:2010:UpdateDocumentSet"/>
        <response action="urn:ihe:iti:2010:UpdateDocumentSetResponse"/>
        <implClass value="unused"/>
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
    </actor>
</ActorsTransactions>
'''
    @Before
    void setup() {
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(config)
        actorType = new ActorTransactionTypeFactory().getActorTypeIfAvailable("reg");
        register = new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb')
    }

	@Test
	public void setGetTest()  {
		EndpointType label = new EndpointType(actorType, "rb");

		assertEquals("", register, label.getTransType());
		
		label.setTransType(register);
		assertEquals("", register, label.getTransType());
		
		label.setAsync(AsyncType.ASYNC);
		assertEquals("", AsyncType.ASYNC, label.getAsyncType());
	
		label.setAsync(AsyncType.SYNC);
		assertEquals("", AsyncType.SYNC, label.getAsyncType());
	
		label.setTls(TlsType.TLS);
		assertEquals("", TlsType.TLS, label.getTlsType());

		label.setTls(TlsType.NOTLS);
		assertEquals("", TlsType.NOTLS, label.getTlsType());
}
	
}
