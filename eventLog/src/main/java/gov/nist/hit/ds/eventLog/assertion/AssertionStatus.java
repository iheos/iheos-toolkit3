package gov.nist.hit.ds.eventLog.assertion;

public enum AssertionStatus {
	NONE, INFO, SUCCESS, WARNING, ERROR, FAULT, INTERNALERROR;
	
	public AssertionStatus getMax(AssertionStatus as) {
		if (as.ordinal() > ordinal())
			return as;
		return this;
	}
	
	public boolean isError() {
		if (ordinal() >= ERROR.ordinal())
			return true;
		return false;
	}
	
}
