package gov.nist.hit.ds.simSupport.serializer

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.EndpointType
import gov.nist.hit.ds.simSupport.client.ActorSimConfig
import gov.nist.hit.ds.simSupport.client.configElementTypes.*
import gov.nist.hit.ds.simSupport.endpoint.EndpointValue
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
import groovy.xml.MarkupBuilder
/**
 * Created by bmajur on 9/23/14.
 */
class SimulatorDAO {

    String toXML(ActorSimConfig config) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.actor(type: config.actorType.shortName) {
            // transname => node map
            config.elements.each { SimConfigElement ele ->
                if (ele instanceof BooleanSimConfigElement) {
                    xml.boolean(name: ele.name, value: ele.value)
                } else if (ele instanceof CallbackSimConfigElement) {
                    xml.callback(name: ele.name, transaction: ele.transactionId, restUrl: ele.restURL)
                } else if (ele instanceof TransactionSimConfigElement) {
                    xml.transaction(name: ele.transactionName) {
                        xml.endpoint(value: ele.endpointValue.value, type: ele.endpointType.label())
                        TransactionSimConfigElement tr = (TransactionSimConfigElement) ele
                        xml.settings() {
                            tr.elements.each { SimConfigElement tele ->
                                if (tele instanceof BooleanSimConfigElement) {
                                    xml.boolean(name: tele.name, value:tele.value)
                                }
                            }
                        }
                    }
                } else if (ele instanceof RepositoryUniqueIdSimConfigElement) {
                    xml.repositoryUid(value: ele.value)
                } else if (ele instanceof TextSimConfigElement) {
                    xml.text(name: ele.name, value: ele.value)
                } else if (ele instanceof TimeSimConfigElement) {
                    xml.time(name: ele.name, value: ele.date)
                } else {
                    assert false
                }
            }
        }
        return writer.toString()
    }

    ActorSimConfig toModel(String xmlText) {
        def actor = new XmlSlurper().parseText(xmlText)
        ActorSimConfig actorSimConfig = new ActorSimConfig()
        actorSimConfig.actorType = new ActorTransactionTypeFactory().getActorType(actor.@type as String)
        actor.boolean.each { actorSimConfig.add(new BooleanSimConfigElement(it.@name as String, it.@value as Boolean)) }
        actor.callback.each { actorSimConfig.add(new CallbackSimConfigElement(it.@name as String, it.@transaction as String, it.@restUrl as String))}
        actor.transaction.each { trans ->
            TransactionSimConfigElement transactionSimConfigElement = null
            // There should be exactly one endpoint
            trans.endpoint.each { e ->
                EndpointType type = new EndpointType(actorSimConfig.actorType, e.@type as String)
                EndpointValue endpoint = new EndpointValue(e.@value as String)
                transactionSimConfigElement = new TransactionSimConfigElement(type, endpoint)
                actorSimConfig.add(transactionSimConfigElement)
            }
            if (!transactionSimConfigElement) throw new ToolkitRuntimeException("Loading config.xml: <endpoint> required inside <transaction>")
            trans.settings.boolean.each {
                transactionSimConfigElement.setBool(it.@name as String, bool(it.@value as String))
            }
        }
        actor.repositoryUid.each { actorSimConfig.add(new RepositoryUniqueIdSimConfigElement(it.@value as String)) }
        actor.text.each { actorSimConfig.add(new TextSimConfigElement(it.@name as String, it.@value as String))}
        actor.time.each { actorSimConfig.add(new TimeSimConfigElement(it.@name as String, it.@value as String))}

        return actorSimConfig
    }

    boolean bool(String value) {
        if (value.compareToIgnoreCase('true') == 0) return true
        return false
    }
}
