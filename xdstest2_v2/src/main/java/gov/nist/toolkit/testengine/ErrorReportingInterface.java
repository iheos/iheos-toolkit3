package gov.nist.toolkit.testengine;

import gov.nist.hit.ds.xdsExceptions.XdsInternalException;

public interface ErrorReportingInterface {
	public void fail(String msg) throws XdsInternalException;
	public void setInContext(String title, Object value);
}
