package gov.nist.toolkit.soap.wsseToolkitAdapter.log4jToErrorRecorder;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.toolkit.valsupport.client.ValidationContext;
import gov.nist.toolkit.valsupport.engine.MessageValidatorEngine;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;


public/* static */class AppenderForErrorRecorder extends AppenderSkeleton {
	
	private ValidationContext vc;
	private IAssertionGroup er;
	private MessageValidatorEngine mvc;

	public AppenderForErrorRecorder(ValidationContext vc, IAssertionGroup er, MessageValidatorEngine mvc){
		this.vc = vc;
		this.er = er;
		this.mvc = mvc;
	}
	
	 
	protected void append(LoggingEvent event) {
		
		if (event.getLevel() == Level.ERROR) {
			er.err(XdsErrorCode.Code.NoCode, new ErrorContext(event.getRenderedMessage(), event.getLevel().toString()), null);
		}
		if (event.getLevel() == Level.WARN) {
			er.detail(event.getRenderedMessage() + "\n");
		}
		if (event.getLevel() == Level.INFO) {
			er.detail(event.getRenderedMessage() + "\n");
		}
	}

	public void close() {
	}

	public boolean requiresLayout() {
		return false;
	}

}
