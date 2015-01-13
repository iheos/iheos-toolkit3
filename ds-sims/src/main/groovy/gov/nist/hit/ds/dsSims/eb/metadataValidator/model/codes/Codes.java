package gov.nist.hit.ds.dsSims.eb.metadataValidator.model.codes;

import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.UuidModel;

import java.util.ArrayList;
import java.util.List;

public class Codes {
	private UuidModel classificationScheme;
	private List<Code> codes = new ArrayList<Code>();
	private int pickIndex = -1;
	
	public Codes(UuidModel classificationScheme) {
		this.classificationScheme = classificationScheme;
	}
	
	public Codes add(Code code) {
		codes.add(code);
		return this;
	}
	
	public UuidModel getClassificationScheme() { return classificationScheme; }
	
	private int pickIndex() {
		pickIndex++;
		if (pickIndex >= codes.size()) pickIndex = 0;
		return pickIndex;
	}
	
	public Code pick() {
		int index = pickIndex();
		return codes.get(index);
	}
	
	public boolean exists(Code code) {
		for (Code c : codes) if (c.equals(code)) return true;
		return false;
	}
}
