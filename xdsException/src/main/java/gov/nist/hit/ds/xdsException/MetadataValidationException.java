package gov.nist.hit.ds.xdsException;

public class MetadataValidationException extends MetadataException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MetadataValidationException(String msg, String resource) {
		super(msg, resource);
	}

	public MetadataValidationException(String msg, String resource, Throwable cause) {
		super(msg, resource, cause);
	}

}
