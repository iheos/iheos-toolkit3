package gov.nist.hit.ds.actorTransaction

import org.junit.Before
import org.junit.Test

import static org.junit.Assert.*

public class EndpointLabelBuildTest {
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
        <property displayName="repositoryUniqueId" value="1.2.3.4"/>
    </actor>
</ActorsTransactions>
'''
    @Before
    void setup() {
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(config)
        actorType = new ActorTransactionTypeFactory().getActorTypeIfAvailable("reg");
    }

	@Test
	public void buildTest()  {
		TransactionType register = actorType.findSimple("rb", false);
		EndpointType label;
		label = new EndpointType(
				register,
				TlsType.TLS,
				AsyncType.ASYNC
				);
		assertEquals("", "rb_TLS_ASYNC", label.label());
		assertTrue(label.isTls());
		assertTrue(label.isAsync());
		assertEquals("", register, label.getTransType());

		label = new EndpointType(
				register,
				TlsType.NOTLS,
				AsyncType.ASYNC
				);
		assertEquals("", "rb_ASYNC", label.label());
		assertFalse(label.isTls());
		assertTrue(label.isAsync());
		assertEquals("", register, label.getTransType());

		label = new EndpointType(
				register,
				TlsType.TLS,
				AsyncType.SYNC
				);
		assertEquals("", "rb_TLS", label.label());
		assertTrue(label.isTls());
		assertFalse(label.isAsync());
		assertEquals("", register, label.getTransType());

		label = new EndpointType(
				register,
				TlsType.NOTLS,
				AsyncType.SYNC
				);
		assertEquals("", "rb", label.label());
		assertFalse(label.isTls());
		assertFalse(label.isAsync());
		assertEquals("", register, label.getTransType());
}

}
