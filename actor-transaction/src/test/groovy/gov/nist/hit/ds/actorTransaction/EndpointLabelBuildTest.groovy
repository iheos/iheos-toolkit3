package gov.nist.hit.ds.actorTransaction

import org.junit.Before
import org.junit.Test

import static org.junit.Assert.*

public class EndpointLabelBuildTest {
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
        new ActorTransactionTypeFactory().loadFromString(config)
        actorType = new ActorTransactionTypeFactory().getActorTypeIfAvailable("reg");
    }

	@Test
	public void buildTest()  {
		TransactionType register = actorType.find("register");
		EndpointType label;
		label = new EndpointType(
				register,
				TlsType.TLS,
				AsyncType.ASYNC
				);
		assertEquals("", "Register_TLS_ASYNC", label.label());
		assertTrue(label.isTls());
		assertTrue(label.isAsync());
		assertEquals("", register, label.getTransType());

		label = new EndpointType(
				register,
				TlsType.NOTLS,
				AsyncType.ASYNC
				);
		assertEquals("", "Register_ASYNC", label.label());
		assertFalse(label.isTls());
		assertTrue(label.isAsync());
		assertEquals("", register, label.getTransType());

		label = new EndpointType(
				register,
				TlsType.TLS,
				AsyncType.SYNC
				);
		assertEquals("", "Register_TLS", label.label());
		assertTrue(label.isTls());
		assertFalse(label.isAsync());
		assertEquals("", register, label.getTransType());

		label = new EndpointType(
				register,
				TlsType.NOTLS,
				AsyncType.SYNC
				);
		assertEquals("", "Register", label.label());
		assertFalse(label.isTls());
		assertFalse(label.isAsync());
		assertEquals("", register, label.getTransType());
}

}
