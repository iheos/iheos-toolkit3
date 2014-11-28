package gov.nist.hit.ds.simServlet

import gov.nist.hit.ds.simServlet.api.SimApi
import gov.nist.hit.ds.simServlet.servlet.SimServlet
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.config.TransactionSimConfigElement
import gov.nist.hit.ds.simSupport.manager.ActorSimConfigManager
import gov.nist.hit.ds.simSupport.serializer.SimulatorDAO
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import groovy.xml.XmlUtil
import spock.lang.Specification
/**
 * Created by bmajur on 10/7/14.
 */
class ConfigEditTest extends Specification {
    def simId1 = new SimId('ConfigEditTest1')
    def simId2 = new SimId('ConfigEditTest2')
    def simId3 = new SimId('ConfigEditTest3')
    def simServlet
    def simHandle

    def setup() {
        simServlet = new SimServlet()
        simServlet.init()
        SimUtils.delete(simId1)
        SimUtils.delete(simId2)
    }

    def cleanup() {
    }

    def 'Sim create should include Transaction config' () {
        when: '''Create a sim'''
        simHandle = SimUtils.recreate('docrec', simId1)

        then:
        simHandle.actorSimConfig.transactions.size() > 0
    }

    def 'Sim Config edits should be persist'() {
        when: '''Create a sim'''
        simHandle = SimUtils.recreate('docrec', simId1)

        and: '''Change the Sim's SCHEMACHECK setting.'''
        // set no message validation options
        def actorSimConfigManager = new ActorSimConfigManager(simHandle.actorSimConfig)
        println actorSimConfigManager
        def configs = actorSimConfigManager.getSimConfigElements()
        def config = configs.first()
        config.setBool(TransactionSimConfigElement.SCHEMACHECK, false)
        actorSimConfigManager.save(simHandle.configAsset)

        and: '''Reload sim configuration'''
        def simHandle2 = SimUtils.open(simId1)
        def actorSimConfigManager2 = new ActorSimConfigManager(simHandle2.actorSimConfig)
        def config2s = actorSimConfigManager2.getSimConfigElements()
        def config2 = config2s.first()

        then: '''Schema Check setting should have persisted'''
        !config2.getBool(TransactionSimConfigElement.SCHEMACHECK).value
    }

    def bool(String str) { str.toLowerCase().equals('true')}

    Boolean getSchemaCheck(String actorXML) {
        def actor = new XmlSlurper().parseText(actorXML)
        def value = actor.transaction.find {
            println "Got transaction ${it.@name.text()}"
            it.@name.text() == 'prb'
        }.settings.boolean.find {
            it.@name.text() == 'schemaCheck'
        }.@value.text()
        value = new Boolean(bool(value))
        return value
    }

    String setSchemaCheck(String actorXML, boolean value) {
        def actor = new XmlSlurper().parseText(actorXML)
        def transaction = actor.transaction.find {
            it.@name == 'prb'
        }
        def node = transaction.settings.boolean.find {
            it.@name == 'schemaCheck'
        }
        node.@value = value as String
        return XmlUtil.serialize(actor)
    }

    String getMsgCallback(String actorXML) {
        def actor = new XmlSlurper().parseText(actorXML)
        def value = actor.transaction.find {
            it.@name == 'prb'
        }.settings.text.find {
            it.@name == 'msgCallback'
        }.@value.text()
        return value
    }

    String setMsgCallback(String actorXML, String value) {
        def actor = new XmlSlurper().parseText(actorXML)
        def transaction = actor.transaction.find {
            it.@name == 'prb'
        }
        def node = transaction.settings.text.find {
            it.@name == 'msgCallback'
        }
        node.@value = value
        return XmlUtil.serialize(actor)
    }

    def 'Verify getSchemaCheck and setSchemaCheck'() {
        when: '''Create a sim'''
        simHandle = SimUtils.create('docrec', simId2)
        def updater = new SimApi()

        and: '''Get config (XML) through API'''
        def config = updater.getConfig(simHandle.simId)

        and: 'set schemaCheck to true'
        config = setSchemaCheck(config, true)

        then: '''Verify  value of schemaCheck'''
        getSchemaCheck(config)

        when: 'set schemaCheck to false'
        config = setSchemaCheck(config, false)

        then: '''Verify value of schemaCheck'''
        !getSchemaCheck(config)

    }

    def 'Model should show updates'() {
        when: '''Create a sim'''
        SimUtils.delete(simId2)
        simHandle = SimUtils.recreate('docrec', simId2)
        def updater = new SimApi()

        and: '''Get config (XML) through API'''
        def configString = updater.getConfig(simHandle.simId)
        println 'Config as Read' + configString

        and: '''Update config - set schemaCheck to false'''
        def config2String = setSchemaCheck(configString, false)
        println 'Config as Updated' + config2String

        then:  'In memory copy has been updated'
        !getSchemaCheck(config2String)

        when: 'Save changes to model'
        SimulatorDAO dao = new SimulatorDAO()
        dao.updateModel(simHandle.actorSimConfig, config2String)

        and: 'Get changes back in XML'
        String updatedConfig = dao.toXML(simHandle.actorSimConfig)

        then:
        !getSchemaCheck(updatedConfig)
    }

    def 'Update of boolean settings should be saved in config'() {
        when: '''Create a sim'''
        SimUtils.delete(simId2)
        simHandle = SimUtils.recreate('docrec', simId2)
        def updater = new SimApi()

        and: '''Get config (XML) through API'''
        def config = updater.getConfig(simHandle.simId)
        println 'Config as Read' + config

        and: '''Update config - set schemaCheck to false'''
        def config2 = setSchemaCheck(config, false)
        println 'Config as Updated' + config2

        then:  'In memory copy has been updated'
        !getSchemaCheck(config2)

        when: 'Save out updates'
        updater.updateConfig(simHandle.simId, config2)

        and: '''Re-read updated config'''
        def config3 = updater.getConfig(simHandle.simId)

        then: '''Verify change has been changed'''
        !getSchemaCheck(config3)
    }

    def 'Update of text settings should be saved in config'() {
        when: '''Create a sim'''
        def api = new SimApi()
        api.delete(simId3)
        simHandle = api.create('docrec', simId3)

        and: '''Get config (XML) through API'''
        def config = api.getConfig(simHandle.simId)
        println 'Config as Read' + config

        and: '''Update config - set schemaCheck to false'''
        def config2 = setMsgCallback(config, 'http://foo/bar')
        println 'Config as Updated' + config2

        then:  'In memory copy has been updated'
        getMsgCallback(config2) == 'http://foo/bar'

        when: 'Save out updates'
        api.updateConfig(simHandle.simId, config2)

        and: '''Re-read updated config'''
        def config3 = api.getConfig(simHandle.simId)

        then: '''Verify change has been changed'''
        getMsgCallback(config3) == 'http://foo/bar'
    }

}
