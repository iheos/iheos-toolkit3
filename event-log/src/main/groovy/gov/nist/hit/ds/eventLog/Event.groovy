package gov.nist.hit.ds.eventLog
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.api.RepositoryException
/**
 * Created by bill on 4/15/14.
 */
class Event {
    InOutMessages inOut = new InOutMessages()
    Artifacts artifacts = new Artifacts();
    AssertionGroup assertionGroup = new AssertionGroup()
    Fault fault = null
    Asset eventAsset

    Event(Asset eventAsset) { this.eventAsset = eventAsset }

    boolean hasErrors() { assertionGroup.hasErrors() }

    boolean hasFault() { fault }

    void addArtifact(String name, String value) throws RepositoryException {
        artifacts.add(name, value);
    }

    InOutMessages getInOutMessages() { return inOut }
    AssertionGroup getAssertionGroup() { return assertionGroup }
}
