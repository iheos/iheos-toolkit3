package gov.nist.hit.ds.soapSupport.exceptions;

import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.soapSupport.soapFault.FaultCode;

public class EndpointUnavailableException extends SoapFaultException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EndpointUnavailableException(IAssertionGroup er, String reason) {
		super(er, FaultCode.EndpointUnavailable, reason);
	}
}
