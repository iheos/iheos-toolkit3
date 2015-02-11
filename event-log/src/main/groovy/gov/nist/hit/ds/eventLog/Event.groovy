package gov.nist.hit.ds.eventLog
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup
import gov.nist.hit.ds.eventLog.assertion.AssertionGroupDAO
import gov.nist.hit.ds.eventLog.assertion.AssertionStatus
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.repository.shared.ValidationLevel
import groovy.util.logging.Log4j

/**
 * Created by bill on 4/15/14.
 */
@Log4j
class Event {
    InOutMessages inOut = new InOutMessages()
    Artifacts artifacts = new Artifacts();
    Set<AssertionGroup> allAssetionGroups = []
    Fault fault = null
    Asset eventAsset
    EventDAO eventDAO
    ArtifactsDAO artDAO
    Asset validatorsAsset
    int displayOrder = 1
    static ValidationLevel defaultValidationLevel = ValidationLevel.ERROR
    ValidationLevel validationLevel = defaultValidationLevel

    ResultsStack resultsStack = new ResultsStack()

    /*
     * Asset types used
     * Event - event
     * Validators - validators
     * AssertionGroup - assertionGroup
     *
     * Remember that AssertionGroups can be nested (child results)
     * Error propagation up the tree is done by ResultsStack
     */

    Set<String> errorAssertionIds() { allAssetionGroups.collect { it.errorAssertionIds() }.flatten() as Set }

    // Parent present because results can be nested
    // Your parent is somewhere in the resultsStack
    def initResults(Asset parentAsset, validatorName) {
        if (!parentAsset) parentAsset = eventDAO.validatorsAsset
        def result = new ValidatorResults(parentAsset, validatorName, this)
        result.assertionGroup = new AssertionGroup(validationLevel)
        result.assertionGroup.validatorName = validatorName
        result.aDAO = new AssertionGroupDAO(result.assertionGroup, parentAsset, nextDisplayOrder());
        resultsStack.push(result)
    }
    ValidatorResults currentResults() { assert !resultsStack.empty(); return resultsStack.last() }

    def addPeerResults(validatorName) {
        log.debug("Add peer results ${validatorName} AG")
        Asset parent = (resultsStack.empty()) ? eventDAO.validatorsAsset : resultsStack.last().parentAsset
        initResults(parent, validatorName)
        resultsStack.last().flush(ValidatorResults.FlushStatus.Force)
    }
    def addChildResults(childName) {
        log.debug("Add child results ${childName} AG")
        assert !resultsStack.empty()
        def result = resultsStack.last()
        initResults(result.getaDAO().getAsset(), childName)
        result.flush(ValidatorResults.FlushStatus.Force)
    }
    def addSelfResults(validatorName) {
        log.debug("Add self results ${validatorName} AG")
        if (resultsStack.empty()) init()
    }

    def close() {
        log.debug "Closing ${resultsStack}"
        assert !resultsStack.empty()
        log.debug "Closing from ${resultsStack}"
        def result = resultsStack.pop()
        if (!resultsStack.empty())
            propagateStatus(result, resultsStack.last())
        result.flush(ValidatorResults.FlushStatus.Force)
        resultsStack.propogateStatus(this)
    }
    def popChildResults() {
        log.debug "Popping from ${resultsStack}"
        assert !resultsStack.empty()
        def result = resultsStack.pop()
        if (!resultsStack.empty()) {
            def parentResult = resultsStack.last()
            propagateStatus(result, parentResult)
        }
        if (result.getStatus() >= AssertionStatus.WARNING)
            result.flush(ValidatorResults.FlushStatus.Force)
        else
            result.flush(ValidatorResults.FlushStatus.NoForce)
    }

//    // TODO - I don't think this works - test and delete
    def propagateStatus(ValidatorResults from, ValidatorResults to) {
        log.debug("Propagate status from ${from.validatorName} to ${to.validatorName}?")
        log.info "status is ${from.getStatus()}"
        if (from.getStatus() >= AssertionStatus.WARNING) {
            to.setStatus(from.getStatus())
            log.debug "parent status is now ${to.getStatus()}"
        }
    }

    def flushAllResultsForExit() {
        // it is important that this be done bottom up so errors can
        // propagate up
        log.debug("Flushing results: empty? = ${resultsStack.empty()}")
        while (!resultsStack.empty()) { popChildResults() }
        flush()
    }

    Event(Asset eventAsset) {
        this.eventAsset = eventAsset
    }

    void init() {
        eventDAO = new EventDAO(this)
        eventDAO.init()
        artDAO = eventDAO.artifacts
        initResults(eventDAO.validatorsAsset, 'TopLevel')
        validatorsAsset = eventDAO.validatorsAsset
    }

    // Called by TransactionRunner when trying to flushAll an
    // entire event.
    void flushAll() {
        flushAllResultsForExit()
    }

    void flushOutput() {
        eventDAO.saveInOut()
    }

    def flushValidators() {
        if (!resultsStack.empty()) {
            ValidatorResults ele = resultsStack.last()
            ele.flush(ValidatorResults.FlushStatus.Force)
        }
    }

    void flush() {
        flushValidators()
        eventDAO.save()
    }

    boolean hasFault() { fault }

    boolean hasErrors() {
        allAssetionGroups.find { it.hasErrors() }
    }

    void addArtifact(String name, Object value) throws RepositoryException {
        if (!(value instanceof String))
            value = value.toString()
        artifacts.add(name, value);
    }

    InOutMessages getInOutMessages() { return inOut }
    AssertionGroup getAssertionGroup() { return currentResults().assertionGroup }
    AssertionGroup getAssertionGroup(String validatorName) {
        return allAssetionGroups.find { it.validatorName == validatorName }
    }
    def getAssertions(id) { resultsStack.getAssertions(id)}
    def getErrorAssertionIds() {
        allAssetionGroups.collect { it.errorAssertionIds() }.flatten()
    }

    int nextDisplayOrder() { displayOrder++ }

    String toString() { "Event(${eventAsset.id})"}
}
