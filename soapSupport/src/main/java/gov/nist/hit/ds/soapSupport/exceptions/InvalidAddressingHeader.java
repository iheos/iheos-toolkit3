package gov.nist.hit.ds.soapSupport.exceptions;

import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.soapSupport.core.FaultCode;

public class InvalidAddressingHeader extends SoapFaultException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidAddressingHeader(IAssertionGroup er, String reason) {
		super(er, FaultCode.InvalidAddressingHeader, reason);
	}
}
