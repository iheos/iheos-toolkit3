package gov.nist.hit.ds.simSupport.loader;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.http.environment.EventLog;
import gov.nist.hit.ds.simSupport.engine.Inject;
import gov.nist.hit.ds.soapSupport.core.FaultCode;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;

import java.io.IOException;

public class ByInjectionLogLoader extends AbstractLogLoader {

	@Inject
	public void setSimDb(EventLog eventLog) throws SoapFaultException {
		try {
			header = eventLog.getHeader();
			body = eventLog.getBody();
		} catch (IOException e) {
			throw new SoapFaultException(
					er,
					FaultCode.Receiver,
					new ErrorContext("Internal Error: cannot load request message: " + e.getMessage())
					);
		}
	}
}
