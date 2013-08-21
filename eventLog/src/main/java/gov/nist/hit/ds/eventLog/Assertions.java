package gov.nist.hit.ds.eventLog;

import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.SimpleType;
import gov.nist.hit.ds.utilities.csv.CSVTable;

public class Assertions {
	Asset assertionsAsset;
	int counter = 1;
	
	public Asset init(Asset parent) throws RepositoryException {
		assertionsAsset = AssetHelper.createChildAsset(parent, "Assertions", "", new SimpleType("simAssertions"));
		return assertionsAsset;
	}
	
	public void add(String validatorName, CSVTable assertionTable) throws RepositoryException {
		Asset a = AssetHelper.createChildAsset(assertionsAsset, validatorName, "", new SimpleType("simpleType"));
		AssetHelper.setOrder(a, counter++);
		a.updateContent(assertionTable.toString(), "text/csv");

	}
}
