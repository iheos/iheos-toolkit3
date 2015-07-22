package gov.nist.hit.ds.simSupport.validationEngine
import gov.nist.hit.ds.eventLog.assertion.Assertion
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import org.apache.log4j.Logger
/**
 * Created by bmajur on 11/2/14.
 */

class ValidatorWithError extends ValComponentBase {
    private static Logger log = Logger.getLogger(ValidatorWithError);

    def error(SimHandle simHandle, String errorCode, String errorMessage) {
        ag = simHandle.event.assertionGroup
        Assertion a = ag.fail(errorMessage, true)
        a.setId("Error001")
        a.setMsg(errorMessage)
        a.setCode(errorCode)
    }
}
