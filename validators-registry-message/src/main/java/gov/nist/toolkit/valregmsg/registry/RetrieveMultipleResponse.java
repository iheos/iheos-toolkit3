package gov.nist.toolkit.valregmsg.registry;

import gov.nist.hit.ds.xdsException.XdsInternalException;
import gov.nist.toolkit.registrymsgformats.registry.RegistryErrorListGenerator;
import gov.nist.toolkit.registrymsgformats.registry.Response;
import gov.nist.toolkit.registrysupport.MetadataSupport;
import org.apache.axiom.om.OMElement;

public class RetrieveMultipleResponse extends Response {
	OMElement rdsr = null;
	
	public OMElement getRoot() { return rdsr; }

	public RetrieveMultipleResponse() throws XdsInternalException {
		super();
		response = MetadataSupport.om_factory.createOMElement("RegistryResponse", ebRSns);
		rdsr = MetadataSupport.om_factory.createOMElement("RetrieveDocumentSetResponse", MetadataSupport.xdsB);
		rdsr.addChild(response);
	}
	
	public RetrieveMultipleResponse(RegistryErrorListGenerator rel) throws XdsInternalException {
		super(rel);
		response = MetadataSupport.om_factory.createOMElement("RegistryResponse", ebRSns);
		rdsr = MetadataSupport.om_factory.createOMElement("RetrieveDocumentSetResponse", MetadataSupport.xdsB);
		rdsr.addChild(response);
	}
	
	public void addQueryResults(OMElement metadata) {
		
	}


}
