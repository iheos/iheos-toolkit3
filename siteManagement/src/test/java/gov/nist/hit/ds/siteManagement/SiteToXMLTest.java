package gov.nist.hit.ds.siteManagement;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.siteManagement.CombinedSiteLoader;
import gov.nist.hit.ds.siteManagement.Sites;
import gov.nist.hit.ds.siteManagement.client.Site;
import gov.nist.hit.ds.siteManagement.client.TransactionBean.RepositoryType;

import org.apache.axiom.om.OMElement;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SiteToXMLTest {

	@Before
	public void setup() {
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
	public void testTransactionSitw() {
		CombinedSiteLoader loader = new CombinedSiteLoader();
		Sites sites1 = new Sites();
		Site site = new Site("bjar");
		site.addTransaction("sqb", "http://bjar", true, false);
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
		site.addRepository("1.1.1", RepositoryType.REPOSITORY, "http://bjar", true, false);
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
	public void testCombinedSite() {
		CombinedSiteLoader loader = new CombinedSiteLoader();
		Sites sites1 = new Sites();
		
		Site site = new Site("bjar");
		site.addRepository("1.1.1", RepositoryType.REPOSITORY, "http://bjar", true, false);
		sites1.add(site);
		
		Site sitea = new Site("bjar");
		sitea.addTransaction("sqb", "http://bjarx", false, false);
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
