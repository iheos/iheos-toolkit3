package gov.nist.hit.ds.registryMsgFormats;

import gov.nist.hit.ds.xdsException.XdsInternalException;
import gov.nist.toolkit.registrysupport.MetadataSupport;
import org.apache.axiom.om.OMElement;

import java.util.ArrayList;
import java.util.List;

public class RegistryResponse extends Response {
	
	public RegistryResponse()  {
		super(version_3);
		response = MetadataSupport.om_factory.createOMElement("RegistryResponse", ebRSns);
	}
	
	public RegistryResponse(short version, RegistryErrorListGenerator rel) throws XdsInternalException {
		super(version, rel);
		response = MetadataSupport.om_factory.createOMElement("RegistryResponse", ebRSns);
	}
	
	public void addQueryResults(OMElement metadata) {
		
	}
	
	public OMElement getRoot() { return response; }

    boolean success;
    List<RegistryError> registryErrorList = new ArrayList<RegistryError>();
    OMElement topElement;

    public boolean isSuccess() { return success; }
//    public List<RegistryError> getRegistryErrorList() { return registryErrorList; }
    public OMElement getTopElement() { return topElement; }

    public void addErrorsTo(RegistryErrorListGenerator rel) {
        for (RegistryError e : registryErrorList) {
            rel.add_error(e.errorCode, e.codeContext, null);
        }
    }

}
