package gov.nist.hit.ds.soap.exceptions;

import gov.nist.hit.ds.soap.core.FaultCodes;

public class SoapFaultException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	FaultCodes faultCode;
	String reason;

	public FaultCodes getFaultCode() {
		return faultCode;
	}

	public String getReason() {
		return reason;
	}

	public SoapFaultException(FaultCodes faultCode, String reason) {
		super(faultCode.toString() + ": " + reason);
		this.faultCode = faultCode;
		this.reason = reason;
	}
}
