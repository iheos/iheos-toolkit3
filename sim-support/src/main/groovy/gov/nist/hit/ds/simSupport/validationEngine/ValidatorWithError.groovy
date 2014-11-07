package gov.nist.hit.ds.simSupport.validationEngine

import gov.nist.hit.ds.eventLog.assertion.Assertion
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import groovy.util.logging.Log4j

/**
 * Created by bmajur on 11/2/14.
 */
@Log4j
class ValidatorWithError extends ValComponentBase {

    def error(SimHandle simHandle, String errorCode, String errorMessage) {
        ag = simHandle.event.assertionGroup
        Assertion a = ag.fail(errorMessage, true)
        a.setId("Error001")
        a.setMsg(errorMessage)
        a.setCode(errorCode)
    }
}
