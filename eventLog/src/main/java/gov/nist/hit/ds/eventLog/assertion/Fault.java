package gov.nist.hit.ds.eventLog.assertion;

import gov.nist.hit.ds.repository.AssetHelper;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.SimpleType;

public class Fault {
	Asset faultAsset;
	Asset parent;
	int order;
	
	public void init(Asset parent, int order) throws RepositoryException {
		this.parent = parent;
		this.order = order;
	}
	
	public void add(String reportString) throws RepositoryException {
		Asset a = AssetHelper.createChildAsset(parent, "SoapFault", "", new SimpleType("simpleType"));
		AssetHelper.setOrder(a, order);
		a.updateContent(reportString, "text/plain");
	}
}
