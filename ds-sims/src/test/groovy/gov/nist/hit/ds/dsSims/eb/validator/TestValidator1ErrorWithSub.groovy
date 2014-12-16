package gov.nist.hit.ds.dsSims.eb.validator
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
/**
 * Created by bmajur on 7/30/14.
 */
class TestValidator1ErrorWithSub extends ValComponentBase {
    Event event

    TestValidator1ErrorWithSub(Event event) {
        super(event)
        this.event = event
    }

    @Validation(id="TV401", msg="A test Validation", ref="??")
    def val1() { }

    @Validation(id="TV402",  msg="A test Validation", ref="??")
    def val2() { }

    void runAfter() {
        new TestValidatorErrorSub1(event).asChild().run()
    }
}
