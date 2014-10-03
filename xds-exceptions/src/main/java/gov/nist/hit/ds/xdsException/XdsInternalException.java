package gov.nist.hit.ds.xdsException;

public class XdsInternalException extends XdsException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public XdsInternalException(String reason) {
		super(reason, "Internal Error");
	}


	public XdsInternalException(String msg, Throwable cause) {
		super(msg,  "Internal Error", cause);
	}
}
