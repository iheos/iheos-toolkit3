package gov.nist.hit.ds.registryMsgFormats;

import gov.nist.hit.ds.registrysupport.MetadataSupport;
import gov.nist.hit.ds.utilities.xml.XmlUtil;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import org.apache.axiom.om.OMElement;

public class AdhocQueryResponseParser {
	AdhocQueryResponse response = new AdhocQueryResponse();
	
	public AdhocQueryResponseParser(OMElement ele) throws XdsInternalException {
		response.ele = ele;
		
		response.status = ele.getAttributeValue(MetadataSupport.status_qname);
		
		response.registryErrorList = new RegistryErrorListParser(ele).getRegistryErrorList();
		
		response.registryObjectListEle = XmlUtil.firstDecendentWithLocalName(ele, "RegistryObjectList");

		response.registryErrorListEle = XmlUtil.firstDecendentWithLocalName(ele, "RegistryErrorList");
}
	
	public AdhocQueryResponse getResponse() { return response; }
}
