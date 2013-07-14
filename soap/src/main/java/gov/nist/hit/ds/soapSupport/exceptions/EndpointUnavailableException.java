package gov.nist.hit.ds.soapSupport.exceptions;

import gov.nist.hit.ds.soapSupport.core.FaultCodes;

public class EndpointUnavailableException extends SoapFaultException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EndpointUnavailableException(String reason) {
		super(FaultCodes.EndpointUnavailable, reason);
	}
}
