import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.siteManagement.client.TransactionBean
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.*
/**
 * This test may be obsolete.  Are transaction types still usable
 * given the configuration driven v3 approach?
 */
class TransactionBeanTest {
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
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().load(config)
    }

    ///////////////////////////////////////////////////////////
    @Test
    public void testSameObjectHasSameIndex() {
        TransactionBean b = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        assertTrue b.hasSameIndex(b)
    }

    @Test
    public void testHasSameIndex() {
        TransactionBean b = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        TransactionBean c = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        assertTrue b.hasSameIndex(c)
    }

    @Test
    public void testNotHasSameIndex() {
        TransactionBean b = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        TransactionBean c = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                true,
                false)
        assertFalse b.hasSameIndex(c)
    }

    ///////////////////////////////////////////////////////////
    @Test
    public void testEqualsTransactionBean() {
        TransactionBean b = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        TransactionBean c = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        TransactionBean d = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                true,
                false)
        assertEquals b, b
        assertTrue b.equals(c)
        assertFalse b.equals(d)
    }

    ///////////////////////////////////////////////////////////
    @Test
    public void testHasName() {
        TransactionBean b = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        assertTrue b.hasName('rb')
    }

    ///////////////////////////////////////////////////////////
    @Test
    public void testGetName() {
        TransactionBean b = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        assertEquals(b.getName(), new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb').name)
        assertEquals(b.getName(), "rb")
    }

    ///////////////////////////////////////////////////////////

    // This should test isNameUid AND repositorytype not NONE AND is transaction type correct
    // have to fix users first
    // Have to use String as first parameter since that is whre repUid is passed in
    // Should have to use special type RepUid which wraps repUid to avoid confusion
    // RepositoryType and name should be consistent
    @Test
    public void testIsRetrieve() {
        TransactionBean b = new TransactionBean('1.2.3',
                TransactionBean.RepositoryType.REPOSITORY,
                'http://fooo:40/bar',
                false,
                false)
        assertTrue b.isRetrieve()
    }

    @Test
    public void testIsRetrieveButBadrepUid() {
        try {
            TransactionBean b = new TransactionBean('foo',
                    TransactionBean.RepositoryType.NONE,
                    'http://fooo:40/bar',
                    false,
                    false)
        } catch (ToolkitRuntimeException e) {
            return
        }
        fail()
    }

    @Test
    public void testIsNotRetrieve() {
        TransactionBean b = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        assertFalse b.isRetrieve()
    }

    ///////////////////////////////////////////////////////////
    @Test
    public void testIsNameUid() {
        TransactionBean b = new TransactionBean('1.2.3',
                TransactionBean.RepositoryType.REPOSITORY,
                'http://fooo:40/bar',
                false,
                false)
        assertTrue b.isRetrieve()
    }

    ///////////////////////////////////////////////////////////
    @Test
    public void testGetTransactionType() {
        TransactionBean b = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        assertTrue b.getTransactionType() == new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb')
    }

    @Test
    ///////////////////////////////////////////////////////////
    public void testIsType() {
        TransactionBean b = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        assertTrue b.isType(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'))
    }

    ///////////////////////////////////////////////////////////
    @Test
    public void testHasEndpoint() {
        TransactionBean b = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        assertTrue b.hasEndpoint()
    }

}
