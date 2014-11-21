package gov.nist.hit.ds.simSupport.manager

import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.simSupport.config.SimConfig
import gov.nist.hit.ds.simSupport.config.TransactionSimConfigElement
import gov.nist.hit.ds.simSupport.serializer.SimulatorDAO

/**
 * Created by bmajur on 10/7/14.
 */
/**
 * Manage Simulator configurations - used only to support unit tests
 */

class SimConfigManager {
    SimConfig actorSimConfig

    SimConfigManager(SimConfig _actorSimConfig) { actorSimConfig = _actorSimConfig }

    def save(Asset a) { a.updateContent(new SimulatorDAO().toXML(actorSimConfig)) }

    TransactionSimConfigElement getSimConfigElement() { return actorSimConfig.elements.first() }
}
