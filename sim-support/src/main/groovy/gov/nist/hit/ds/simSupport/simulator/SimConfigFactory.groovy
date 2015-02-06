package gov.nist.hit.ds.simSupport.simulator
import gov.nist.hit.ds.actorTransaction.ActorType
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.config.RepositoryUniqueIdSimConfigElement
import gov.nist.hit.ds.simSupport.config.RetrieveTransactionSimConfigElement
import gov.nist.hit.ds.simSupport.config.SimConfig
import gov.nist.hit.ds.simSupport.config.TransactionSimConfigElement
import gov.nist.hit.ds.simSupport.endpoint.EndpointBuilder
import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException
import groovy.util.logging.Log4j
/**
 * Created by bmajur on 6/6/14.
 */
@Log4j
class SimConfigFactory {

    SimConfig buildSim(SimSystemConfig config, String user, SimId simId, ActorType actorType) {
//        buildSim(config.host, config.port, config.service, user, simId, actorType)
//    }
//
//    SimConfig buildSim(String server, String port, String base, String user, SimId simId, ActorType actorType) {
        log.debug("SimConfigFactory: actorType ${actorType.name}")
        SimConfig actorSimConfig = new SimConfig(actorType)

        EndpointBuilder endpointBuilder = new EndpointBuilder(config, user, simId)

        // Not clear that properties are being used at the moment
        actorType.props.each { name, value ->
            log.debug("SimConfigFactory: property ${name}")
            // I should use reflection to do fancy stuff here.  Maybe later.
            if (name == 'RepositoryUniqueId') {
                def ele = new RepositoryUniqueIdSimConfigElement()
                actorSimConfig.add(ele)
            } else {
                def msg = "ActorType ${actorTypeName} has property class ${name} which does not extend class AbstractActorSimConfigElement"
                log.fatal(msg)
                throw new ToolkitRuntimeException(msg)
            }
        }

        actorType.endpointTypes().each {
            log.debug("SimConfigFactory: endpoint ${it}")
            if (it.transType.isRetrieve) {
                actorSimConfig.add(new RetrieveTransactionSimConfigElement(it, endpointBuilder.makeEndpoint(actorType, it)))
            } else {
                actorSimConfig.add(new TransactionSimConfigElement(it, endpointBuilder.makeEndpoint(actorType, it)))
            }
        }
        return actorSimConfig
    }
}
