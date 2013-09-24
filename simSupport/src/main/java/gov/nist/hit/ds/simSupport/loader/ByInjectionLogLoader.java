package gov.nist.hit.ds.simSupport.loader;

import org.apache.log4j.Logger;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.engine.Inject;
import gov.nist.hit.ds.soapSupport.core.FaultCode;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;

public class ByInjectionLogLoader extends AbstractLogLoader {
	static Logger logger = Logger.getLogger(ByInjectionLogLoader.class);

	@Inject
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
}
