package gov.nist.hit.ds.soap.exceptions;

import gov.nist.hit.ds.soap.core.FaultCodes;

public class EndpointUnavailableException extends SoapFaultException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EndpointUnavailableException(String reason) {
		super(FaultCodes.EndpointUnavailable, reason);
	}
}
