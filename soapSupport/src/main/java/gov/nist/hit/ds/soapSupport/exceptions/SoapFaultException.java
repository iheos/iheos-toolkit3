package gov.nist.hit.ds.soapSupport.exceptions;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode.Code;
import gov.nist.hit.ds.soapSupport.core.FaultCode;

public class SoapFaultException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	FaultCode faultCode;
	String faultString;
	String faultActor;
	String faultDetail;

	public FaultCode getFaultCode() {
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

	public SoapFaultException(ErrorRecorder er, FaultCode faultCode, String faultString, String faultActor, String faultDetail) {
		super(faultCode.toString() + ": " + faultString);
		this.faultCode = faultCode;
		this.faultString = faultString;
		this.faultActor = faultActor;
		this.faultDetail = faultDetail;
		if (er != null)
			er.err(Code.SoapFault, this);
	}

	public SoapFaultException(ErrorRecorder er, FaultCode faultCode, String faultString) {
		this(er, faultCode, faultString, null, null);
	}
	
	public SoapFaultException(ErrorRecorder er, FaultCode faultCode, ErrorContext errorContext) {
		this(er, faultCode, errorContext.toString(), null, null);
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append(faultCode.toString()).append(": ").append(faultString);
		if (faultActor != null)
			buf.append("\nactor: ").append(faultActor);
		if (faultDetail != null)
			buf.append("\ndetail: ").append(faultDetail);
		
		return buf.toString();
	}
}
