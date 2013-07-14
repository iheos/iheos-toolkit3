package gov.nist.hit.ds.soapSupport.exceptions;

import gov.nist.hit.ds.soapSupport.core.FaultCodes;

public class SoapFaultException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	FaultCodes faultCode;
	String faultString;
	String faultActor;
	String faultDetail;

	public FaultCodes getFaultCode() {
		return faultCode;
	}

	public String getFaultString() {
		return faultString;
	}

	public String getFaultActor() {
		return faultActor;
	}

	public String getFaultDetail() {
		return faultDetail;
	}

	public SoapFaultException(FaultCodes faultCode, String faultString, String faultActor, String faultDetail) {
		super(faultCode.toString() + ": " + faultString);
		this.faultCode = faultCode;
		this.faultString = faultString;
		this.faultActor = faultActor;
		this.faultDetail = faultDetail;
	}

	public SoapFaultException(FaultCodes faultCode, String faultString) {
		super(faultCode.toString() + ": " + faultString);
		this.faultCode = faultCode;
		this.faultString = faultString;
		this.faultActor = null;
		this.faultDetail = null;
	}
}
