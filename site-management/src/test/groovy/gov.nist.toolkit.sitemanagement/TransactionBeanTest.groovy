import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.ActorTypeFactory
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

    @Before
    public void setup() {
        new ActorTransactionTypeFactory().load();
    }

    ///////////////////////////////////////////////////////////
    @Test
    public void testSameObjectHasSameIndex() {
        TransactionBean b = new TransactionBean(ActorTypeFactory.find('registry', 'register'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        assertTrue b.hasSameIndex(b)
    }

    @Test
    public void testHasSameIndex() {
        TransactionBean b = new TransactionBean(ActorTypeFactory.find('registry', 'register'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        TransactionBean c = new TransactionBean(ActorTypeFactory.find('registry', 'register'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        assertTrue b.hasSameIndex(c)
    }

    @Test
    public void testNotHasSameIndex() {
        TransactionBean b = new TransactionBean(ActorTypeFactory.find('registry', 'register'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        TransactionBean c = new TransactionBean(ActorTypeFactory.find('registry', 'register'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                true,
                false)
        assertFalse b.hasSameIndex(c)
    }

    ///////////////////////////////////////////////////////////
    @Test
    public void testEqualsTransactionBean() {
        TransactionBean b = new TransactionBean(ActorTypeFactory.find('registry', 'register'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        TransactionBean c = new TransactionBean(ActorTypeFactory.find('registry', 'register'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        TransactionBean d = new TransactionBean(ActorTypeFactory.find('registry', 'register'),
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
        TransactionBean b = new TransactionBean(ActorTypeFactory.find('registry', 'register'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        assertTrue b.hasName('Register')
    }

    ///////////////////////////////////////////////////////////
    @Test
    public void testGetName() {
        TransactionBean b = new TransactionBean(ActorTypeFactory.find('registry', 'register'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        assertEquals(b.getName(), ActorTypeFactory.find('registry', 'register').name)
        assertEquals(b.getName(), "Register")
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
        TransactionBean b = new TransactionBean(ActorTypeFactory.find('registry', 'register'),
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
        TransactionBean b = new TransactionBean(ActorTypeFactory.find('registry', 'register'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        assertTrue b.getTransactionType() == ActorTypeFactory.find('registry', 'register')
    }

    @Test
    ///////////////////////////////////////////////////////////
    public void testIsType() {
        TransactionBean b = new TransactionBean(ActorTypeFactory.find('registry', 'register'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        assertTrue b.isType(ActorTypeFactory.find('registry', 'register'))
    }

    ///////////////////////////////////////////////////////////
    @Test
    public void testHasEndpoint() {
        TransactionBean b = new TransactionBean(ActorTypeFactory.find('registry', 'register'),
                TransactionBean.RepositoryType.NONE,
                'http://fooo:40/bar',
                false,
                false)
        assertTrue b.hasEndpoint()
    }

}
