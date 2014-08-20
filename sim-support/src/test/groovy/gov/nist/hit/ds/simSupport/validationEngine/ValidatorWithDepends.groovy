package gov.nist.hit.ds.simSupport.validationEngine

import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.soapSupport.SoapFaultException

/**
 * Created by bmajur on 4/29/14.
 */
class ValidatorWithDepends extends ValComponentBase {

    @Override
    void run() throws SoapFaultException, RepositoryException {
        runValidationEngine()
    }

    @Validation(id="VAL1", msg="A Validation", ref="First Grade", dependsOn=["VAL2"])
    public void validation1Test() throws SoapFaultException {
        assertEquals(1,1);
    }

    @Validation(id="VAL2", msg="A Validation", ref="First Grade", dependsOn=["VAL3"])
    public void validation2Test() throws SoapFaultException {
        assertEquals(1,1);
    }

    @Validation(id="VAL3", msg="A Validation", ref=["First Grade"])
    public void validation3Test() throws SoapFaultException {
        assertEquals(1,1);
    }

    @Override
    boolean showOutputInLogs() {
        return false
    }
}
