package gov.nist.hit.ds.dsSims.eb.metadataValidator.model.codes;

import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.UuidModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AllCodes {
	private Map<UuidModel, Codes> allCodes = new HashMap<UuidModel, Codes>();
	
	public AllCodes add(Codes codes) {
		allCodes.put(codes.getClassificationScheme(), codes);
		return this;
	}
	
	public boolean exists(UuidModel classification, Code code) {
		Codes codes = allCodes.get(classification);
		if (codes == null) return false;
		return codes.exists(code);
	}
	
	public boolean isKnownClassification(UuidModel classification) {
		return allCodes.containsKey(classification);
	}
	
	public Set<UuidModel> definedClassifications() {
		return allCodes.keySet();
	}
	
	public Code pick(UuidModel classification) {
		Codes codes = allCodes.get(classification);
		if (codes == null) return null;
		return codes.pick();
	}
}
