package gov.nist.hit.ds.eventLog

import gov.nist.hit.ds.eventLog.assertion.AssertionGroupDAO
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.api.RepositoryException

/**
 * Created by bill on 4/15/14.
 */
class EventDAO {

    void save(Event event) throws RepositoryException {
        if (event.eventAsset == null) return  // in-memory only

        Asset a

        def inOut = new InOutMessagesDAO()
        a = inOut.init(event.eventAsset)
        a.setOrder(1)
        inOut.save(event.inOutMessages)

        def artifacts = new ArtifactsDAO()
        a = artifacts.init(event.eventAsset)
        a.setOrder(2)
        artifacts.save(event.artifacts)

        def assertionGroup = new AssertionGroupDAO()
        a = assertionGroup.init(event.eventAsset);
        a.setOrder(3);
        assertionGroup.save(event.assertionGroup)

        // Created only if needed
        if (event.fault) {
            def fault = new FaultDAO();
            fault.init(event)
            fault.add(event.fault)
            fault.setOrder(4)
        }
    }

}
