package gov.nist.hit.ds.simServlet.api

import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.serializer.SimulatorDAO
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.utilities.SimUtils
/**
 * API used by REST calls
 * Created by bmajur on 10/23/14.
 */
class SimApi {

    static SimHandle create(String actorTypeName, String username, SimId simId) {
        SimUtils.create(actorTypeName, simId, username)
    }

    static delete(String username, SimId simId) {
        SimUtils.delete(simId, username)
    }

    // return is an XML blob
    static String getConfig(String username, SimId simId) {
        SimHandle simHandle = SimUtils.open(simId.toString(), username)
        return new String(simHandle.configAsset.content)
    }

    static String updateConfig(String username, SimId simId, String configXml) {
        SimHandle simHandle = SimUtils.open(simId.toString(), username)
        SimulatorDAO dao = new SimulatorDAO()
        // updates actorSimConfig with only the entries
        // that are allowed to be updated
        dao.updateModel(simHandle.actorSimConfig, configXml)
        // push update
        String updatedConfig = dao.toXML(simHandle.actorSimConfig)
        SimUtils.storeConfig(simHandle, updatedConfig)
        return updatedConfig
    }

}
