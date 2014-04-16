package gov.nist.hit.ds.simSupport.loader;

import org.apache.log4j.Logger;

import gov.nist.hit.ds.eventLog.errorRecording.ErrorContext;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.annotations.SimComponentInject;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.soapSupport.soapFault.FaultCode;

public class ByInjectionLogLoader extends AbstractLogLoader {
	static Logger logger = Logger.getLogger(ByInjectionLogLoader.class);

	@SimComponentInject
	public void setSimDb(Event event) throws SoapFaultException {
		logger.debug("ByInjectionLogLoader: set Event");
		try {
			header = event.getInOutMessages().getRequestHeader();
			body = event.getInOutMessages().getRequestBody();
		} catch (RepositoryException e) {
			throw new SoapFaultException(
					ag,
					FaultCode.Receiver,
					new ErrorContext("Internal Error: cannot load request message: " + e.getMessage())
					);
		}
	}

	@Override
	public boolean showOutputInLogs() {
		return false;
	}
}
