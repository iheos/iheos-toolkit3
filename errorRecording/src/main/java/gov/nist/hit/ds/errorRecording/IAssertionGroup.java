package gov.nist.hit.ds.errorRecording;

import gov.nist.hit.ds.errorRecording.client.ValidatorErrorItem;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.errorRecording.factories.ErrorRecorderBuilder;

import java.util.List;

/**
 * Interface for all ErrorRecorders
 * @author bill
 *
 */
public interface IAssertionGroup extends ErrorRecorderBuilder {
	// Object location is interesting so that the class name of the location can be output
	public void err(XdsErrorCode.Code code, ErrorContext context, Object location);
	public void err(XdsErrorCode.Code code, Exception e);  // error in tool
	public void warning(String code, ErrorContext context, String location);
	public void warning(XdsErrorCode.Code code, ErrorContext context, String location);
	public void sectionHeading(String msg); // section heading
	public void challenge(String msg); // statement of challenge
	public void externalChallenge(String msg); // statement of challenge that requires registry query
	public void detail(String msg); // detail findings
	public void success(String dts, String name, String found, String expected, String RFC);
	public void error(String dts, String name, String found, String expected, String RFC);
	public void warning(String dts, String name, String found, String expected, String RFC);
	public void info(String dts, String name, String found, String expected, String RFC);
	public void summary(String msg, boolean success, boolean part);
	public void finish();
	public void showErrorInfo();
	public boolean hasErrors();
	public int getNbErrors();
	public void concat(IAssertionGroup er);
	public List<ValidatorErrorItem> getErrMsgs();
	
	public ErrorRecorderBuilder getErrorRecorderBuilder();

	/*
	 * ErrorRecorderBuilder implementation
	 * 
	 */
	
	public IAssertionGroup buildNewErrorRecorder();  // some code only accepts ErrorRecorder.  This gets around this

}
