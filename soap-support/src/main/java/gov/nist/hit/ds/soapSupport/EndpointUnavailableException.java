package gov.nist.hit.ds.soapSupport;


import gov.nist.hit.ds.eventLog.errorRecording.IAssertionGroup;

public class EndpointUnavailableException extends SoapFaultException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EndpointUnavailableException(IAssertionGroup er, String reason) {
		super(er, FaultCode.EndpointUnavailable, reason);
	}
}
