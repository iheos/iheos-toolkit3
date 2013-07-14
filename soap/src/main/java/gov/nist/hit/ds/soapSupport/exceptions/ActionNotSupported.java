package gov.nist.hit.ds.soapSupport.exceptions;

import gov.nist.hit.ds.soapSupport.core.FaultCodes;

public class ActionNotSupported extends SoapFaultException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ActionNotSupported(String reason) {
		super(FaultCodes.ActionNotSupported, reason);
	}
}
