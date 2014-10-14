package gov.nist.hit.ds.siteManagement;

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory;
import gov.nist.hit.ds.siteManagement.client.Site;
import gov.nist.hit.ds.siteManagement.client.TransactionBean.RepositoryType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public class SiteBuilderTest {
	Site site;
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
    <property displayName="repositoryUniqueId" value="1.2.3.4"/>
    </actor>
    </ActorsTransactions>
            '''

    @Before
    public void setup() {
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(config)
        site = new Site("mysite");
    }

	@Test
	public void testSiteName() {
		assertTrue("mysite".equals(site.getSiteName()));
		assertTrue(site.validate());
	}

	@Test
	public void testSimpleSiteConstruction() {
		String transName = "sq";
		String endpoint = "http://foo.bar";
		boolean isSecure = true;
		boolean isAsync = false;
		
		site.addTransaction(transName, endpoint, isSecure, isAsync);
		
		assertTrue(site.size() == 1);
		assertTrue(site.hasTransaction(new ActorTransactionTypeFactory().getTransactionTypeIfAvailable("sq")));
		assertTrue(site.hasActor(new ActorTransactionTypeFactory().getActorTypeIfAvailable("reg")));
		assertTrue(site.validate());
	}

	@Test
	public void testRepositorySiteConstruction() {
		String uid="1.1.1";
		RepositoryType type = RepositoryType.REPOSITORY;
		String endpoint = "http://bar";
		boolean isSecure = false;
		boolean isAsync = true;
		site.addRepository(uid, type, endpoint, isSecure, isAsync);

		assertTrue(site.size() == 1);
		assertTrue(site.hasRepositoryB());
		try {
			assertTrue(uid.equals(site.getRepositoryUniqueId()));
			assertTrue(endpoint.equals(site.getRetrieveEndpoint(uid, isSecure, isAsync)));
		} catch (Exception e) { fail(); }
		assertTrue(site.validate());
	}

	@Test
	public void testSecInsecRepositorySiteConstruction() {
		String uid="1.1.1";
		RepositoryType type = RepositoryType.REPOSITORY;
		String endpoint = "http://bar";
		boolean isSecure = false;
		boolean isAsync = false;
		site.addRepository(uid, type, endpoint, false, isAsync);
		site.addRepository(uid, type, endpoint, true, isAsync);

		assertTrue(site.size() == 2);
		assertTrue(site.hasRepositoryB());
		assertTrue(site.repositoryUniqueIds().size() == 1);
		try {
			assertTrue(uid.equals(site.getRepositoryUniqueId()));
			assertTrue(endpoint.equals(site.getRetrieveEndpoint(uid, isSecure, isAsync)));
		} catch (Exception e) { fail(); }
		assertTrue(site.validate());
	}


}
