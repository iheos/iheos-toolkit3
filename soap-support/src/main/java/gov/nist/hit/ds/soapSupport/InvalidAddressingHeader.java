package gov.nist.hit.ds.soapSupport;


import gov.nist.hit.ds.eventLog.errorRecording.IAssertionGroup;

public class InvalidAddressingHeader extends SoapFaultException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidAddressingHeader(IAssertionGroup er, String reason) {
		super(er, FaultCode.InvalidAddressingHeader, reason);
	}
}
