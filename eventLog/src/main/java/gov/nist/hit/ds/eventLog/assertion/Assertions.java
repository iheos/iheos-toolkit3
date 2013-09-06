package gov.nist.hit.ds.eventLog.assertion;

import gov.nist.hit.ds.eventLog.AssetHelper;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.SimpleType;

public class Assertions {
	Asset assertionsAsset;
	int counter = 1;
	
	public Asset init(Asset parent) throws RepositoryException {
		assertionsAsset = AssetHelper.createChildAsset(parent, "Assertions", "", new SimpleType("simAssertions"));
		return assertionsAsset;
	}
	
	public void add(String validatorName, AssertionGroup ag) throws RepositoryException {
		Asset a = AssetHelper.createChildAsset(assertionsAsset, validatorName, "", new SimpleType("simpleType"));
		AssetHelper.setOrder(a, counter++);
		a.setProperty("status", ag.getMaxStatus().name());
		a.updateContent(ag.getTable().toString(), "text/csv");

	}
}
