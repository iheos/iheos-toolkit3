package gov.nist.hit.ds.soapSupport;

import gov.nist.hit.ds.eventLog.errorRecording.IAssertionGroup;

public class ActionNotSupported extends SoapFaultException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ActionNotSupported(IAssertionGroup er, String reason) {
		super(er, FaultCode.ActionNotSupported, reason);
	}
}
