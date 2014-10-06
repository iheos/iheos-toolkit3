package gov.nist.hit.ds.eventLog

import gov.nist.hit.ds.repository.AssetHelper
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.repository.simple.SimpleType
/**
 * Created by bill on 4/15/14.
 */
class EventDAO {
    def validatorsAsset = null
    Event event
    def artifacts
    def inOut

    def EventDAO(event) { this.event = event }

    void init() throws RepositoryException {
        if (event.eventAsset == null) return  // in-memory only

        Asset a

        inOut = new InOutMessagesDAO()
        a = inOut.init(event.eventAsset)
        a.setOrder(1)
//        inOut.save(event.inOutMessages)

        artifacts = new ArtifactsDAO()
        a = artifacts.init(event.eventAsset)
        a.setOrder(2)
  //      artifacts.save(event.artifacts)

        if (!validatorsAsset) {
            addValidatorsAsset()
        }

        // Created only if needed
//        if (event.fault) {
//            def fault = new FaultDAO();
//            fault.init(event.eventAsset)
//            fault.add(event.fault)
//            fault.asset.setOrder(4)
//        }
        save()
    }

    def save() {
        inOut.save(event.inOutMessages)
        artifacts.save(event.artifacts)
        if (event.fault) {
            def fault = new FaultDAO();
            fault.init(event.eventAsset)
            fault.add(event.fault)
            fault.asset.setOrder(4)
        }
    }

    def addValidatorsAsset() {
        if (event.eventAsset == null) return  // in-memory only
        if (validatorsAsset) return
        validatorsAsset =  AssetHelper.createChildAsset(event.eventAsset, "Validators", "", new SimpleType("validators"))
        validatorsAsset.setOrder(3)
    }

}
