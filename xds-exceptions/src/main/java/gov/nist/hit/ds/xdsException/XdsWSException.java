package gov.nist.hit.ds.xdsException;

public class XdsWSException extends XdsException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XdsWSException(String msg, String resource) {
		super(msg, resource);
	}

	public XdsWSException(String msg, String resource, Throwable cause) {
		super(msg, resource, cause);
	}
}
