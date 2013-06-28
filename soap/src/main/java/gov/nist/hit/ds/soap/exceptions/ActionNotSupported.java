package gov.nist.hit.ds.soap.exceptions;

import gov.nist.hit.ds.soap.FaultCodes;

public class ActionNotSupported extends SoapFaultException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ActionNotSupported(String reason) {
		super(FaultCodes.ActionNotSupported, reason);
	}
}
