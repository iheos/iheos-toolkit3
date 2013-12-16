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
	AssertionGroup ag = null;
	static Logger logger = Logger.getLogger(Assertions.class);

	public Asset init(Asset parent) throws RepositoryException {
		assertionsAsset = AssetHelper.createChildAsset(parent, "Validators", "", new SimpleType("simAssertions"));
		return assertionsAsset;
	}
	
	public void add(AssertionGroup ag) throws RepositoryException {
		this.ag = ag;
	}
	
	public void flush() throws RepositoryException {
		logger.debug("AssertionGroup " + ag.toString());
		if (!ag.isSaveInLog())
			return;
		Asset a = AssetHelper.createChildAsset(assertionsAsset, ag.getValidatorName(), "", new SimpleType("simpleType"));
		a.setOrder(counter++);
		a.setProperty(PropertyKey.STATUS, ag.getMaxStatus().name());
		logger.debug("flushing CSVTable:");
		logger.debug(ag.getTable().toString());
		a.updateContent(ag.getTable().toString(), "text/csv");
	}
	
	AssertionStatus getAssertionStatus() {
		if (ag == null)
			return AssertionStatus.SUCCESS;
		return ag.getMaxStatus();
	}
	
	public boolean hasErrors() {
		return getAssertionStatus().isError();
	}
}
