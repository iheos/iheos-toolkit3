package gov.nist.hit.ds.simSupport.manager

import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.simSupport.client.ActorSimConfig
import gov.nist.hit.ds.simSupport.client.configElementTypes.TransactionSimConfigElement
import gov.nist.hit.ds.simSupport.serializer.SimulatorDAO

/**
 * Created by bmajur on 10/7/14.
 */
/**
 * Manage Simulator configurations
 */

class ActorSimConfigManager {
    ActorSimConfig actorSimConfig

    ActorSimConfigManager(ActorSimConfig _actorSimConfig) {
        actorSimConfig = _actorSimConfig
    }

    def save(Asset a) {
        a.updateContent(new SimulatorDAO().toXML(actorSimConfig))
    }

    TransactionSimConfigElement getSimConfigElement() {
        return actorSimConfig.getByClass(TransactionSimConfigElement.class)
    }
}
