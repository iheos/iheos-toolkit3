package gov.nist.hit.ds.simSupport.loader;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.soapSupport.SoapFaultException;
import org.apache.log4j.Logger;

import gov.nist.hit.ds.simSupport.annotations.SimComponentInject;

public class ByInjectionLogLoader extends AbstractLogLoader {
    static Logger logger = Logger.getLogger(ByInjectionLogLoader.class);

    @SimComponentInject
    public void setSimDb(Event event) throws SoapFaultException {
        logger.debug("ByInjectionLogLoader: set Event");
        header = event.getInOutMessages().getRequestHeader();
        body = event.getInOutMessages().getRequestBody();
    }

    @Override
    public boolean showOutputInLogs() {
        return false;
    }
}
