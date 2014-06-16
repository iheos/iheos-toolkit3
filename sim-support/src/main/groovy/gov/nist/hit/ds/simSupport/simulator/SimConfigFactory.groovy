package gov.nist.hit.ds.simSupport.simulator
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.ActorType
import gov.nist.hit.ds.simSupport.client.ActorSimConfig
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.client.configElementTypes.EndpointActorSimConfigElement
import gov.nist.hit.ds.simSupport.client.configElementTypes.RepositoryUniqueIdSimConfigElement
import gov.nist.hit.ds.simSupport.endpoint.EndpointBuilder
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
import groovy.util.logging.Log4j
/**
 * Created by bmajur on 6/6/14.
 */
@Log4j
class SimConfigFactory {

    ActorSimConfig buildSim(String server, String port, String base, SimId simId, String actorTypeName) {
        log.debug("SimConfigFactory: actorType ${actorTypeName}")
        ActorType actorType= new ActorTransactionTypeFactory().getActorType(actorTypeName)
        ActorSimConfig actorSimConfig = new ActorSimConfig(actorType)
        EndpointBuilder endpointBuilder = new EndpointBuilder(server, port, base, simId)
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
        actorType.endpointLabels().each {
            log.debug("SimConfigFactory: endpoint ${it}")
            if (it.transType.hasTransactionProperty('RepositoryUniqueId')) {
                actorSimConfig.add(new RepositoryUniqueIdSimConfigElement(it, endpointBuilder.makeEndpoint(actorTypeName, it)))
            } else {
                actorSimConfig.add(new EndpointActorSimConfigElement(it, endpointBuilder.makeEndpoint(actorTypeName, it)))
            }
        }
        return actorSimConfig
    }
}
