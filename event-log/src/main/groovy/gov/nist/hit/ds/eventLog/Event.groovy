package gov.nist.hit.ds.eventLog
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup
import gov.nist.hit.ds.eventLog.assertion.AssertionGroupDAO
import gov.nist.hit.ds.eventLog.assertion.AssertionStatus
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.api.RepositoryException
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


    // This class and the helper functions below it are used to manage
    // AssertionGroup result recording which can be a flat list or
    // have a tree structure. The storage area, resultsStack is part of
    // the state of the Event. The class definition and helper functions
    // exist to manage that stack.

    enum FlushStatus { NoForce, Force }

    class ValidatorResults {
        AssertionGroupDAO aDAO
        AssertionGroup assertionGroup = new AssertionGroup()
        Asset parentAsset
        String validatorName

        ValidatorResults(Asset parentAsset, String validatorName) {
            this.parentAsset = parentAsset
            this.validatorName = validatorName
            aDAO = new AssertionGroupDAO(assertionGroup, parentAsset)
        }
        AssertionStatus getStatus() { return assertionGroup.status() }
        def setStatus(AssertionStatus status) { assertionGroup.setErrorStatus(status) }

        Asset flush(FlushStatus flushStatus) {
            log.debug("Flushing ${assertionGroup.validatorName} AG")
            if (flushStatus == FlushStatus.Force || assertionGroup?.needsFlushing()) {
                allAssetionGroups << assertionGroup
                def asset = aDAO.save()
                return asset
            }
            return null
        }

        String toString() { "Results:${validatorName}"}
    }
    List<ValidatorResults> resultsStack = []

    // Init results collection
    def initResults(Asset parentAsset, validatorName) {
        if (!parentAsset) parentAsset = eventDAO.validatorsAsset
        def result = new ValidatorResults(parentAsset, validatorName)
        result.assertionGroup = new AssertionGroup()
        result.assertionGroup.validatorName = validatorName
        result.aDAO = new AssertionGroupDAO(result.assertionGroup, parentAsset);
        resultsStack << result
    }
    ValidatorResults currentResults() { assert resultsStack.size() > 0; return resultsStack.last() }

    def addPeerResults(validatorName) {
        log.debug("Add peer results ${validatorName} AG")
        Asset parent = (resultsStack.empty) ? eventDAO.validatorsAsset : resultsStack.last().parentAsset
        initResults(parent, validatorName)
        resultsStack.last().flush(FlushStatus.Force)
    }
    def addChildResults(childName) {
        log.debug("Add child results ${childName} AG")
        assert !resultsStack.empty
        def result = resultsStack.last()
//        initResults(result.parentAsset, childName)
        initResults(result.getaDAO().getAsset(), childName)
        result.flush(FlushStatus.Force)
    }
    def addSelfResults(validatorName) {
        log.debug("Add self results ${validatorName} AG")
        if (resultsStack.empty) init()
    }

    def close() {
        println "Closing ${resultsStack}"
        assert !resultsStack.empty
        println "Closing from ${resultsStack}"
        def result = resultsStack.pop()
        if (!resultsStack.empty)
            propagateStatus(result, resultsStack.last())
        result.flush(FlushStatus.Force)
    }
    def popChildResults() {
        println "Popping from ${resultsStack}"
        assert !resultsStack.empty
        def result = resultsStack.pop()
        if (!resultsStack.empty) {
            def parentResult = resultsStack.last()
            propagateStatus(result, parentResult)
        }
        if (result.getStatus() >= AssertionStatus.WARNING)
            result.flush(FlushStatus.Force)
        else
            result.flush(FlushStatus.NoForce)
    }

    def propagateStatus(ValidatorResults from, ValidatorResults to) {
        log.debug("Propagate status from ${from.validatorName} to ${to.validatorName}?")
        println "status is ${from.getStatus()}"
        if (from.getStatus() >= AssertionStatus.WARNING) {
            to.setStatus(from.getStatus())
            println "parent status is now ${to.getStatus()}"
        }
    }

    def flushAllResultsForExit() {
        // it is important that this be done bottom up so errors can
        // propagate up
        log.debug("Flushing results: empty? = ${resultsStack.empty}")
        while (!resultsStack.empty) { popChildResults() }
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
        if (!resultsStack.empty)
            resultsStack.last().flush(FlushStatus.Force)
    }

    void flush() {
        flushValidators()
        eventDAO.save()
    }

    boolean hasFault() { fault }

    boolean hasErrors() {
        allAssetionGroups.find { it.hasErrors() }
    }

    void addArtifact(String name, String value) throws RepositoryException {
        artifacts.add(name, value);
    }

    InOutMessages getInOutMessages() { return inOut }
    AssertionGroup getAssertionGroup() { return currentResults().assertionGroup }
    AssertionGroup getAssertionGroup(String validatorName) {
        return allAssetionGroups.find { it.validatorName == validatorName }
    }
}
