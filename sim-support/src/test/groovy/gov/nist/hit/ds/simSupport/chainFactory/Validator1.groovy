package gov.nist.hit.ds.simSupport.chainFactory

import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.simSupport.engine.SimComponentBase
import gov.nist.hit.ds.soapSupport.SoapFaultException

/**
 * Created by bmajur on 6/25/14.
 */
class Validator1 extends SimComponentBase {
    @Override
    void run() throws SoapFaultException, RepositoryException {

    }

    @Override
    boolean showOutputInLogs() {
        return true
    }
}
