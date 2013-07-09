package gov.nist.hit.ds.soap.exceptions;

import gov.nist.hit.ds.soap.core.FaultCodes;

public class InvalidAddressingHeader extends SoapFaultException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidAddressingHeader(String reason) {
		super(FaultCodes.InvalidAddressingHeader, reason);
	}
}
