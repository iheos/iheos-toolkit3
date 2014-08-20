package gov.nist.hit.ds.dsSims.validator
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.utilities.datatypes.RequiredOptional

/**
 * Created by bmajur on 7/30/14.
 */
class TestValidator1 extends ValComponentBase {
    TestValidator1(Event event) {
        super(event)
    }

    @Validation(id="TV101", required=RequiredOptional.R, msg="A test Validation", ref="??")
    def val1() { }

    @Validation(id="TV102", required=RequiredOptional.R, msg="A test Validation", ref="??")
    def val2() { }
}
