package gov.nist.hit.ds.eventLog

import gov.nist.hit.ds.eventLog.assertion.AssertionGroup
import gov.nist.hit.ds.eventLog.assertion.AssertionGroupDAO
import gov.nist.hit.ds.eventLog.assertion.AssertionStatus
import gov.nist.hit.ds.repository.api.Asset
import groovy.util.logging.Log4j

/**
 * Created by bmajur on 12/6/14.
 */

// This class and the helper functions below it are used to manage
// AssertionGroup result recording which can be a flat list or
// have a tree structure. The storage area, resultsStack is part of
// the state of the Event. The class definition and helper functions
// exist to manage that stack.

@Log4j
class ValidatorResults {
    enum FlushStatus { NoForce, Force }
    AssertionGroupDAO aDAO
    AssertionGroup assertionGroup = new AssertionGroup()
    Asset parentAsset
    String validatorName
    Event event

    ValidatorResults(Asset parentAsset, String validatorName, Event _event) {
        this.parentAsset = parentAsset
        this.validatorName = validatorName
        event = _event
        aDAO = new AssertionGroupDAO(assertionGroup, parentAsset)
    }
    AssertionStatus getStatus() { return assertionGroup.status() }
    def setStatus(AssertionStatus status) { assertionGroup.setErrorStatus(status) }

    // Returns AssertionGroup Asset
    Asset flush(FlushStatus flushStatus) {
        log.debug("Flushing ${assertionGroup.validatorName} AG")
        if (flushStatus == FlushStatus.Force || assertionGroup?.needsFlushing()) {
            assert event
            event.allAssetionGroups << assertionGroup
            def asset = aDAO.save()
            return asset
        }
        return null
    }

    def getAssertions(id) { assertionGroup.getAssertions(id)}
    def getAssertions() { assertionGroup.assertions }

    String toString() { "Results:${validatorName}"}
}
