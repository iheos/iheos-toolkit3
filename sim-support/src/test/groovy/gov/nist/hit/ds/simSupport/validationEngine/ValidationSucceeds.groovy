package gov.nist.hit.ds.simSupport.validationEngine

import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.soapSupport.SoapFaultException

/**
 * Created by bmajur on 4/29/14.
 */
class ValidationSucceeds extends ValComponentBase {
    @Override
    void run() throws SoapFaultException, RepositoryException {
        runValidationEngine()
    }

    @Validation(id="VAL1", msg="One must equal one", ref=["First Grade"])
    public void validationTest() throws SoapFaultException {
        assertEquals(1,1);
    }


    @Override
    boolean showOutputInLogs() {
        return false
    }
}
