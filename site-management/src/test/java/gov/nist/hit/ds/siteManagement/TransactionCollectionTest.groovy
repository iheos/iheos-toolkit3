package gov.nist.hit.ds.siteManagement
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.TransactionType
import gov.nist.hit.ds.siteManagement.client.TransactionBean
import gov.nist.hit.ds.siteManagement.client.TransactionCollection
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.*

class TransactionCollectionTest {
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
        new ActorTransactionTypeFactory().loadFromString(config)
    }

	///////////////////////////////////////////////////////////
	@Test
	public void testEqualsTransactionCollection() {
        TransactionType tt = new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb')
        assertNotNull tt
		TransactionBean b = new TransactionBean(tt,
			TransactionBean.RepositoryType.NONE,
			'http://fooo:40/bar',
			false,
			false)
		TransactionCollection tc = new TransactionCollection(false)
		tc.addTransaction(b)
		TransactionBean b1 = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
			TransactionBean.RepositoryType.NONE,
			'http://fooo:40/bar',
			false,
			false)
		TransactionCollection tc1 = new TransactionCollection(false)
		tc1.addTransaction(b1)
		
		assertTrue tc.equals(tc1)
	}

	@Test
	public void testNotEqualsTransactionCollection() {
		TransactionBean b = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
			TransactionBean.RepositoryType.NONE,
			'http://fooo:40/bar',
			true,
			false)
		TransactionCollection tc = new TransactionCollection(false)
		tc.addTransaction(b)
		TransactionBean b1 = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
			TransactionBean.RepositoryType.NONE,
			'http://fooo:40/bar',
			false,
			false)
		TransactionCollection tc1 = new TransactionCollection(false)
		tc1.addTransaction(b1)
		
		assertFalse tc.equals(tc1)
	}

	@Test
	public void testFixTlsEndpoints() {
		String origEndpoint = 'http://fooo:40/bar' 
		TransactionBean b = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
			TransactionBean.RepositoryType.NONE,
			origEndpoint,
			true,
			false)
		TransactionCollection tc = new TransactionCollection(false)
		tc.addTransaction(b)
		
		TransactionBean b2 = tc.lookup(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'), true, false)
		assertTrue b2 != null
		String endpoint = b2.getEndpoint();
		assertTrue endpoint != null
		assertEquals origEndpoint, endpoint
		tc.fixTlsEndpoints()
		String fixedEndpoint = b2.getEndpoint()
		assertTrue fixedEndpoint != null
		assertFalse endpoint == fixedEndpoint
		assertTrue fixedEndpoint.startsWith('https')
		assertFalse endpoint.startsWith('https')
	}

	///////////////////////////////////////////////////////////
	@Test
	public void testContains() {
		TransactionBean b = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
			TransactionBean.RepositoryType.NONE,
			'http://fooo:40/bar',
			false,
			false)
		TransactionCollection tc = new TransactionCollection(false)
		tc.addTransaction(b)
		assertTrue tc.contains(b)
	}

	@Test
	public void testAddTransaction() {
		// testContains() handles this
	}

	@Test
	public void testSize() {
		TransactionBean b = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
			TransactionBean.RepositoryType.NONE,
			'http://fooo:40/bar',
			false,
			false)
		TransactionCollection tc = new TransactionCollection(false)
		tc.addTransaction(b)
		Assert.assertEquals tc.size(), 1
	}

	@Test
	public void testHasActor() {
		String origEndpoint = 'http://fooo:40/bar' 
		TransactionBean b = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
			TransactionBean.RepositoryType.NONE,
			origEndpoint,
			true,
			false)
		TransactionCollection tc = new TransactionCollection(false)
		tc.addTransaction(b)
		assertTrue tc.hasActor(new ActorTransactionTypeFactory().getActorTypeIfAvailable('reg'))
		assertFalse tc.hasActor(new ActorTransactionTypeFactory().getActorTypeIfAvailable('rep'))
	}

	@Test
	public void testHasTransaction() {
		String origEndpoint = 'http://fooo:40/bar' 
		TransactionBean b = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
			TransactionBean.RepositoryType.NONE,
			origEndpoint,
			true,
			false)
		TransactionCollection tc = new TransactionCollection(false)
		tc.addTransaction(b)
		
		assertTrue tc.hasTransaction(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'))
		assertFalse tc.hasTransaction(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('provideRegister'))
	}

	@Test
	public void testFindTransactionTypeBooleanBoolean() {
		String origEndpoint = 'http://fooo:40/bar' 
		TransactionBean b = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
			TransactionBean.RepositoryType.NONE,
			origEndpoint,
			true,
			false)
		TransactionCollection tc = new TransactionCollection(false)
		tc.addTransaction(b)

		assertTrue null != tc.lookup(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'), true, false)
		assertTrue null == tc.lookup(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'), false, false)
	}

	@Test
	public void testFindStringBooleanBoolean() {
		TransactionBean b = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
			TransactionBean.RepositoryType.NONE,
			'http://fooo:40/bar',
			true,
			false)
		TransactionCollection tc = new TransactionCollection(false)
		tc.addTransaction(b)
		
		TransactionBean b2 = tc.lookup(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'), true, false)
		assertTrue b2 != null
	}

	@Test
	public void testFindAll() {
		TransactionBean b = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
			TransactionBean.RepositoryType.NONE,
			'http://fooo:40/bar',
			true,
			false)
		TransactionCollection tc = new TransactionCollection(false)
		tc.addTransaction(b)

		List<TransactionBean> tb = tc.lookupAll("rb", true, false)
		assertEquals 1, tb.size()
		tb = tc.lookupAll("sq", true, false)
		assertEquals 0, tb.size()
	}

	@Test
	public void testGetTransactionTypeBooleanBoolean() {
		TransactionBean b = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
			TransactionBean.RepositoryType.NONE,
			'http://fooo:40/bar',
			true,
			false)
		TransactionCollection tc = new TransactionCollection(false)
		tc.addTransaction(b)

		assertEquals 'http://fooo:40/bar', tc.get(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'), true, false)
	}

	@Test
	public void testGetStringBooleanBoolean() {
		TransactionBean b = new TransactionBean(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable('rb'),
			TransactionBean.RepositoryType.NONE,
			'http://fooo:40/bar',
			true,
			false)
		TransactionCollection tc = new TransactionCollection(false)
		tc.addTransaction(b)

		assertEquals 'http://fooo:40/bar', tc.get("rb", true, false)
	}

	@Test
	public void testAdd() {
		TransactionCollection tc = new TransactionCollection(false)
		tc.add("rb", 'http://fooo:40/bar', true, false)
		assertEquals 'http://fooo:40/bar', tc.get("rb", true, false)
	}

}
