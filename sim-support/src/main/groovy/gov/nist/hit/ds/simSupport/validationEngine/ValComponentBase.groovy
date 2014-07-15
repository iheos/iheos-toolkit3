package gov.nist.hit.ds.simSupport.validationEngine;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.*;
import gov.nist.hit.ds.eventLog.errorRecording.ErrorContext;
import gov.nist.hit.ds.eventLog.assertion.annotations.Validation;
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ValidationFault;
import gov.nist.hit.ds.soapSupport.FaultCode;
import gov.nist.hit.ds.soapSupport.SoapFaultException;
import gov.nist.hit.ds.xdsException.ExceptionUtil
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;
import org.apache.log4j.Logger

/**
 * An abstract class that makes use of the SimComponent interface easier
 * by implementing most of the required methods leaving only the injectAll
 * method to be implemented by the component class being built.
 *
 * Most of this implementation focuses on interfacing to the AssertionGroup,
 * the data structure that keeps the status of the running of individual assertions.
 *
 * @author bmajur
 *
 */
public abstract class ValComponentBase implements ValComponent {
    public AssertionGroup ag;
    public Event event;
    String name;
    String description;
    ValidationEngine validationEngine;
    static Logger logger = Logger.getLogger(ValComponentBase.class);

    ValComponentBase() {}

    ValComponentBase(Event event) {
        event.startNewValidator(this.class.simpleName)
        validationEngine = new ValidationEngine(this, event)
    }

    @Override void setEvent(Event event) { this.event = event }

    @Override void setAssertionGroup(AssertionGroup ag) { this.ag = ag }

    @Override String getName() { return name }

    @Override void setName(String name) { this.name = name }

    @Override String getDescription() { return description }

    @Override void setDescription(String description) { this.description = description }

    void runValidationEngine() throws SoapFaultException, RepositoryException { validationEngine.run() }

    ValidationEngine getValidationEngine() { return validationEngine }

    void withNewAssertionGroup() { ag = new AssertionGroup() }

    /******************************************
     *
     * Cooperate with ValidationEngine
     *
     * These assert calls are wrappers for the calls of the same
     * name(s) in AssertionGroup where the actual comparisons are made.
     *
     * The AssertionGroup calls make the actual comparisons and record
     * the assertions in the AssertionGroup.  The recordAssertion
     * method (this class) pulls the information encoded in the Java
     * annotation of the assertion and fills in the Assertion instance. It
     * also throws a SoapFaultException if appropriate. Different annotations
     * require that a SOAPFaul be generated if the asseration fails.
     * @throws SoapFaultException
     *
     */

    public boolean infoFound(boolean found) throws SoapFaultException {
        Assertion a = ag.infoFound(found);
        recordAssertion(a);
        return true;
    }

    public boolean infoFound(String found) throws SoapFaultException {
        Assertion a = ag.infoFound(found);
        recordAssertion(a);
        return true;
    }

    public boolean fail(String expected) throws SoapFaultException {
        Assertion a = ag.fail(expected);
        recordAssertion(a);
        return !a.failed();
    }

    public boolean assertIn(String[] expecteds, String value) throws SoapFaultException {
        Assertion a = ag.assertIn(expecteds, value);
        recordAssertion(a);
        return !a.failed();
    }

    public boolean assertEquals(String expected, String found) throws SoapFaultException {
        Assertion a = ag.assertEquals(expected, found);
        log.debug("Assertion: ${a}")
        recordAssertion(a);
        return !a.failed();
    }

    public boolean assertEquals(boolean expected, boolean found) throws SoapFaultException {
        Assertion a = ag.assertEquals(expected, found);
        log.debug("Assertion: ${a}")
        recordAssertion(a);
        return !a.failed();
    }

    public boolean assertEquals(int expected, int found) throws SoapFaultException {
        Assertion a = ag.assertEquals(expected, found);
        log.debug("Assertion: ${a}")
        recordAssertion(a);
        return !a.failed();
    }

    public boolean assertTrue(boolean value) throws SoapFaultException {
        Assertion a = ag.assertTrue(value);
        recordAssertion(a);
        return !a.failed();
    }

    public boolean assertTrueNoLog(boolean value) throws SoapFaultException {
        if (!value)
            return assertTrue(value);
        return true;
    }

    public boolean assertFalse(boolean value) throws SoapFaultException {
        Assertion a = ag.assertTrue(!value);
        recordAssertion(a);
        return !a.failed();
    }

    public boolean assertNotNull(Object value) throws SoapFaultException {
        Assertion a = ag.assertNotNull(value);
        recordAssertion(a);
        return !a.failed();
    }

    public boolean assertNotNullNoLog(Object value) throws SoapFaultException {
        if (value == null)
            return assertNotNull(value);
        return true;
    }

    /*******************************************************************
     * This collection of assertions is for when the ValidationEngine is not used
     */

    public boolean infoFound(boolean found, ValidationRef vr) {
        Assertion a = ag.infoFound(found);
        recordAssertion(a, vr);
        return true;
    }

    public boolean infoFound(String found, ValidationRef vr) {
        Assertion a = ag.infoFound(found);
        recordAssertion(a, vr);
        return true;
    }

    public boolean fail(String expected, ValidationRef vr) {
        Assertion a = ag.fail(expected);
        recordAssertion(a, vr);
        return !a.failed();
    }

    public boolean fail(ValidationRef vr) {
        Assertion a = ag.fail("");
        recordAssertion(a, vr);
        return !a.failed();
    }

    public boolean assertIn(String[] expecteds, String value, ValidationRef vr) {
        Assertion a = ag.assertIn(expecteds, value);
        recordAssertion(a, vr);
        return !a.failed();
    }

    public boolean assertEquals(String expected, String found, ValidationRef vr) {
        Assertion a = ag.assertEquals(expected, found);
        recordAssertion(a, vr);
        return !a.failed();
    }

    public boolean assertEquals(int expected, int found, ValidationRef vr) {
        Assertion a = ag.assertEquals(expected, found);
        recordAssertion(a, vr);
        return !a.failed();
    }

    public boolean assertTrue(boolean value, ValidationRef vr) {
        Assertion a = ag.assertTrue(value);
        recordAssertion(a, vr);
        return !a.failed();
    }

    public boolean assertTrueNoLog(boolean value, ValidationRef vr) {
        if (!value)
            return assertTrue(value, vr);
        return true;
    }

    public boolean assertFalse(boolean value, ValidationRef vr) {
        Assertion a = ag.assertTrue(!value);
        recordAssertion(a, vr);
        return !a.failed();
    }

    public boolean assertNotNull(Object value, ValidationRef vr) {
        Assertion a = ag.assertNotNull(value);
        recordAssertion(a, vr);
        return !a.failed();
    }

    public boolean assertNotNullNoLog(Object value, ValidationRef vr) {
        if (value == null)
            return assertNotNull(value, vr);
        return true;
    }

    /**************************************************************/

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
        if (validationEngine.validationFaultAnnotation != null) {
            ValidationFault vf = validationEngine.validationFaultAnnotation;
            recordAssertion(a, vf);
        }
        else if (validationEngine.validationAnnotation != null) {
            Validation vf = validationEngine.validationAnnotation;
            recordAssertion(a, vf);
        } else {
            throw new ToolkitRuntimeException("Failed to record assertion ${a}")
        }
    }

    private void recordAssertion(Assertion a, ValidationRef vr) {
        String id = vr.getId();
        if ("".equals(id)) {
            throw new RuntimeException(ExceptionUtil.here("Assertion has no id"));
        } else {
            if (idsAsserted.contains(id)) {
                a.setId(id);
                a.setMsg("Validator contains multiple assertions with this id");
                String[] refs = [ ]
                a.setReference(refs);
                a.setExpected("");
                a.setFound("");
                a.setCode(FaultCode.Receiver.toString());
                a.setStatus(AssertionStatus.INTERNALERROR);
                throw new RuntimeException("Validator contains multiple assertions with the id <" + id + ">");
            }
            idsAsserted.add(vr.getId());
        }

        a.setId(id);
        a.setCode(vr.getErrCode());
        a.setMsg(vr.getMsg());
        a.setReference(vr.getRef());
        a.setLocation(vr.getLocation());

        logger.debug("Assertion: " + a);
    }

    boolean validationAlreadyRecorded(String id) { idsAsserted.contains(id) }

    private void recordAssertion(Assertion a, Validation vf)
            throws SoapFaultException {

        log.debug("Recording validation ${vf.id()}")
        // if this is a duplicate assertion id
        if (validationAlreadyRecorded(vf.id())) {
            a.setId(vf.id());
            a.setMsg("Validator contains multiple assertions with this id");
            String[] refs = [ ]
            a.setReference(refs);
            a.setExpected("");
            a.setFound("");
            a.setCode(FaultCode.Receiver.toString());
            a.setStatus(AssertionStatus.INTERNALERROR);
            throw new SoapFaultException(
                    ag,
                    FaultCode.Receiver,
                    new ErrorContext("Validator contains multiple assertions with this id", "")
            );
        }
        idsAsserted.add(vf.id());

        String id = vf.id();
        a.setId(id);
        a.setMsg(vf.msg());
        a.setReference(vf.ref());
    }

    private void recordAssertion(Assertion a, ValidationFault vf)
            throws SoapFaultException {
//        if (validationAlreadyRecorded(vf.id())) {
//            a.setId(vf.id());
//            a.setMsg("Validator contains multiple assertions with this id");
//            String[] refs = [ ]
//            a.setReference(refs);
//            a.setExpected("");
//            a.setFound("");
//            a.setCode(FaultCode.Receiver.toString());
//            a.setStatus(AssertionStatus.INTERNALERROR);
//            throw new SoapFaultException(
//                    ag,
//                    FaultCode.Receiver,
//                    new ErrorContext("Validator contains multiple assertions with this id", "")
//            );
//        }
        idsAsserted.add(vf.id());

        a.setId(vf.id());
        a.setMsg(vf.msg());
        a.setReference(vf.ref());
        a.setCode(vf.faultCode().toString());
        if (a.getStatus().isError()) {
            a.setStatus(AssertionStatus.FAULT);
            throw new SoapFaultException(
                    ag,
                    vf.faultCode(),
                    new ErrorContext(a.getMsg(), new AssertionDAO().buildSemiDivided(vf.ref()))
            );
        }
    }
}
