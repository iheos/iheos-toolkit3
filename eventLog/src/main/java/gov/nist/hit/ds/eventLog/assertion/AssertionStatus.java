package gov.nist.hit.ds.eventLog.assertion;

public enum AssertionStatus {
	SUCCESS, WARNING, ERROR, FAULT;
	
	public AssertionStatus getMax(AssertionStatus as) {
		if (as.ordinal() > ordinal())
			return as;
		return this;
	}
	
}
