package gov.nist.hit.ds.simSupport.validationEngine
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.eventLog.Fault
import gov.nist.hit.ds.eventLog.assertion.Assertion
import gov.nist.hit.ds.eventLog.assertion.AssertionDAO
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup
import gov.nist.hit.ds.eventLog.assertion.AssertionStatus
import gov.nist.hit.ds.eventLog.errorRecording.ErrorContext
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.utilities.datatypes.RequiredOptional
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
import groovy.util.logging.Log4j
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

// TODO: Needs refactoring - non assert methods should be delegated
@Log4j
public abstract class ValComponentBase implements ValComponent {
    public AssertionGroup ag;
    public Event event;
    String name;
    String description;
    ValidationEngine validationEngine;

    enum Relation { NONE, PEER, CHILD, SELF}
    Relation parentRelation = Relation.PEER
    ValComponentBase parent = null
    int level = 0

    ValComponentBase() {}

    ValComponentBase(Event _event) {
        event = _event
        log.debug "ValComponentBase() - ${this.class.name} - ${event}"
    }

    ValComponentBase(SimHandle simHandle) { this(simHandle.event)}

    ValComponentBase asPeer() { parentRelation = Relation.PEER; return this }
    ValComponentBase asChild() { parentRelation = Relation.CHILD; return this }
    ValComponentBase asSelf(ValComponentBase _parent) {
        parent = _parent
        level = parent.level + 1
        parentRelation = Relation.SELF
        return this
    }

    void runValidationEngine() throws SoapFaultException, RepositoryException {
        if (!name) name = this.class.simpleName
        log.info("Validator: ${parentRelation} ${name}")
        if (event == null) log.error("Validator ${name} not initialized correctly, must call super(event) in constructor.")
        log.debug("resultsStack before init: ${event.resultsStack}")
        if (parentRelation == Relation.NONE) throw new ToolkitRuntimeException("Validation ${name} has no established relationhip to parent")
        if (parentRelation == Relation.PEER) event.addPeerResults(name)
        else if (parentRelation == Relation.CHILD) event.addChildResults(name)
        else if (parentRelation == Relation.SELF) event.addSelfResults(name)
        log.debug("resultsStack after init: ${event.resultsStack}")
        try {

        runBefore()
//        event.flush()
        validationEngine = new ValidationEngine(this, event)
        validationEngine.run()
//        log.debug("Flushing ${parentRelation} ${name}")
//        event.flush()
        runAfter()
        event.flush()
        } catch (SoapFaultException sfe) {
            event.flush()
            throw sfe
        } finally {
            if (parentRelation != Relation.SELF) {
                log.debug("Closing ${parentRelation} ${name}")
                event.close()
            }
        }
    }

    def quit() { validationEngine.quit = true}

    ValidationMethod currentValidationMethod() { validationEngine.currentValidationMethod }

    @Override void setEvent(Event event) { this.event = event }

    @Override void setAssertionGroup(AssertionGroup ag) { this.ag = ag }

    @Override String getName() { return name }

    @Override void setName(String name) { this.name = name }

    @Override String getDescription() { return description }

    @Override void setDescription(String description) { this.description = description }

    @Override public boolean showOutputInLogs() { return true }

    public void run() throws SoapFaultException, RepositoryException { runValidationEngine() }

    void runBefore() {}
    void runAfter() {}

    ValidationEngine getValidationEngine() { return validationEngine }

    void withNewAssertionGroup() { ag = new AssertionGroup() }

    /******************************************
     *
     * Cooperate with ValidationEngine
     *
     * These assert calls are wrappers for the calls of the same
     * displayName(s) in AssertionGroup where the actual comparisons are made.
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
        Assertion a = ag.fail(msg, currentValidationMethod().required);
        recordAssertion(a);
        return a
    }

    public Assertion fail(String msg, String found) throws SoapFaultException {
        Assertion a = ag.fail(msg, found, currentValidationMethod().required);
        recordAssertion(a);
        return a
    }

    public boolean defaultMsg() throws SoapFaultException {
        Assertion a = ag.defaultMsg()
        recordAssertion(a)
        return true
    }

    public Assertion assertIn(String[] expecteds, String value) throws SoapFaultException {
        Assertion a = ag.assertIn(expecteds, value, currentValidationMethod().required);
        recordAssertion(a);
        return a
    }

    public Assertion assertIn(List<String> expecteds, String value) throws SoapFaultException {
        Assertion a = ag.assertIn(expecteds, value, currentValidationMethod().required);
        recordAssertion(a);
        return a
    }

    public Assertion assertEquals(String expected, String found) throws SoapFaultException {
        Assertion a = ag.assertEquals(expected, found, currentValidationMethod().required);
        log.debug("Assertion: ${a}")
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
        Assertion a = ag.assertEquals(expected, found, currentValidationMethod().required);
        log.debug("Assertion: ${a}")
        recordAssertion(a);
        return a
    }

    public Assertion assertHasValue(String value) throws SoapFaultException {
        Assertion a = ag.assertHasValue(value, currentValidationMethod().required);
        log.debug("Assertion: ${a}")
        recordAssertion(a);
        return a
    }

    public Assertion assertStartsWith(String value, String prefix) throws SoapFaultException {
        Assertion a = ag.assertStartsWith(value, prefix, currentValidationMethod().required);
        log.debug("Assertion: ${a}")
        recordAssertion(a);
        return a
    }

    public Assertion assertTrue(boolean value) throws SoapFaultException {
        Assertion a = ag.assertTrue(value, currentValidationMethod().required);
        recordAssertion(a);
        return a
    }

    public Assertion assertTrue(boolean value, String found) throws SoapFaultException {
        Assertion a = ag.assertTrue(value, found, currentValidationMethod().required);
        recordAssertion(a);
        return a
    }

    public Assertion assertMoreThan(int reference, int value) throws SoapFaultException {
        Assertion a = ag.assertMoreThan(reference, value, currentValidationMethod().required);
        recordAssertion(a);
        return a
    }

    public boolean assertTrueNoLog(boolean value) throws SoapFaultException {
        if (!value)
            return assertTrue(value);
        return true;
    }

    public Assertion assertFalse(boolean value) throws SoapFaultException {
        Assertion a = ag.assertTrue(!value, currentValidationMethod().required);
        recordAssertion(a);
        return a
    }

    public Assertion assertNotNull(Object value) throws SoapFaultException {
        Assertion a = ag.assertNotNull(value, currentValidationMethod().required);
        recordAssertion(a);
        return a
    }

    public boolean assertNotNullNoLog(Object value) throws SoapFaultException {
        if (value == null)
            return assertNotNull(value);
        return true;
    }

    /*******************************************************************
     * This collection of assertions is for when the ValidationEngine is not used
     */

//    public boolean infoFound(boolean found, ValidationRef vr) {
//        Assertion a = ag.infoFound(found);
//        recordAssertion(a, vr);
//        return true;
//    }
//
//    public boolean infoFound(String found, ValidationRef vr) {
//        Assertion a = ag.infoFound(found);
//        recordAssertion(a, vr);
//        return true;
//    }
//
//    public boolean fail(String expected, ValidationRef vr) {
//        Assertion a = ag.fail(expected, currentValidationMethod().required);
//        recordAssertion(a, vr);
//        return !a.failed();
//    }
//
//    public boolean fail(ValidationRef vr) {
//        Assertion a = ag.fail("", currentValidationMethod().required);
//        recordAssertion(a, vr);
//        return !a.failed();
//    }
//
//    public boolean assertIn(String[] expecteds, String value, ValidationRef vr) {
//        Assertion a = ag.assertIn(expecteds, value, currentValidationMethod().required);
//        recordAssertion(a, vr);
//        return !a.failed();
//    }
//
//    public boolean assertEquals(String expected, String found, ValidationRef vr) {
//        Assertion a = ag.assertEquals(expected, found, currentValidationMethod().required);
//        recordAssertion(a, vr);
//        return !a.failed();
//    }
//
//    public boolean assertEquals(int expected, int found, ValidationRef vr) {
//        Assertion a = ag.assertEquals(expected, found, currentValidationMethod().required);
//        recordAssertion(a, vr);
//        return !a.failed();
//    }
//
//    public boolean assertTrue(boolean value, ValidationRef vr) {
//        Assertion a = ag.assertTrue(value, currentValidationMethod().required);
//        recordAssertion(a, vr);
//        return !a.failed();
//    }
//
//    public boolean assertTrueNoLog(boolean value, ValidationRef vr) {
//        if (!value)
//            return assertTrue(value, vr);
//        return true;
//    }
//
//    public boolean assertFalse(boolean value, ValidationRef vr) {
//        Assertion a = ag.assertTrue(!value, currentValidationMethod().required);
//        recordAssertion(a, vr);
//        return !a.failed();
//    }
//
//    public boolean assertNotNull(Object value, ValidationRef vr) {
//        Assertion a = ag.assertNotNull(value, currentValidationMethod().required);
//        recordAssertion(a, vr);
//        return !a.failed();
//    }
//
//    public boolean assertNotNullNoLog(Object value, ValidationRef vr) {
//        if (value == null)
//            return assertNotNull(value, vr);
//        return true;
//    }

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
        if (validationEngine.validationAnnotation != null) {
            Validation vf = validationEngine.validationAnnotation
            recordAssertion(a, vf)
        } else {
            throw new ToolkitRuntimeException("Failed to record assertion ${a}")
        }
    }

    private void recordAssertion(Assertion a, Validation vf)
            throws SoapFaultException {

        log.debug("Recording validation ${vf.id()}")
        idsAsserted.add(vf.id());
        ValidationMethod validationMethod = currentValidationMethod()

        String id = vf.id();
        if (!validationMethod.required)
            a.setRequiredOptional(RequiredOptional.O)
        a.setId(id);
        def msgPrefix = []
        level.times { msgPrefix << '...'}
        msgPrefix = msgPrefix.join()
        a.setMsg(msgPrefix + vf.msg());
        a.setReference(vf.ref());
        if (a.getStatus().isError())
            a.setCode(currentValidationMethod().errorCode)
        if (a.getStatus().isError() && validationMethod.type == RunType.FAULT) {
            a.setCode(validationMethod.faultCode.toString())
            a.setStatus(AssertionStatus.FAULT)

            Fault f = new Fault(vf.msg(), validationMethod.faultCode.toString(), '??', '')
            event.fault = f
            event.flush()

            throw new SoapFaultException(
                    ag,
                    currentValidationMethod().faultCode,
                    new ErrorContext("${a.getMsg()} - ${a.expectedFoundString()}", new AssertionDAO().buildSemiDivided(vf.ref()))
            );
        }
    }

//    private void recordAssertion(Assertion a, ValidationRef vr) {
//        String id = vr.getId();
//        if ("".equals(id)) {
//            throw new RuntimeException(ExceptionUtil.here("Assertion has no id"));
//        } else {
//            if (idsAsserted.contains(id)) {
//                a.setId(id);
//                a.setMsg("Validator contains multiple assertions with this id");
//                String[] refs = [ ]
//                a.setReference(refs);
//                a.setExpected("");
//                a.setFound("");
//                a.setCode(FaultCode.Receiver.toString());
//                a.setStatus(AssertionStatus.INTERNALERROR);
//                throw new RuntimeException("Validator contains multiple assertions with the id <" + id + ">");
//            }
//            idsAsserted.add(vr.getId());
//        }
//
//        a.setId(id);
//        if (a.getStatus().isError())
//            a.setCode(currentValidationMethod().errorCode);
//        a.setMsg(vr.getMsg());
//        a.setReference(vr.getRef());
//        a.setLocation(vr.getLocation());
//
//        log.debug("Assertion: " + a);
//    }

    boolean validationAlreadyRecorded(String id) { idsAsserted.contains(id) }

}
