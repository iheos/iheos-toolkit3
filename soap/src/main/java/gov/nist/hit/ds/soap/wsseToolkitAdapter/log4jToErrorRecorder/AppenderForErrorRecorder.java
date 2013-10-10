package gov.nist.hit.ds.soap.wsseToolkitAdapter.log4jToErrorRecorder;
import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.valSupport.client.ValidationContext;
import gov.nist.hit.ds.valSupport.engine.MessageValidatorEngine;

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
			er.err(XdsErrorCode.Code.NoCode,new ErrorContext(event.getRenderedMessage(),event.getLevel().toString()), event.getLoggerName());
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
