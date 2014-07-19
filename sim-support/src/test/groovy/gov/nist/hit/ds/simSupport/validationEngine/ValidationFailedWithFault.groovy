package gov.nist.hit.ds.simSupport.validationEngine

import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ValidationFault
import gov.nist.hit.ds.soapSupport.FaultCode
import gov.nist.hit.ds.soapSupport.SoapFaultException

/**
 * Created by bmajur on 4/29/14.
 */
class ValidationFailedWithFault extends ValComponentBase{
    @Override
    void run() throws SoapFaultException, RepositoryException {
        runValidationEngine()
    }

    @ValidationFault(id="VAL1", faultCode=FaultCode.ActionNotSupported, msg="One must equal one", ref=["First Grade"])
    public void validationTest() throws SoapFaultException {
        assertEquals(1,2);
    }


    @Override
    boolean showOutputInLogs() {
        return false
    }
}