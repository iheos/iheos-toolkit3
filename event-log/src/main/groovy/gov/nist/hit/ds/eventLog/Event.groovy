package gov.nist.hit.ds.eventLog

import gov.nist.hit.ds.eventLog.assertion.AssertionGroup
import gov.nist.hit.ds.repository.api.RepositoryException

/**
 * Created by bill on 4/15/14.
 */
class Event {
    InOutMessages inOut = new InOutMessages()
    Artifacts artifacts = new Artifacts();
    AssertionGroup assertionGroup = new AssertionGroup()
    Fault fault = null;

    boolean hasErrors() {
        assertionGroup.hasErrors()
    }

    void addArtifact(String name, String value) throws RepositoryException {
        artifacts.add(name, value);
    }

}
