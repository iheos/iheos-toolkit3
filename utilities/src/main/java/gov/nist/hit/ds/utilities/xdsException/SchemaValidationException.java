package gov.nist.hit.ds.utilities.xdsException;

public class SchemaValidationException extends ToolkitRuntimeException {
	
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
