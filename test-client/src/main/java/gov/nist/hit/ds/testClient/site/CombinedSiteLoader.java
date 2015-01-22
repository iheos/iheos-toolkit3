package gov.nist.hit.ds.testClient.site;

import gov.nist.toolkit.utilities.xml.Util;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class CombinedSiteLoader extends AbstractSiteLoader {
	String defaultSiteName = null;
	
	public Sites load(OMElement conf, Sites sites) throws Exception {
		if (sites == null)
			sites = new Sites();
		siteMap = sites.getSiteMap();
		
		parse(conf);
		
		sites.setDefaultSite(defaultSiteName);
		sites.setSites(siteMap);
		sites.buildRepositoriesSite();
		
		return sites;
	}
	
	public Sites load(File actorsFile, Sites sites) throws Exception {
		OMElement conf = Util.parse_xml(actorsFile);
		return load(conf, sites);
	}
	
	public Sites load(String actorsString, Sites sites) throws Exception {
		OMElement conf = Util.parse_xml(actorsString);
		return load(conf, sites);
	}

	public synchronized void saveToFile(File actorsFile, OMElement xml) throws IOException {
		Util.write_xml(actorsFile, xml);
	}

	public synchronized void saveToFile(File actorsFile, Sites sites) throws Exception {
		StringBuffer errs = new StringBuffer();
		sites.validate(errs);
		if (errs.length() != 0)
			throw new Exception("Validation errors: " + errs.toString());
		Util.write_xml(actorsFile, toXML(sites));
	}
	
	public OMElement toXML(Sites sites) {
		OMElement sites_ele = OMAbstractFactory.getOMFactory().createOMElement("sites", null);

		if (sites.defaultSiteName != null) {
			OMElement dsn = OMAbstractFactory.getOMFactory().createOMElement("defaultsite", null);
			dsn.setText(sites.defaultSiteName);
			sites_ele.addChild(dsn);
		}

		for (String name : sites.siteMap.keySet()) {
			if (name.equals(sites.getAllRepositoriesSiteName()))
				continue;
			Site s = sites.siteMap.get(name);
			OMElement site_ele = new SeparateSiteLoader().siteToXML(s);
			sites_ele.addChild(site_ele);
		}

		return sites_ele;
	}

	@SuppressWarnings("unchecked")
	void parse(OMElement conf) throws Exception {
		for (Iterator it= conf.getChildElements(); it.hasNext(); ) {
			OMElement ele = (OMElement) it.next();
			String ele_name = ele.getLocalName();
			if ("defaultsite".equals(ele_name)) {
				defaultSiteName = ele.getText();
				//System.out.println("default site is " + defaultSiteName);
			} else if ("site".equals(ele_name)) {
				parseSite(ele);
			}
		}

	}

}
