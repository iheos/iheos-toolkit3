package gov.nist.hit.ds.eventLog

import gov.nist.hit.ds.eventLog.assertion.AssertionGroupDAO
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.api.RepositoryException

/**
 * Created by bill on 4/15/14.
 */
class EventDAO {

    void save(Event event) throws RepositoryException {
        Asset a;
        def inOut = new InOutMessagesDAO();
        a = inOut.init(event);
        a.setOrder(1);

        def artifacts = new ArtifactsDAO();
        a = artifacts.init(event);
        a.setOrder(2);

        def assertionGroupDAO = new AssertionGroupDAO();
        a = assertionGroupDAO.init(event);
        a.setOrder(3);

        // Created only if needed
        if (event.fault) {
            def fault = new Fault();
            fault.init(event)
            fault.setOrder(4)
        }
    }

}
