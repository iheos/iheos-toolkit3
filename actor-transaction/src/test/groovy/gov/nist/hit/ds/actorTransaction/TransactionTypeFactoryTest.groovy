package gov.nist.hit.ds.actorTransaction;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TransactionTypeFactoryTest {
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
        <params multipart="true" soap="true"/>
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
    }

	// This is testing against the registerTransaction.properties file
	// which is part of toolkit configuration
	@Test
	public void getTypeNamesTest() {
		assertNotNull(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'))
	}

	@Test
	public void getTransactionTypeByAsyncCodeTest()  {
		assertNotNull(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable("r.as"));
	}

	@Test
	public void verifyFieldsTestrb()   {
		TransactionType tt = new ActorTransactionTypeFactory().getTransactionTypeIfAvailable("rb");
        println tt
		assertTrue(tt.code.equals("rb"));
		assertTrue(tt.asyncCode.equals("r.as"));
		assertTrue(tt.requestAction.equals("urn:ihe:iti:2007:RegisterDocumentSet-b"));
		assertTrue(tt.responseAction.equals("urn:ihe:iti:2007:RegisterDocumentSet-bResponse"));
	}


    @Test
    public void verifyFieldsTestprb()   {
        TransactionType tt = new ActorTransactionTypeFactory().getTransactionTypeIfAvailable("prb");
        println tt
        assertTrue(tt.multiPart)
        assertTrue(tt.soap)
    }
}
