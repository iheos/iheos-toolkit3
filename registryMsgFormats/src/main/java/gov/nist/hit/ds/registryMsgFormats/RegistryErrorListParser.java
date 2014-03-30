package gov.nist.hit.ds.registryMsgFormats;

import gov.nist.hit.ds.registryMsgFormats.RegistryError;
import gov.nist.toolkit.registrysupport.MetadataSupport;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;

public class RegistryErrorListParser {
	List<RegistryError> registryErrorList = new ArrayList<RegistryError>();
	
	public RegistryErrorListParser(OMElement registryErrorListEle) {
		
		if (registryErrorListEle == null)
			return;
		
		if (!registryErrorListEle.getLocalName().equals("RegistryErrorList")) {
			registryErrorListEle = MetadataSupport.firstDecendentWithLocalName(registryErrorListEle, "RegistryErrorList");
			if (registryErrorListEle == null)
				return;
		}
		
		for (OMElement registry_error : MetadataSupport.childrenWithLocalName(registryErrorListEle, "RegistryError")) {
			RegistryError registryError = new RegistryError();
			registryErrorList.add(registryError);
			String severity = get_att(registry_error, "severity");

			if (severity == null)
				registryError.isWarning = false;
			else if (severity.endsWith("Warning"))
				registryError.isWarning = true;
			else
				registryError.isWarning = false;
			
			registryError.codeContext = get_att(registry_error, "codeContext");
			
			registryError.errorCode = get_att(registry_error, "errorCode");

			registryError.location = get_att(registry_error, "location");

		}

	}
	
	String get_att(OMElement ele, String name) {
		OMAttribute att = ele.getAttribute(new QName(name));
		if (att == null)
			return null;
		return att.getAttributeValue();

	}

	
	public List<RegistryError> getRegistryErrorList() { return registryErrorList; }
}