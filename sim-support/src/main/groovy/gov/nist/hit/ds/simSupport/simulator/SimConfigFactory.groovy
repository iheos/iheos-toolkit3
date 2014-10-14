package gov.nist.hit.ds.simSupport.simulator
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.ActorType
import gov.nist.hit.ds.simSupport.client.ActorSimConfig
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.client.configElementTypes.RepositoryUniqueIdSimConfigElement
import gov.nist.hit.ds.simSupport.client.configElementTypes.RetrieveTransactionSimConfigElement
import gov.nist.hit.ds.simSupport.client.configElementTypes.TransactionSimConfigElement
import gov.nist.hit.ds.simSupport.endpoint.EndpointBuilder
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
import groovy.util.logging.Log4j
/**
 * Created by bmajur on 6/6/14.
 */
@Log4j
class SimConfigFactory {

    ActorSimConfig buildSim(String server, String port, String base, SimId simId, ActorType actorType) {
        log.debug("SimConfigFactory: actorType ${actorType.name}")
        ActorSimConfig actorSimConfig = new ActorSimConfig(actorType)

        EndpointBuilder endpointBuilder = new EndpointBuilder(server, port, base, simId)

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
