package gov.nist.hit.ds.registryMsgFormats;

import java.util.ArrayList;
import java.util.List;

public class RegistryErrorList {
	List<RegistryError> errorList = new ArrayList<RegistryError>();
	
	public RegistryErrorList() {
		
	}
	
	public RegistryErrorList add(RegistryError e) {
		errorList.add(e);
		return this;
	}
}
