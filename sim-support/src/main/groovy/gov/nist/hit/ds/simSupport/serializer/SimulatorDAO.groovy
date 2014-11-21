package gov.nist.hit.ds.simSupport.serializer
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.EndpointType
import gov.nist.hit.ds.simSupport.config.SimConfig
import gov.nist.hit.ds.simSupport.client.configElementTypes.*
import gov.nist.hit.ds.simSupport.config.AbstractSimConfigElement
import gov.nist.hit.ds.simSupport.config.BooleanSimConfigElement
import gov.nist.hit.ds.simSupport.config.CallbackSimConfigElement
import gov.nist.hit.ds.simSupport.config.RepositoryUniqueIdSimConfigElement
import gov.nist.hit.ds.simSupport.config.TextSimConfigElement
import gov.nist.hit.ds.simSupport.config.TimeSimConfigElement
import gov.nist.hit.ds.simSupport.config.TransactionSimConfigElement
import gov.nist.hit.ds.simSupport.endpoint.EndpointValue
import groovy.xml.MarkupBuilder
/**
 * Created by bmajur on 9/23/14.
 */

// TODO: needs unit tests
class SimulatorDAO {

    String toXML(SimConfig config) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.actor(type: config.actorType.shortName) {
            def transactions = config.elements.findAll { AbstractSimConfigElement ele ->
                ele instanceof TransactionSimConfigElement
            }
            def transactionNames = transactions.collect { it.transactionName } as Set
            transactionNames.each { transactionName ->
                def trans = transactions.findAll { it.transactionName == transactionName }
                def aTrans = trans.first()
                xml.transaction(name: trans.first().transactionName) {
                    xml.endpoint(value: trans.first().endpointValue.value, readOnly: true)

                    xml.settings() {
                        aTrans.elements.each { AbstractSimConfigElement tele ->
                            if (tele instanceof BooleanSimConfigElement) {
                                xml.boolean(name: tele.name, value: tele.value)
                            }
                            if (tele instanceof TextSimConfigElement) {
                                xml.text(name: tele.name, value: tele.value)
                            }
                        }
                    }

                    transactions.each {
                        xml.webService(value: it.endpointType.label(), readOnly: true)
                    }
                }
            }
            config.elements.each { AbstractSimConfigElement ele ->
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

    SimConfig toModel(String xmlText) {
        def actor = new XmlSlurper().parseText(xmlText)
        SimConfig actorSimConfig = new SimConfig()
        actorSimConfig.actorType = new ActorTransactionTypeFactory().getActorType(actor.@type as String)
        actor.boolean.each { actorSimConfig.add(new BooleanSimConfigElement(it.@name as String, it.@value as Boolean)) }
        actor.callback.each { actorSimConfig.add(new CallbackSimConfigElement(it.@name as String, it.@transaction as String, it.@restUrl as String))}
        actor.transaction.each { trans ->
            def endpointString = trans.endpoint.@value as String

            def setting = [ : ]
            trans.settings.boolean.each { settings ->
                setting[settings.@name as String] = bool(settings.@value as String)
            }
            trans.settings.text.each { settings ->
                setting[settings.@name as String] = text(settings.@value as String)
            }
            trans.webService.each { ws ->
                def label = ws.@value as String
                EndpointType etype = new EndpointType(actorSimConfig.actorType, label)
                assert etype.transType
                TransactionSimConfigElement transactionSimConfigElement = new TransactionSimConfigElement(etype, new EndpointValue(endpointString))
                setting.each { type, value ->
                    if (value instanceof Boolean)
                        transactionSimConfigElement.setBool(type, value)
                    else
                        transactionSimConfigElement.setText(type, value)
                }
                actorSimConfig.add(transactionSimConfigElement)
            }

        }
        actor.repositoryUid.each { actorSimConfig.add(new RepositoryUniqueIdSimConfigElement(it.@value as String)) }
        actor.text.each { actorSimConfig.add(new TextSimConfigElement(it.@name as String, it.@value as String))}
        actor.time.each { actorSimConfig.add(new TimeSimConfigElement(it.@name as String, it.@value as String))}

        return actorSimConfig
    }

    // xmlText is the entire actor structure.  The only parts that can be updated are
    // the settings.  Rest are read only for now.
    SimConfig updateModel(SimConfig actorSimConfig, String xmlText) {
        def actor = new XmlSlurper().parseText(xmlText)
        actor.children().findAll { it.name() == 'transaction'}
        .each { trans ->
            def setting = [ : ]
            trans.settings.children().findAll { it.name() == 'boolean'}
            .each { buul ->
                setting[buul.@name.text()] = new Boolean(buul.@value.text())
            }
            trans.settings.children().findAll { it.name() == 'text'}
                    .each { buul ->
                setting[buul.@name.text()] = buul.@value.text()
            }
            TransactionSimConfigElement transactionSimConfigElement = actorSimConfig.getElements().first()
            if (!transactionSimConfigElement) return
            setting.each { name, value ->
                if (value instanceof Boolean)
                    transactionSimConfigElement.setBool(name, value)
                else
                    transactionSimConfigElement.setText(name, value)
            }
        }

        return actorSimConfig
    }

    boolean bool(String value) {
        if (value.compareToIgnoreCase('true') == 0) return true
        return false
    }

    String text(String value) { return value }

}
