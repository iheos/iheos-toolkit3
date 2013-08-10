package gov.nist.hit.ds.simSupport.loader;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.http.environment.Event;
import gov.nist.hit.ds.simSupport.engine.Inject;
import gov.nist.hit.ds.soapSupport.core.FaultCode;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;

import java.io.IOException;

public class ByInjectionLogLoader extends AbstractLogLoader {

	@Inject
	public void setSimDb(Event event) throws SoapFaultException {
		try {
			header = event.getRequestHeader();
			body = event.getRequestBody();
		} catch (IOException e) {
			throw new SoapFaultException(
					er,
					FaultCode.Receiver,
					new ErrorContext("Internal Error: cannot load request message: " + e.getMessage())
					);
		}
	}
}
