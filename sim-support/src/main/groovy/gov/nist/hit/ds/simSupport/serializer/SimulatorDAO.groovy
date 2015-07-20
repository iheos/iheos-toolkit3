package gov.nist.hit.ds.simSupport.serializer
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.EndpointType
import gov.nist.hit.ds.simSupport.config.*
import gov.nist.hit.ds.simSupport.endpoint.EndpointValue
import gov.nist.hit.ds.toolkit.environment.EnvironmentAccess
import gov.nist.hit.ds.xdsExceptions.ToolkitException
import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException
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
        // TODO - for now only applies to client sims and this checker does not have enough context
        // to judge whether it is required
//        if (!environmentName) throw new ToolkitRuntimeException('Simulator config does not reference an environment.')
        try {
            if (environmentName) simConfig.environmentAccess = new EnvironmentAccess(environmentName)
        } catch (ToolkitRuntimeException tre) {
            throw new ToolkitRuntimeException("Simulator config has bad environment reference: ${tre.message}")
        }

        def hasTransactions = false
        actor.transaction.each { trans ->
            hasTransactions = true
            def name = trans.@name.text()
            if (!name) throw new ToolkitRuntimeException('Simulator config has bad transaction - no name specified')
            def endpointString = trans.endpoint.@value.text()
            if (!endpointString) throw new ToolkitRuntimeException('Simulator config has bad transaction - no endpoint specified')
            def ws = trans.webService
            if (!ws) throw new ToolkitRuntimeException('Simulator config has bad transaction - no webService specification included')
            def label = ws.@value.text()
            if (!label) throw new ToolkitRuntimeException('Simulator config has bad transaction - webService specification has no value')
            log.debug("Endpoint label is ${label}")
            EndpointType etype = new EndpointType(simConfig.actorType, label)

            if (name != etype.transType.code)
                throw new ToolkitRuntimeException("Simulator config has bad transaction - webService label ${etype.transType.code} does not match declared transaction ${name}")

            if (!etype.isValid())
                throw new ToolkitRuntimeException("Simulator config has bad transaction - Endpoint ${endpointString}: ${etype.nonValidErrorMsg()}")

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
        if (!hasTransactions) throw new ToolkitRuntimeException('Simulator config contains no configured transactions.')
        return simConfig
    }

    // elements update-able but endpoints are not
    static updateModel(SimConfig simConfig, String updateConfig) {
        log.debug("Updating config for ${simConfig}")
        SimConfig update = toModel(updateConfig)
        simConfig.transactions.each { TransactionSimConfigElement transElement ->
            def name = transElement.name
            TransactionSimConfigElement updateTransaction = update.transactions.find { it.name == name}
            if (!updateTransaction) return
//            log.debug("...${name} updated with ${updateTransaction.endpointValue}")
            transElement.elements = updateTransaction.elements
//            transElement.endpointValue = updateTransaction.endpointValue
        }
        simConfig.environmentAccess = update.environmentAccess
    }

    static boolean bool(String value) {
        if (value.compareToIgnoreCase('true') == 0) return true
        return false
    }
}
