package gov.nist.hit.ds.httpSoapValidator.wsseToolkitAdapter.log4jToErrorRecorder;
import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.simSupport.transaction.ValidationContext;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

public/* static */class AppenderForErrorRecorder extends AppenderSkeleton {
	
	private ValidationContext vc;
	private ErrorRecorder er;
	private MessageValidatorEngine mvc;

	public AppenderForErrorRecorder(ValidationContext vc, ErrorRecorder er, MessageValidatorEngine mvc){
		this.vc = vc;
		this.er = er;
		this.mvc = mvc;
	}
	
	@Override
	protected void append(LoggingEvent event) {
		
		if (event.getLevel() == Level.ERROR) {
			er.err(XdsErrorCode.Code.NoCode, new ErrorContext(event.getRenderedMessage() + " : " + event.getLevel().toString(), null), event.getLoggerName());
		}
		if (event.getLevel() == Level.INFO) {
			er.detail(event.getRenderedMessage());
		}
	}

	public void close() {
	}

	public boolean requiresLayout() {
		return false;
	}

}
