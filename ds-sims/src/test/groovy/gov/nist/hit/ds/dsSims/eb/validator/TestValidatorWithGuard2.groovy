package gov.nist.hit.ds.dsSims.eb.validator
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
/**
 * Created by bmajur on 7/30/14.
 */
class TestValidatorWithGuard2 extends ValComponentBase {
    TestValidatorWithGuard2(Event event) {
        super(event)
    }

    @Guard(methodNames = ['guardTrue'])
    @Validation(id="TV101", msg="A test Validation", ref="??")
    def val1() { }

    @Guard(methodNames=['guardFalse'])
    @Validation(id="TV102", msg="A test Validation", ref="??")
    def val2() { }

    boolean guardTrue() { return true }

    boolean guardFalse() { return false }

}
