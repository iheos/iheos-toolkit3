package gov.nist.hit.ds.eventLog.assertion;

import gov.nist.hit.ds.repository.AssetHelper;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.PropertyKey;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.SimpleType;

import org.apache.log4j.Logger;

public class Assertions {
	Asset assertionsAsset;
	int counter = 1;
	AssertionStatus maxStatus = AssertionStatus.SUCCESS;
	static Logger logger = Logger.getLogger(Assertions.class);

	public Asset init(Asset parent) throws RepositoryException {
		assertionsAsset = AssetHelper.createChildAsset(parent, "Assertions", "", new SimpleType("simAssertions"));
		return assertionsAsset;
	}
	
	public void add(AssertionGroup ag) throws RepositoryException {
		logger.debug("AssertionGroup\n" + ag.toString());
		Asset a = AssetHelper.createChildAsset(assertionsAsset, ag.getValidatorName(), "", new SimpleType("simpleType"));
		a.setOrder(counter++);
		a.setProperty(PropertyKey.STATUS, ag.getMaxStatus().name());
		a.updateContent(ag.getTable().toString(), "text/csv");

	}
}
