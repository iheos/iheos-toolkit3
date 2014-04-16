package gov.nist.hit.ds.utilities.xdsException;

public class XdsParameterException extends XdsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XdsParameterException(String msg, String resource) {
		super(msg, resource);
	}
	
	public XdsParameterException(String msg, String resource, Throwable cause) {
		super(msg, resource, cause);
	}

}
