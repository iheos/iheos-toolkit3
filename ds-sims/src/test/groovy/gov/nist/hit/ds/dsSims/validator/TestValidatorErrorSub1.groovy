package gov.nist.hit.ds.dsSims.validator
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.eventLog.assertion.annotations.Validation
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.utilities.datatypes.RequiredOptional

/**
 * Created by bmajur on 7/30/14.
 */
class TestValidatorErrorSub1 extends ValComponentBase {
    TestValidatorErrorSub1(Event event) {
        super(event)
    }

    @Validation(id="TV301", required=RequiredOptional.R, msg="A test Validation", ref="??")
    def val1() { fail('Ouch') }

    @Validation(id="TV302", required=RequiredOptional.R, msg="A test Validation", ref="??")
    def val2() { }
}
