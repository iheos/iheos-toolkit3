package gov.nist.hit.ds.xdsExceptions;


public class XdsException extends Exception {
	// TODO: parameter resource does not seem to ever be used
	// Use of this exception needs rethinking.
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String resource = null;   // pointer back into documentation 

	public XdsException(String msg, String resource) {
		super(msg);
		this.resource = resource;
	}

	public XdsException(String msg, String resource, Throwable cause) {
		super(msg, cause);
		this.resource = resource;
	}
	
	public String getResource() {
		return resource;
	}
	
	public String getDetails() {
		return ExceptionUtil.exception_details(this);
	}
}
