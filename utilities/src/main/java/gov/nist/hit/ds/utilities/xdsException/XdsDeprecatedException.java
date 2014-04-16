package gov.nist.hit.ds.utilities.xdsException;

public class XdsDeprecatedException extends XdsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XdsDeprecatedException(String msg, String resource) {
		super(msg, resource);
	}

	public XdsDeprecatedException(String msg, String resource, Throwable cause) {
		super(msg, resource, cause);
	}

}
