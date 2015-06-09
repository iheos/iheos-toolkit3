package gov.nist.hit.ds.simServlet.api

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.TransactionType
import gov.nist.hit.ds.dsSims.eb.transactionSupport.EbSendRequest
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.client.SimIdentifier
import gov.nist.hit.ds.simSupport.serializer.SimulatorDAO
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException
import groovy.util.logging.Log4j

/**
 * API used by REST calls
 * Created by bmajur on 10/23/14.
 */
@Log4j
class SimApi {

    static SimHandle createServer(String actorTypeName, String username, SimId simId) {
        SimUtils.create(actorTypeName, simId, username)
    }

    static SimHandle createServer(String actorTypeName, String username, SimId simId, String configXml) {
        SimHandle simHandle = createServer(actorTypeName, username, simId)
        updateConfig(username, simId, configXml)
        return simHandle
    }

    static SimHandle createClient(String actorTypeName, String username, SimId simId, String configXml) {
        SimHandle simHandle = SimUtils.create(actorTypeName, simId, username)
        setConfig(username, simId, configXml)
        return simHandle
    }

    static delete(String username, SimId simId) {
        SimUtils.delete(simId, username)
    }

    // return is an XML blob
    static String getConfig(String username, SimId simId) {
        SimHandle simHandle = SimUtils.open(simId.toString(), username)
        return new String(simHandle.configAsset.content)
    }

    // Used to set the config of a client sim.  The requester controls the
    // endpoints so all aspects are under control of the requester
    static String setConfig(String username, SimId simId, String configXml) {
        SimHandle simHandle = SimUtils.open(simId.toString(), username)
        SimUtils.storeConfig(simHandle, configXml)
        configXml
    }

    // Used to update config of a server sim.  Most parameters are updateable
    // but endpoints are not. They are controlled by the simulator manager
    static String updateConfig(String username, SimId simId, String configXml) {
        SimHandle simHandle = SimUtils.open(simId.toString(), username)
        SimulatorDAO dao = new SimulatorDAO()
        // updates actorSimConfig with only the entries
        // that are allowed to be updated
        SimulatorDAO.updateModel(simHandle.actorSimConfig, configXml)
        // push update
        String updatedConfig = dao.toXML(simHandle.actorSimConfig)
        SimUtils.storeConfig(simHandle, updatedConfig)
        return updatedConfig
    }

    static SimHandle send(SimIdentifier simIdentifier, EbSendRequest request) {
        log.info "SimApi: Send ${request}"
        ActorTransactionTypeFactory factory = new ActorTransactionTypeFactory()
        TransactionType ttype = factory.getTransactionTypeIfAvailable(request.transactionName)
        if (!ttype) throw new ToolkitRuntimeException("client: no transaction type")
        SimHandle simHandle = SimUtils.open(simIdentifier)
        if (!simHandle)
            throw new ToolkitRuntimeException("Sim(${simIdentifier}) does not exist")
        simHandle.transactionType = ttype
        SimUtils.sendTransactionRequest(simHandle, request)
        SimUtils.close(simHandle)
        return simHandle
    }

}
