package gov.nist.hit.ds.simSupport.validationEngine

import gov.nist.hit.ds.eventLog.assertion.annotations.Validation
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.soapSupport.FaultCode
import gov.nist.hit.ds.soapSupport.SoapFaultException
/**
 * Created by bmajur on 4/29/14.
 */
class ThrowsSoapFaultException extends ValComponentBase {
    @Override
    void run() throws SoapFaultException, RepositoryException {
        runValidationEngine();
    }

    @Validation(id="VAL1", msg="Throws an exception", ref="First Grade")
    public void validationTest() throws SoapFaultException {
        throw new SoapFaultException(
                ag,
                FaultCode.ActionNotSupported,
                "My Fault, Sorry.");
    }

    @Override
    boolean showOutputInLogs() {
        return true
    }
}
