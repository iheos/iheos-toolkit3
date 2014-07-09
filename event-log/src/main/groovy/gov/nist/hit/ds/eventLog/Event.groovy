package gov.nist.hit.ds.eventLog
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup
import gov.nist.hit.ds.eventLog.assertion.AssertionGroupDAO
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
    AssertionGroup assertionGroup = new AssertionGroup()
    Fault fault = null
    Asset eventAsset
    EventDAO eventDAO
    AssertionGroupDAO aDAO

    Event(Asset eventAsset) {
        this.eventAsset = eventAsset
    }

    void init() {
        eventDAO = new EventDAO(this)
        eventDAO.init()
        aDAO = new AssertionGroupDAO();
        aDAO.init(eventDAO)
    }

    boolean hasErrors() { assertionGroup.hasErrors() }

    void startNewValidator(validatorName) {
        flush()
//        if (assertionGroup?.hasContent()) {
//            log.debug("AG ${assertionGroup?.validatorName} has content")
//            aDAO.save(assertionGroup)
//        }
//        else log.debug("AG ${assertionGroup?.validatorName} disgarded")

        log.debug("Creating ${validatorName} AG")
        assertionGroup = new AssertionGroup()
        assertionGroup.validatorName = validatorName
    }

    void flush() {
        log.debug("Flushing ${assertionGroup.validatorName} AG")
        if (assertionGroup?.needsFlushing()) {
            aDAO.save(assertionGroup)
            assertionGroup.saved = true
        } else log.debug('Nothing to flush')
    }

    boolean hasFault() { fault }

    void addArtifact(String name, String value) throws RepositoryException {
        artifacts.add(name, value);
    }

    InOutMessages getInOutMessages() { return inOut }
    AssertionGroup getAssertionGroup() { return assertionGroup }
}
