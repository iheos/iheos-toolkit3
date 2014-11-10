package gov.nist.hit.ds.simServlet.api

import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.serializer.SimulatorDAO
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.utilities.SimUtils
/**
 * Created by bmajur on 10/23/14.
 */
class SimApi {

    SimHandle create(String actorTypeName, SimId simId) {
        SimUtils.create(actorTypeName, simId)
    }

    def delete(SimId simId) {
        SimUtils.delete(simId)
    }

    // return is an XML blob
    String getConfig(SimId simId) {
        SimHandle simHandle = SimUtils.open(simId)
        return new String(simHandle.configAsset.content)
    }

    def updateConfig(SimId simId, String configXml) {
        SimHandle simHandle = SimUtils.open(simId)
        SimulatorDAO dao = new SimulatorDAO()
        // updates actorSimConfig with only the entries
        // that are allowed to be updated
        dao.updateModel(simHandle.actorSimConfig, configXml)
        // push update
        String updatedConfig = dao.toXML(simHandle.actorSimConfig)
        SimUtils.storeConfig(simHandle, updatedConfig)
    }

}
