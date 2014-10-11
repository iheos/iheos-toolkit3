package gov.nist.hit.ds.httpSoap.wsseToolkitAdapter.log4jToErrorRecorder;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

public/* static */class AppenderForErrorRecorder extends AppenderSkeleton {
    protected void append(LoggingEvent loggingEvent) {

    }

    public void close() {

    }

    public boolean requiresLayout() {
        return false;
    }

//	private ValidationContext vc;
//	private IAssertionGroup er;
//	private MessageValidatorEngine mvc;
//
//	public AppenderForErrorRecorder(ValidationContext vc, IAssertionGroup er, MessageValidatorEngine mvc){
//		this.vc = vc;
//		this.er = er;
//		this.mvc = mvc;
//	}
//
//	@Override
//	protected void append(LoggingEvent event) {
//
//		if (event.getLevel() == Level.ERROR) {
//			er.err(XdsErrorCode.Code.NoCode, new ErrorContext(event.getRenderedMessage() + " : " + event.getLevel().toString(), null), event.getLoggerName());
//		}
//		if (event.getLevel() == Level.INFO) {
//			er.detail(event.getRenderedMessage());
//		}
//	}
//
//	public void close() {
//	}
//
//	public boolean requiresLayout() {
//		return false;
//	}

}
