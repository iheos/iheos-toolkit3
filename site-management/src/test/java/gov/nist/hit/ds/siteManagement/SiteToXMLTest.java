package gov.nist.hit.ds.siteManagement;

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory;
import gov.nist.hit.ds.siteManagement.client.Site;
import gov.nist.hit.ds.siteManagement.client.TransactionBean;
import gov.nist.hit.ds.siteManagement.loader.CombinedSiteLoader;
import gov.nist.hit.ds.siteManagement.loader.Sites;
import gov.nist.hit.ds.xdsException.ExceptionUtil;
import org.apache.axiom.om.OMElement;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SiteToXMLTest {

	@Before
	public void setup() {
        new ActorTransactionTypeFactory().load();
    }
	
	@Test
	public void testEmptySite() {
		CombinedSiteLoader loader = new CombinedSiteLoader();
		Sites sites1 = new Sites();
		OMElement sitesX = loader.toXML(sites1);
		try {
			Sites sites2 = loader.load(sitesX, new Sites());
			assertTrue(sites1.equals(sites2));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testTransactionSite() {
		CombinedSiteLoader loader = new CombinedSiteLoader();
		Sites sites1 = new Sites();
		Site site = new Site("bjar");
		site.addTransaction("sq", "http://bjar", true, false);
		sites1.add(site);
		OMElement sitesX = loader.toXML(sites1);
		try {
			Sites sites2 = loader.load(sitesX, new Sites());
			assertTrue(sites1.equals(sites2));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testRepositorySite() {
		CombinedSiteLoader loader = new CombinedSiteLoader();
		Sites sites1 = new Sites();
		Site site = new Site("bjar");
		site.addRepository("1.1.1", TransactionBean.RepositoryType.REPOSITORY, "http://bjar", true, false);
		sites1.add(site);
		OMElement sitesX = loader.toXML(sites1);
		try {
			Sites sites2 = loader.load(sitesX, new Sites());
			assertTrue(sites1.equals(sites2));
		} catch (Exception e) {
			fail(ExceptionUtil.exception_details(e));
		}
	}
	
	@Test
	public void testCombinedSite() {
		CombinedSiteLoader loader = new CombinedSiteLoader();
		Sites sites1 = new Sites();
		
		Site site = new Site("bjar");
		site.addRepository("1.1.1", TransactionBean.RepositoryType.REPOSITORY, "http://bjar", true, false);
		sites1.add(site);
		
		Site sitea = new Site("bjar");
		sitea.addTransaction("sq", "http://bjarx", false, false);
		sites1.add(sitea);		

		OMElement sitesX = loader.toXML(sites1);
		try {
			Sites sites2 = loader.load(sitesX, new Sites());
			assertTrue(sites1.equals(sites2));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
