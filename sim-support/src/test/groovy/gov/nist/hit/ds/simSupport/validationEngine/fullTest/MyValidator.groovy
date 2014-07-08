package gov.nist.hit.ds.simSupport.validationEngine.fullTest

import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.eventLog.assertion.annotations.Validation
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.soapSupport.SoapFaultException
import groovy.util.logging.Log4j

/**
 * Created by bmajur on 6/29/14.
 */
@Log4j
class MyValidator extends ValComponentBase {
    String msg;

    MyValidator(Event event, String msg) { super(event); this.msg = msg }

    @Validation(id='step1', msg='Whole Message', ref='Strings')
    def validationStep1() {
        log.debug('step1 is running')
        assertEquals('Message', msg)
    }

    @Validation(id='step2', msg='Nothing to do', ref='/dev/null')
    def validationStep2() {
        log.debug('step2 is running')
        assertEquals(1, 1)
    }

    @Override
    void run() throws SoapFaultException, RepositoryException {
        runValidationEngine()
    }

    @Override
    boolean showOutputInLogs() {
        return true
    }
}
