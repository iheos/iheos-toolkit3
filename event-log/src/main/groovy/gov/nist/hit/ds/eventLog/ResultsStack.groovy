package gov.nist.hit.ds.eventLog
import gov.nist.hit.ds.eventLog.assertion.AssertionStatus
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.shared.PropertyKey
import groovy.util.logging.Log4j
import org.apache.log4j.Logger

/**
 * Created by bmajur on 12/6/14.
 */

class ResultsStack {
    private static Logger log = Logger.getLogger(ResultsStack);
    List<ValidatorResults> stack = []
    List<ValidatorResults> backing = []  // entire history

    def push(ValidatorResults results) { stack.push(results); backing.push(results) }
    ValidatorResults pop() { stack.pop() }
    boolean empty() { stack.empty }
    ValidatorResults last() { stack.last()}
    def getAssertions(id) { backing.collect { it.getAssertions(id)}.flatten() }
    def getAssertions() { stack.collect { it.getAssertions()}.flatten()}
    def getWorstStatus() {
        def worsts = stack.collect { it.assertionGroup.worstStatus }
        AssertionStatus.getWorst(worsts)
    }
    String toString() { "ResultsStack: ${getAssertions().size()} Assertions, worst status is ${getWorstStatus()}"}

    def propogateStatusInAssertionGroup() {
        backing.each {
            if (it.assertionGroup.hasErrors() && it.assertionGroup.asset) setErrorOnAsset(it.assertionGroup.asset)
        }
    }

    def setErrorOnAsset(Asset a) {
        a.setProperty(PropertyKey.STATUS, "ERROR")
        a.autoFlush = true
        a.setProperty(PropertyKey.COLOR, "red")
    }

    def propogateStatusOneCycle() {
        def updated = false
        backing.each { ValidatorResults vr ->
            if (!vr.assertionGroup.asset) return
            def myStatus = vr.assertionGroup.asset.getProperty(PropertyKey.STATUS)
            def parentStatus = vr.parentAsset.getProperty(PropertyKey.STATUS)
            if (myStatus == 'ERROR' && parentStatus != 'ERROR') {
                updated = true
                setErrorOnAsset(vr.parentAsset)
            }
        }
        return updated
    }

    def markNullStatusAsSuccess() {
        backing.each { ValidatorResults vr ->
            if (!vr.assertionGroup.asset) return
            def myStatus = vr.assertionGroup.asset.getProperty(PropertyKey.STATUS)
            if (!myStatus) vr.assertionGroup.asset.setProperty(PropertyKey.STATUS, 'SUCCESS')
        }
    }

    // This will propogate up to the Validators asset
    def propogateStatusUpTree() {
        while (propogateStatusOneCycle())
            ;
    }

    def propogateStatus(Event event) {
        propogateStatusInAssertionGroup()
        propogateStatusUpTree()
        markNullStatusAsSuccess()
        def validatorsStatus = event.validatorsAsset.getProperty(PropertyKey.STATUS)
        if (validatorsStatus == 'ERROR') setErrorOnAsset(event.eventAsset)
        else event.eventAsset.setProperty(PropertyKey.STATUS, 'SUCCESS')
    }
}
