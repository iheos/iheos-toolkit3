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
import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException
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
    String dots = '...'

    enum Relation { NONE, PEER, CHILD, SELF}
    Relation parentRelation = Relation.PEER
    ValComponentBase parent = null
    int level = 0

    @Delegate AssertionApi assertionApi

    ValComponentBase() {
        assertionApi = new AssertionApi(this)
    }

    ValComponentBase(Event _event) {
        event = _event
        assertionApi = new AssertionApi(this)
//        log.debug "ValComponentBase() - ${this.class.name} - ${event}"
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
//        log.info("Validator: ${parentRelation} ${name}")
        if (event == null) log.error("Validator ${name} not initialized correctly, must call super(event) in constructor.")
//        log.debug("resultsStack before init: ${event.resultsStack}")
        if (parentRelation == Relation.NONE) throw new ToolkitRuntimeException("Validation ${name} has no established relationhip to parent")
        if (parentRelation == Relation.PEER) event.addPeerResults(name)
        else if (parentRelation == Relation.CHILD) event.addChildResults(name)
        else if (parentRelation == Relation.SELF) event.addSelfResults(name)
//        log.debug("resultsStack after init: ${event.resultsStack}")
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

    @Override void setAssertionGroup(AssertionGroup ag) { setAg(ag) }

    def setAg(AssertionGroup ag) { this.ag = ag; assertionApi.ag = ag }

    @Override String getName() { return name }

    @Override void setName(String name) { this.name = name }

    @Override String getDescription() { return description }

    @Override void setDescription(String description) { this.description = description }

    @Override public boolean showOutputInLogs() { return true }

    public void run() throws SoapFaultException, RepositoryException { runValidationEngine() }

    void runBefore() {}
    void runAfter() {}

    ValidationEngine getValidationEngine() { return validationEngine }

//    void withNewAssertionGroup() { ag = new AssertionGroup() }

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


}
