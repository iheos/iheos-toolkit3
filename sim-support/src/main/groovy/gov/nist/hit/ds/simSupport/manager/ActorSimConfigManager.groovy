package gov.nist.hit.ds.simSupport.manager

import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.simSupport.config.SimConfig
import gov.nist.hit.ds.simSupport.config.ServerTransactionSimConfigElement
import gov.nist.hit.ds.simSupport.serializer.SimulatorDAO

/**
 * Created by bmajur on 10/7/14.
 */
/**
 * Manage Simulator configurations
 */

class ActorSimConfigManager {
    SimConfig actorSimConfig

    ActorSimConfigManager(SimConfig _actorSimConfig) {
        actorSimConfig = _actorSimConfig
    }

    def save(Asset a) {
        a.updateContent(new SimulatorDAO().toXML(actorSimConfig))
    }

    List<ServerTransactionSimConfigElement> getSimConfigElements() { actorSimConfig.transactions }
}
