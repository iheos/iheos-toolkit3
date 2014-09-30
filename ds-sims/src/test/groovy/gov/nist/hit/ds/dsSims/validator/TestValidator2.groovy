package gov.nist.hit.ds.dsSims.validator
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
/**
 * Created by bmajur on 7/30/14.
 */
class TestValidator2 extends ValComponentBase {
    TestValidator2(Event event) {
        super(event)
    }

    @Validation(id="TV201", msg="A test Validation", ref="??")
    def val1() { }

    @Validation(id="TV202", msg="A test Validation", ref="??")
    def val2() { }
}
