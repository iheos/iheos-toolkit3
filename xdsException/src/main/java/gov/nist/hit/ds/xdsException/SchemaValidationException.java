package gov.nist.hit.ds.xdsException;

public class SchemaValidationException extends XdsInternalException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SchemaValidationException(String msg) {
		super(msg);
	}

	public SchemaValidationException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
