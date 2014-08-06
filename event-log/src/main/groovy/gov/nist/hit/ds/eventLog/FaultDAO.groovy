package gov.nist.hit.ds.eventLog;

import gov.nist.hit.ds.repository.AssetHelper;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.SimpleType

import javax.xml.ws.soap.SOAPFaultException;

class FaultDAO {
	Asset asset = null

	public Asset init(Asset parent) throws RepositoryException {
        asset = AssetHelper.createChildAsset(parent, "SoapFault", "", new SimpleType("simpleType"));
        return asset
	}
	
	public void add(Fault fault) throws RepositoryException {
		asset.setContent(fault.toString(), "text/plain");
	}

    public void add(SOAPFaultException e) {
        add(e.message)
    }
}
