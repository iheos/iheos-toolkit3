package gov.nist.hit.ds.eventLog

import gov.nist.hit.ds.eventLog.assertion.AssertionGroup
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.api.RepositoryException

import javax.xml.ws.soap.SOAPFaultException

/**
 * Created by bill on 4/15/14.
 */
class Event {
    InOutMessages inOut = new InOutMessages()
    Artifacts artifacts = new Artifacts();
    AssertionGroup assertionGroup = new AssertionGroup()
    Fault fault = null;
    Asset eventAsset

    Event(Asset eventAsset) { this.eventAsset = eventAsset }

    boolean hasErrors() {
        assertionGroup.hasErrors()
    }

    void addArtifact(String name, String value) throws RepositoryException {
        artifacts.add(name, value);
    }

    InOutMessages getInOutMessages() { return inOut }
    AssertionGroup getAssertionGroup() { return assertionGroup }
    void setFault(def msg) {
        fault = new Fault()
        fault.add(msg)
    }

}
