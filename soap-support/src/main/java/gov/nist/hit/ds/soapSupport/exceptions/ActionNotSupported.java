package gov.nist.hit.ds.soapSupport.exceptions;

import gov.nist.hit.ds.eventLog.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.soapSupport.soapFault.FaultCode;

public class ActionNotSupported extends SoapFaultException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ActionNotSupported(IAssertionGroup er, String reason) {
		super(er, FaultCode.ActionNotSupported, reason);
	}
}
