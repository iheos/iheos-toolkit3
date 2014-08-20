package gov.nist.hit.ds.dsSims.validator
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.utilities.datatypes.RequiredOptional

/**
 * Created by bmajur on 7/30/14.
 */
class TestValidator1ErrorWithSub extends ValComponentBase {
    Event event

    TestValidator1ErrorWithSub(Event event) {
        super(event)
        this.event = event
    }

    @Validation(id="TV401", required=RequiredOptional.R, msg="A test Validation", ref="??")
    def val1() { }

    @Validation(id="TV402", required=RequiredOptional.R, msg="A test Validation", ref="??")
    def val2() { }

    @Override
    public void run() {
        runValidationEngine()
        event.addChildResults('TestValidatorErrorSub1')
        new TestValidatorErrorSub1(event).run()
    }
}
