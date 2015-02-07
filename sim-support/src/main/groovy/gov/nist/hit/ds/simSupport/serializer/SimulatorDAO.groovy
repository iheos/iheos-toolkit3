package gov.nist.hit.ds.simSupport.serializer
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.EndpointType
import gov.nist.hit.ds.simSupport.config.*
import gov.nist.hit.ds.simSupport.endpoint.EndpointValue
import gov.nist.hit.ds.toolkit.environment.EnvironmentAccess
import groovy.util.logging.Log4j
import groovy.xml.MarkupBuilder
/**
 * Created by bmajur on 9/23/14.
 */

// TODO: needs unit tests
@Log4j
class SimulatorDAO {

    static String toXML(SimConfig config) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.actor(type: config.actorType.shortName) {
            if (config.environmentAccess) xml.environment(name: config.environmentAccess.name)
            config.transactions.each { transaction ->
                xml.transaction(name: transaction.name) {
                    xml.endpoint(value: transaction.endpointValue.value)
                    xml.settings() {
                        transaction.elements.each { AbstractConfigElement tele ->
                            if (tele instanceof BooleanSimConfigElement) {
                                xml.boolean(name: tele.name, value: tele.value)
                            }
                            else if (tele instanceof TextSimConfigElement) {
                                xml.text(name: tele.name, value: tele.value)
                            }
                            else if (tele instanceof CallbackSimConfigElement) {
                                xml.callback(name: tele.name, value: tele.value)
                            } else
                                assert "Cannot save SimConfigElement of type ${tele.class.name}"
                        }
                    }
                    xml.webService(value: transaction.endpointType.label())
                }
            }
        }
        return writer.toString()
    }

    static SimConfig toModel(String xmlText) {
        def actor = new XmlSlurper().parseText(xmlText)
        SimConfig simConfig = new SimConfig()

        simConfig.actorType = new ActorTransactionTypeFactory().getActorType(actor.@type.text())
        assert simConfig.actorType

        String environmentName = actor.environment.@name.text()
        if (environmentName) simConfig.environmentAccess = new EnvironmentAccess(environmentName)

        actor.transaction.each { trans ->
            def endpointString = trans.endpoint.@value.text()
            def ws = trans.webService
            assert ws
            def label = ws.@value.text()
            EndpointType etype = new EndpointType(simConfig.actorType, label)

            assert etype.transType
            TransactionSimConfigElement transElement = new TransactionSimConfigElement(etype, new EndpointValue(endpointString))
            simConfig.add(transElement)

            transElement.clear() // remove defaults
            trans.settings.each { setting ->
                setting.boolean.each {
                    transElement.add(new BooleanSimConfigElement(it.@name.text(), bool(it.@value.text())))
                }
                setting.text.each {
                    transElement.add(new TextSimConfigElement(it.@name.text(), it.@value.text()))
                }
                setting.callback.each { transElement.add(new CallbackSimConfigElement(it.@value.text())) }
            }
        }
        return simConfig
    }

    static updateModel(SimConfig simConfig, String updateConfig) {
        SimConfig update = toModel(updateConfig)
        simConfig.transactions.each { TransactionSimConfigElement transElement ->
            def name = transElement.name
            TransactionSimConfigElement updateTransaction = update.transactions.find { it.name == name}
            if (!updateTransaction) return
            transElement.elements = updateTransaction.elements
        }
        simConfig.environmentAccess = update.environmentAccess
    }

    static boolean bool(String value) {
        if (value.compareToIgnoreCase('true') == 0) return true
        return false
    }
}
