package gov.nist.toolkit.valsupport.errrec;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.client.ValidatorErrorItem;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.errorRecording.factories.ErrorRecorderBuilder;
import gov.nist.hit.ds.utilities.xdsException.ExceptionUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class GwtErrorRecorder implements IAssertionGroup {

	ErrorRecorderBuilder errorRecorderBuilder;
	List<ValidatorErrorItem> summary = new ArrayList<ValidatorErrorItem>();
	List<ValidatorErrorItem> errMsgs = new ArrayList<ValidatorErrorItem>();
	int lastErrCount = 0;

	static Logger logger = Logger.getLogger(GwtErrorRecorder.class);


	public String toString() {
		StringBuffer buf = new StringBuffer();

		for (ValidatorErrorItem info : errMsgs) {
			buf.append(info).append("\n");
		}

		return buf.toString();
	}

	public String simLogToHtmlString() {
		StringBuffer buf = new StringBuffer();
		
		// Improved output

		for (ValidatorErrorItem info : errMsgs) {
			if(info.level == ValidatorErrorItem.ReportingLevel.ERROR) {
				buf.append("<span style=\"color: red;\">").append(info).append("</span>").append("<br />").append("\n");
			} else if(info.level == ValidatorErrorItem.ReportingLevel.SECTIONHEADING) {
				buf.append("<h3>").append(info).append("</h3>").append("\n");
			} else {
				if(info.msg.contains(" - ") &&  info.msg.contains(": ")) {
					String[] split = info.msg.split(": ", 1);
					info.msg = "<u>" + split[0] + "</u>: " + split[1]; 
				}
				buf.append(info).append("<br />").append("\n");
			}
			
			
		}

		return buf.toString();
	}

	public String errToString() {
		StringBuffer buf = new StringBuffer();

		for (ValidatorErrorItem info : errMsgs) {
			if (info.level == ValidatorErrorItem.ReportingLevel.ERROR)
				buf.append(info.getCodeString() + ": " + info.msg).append("\n");
		}

		return buf.toString();
	}

	public List<String> getErrorMessages() {
		List<String> msgs = new ArrayList<String>();

		for (ValidatorErrorItem info : errMsgs) {
			if (info.level != ValidatorErrorItem.ReportingLevel.ERROR)
				continue;
			msgs.add(info.msg);
		}

		return msgs;
	}

	public List<String> getErrorCodes() {
		List<String> codes = new ArrayList<String>();

		for (ValidatorErrorItem info : errMsgs) {
			if (info.level != ValidatorErrorItem.ReportingLevel.ERROR)
				continue;
			codes.add(info.getCodeString());
		}

		return codes;
	}

	public List<ValidatorErrorItem> getValidatorErrorInfo() {
		return errMsgs;
	}

	public List<ValidatorErrorItem> getSummaryErrorInfo() {
		return summary;
	}

	public boolean hasErrors() {
		for (ValidatorErrorItem vei : errMsgs) {
			if ((vei.level == ValidatorErrorItem.ReportingLevel.ERROR) || (vei.level == ValidatorErrorItem.ReportingLevel.D_ERROR))
				return true;
		}
		return false;
	}

	public void err(XdsErrorCode.Code code, String msg, String location, String resource) {
		if (msg == null || msg.trim().equals(""))
			return;
		logger.debug(ExceptionUtil.here("err - " + msg));
		ValidatorErrorItem ei = new ValidatorErrorItem();
		ei.level = ValidatorErrorItem.ReportingLevel.ERROR;
		ei.msg = msg;
		ei.setCode(code);
		ei.location = location;
		ei.resource = resource;
		ei.completion = ValidatorErrorItem.ReportingCompletionType.ERROR;
		errMsgs.add(ei);
		lastErrCount++;
		for (int i=errMsgs.size()-1; i>0; i--) {
			if (ei.level == ValidatorErrorItem.ReportingLevel.SECTIONHEADING)
				break;
			if (ei.level == ValidatorErrorItem.ReportingLevel.CHALLENGE) {
				ei.completion = ValidatorErrorItem.ReportingCompletionType.ERROR;
			}
		}
	}

	public void err(XdsErrorCode.Code code, Exception e) {
		err(code, ExceptionUtil.exception_details(e), null, "");
	}

	public void finish() {
		tagLastInfo2();
	}

	int getLastErrCountChange() {
		int cnt = 0;
		for (ValidatorErrorItem msg : errMsgs) {
			if (msg.level == ValidatorErrorItem.ReportingLevel.ERROR)
				cnt++;
		}
		return cnt - lastErrCount;
	}

	void tagLastInfo2() {
		if (errMsgs.size() == 0)
			return;
		if (getLastErrCountChange() != 0) {
			lastErrCount = 0;
			return;
		}

	}

    public void err(XdsErrorCode.Code code, ErrorContext context, Object location) {

    }


    public void warning(String code, ErrorContext context, String location) {

    }

    public void warning(XdsErrorCode.Code code, ErrorContext context, String location) {

    }

    public void sectionHeading(String msg) {
		tagLastInfo2();
		ValidatorErrorItem ei = new ValidatorErrorItem();
		ei.level = ValidatorErrorItem.ReportingLevel.SECTIONHEADING;
		ei.msg = msg;
		errMsgs.add(ei);
	}

	public void sectionHeadingError(String msg) {
		tagLastInfo2();
		ValidatorErrorItem ei = new ValidatorErrorItem();
		ei.level = ValidatorErrorItem.ReportingLevel.SECTIONHEADING;
		ei.msg = msg;
		errMsgs.add(ei);
	}

	public void challenge(String msg) {
		tagLastInfo2();
		ValidatorErrorItem ei = new ValidatorErrorItem();
		ei.level = ValidatorErrorItem.ReportingLevel.CHALLENGE;
		ei.msg = msg;
		errMsgs.add(ei);
	}

	public void showErrorInfo() {
	}

	public void detail(String msg) {
		tagLastInfo2();
		ValidatorErrorItem ei = new ValidatorErrorItem();
		ei.level = ValidatorErrorItem.ReportingLevel.DETAIL;
		ei.msg = msg;
		errMsgs.add(ei);
	}

	public void externalChallenge(String msg) {
		tagLastInfo2();
		ValidatorErrorItem ei = new ValidatorErrorItem();
		ei.level = ValidatorErrorItem.ReportingLevel.EXTERNALCHALLENGE;
		ei.msg = msg;
		errMsgs.add(ei);
	}

	// because of conflict in types, 5th parm is Object and down a few lines is 
	// another method with 5th param of String, the compiler will generate a 
	// call here.  This, the err1 stuff to disambiguate.
	public void err(XdsErrorCode.Code code, String msg, String location, String resource,
			Object log_message) {
		//		if (log_message != null && log_message instanceof String)
		//			err1(code, msg, location, resource, log_message);
		//		else
		err(code, msg, location, resource);
	}

	public void err(String code, String msg, String location, String severity, String resource) {
		err1(code, msg, location, severity, resource);
	}

	public void err(XdsErrorCode.Code code, String msg, Object location, String resource) {
		String loc = "";
		if (location != null)
			loc = location.getClass().getSimpleName();
		err(code, msg, loc, resource);
	}

	//	public void err(String code, String msg, String location, String severity,
	//			String resource) {
	//		err1(code, msg, location, severity, resource);
	//	}

	void err1(String code, String msg, String location, String severity,
			String resource) {
		if (msg == null || msg.trim().equals(""))
			return;
		logger.debug(ExceptionUtil.here("err - " + msg));
		boolean isWarning = (severity == null) ? false : ((severity.indexOf("Warning") != -1));
		ValidatorErrorItem.ReportingCompletionType ctype = (isWarning) ? ValidatorErrorItem.ReportingCompletionType.WARNING : ValidatorErrorItem.ReportingCompletionType.ERROR;
		ValidatorErrorItem ei = new ValidatorErrorItem();
		ei.level = (isWarning) ? ValidatorErrorItem.ReportingLevel.WARNING : ValidatorErrorItem.ReportingLevel.ERROR;
		ei.msg = msg;
		ei.setCode(code);
		ei.location = location;
		ei.resource = resource;
		ei.completion = ctype;
		errMsgs.add(ei);
		lastErrCount++;
		for (int i=errMsgs.size()-1; i>0; i--) {
			if (ei.level == ValidatorErrorItem.ReportingLevel.SECTIONHEADING)
				break;
			if (ei.level == ValidatorErrorItem.ReportingLevel.CHALLENGE) {
				ei.completion = ctype;
			}
		}

	}

	public void err(XdsErrorCode.Code code, String msg, String location, String severity,
			String resource) {
		err1(code.toString(), msg, location, severity, resource);
	}

	 
	public void warning(String code, String msg, String location,
			String resource) {
		err1(code, msg, location, "Warning", resource);
	}

	 
	public void warning(XdsErrorCode.Code code, String msg, String location, String resource) {
		err1(code.toString(), msg, location, "Warning", resource);

	}

	 
	public IAssertionGroup buildNewErrorRecorder() {
		return this;
	}

	 
	public int getNbErrors() {
		int nbErrors = 0;
		for (ValidatorErrorItem vei : errMsgs) {
			if ((vei.level == ValidatorErrorItem.ReportingLevel.ERROR) || (vei.level == ValidatorErrorItem.ReportingLevel.D_ERROR))
				nbErrors++;
		}
		return nbErrors;
	}

    @Override
    public void concat(IAssertionGroup er) {

    }

    public void concat(ErrorRecorder er) {
		this.errMsgs.addAll(er.getErrMsgs());
	}

	public List<ValidatorErrorItem> getErrMsgs() {
		return this.errMsgs;
	}

	 
	public ErrorRecorderBuilder getErrorRecorderBuilder() {
		// TODO Auto-generated method stub
		return errorRecorderBuilder;
	}

	 
	public void success(String dts, String name, String found, String expected, String RFC) {
		tagLastInfo2();
		ValidatorErrorItem ei = new ValidatorErrorItem();
		ei.level = ValidatorErrorItem.ReportingLevel.D_SUCCESS;
		ei.dts = dts;
		ei.name = name;
		ei.found = found;
		ei.expected = expected;
		ei.rfc = RFC;
		ei.status = "Success";
		errMsgs.add(ei);
	}

	 
	public void error(String dts, String name, String found, String expected,String RFC) {
		if (dts == null || dts.trim().equals(""))
			return;
		logger.debug(ExceptionUtil.here("err - " + dts));
		ValidatorErrorItem ei = new ValidatorErrorItem();
		ei.level = ValidatorErrorItem.ReportingLevel.D_ERROR;
		ei.dts = dts;
		ei.name = name;
		ei.found = found;
		ei.expected = expected;
		ei.rfc = RFC;
		ei.status = "Error";
		ei.completion = ValidatorErrorItem.ReportingCompletionType.ERROR;
		errMsgs.add(ei);
		lastErrCount++;
		for (int i=errMsgs.size()-1; i>0; i--) {
			if (ei.level == ValidatorErrorItem.ReportingLevel.SECTIONHEADING)
				break;
			if (ei.level == ValidatorErrorItem.ReportingLevel.CHALLENGE) {
				ei.completion = ValidatorErrorItem.ReportingCompletionType.ERROR;
			}
		}

	}

	 
	public void warning(String dts, String name, String found, String expected, String RFC) {
		ValidatorErrorItem ei = new ValidatorErrorItem();
		ei.level = ValidatorErrorItem.ReportingLevel.D_WARNING;
		ei.dts = dts;
		ei.name = name;
		ei.found = found;
		ei.expected = expected;
		ei.rfc = RFC;
		ei.status = "Warning";
		ei.completion = ValidatorErrorItem.ReportingCompletionType.WARNING;
		errMsgs.add(ei);
		lastErrCount++;
		for (int i=errMsgs.size()-1; i>0; i--) {
			if (ei.level == ValidatorErrorItem.ReportingLevel.SECTIONHEADING)
				break;
			if (ei.level == ValidatorErrorItem.ReportingLevel.CHALLENGE) {
				ei.completion = ValidatorErrorItem.ReportingCompletionType.WARNING;
			}
		}

	}

	 
	public void info(String dts, String name, String found, String expected, String RFC) {
		tagLastInfo2();
		ValidatorErrorItem ei = new ValidatorErrorItem();
		ei.level = ValidatorErrorItem.ReportingLevel.D_INFO;
		ei.dts = dts;
		ei.name = name;
		ei.found = found;
		ei.expected = expected;
		ei.rfc = RFC;
		ei.status = "Info";
		errMsgs.add(ei);
	}

	 
	public void summary(String msg, boolean success, boolean part) {
		ValidatorErrorItem ei = new ValidatorErrorItem();
		if(success) {
			ei.level = ValidatorErrorItem.ReportingLevel.D_SUCCESS;
			ei.status = "Success";
		} else {
			ei.level = ValidatorErrorItem.ReportingLevel.D_ERROR;
			ei.status = "Error";
		}
		ei.summaryPart = part;
		ei.msg = msg;
		summary.add(ei);
	}

	public void addValidatorItem(ValidatorErrorItem e) {
		errMsgs.add(e);
	}



}
