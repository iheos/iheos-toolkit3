package gov.nist.hit.ds.dsSims.eb.validator
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
/**
 * Created by bmajur on 7/30/14.
 */
class TestValidatorWithGuard extends ValComponentBase {
    TestValidatorWithGuard(Event event) {
        super(event)
    }

    @Guard(methodNames=["guardTrue"])
    @Validation(id="TV101", msg="A test Validation", ref="??")
    def val1() {  found('val1()'); println 'val1() running'}

    @Guard(methodNames=["guardFalse"])
    @Validation(id="TV102", msg="A test Validation", ref="??")
    def val2() { found('val2()'); println 'val2() running'}

    @Guard(methodNames=["guardTrue", "guardTrue2"])
    @Validation(id="TV103", msg="A test Validation", ref="??")
    def val3() {  found('val3()'); println 'val3() running'}

    @Guard(methodNames=["guardTrue", "guardFalse"])
    @Validation(id="TV103", msg="A test Validation", ref="??")
    def val4() {  found('val4()'); println 'val4() running'}

    boolean guardTrue() { return true }

    boolean guardTrue2() { return true }

    boolean guardFalse() { return false }

}
