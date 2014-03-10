package gov.nist.toolkit.registrymsgformats.registry;

import gov.nist.hit.ds.xdsException.XdsInternalException;
import gov.nist.toolkit.registrysupport.MetadataSupport;
import org.apache.axiom.om.OMElement;

public class RegistryResponse extends Response {
	
	public RegistryResponse(short version) throws XdsInternalException {
		super(version);
		response = MetadataSupport.om_factory.createOMElement("RegistryResponse", ebRSns);
	}
	
	public RegistryResponse(short version, RegistryErrorListGenerator rel) throws XdsInternalException {
		super(version, rel);
		response = MetadataSupport.om_factory.createOMElement("RegistryResponse", ebRSns);
	}
	
	public void addQueryResults(OMElement metadata) {
		
	}
	
	public OMElement getRoot() { return response; }

}
