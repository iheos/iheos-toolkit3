package gov.nist.hit.ds.eventLog.assertion;

import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode;

/**
 * Describe a reference to a validation.
 * @author bmajur
 *
 */
public class ValidationRef {
	String id;
	String errCode;
	String msg;
	String[] ref;   // references
	String location = null;

	/**
	 * 
	 * @param id - validation id
	 * @param msg - valiation msg
	 * @param ref - validation references
	 */
	public ValidationRef(String id, XdsErrorCode.Code errCode, String msg, String[] ref) {
		this.id = id;
		this.errCode = errCode.name();
		this.msg = msg;
		this.ref = ref;
	}
	
	public ValidationRef(String id, String errCode, String msg, String[] ref) {
		this.id = id;
		this.errCode = errCode;
		this.msg = msg;
		this.ref = ref;
	}
	
	public ValidationRef setLocation(Class<?> claz) {
		location = claz.getSimpleName();
		return this;
	}
	
	public String getLocation() {
		return location;
	}
	
	public String getId() {
		return id;
	}
	
	public String getErrCode() {
		return errCode;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public String[] getRef() {
		return ref;
	}
}
