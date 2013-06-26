package gov.nist.hit.ds.registryMsgFormats;

import gov.nist.hit.ds.utilities.xml.XmlUtil;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import org.apache.axiom.om.OMElement;

public class RegistryResponseGenerator extends ResponseGenerator {
	
	public RegistryResponseGenerator()  {
		response = XmlUtil.om_factory.createOMElement("RegistryResponse", ebRSns);
	}
	
	public RegistryResponseGenerator(RegistryErrorListGenerator rel) throws XdsInternalException {
		super(rel);
		response = XmlUtil.om_factory.createOMElement("RegistryResponse", ebRSns);
	}
	
	public void addQueryResults(OMElement metadata) {
		
	}
	
	public OMElement getRoot() { return response; }

}
