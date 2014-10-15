package gov.nist.hit.ds.simSupport.serializer
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.EndpointType
import gov.nist.hit.ds.simSupport.client.ActorSimConfig
import gov.nist.hit.ds.simSupport.client.configElementTypes.*
import gov.nist.hit.ds.simSupport.endpoint.EndpointValue
import groovy.xml.MarkupBuilder
/**
 * Created by bmajur on 9/23/14.
 */

// TODO: needs unit tests
class SimulatorDAO {

    String toXML(ActorSimConfig config) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.actor(type: config.actorType.shortName) {
            def transactions = config.elements.findAll { SimConfigElement ele ->
                ele instanceof TransactionSimConfigElement
            }
            def transactionNames = transactions.collect { it.transactionName } as Set
            transactionNames.each { transactionName ->
                def trans = transactions.findAll { it.transactionName == transactionName }
                def aTrans = trans.first()
                xml.transaction(name: trans.first().transactionName) {
                    xml.endpoint(value: trans.first().endpointValue.value, readOnly: true)
                    xml.settings() {
                        aTrans.elements.each { SimConfigElement tele ->
                            if (tele instanceof BooleanSimConfigElement) {
                                xml.boolean(name: tele.name, value: tele.value)
                            }
                        }
                    }
                    transactions.each {
                        xml.webService(value: it.endpointType.label())
                    }
                }
            }
            config.elements.each { SimConfigElement ele ->
                if (ele instanceof BooleanSimConfigElement) {
                    xml.boolean(name: ele.name, value: ele.value)
                } else if (ele instanceof CallbackSimConfigElement) {
                    xml.callback(name: ele.name, transaction: ele.transactionId, restUrl: ele.restURL)
                } else if (ele instanceof TransactionSimConfigElement) {
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
            def endpointString = trans.endpoint.@value as String
            def setting = [ : ]
            trans.settings.boolean.each { settings ->
                setting[settings.@name as String] = bool(settings.@value as String)
            }
            trans.webService.each { ws ->
                def label = ws.@value as String
                EndpointType etype = new EndpointType(actorSimConfig.actorType, label)
                assert etype.transType
                TransactionSimConfigElement transactionSimConfigElement = new TransactionSimConfigElement(etype, new EndpointValue(endpointString))
                setting.each { type, value -> transactionSimConfigElement.setBool(type, value)}
                actorSimConfig.add(transactionSimConfigElement)
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
