package gov.nist.toolkit.testengine;

import gov.nist.hit.ds.xdsException.XdsInternalException;

public interface ErrorReportingInterface {
	public void fail(String msg) throws XdsInternalException;
	public void setInContext(String title, Object value);
}
