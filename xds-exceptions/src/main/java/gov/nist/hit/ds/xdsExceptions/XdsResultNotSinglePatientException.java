package gov.nist.hit.ds.xdsExceptions;

public class XdsResultNotSinglePatientException extends XdsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public XdsResultNotSinglePatientException(String reason, String resource) {
		super(reason, resource);
	}


	public XdsResultNotSinglePatientException(String msg, String resource, Throwable cause) {
		super(msg, resource, cause);
	}
}
