package gov.nist.hit.ds.simSupport.serializer

import gov.nist.hit.ds.simSupport.client.ActorSimConfig
import gov.nist.hit.ds.simSupport.client.configElementTypes.*
import groovy.xml.MarkupBuilder
/**
 * Created by bmajur on 9/23/14.
 */
class SimulatorDAO {

    String toXML(ActorSimConfig config) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
                xml.actor(type: config.actorType.shortName) {
                    config.elements.each { SimConfigElement ele ->
                        if (ele instanceof BooleanSimConfigElement) {
                            xml.boolean(name: ele.name, value: ele.value)
                        } else if (ele instanceof CallbackSimConfigElement) {
                            xml.callback(name: ele.name, transaction: ele.transactionId, restUrl: ele.restURL)
                        } else if (ele instanceof EndpointSimConfigElement) {
                            xml.endpoint(value: ele.endpointValue.value, type: ele.endpointType.label())
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

//    Simulator toModel(String xmlText) {
//        Simulator s
//
//        def sl = new XmlSlurper().parseText(xmlText)
//        s = new Simulator(new SimId(sl.@id as String))
//        sl.actor.each { actor ->
//            def actorSimConfig = new ActorSimConfig()
//            s.add(actorSimConfig)
//            actorSimConfig.actorType = new ActorTransactionTypeFactory().getActorType(actor.@type as String)
//            actor.boolean.each { actorSimConfig.add(new BooleanSimConfigElement(it.@name as String, it.@value as String)) }
//            actor.callback.each { actorSimConfig.add(new CallbackSimConfigElement(it.@name as String, it.@transaction as String, it.@restUrl as String))}
//            actor.endpoint.each { e ->
//                EndpointType type = new EndpointType(actorSimConfig.actorType, e.@type as String)
//                EndpointValue endpoint = new EndpointValue(e.@value as String)
//                actorSimConfig.add(new EndpointSimConfigElement(type, endpoint))
//            }
//            actor.repositoryUid.each { actorSimConfig.add(new RepositoryUniqueIdSimConfigElement(it.@value as String)) }
//            actor.text.each { actorSimConfig.add(new TextSimConfigElement(it.@name as String, it.@value as String))}
//            actor.time.each { actorSimConfig.add(new TimeSimConfigElement(it.@name as String, it.@value as String))}
//        }
//        return s
//    }
}
