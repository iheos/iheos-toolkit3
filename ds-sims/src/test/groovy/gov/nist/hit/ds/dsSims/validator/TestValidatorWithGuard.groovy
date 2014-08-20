package gov.nist.hit.ds.dsSims.validator
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.utilities.datatypes.RequiredOptional

/**
 * Created by bmajur on 7/30/14.
 */
class TestValidatorWithGuard extends ValComponentBase {
    TestValidatorWithGuard(Event event) {
        super(event)
    }

    @Validation(id="TV101", guard='guardTrue', required=RequiredOptional.R, msg="A test Validation", ref="??")
    def val1() { }

    @Validation(id="TV101a", dependsOn="TV101", required=RequiredOptional.R, msg="A test Validation", ref="??")
    def val4() { }

    @Validation(id="TV102", guard='guardFalse', required=RequiredOptional.R, msg="A test Validation", ref="??")
    def val2() { }

    @Validation(id="TV102a", dependsOn="TV102", required=RequiredOptional.R, msg="A test Validation", ref="??")
    def val3() { }


    boolean guardTrue() { return true }

    boolean guardFalse() { return false }

}
