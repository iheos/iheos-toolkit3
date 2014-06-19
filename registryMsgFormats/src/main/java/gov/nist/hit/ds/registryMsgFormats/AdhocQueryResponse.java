package gov.nist.hit.ds.registryMsgFormats;

import java.util.List;

import org.apache.axiom.om.OMElement;

public class AdhocQueryResponse {
	String status;
	List<RegistryError> registryErrorList;
	OMElement registryErrorListEle;
	OMElement registryObjectListEle;
	OMElement ele;
	
	public boolean isSuccess() { return status != null && status.endsWith(":Success"); }
	public String getStatus() { return status; }
	public List<RegistryError> getRegistryErrorList() { return registryErrorList; }
	public OMElement getRegistryObjectListEle() { return registryObjectListEle; }
	public OMElement getRegistryErrorListEle() { return registryErrorListEle; }
	public OMElement getMessage() { return ele; }
}