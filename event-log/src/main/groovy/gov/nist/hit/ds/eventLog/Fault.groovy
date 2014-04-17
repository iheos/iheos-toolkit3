package gov.nist.hit.ds.eventLog;

import gov.nist.hit.ds.repository.AssetHelper;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.SimpleType;

class Fault {
	Asset asset

	public Asset init(Asset parent) throws RepositoryException {
        asset = AssetHelper.createChildAsset(parent, "SoapFault", "", new SimpleType("simpleType"));
        return asset
	}
	
	public void add(String reportString) throws RepositoryException {
		asset.updateContent(reportString, "text/plain");
	}
}
