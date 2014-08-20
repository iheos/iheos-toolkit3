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
    List<AssertionGroup> allAssetionGroups = []
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

        ValidatorResults(Asset parentAsset) {
            this.parentAsset = parentAsset
            aDAO = new AssertionGroupDAO()
            aDAO.init(parentAsset)
        }
        AssertionStatus getStatus() { return assertionGroup.status() }
        def setStatus(AssertionStatus status) { assertionGroup.setErrorStatus(status) }
        Asset flush(FlushStatus flushStatus) {
            log.debug("Flushing ${assertionGroup.validatorName} AG")
            if (flushStatus == FlushStatus.Force || assertionGroup?.needsFlushing()) {
                allAssetionGroups << assertionGroup
                assertionGroup.saved = true
                def asset = aDAO.save(assertionGroup)
                return asset
            }
            return null
        }
    }
    List<ValidatorResults> resultsStack = []
    // Init results collection
    def initResults(Asset parentAsset, validatorName) {
        def result = new ValidatorResults(parentAsset)
        result.aDAO = new AssertionGroupDAO();
        result.aDAO.init(parentAsset)
        result.assertionGroup = new AssertionGroup()
        result.assertionGroup.validatorName = validatorName
        resultsStack << result
    }
    ValidatorResults currentResults() { assert resultsStack.size() > 0; return resultsStack.last() }

    def addPeerResults(validatorName) {
        assert !resultsStack.empty
        log.debug("Creating ${validatorName} AG")
        def result = resultsStack.last()
        result.flush(FlushStatus.NoForce)
        def parentAsset = result.parentAsset
        resultsStack.pop()  // remove item to be replaced
        initResults(parentAsset, validatorName)
    }
    def addChildResults(childName) {
        assert !resultsStack.empty
        def result = resultsStack.last()
        Asset parent = result.flush(FlushStatus.Force)
        initResults(parent, childName)
    }
    def popChildResults() {
        println 'Popping'
        assert !resultsStack.empty
        def result = resultsStack.pop()
        if (!resultsStack.empty) {
            def parentResult = resultsStack.last()
            println 'Has Parent'
            println "status is ${result.getStatus()}"
            if (result.getStatus() >= AssertionStatus.WARNING) {
                parentResult.setStatus(result.getStatus())
                println "parent status is now ${parentResult.getStatus()}"
                result.flush(FlushStatus.Force)
                return
            }
        }
        if (result.getStatus() >= AssertionStatus.WARNING)
            result.flush(FlushStatus.Force)
        else
            result.flush(FlushStatus.NoForce)
    }
    def flushAllResultsForExit() {
        // it is important that this be done bottom up so errors can
        // propagate up
        while (!resultsStack.empty) { popChildResults() }
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

    // start a new peer Validator
    void startNewValidator(validatorName) { addPeerResults(validatorName) }

    // Called by TransactionRunner when trying to flush an
    // entire event.
    void flush() {
        flushAllResultsForExit()
        if (!artifacts.empty()) {
            artDAO.save(artifacts)
        }
        if (fault) {
            def faultDAO = new FaultDAO()
            faultDAO.init(eventAsset)
            faultDAO.add(fault)
        }
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
