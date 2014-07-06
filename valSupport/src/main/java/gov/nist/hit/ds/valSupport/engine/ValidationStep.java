package gov.nist.hit.ds.valSupport.engine;

import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.valSupport.message.AbstractMessageValidator;

public class ValidationStep {
	String stepName;
	AbstractMessageValidator validator;
	IAssertionGroup er;
	boolean ran = false;

	public String getStepName() { return stepName; }
	public IAssertionGroup getErrorRecorder() { return er; }
	public boolean hasErrors() { return er.hasErrors(); }

	public String toString() {
		StringBuffer buf = new StringBuffer();

		String className = validator.getClass().getName();
		try {
			className = className.substring(className.lastIndexOf(".") + 1);
		} catch (Exception e) {
			className = "Unknown";
		}
		

		buf
		.append(className).append(" - ").append(stepName)
		.append(validator.toString());
		
		return buf.toString();
	}
}
