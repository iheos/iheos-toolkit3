package gov.nist.hit.ds.registryMsgFormats;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.axiom.om.OMElement;

public class RegistryResponse implements Iterator<RegistryError> {
	boolean success;
	List<RegistryError> registryErrorList = new ArrayList<RegistryError>();
	OMElement topElement;
	int iteratorIndex = 0;
	
	public boolean isSuccess() { return success; }
	public List<RegistryError> getRegistryErrorList() { return registryErrorList; }
	public OMElement getTopElement() { return topElement; }
	
	public void addErrorsTo(RegistryErrorListGenerator rel) {
		for (RegistryError e : registryErrorList) {
			rel.addError(e.errorCode, e.getCodeContext() , e.location);
		}
	}
	
	public int size() {
		return registryErrorList.size();
	}
	
	public boolean hasNext() {
		return iteratorIndex < size();
	}
	public RegistryError next() {
		if (!hasNext())
			return null;
		iteratorIndex++;
		return registryErrorList.get(iteratorIndex - 1);
	}
	public void remove() {		
	}
}
