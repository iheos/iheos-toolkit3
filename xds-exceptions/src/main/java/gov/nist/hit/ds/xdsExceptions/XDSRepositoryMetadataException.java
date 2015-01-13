package gov.nist.hit.ds.xdsExceptions;

public class XDSRepositoryMetadataException extends XdsException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XDSRepositoryMetadataException(String message, String resource) {
		super(message, resource);
	}

	public XDSRepositoryMetadataException(String msg, String resource, Throwable cause) {
		super(msg, resource, cause);
	}

}
