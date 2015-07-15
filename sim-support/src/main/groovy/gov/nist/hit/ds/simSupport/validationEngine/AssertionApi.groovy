package gov.nist.hit.ds.simSupport.validationEngine

import gov.nist.hit.ds.eventLog.Fault
import gov.nist.hit.ds.eventLog.assertion.Assertion
import gov.nist.hit.ds.eventLog.assertion.AssertionDAO
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup
import gov.nist.hit.ds.eventLog.assertion.AssertionStatus
import gov.nist.hit.ds.eventLog.errorRecording.ErrorContext
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.utilities.datatypes.RequiredOptional
import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException
import groovy.util.logging.Log4j

/**
 * Created by bmajur on 2/11/15.
 */
@Log4j
class AssertionApi {
    ValComponentBase base
    AssertionGroup ag
    String dots = '...'
    def useDefaultMessage = false

    String found
    String expected
    String code
    String msg

    def clear() {
        found = null
        expected = null
        msg = null
        code = 'Ok'
    }

    def found(_found) { found = _found }
    def expected(_expected) { expected = _expected}
    def newMsg(_msg) { msg = _msg }

    def flush() {
        if (found || expected) {
            Assertion a = new Assertion()
            a.expected = expected
            a.found = found
            if (msg) a.msg = msg
            ag.addAssertion(a, false)
            recordAssertion(a)
        }
    }

    AssertionApi(ValComponentBase _base) { base = _base}

    public boolean infoMsg(String msg) throws SoapFaultException {
        Assertion a = ag.infoMsg(msg);
        recordAssertion(a);
        return true;
    }

    public boolean infoFound(boolean found) throws SoapFaultException {
        Assertion a = ag.infoFound(found);
        recordAssertion(a);
        return true;
    }

    public Assertion infoFound(String found) throws SoapFaultException {
        Assertion a = ag.infoFound(found);
        recordAssertion(a);
        return a;
    }

    public boolean msg(String msg) throws SoapFaultException {
        Assertion a = ag.msg(msg);
        recordAssertion(a);
        return true;
    }

    public Assertion fail(String msg) throws SoapFaultException {
        Assertion a = ag.fail(msg, base.currentValidationMethod().required);
        recordAssertion(a);
        return a
    }

    public Assertion fail(String msg, String found) throws SoapFaultException {
        Assertion a = ag.fail(msg, found, base.currentValidationMethod().required);
        recordAssertion(a);
        return a
    }

    public boolean defaultMsg() throws SoapFaultException {
        if (!useDefaultMessage) return true
        Assertion a = ag.defaultMsg()
        recordAssertion(a)
        return true
    }

    public Assertion assertIn(String[] expecteds, String value) throws SoapFaultException {
        Assertion a = ag.assertIn(expecteds, value, base.currentValidationMethod().required);
        recordAssertion(a);
        return a
    }

    public Assertion assertIn(List<String> expecteds, String value) throws SoapFaultException {
        Assertion a = ag.assertIn(expecteds, value, base.currentValidationMethod().required);
        recordAssertion(a);
        return a
    }

    public Assertion assertEquals(String expected, String found) throws SoapFaultException {
        Assertion a = ag.assertEquals(expected, found, base.currentValidationMethod().required);
//        log.debug("Assertion: ${a}")
        recordAssertion(a);
        return a
    }

    // produces lousy assertion messages - use string
//    public boolean assertEquals(boolean expected, boolean found) throws SoapFaultException {
//        Assertion a = ag.assertEquals(expected, found);
//        log.debug("Assertion: ${a}")
//        recordAssertion(a);
//        return !a.failed();
//    }

    public Assertion assertEquals(int expected, int found) throws SoapFaultException {
        Assertion a = ag.assertEquals(expected, found, base.currentValidationMethod().required);
//        log.debug("Assertion: ${a}")
        recordAssertion(a);
        return a
    }

    public Assertion assertHasValue(String value) throws SoapFaultException {
        Assertion a = ag.assertHasValue('', value, base.currentValidationMethod().required);
//        log.debug("Assertion: ${a}")
        recordAssertion(a);
        return a
    }

    public Assertion assertHasValue(String msg, String value) throws SoapFaultException {
        Assertion a = ag.assertHasValue(msg, value, base.currentValidationMethod().required);
//        log.debug("Assertion: ${a}")
        recordAssertion(a);
        return a
    }

    public Assertion assertStartsWith(String value, String prefix) throws SoapFaultException {
        Assertion a = ag.assertStartsWith(value, prefix, base.currentValidationMethod().required);
//        log.debug("Assertion: ${a}")
        recordAssertion(a);
        return a
    }

    public Assertion assertTrue(value) throws SoapFaultException {
        Assertion a = ag.assertTrue(value, base.currentValidationMethod().required);
        recordAssertion(a);
        return a
    }

    public Assertion assertTrue(boolean value, String found) throws SoapFaultException {
        Assertion a = ag.assertTrue(value, found, base.currentValidationMethod().required);
        recordAssertion(a);
        return a
    }

    public Assertion success() throws SoapFaultException { assertTrue(true, '') }

    public Assertion assertMoreThan(int reference, int value) throws SoapFaultException {
        Assertion a = ag.assertMoreThan(reference, value, base.currentValidationMethod().required);
        recordAssertion(a);
        return a
    }

    public boolean assertTrueNoLog(boolean value) throws SoapFaultException {
        if (!value)
            return assertTrue(value);
        return true;
    }

    public Assertion assertFalse(def value) throws SoapFaultException {
        Assertion a = ag.assertTrue(!value, base.currentValidationMethod().required);
        recordAssertion(a);
        return a
    }

    public Assertion assertNotNull(Object value) throws SoapFaultException {
        Assertion a = ag.assertNotNull(value, base.currentValidationMethod().required);
        recordAssertion(a);
        return a
    }

    public boolean assertNotNullNoLog(Object value) throws SoapFaultException {
        if (value == null)
            return assertNotNull(value);
        return true;
    }


    List<String> idsAsserted = new ArrayList<String>();

    /**
     * Each time an assert* method is called in a validator, this
     * method is called to transfer the details from the annotation
     * into the Assertion object. Later, outside this method, the
     * Assertion is added to the AssertionGroup which keeps track
     * of all the assertions injectAll in a validator.
     *
     * Some annotations cause a SOAPFault if an assertion fails. This is
     * handled here also.
     * @param a
     * @throws SoapFaultException
     */
    private void recordAssertion(Assertion a) throws SoapFaultException {
        if (base.validationEngine.validationAnnotation != null) {
            Validation vf = base.validationEngine.validationAnnotation
            recordAssertion(a, vf)
        } else {
            throw new ToolkitRuntimeException("Failed to record assertion ${a}")
        }
    }

    private void recordAssertion(Assertion a, Validation vf)
            throws SoapFaultException {

//        log.debug("Recording validation ${vf.id()}")
        idsAsserted.add(vf.id());
        ValidationMethod validationMethod = base.currentValidationMethod()

        String id = vf.id();

        if (found) a.found = found
        if (expected) a.expected = expected



        if (!validationMethod.required)
            a.setRequiredOptional(RequiredOptional.O)
        a.setId(id);
        def msgPrefix = []
        base.level.times { msgPrefix << dots}
        msgPrefix = msgPrefix.join()
        if (!a.msg)
            a.msg = msgPrefix + vf.msg();
        a.setReference(vf.ref());
        if (a.getStatus().isError())
            a.setCode(base.currentValidationMethod().errorCode)
        if (a.getStatus().isError() && validationMethod.type == RunType.FAULT) {
            a.setCode(validationMethod.faultCode.toString())
            a.setStatus(AssertionStatus.FAULT)

            Fault f = new Fault(vf.msg(), validationMethod.faultCode.toString(), '??', a.id)
            base.event.fault = f
            base.event.flush()
            log.debug("Assertion ${a}")

            throw new SoapFaultException(
                    ag,
                    base.currentValidationMethod().faultCode,
                    new ErrorContext("${a.getMsg()} - ${a.expectedFoundString()}", a.id)
                    //new AssertionDAO().buildSemiDivided(vf.ref())
            );
        }
        log.debug("Assertion ${a}")
        clear()
    }

    boolean validationAlreadyRecorded(String id) { idsAsserted.contains(id) }
}
