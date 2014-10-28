package gov.nist.hit.ds.simServlet
import gov.nist.hit.ds.simServlet.api.SimConfigUpdater
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.client.configElementTypes.TransactionSimConfigElement
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

    def 'Sim Config edits should be persist'() {
        when: '''Create a sim'''
        simHandle = SimUtils.create('docrec', simId1)

        and: '''Change the Sim's SCHEMACHECK setting.'''
        // set no message validation options
        def actorSimConfigManager = new ActorSimConfigManager(simHandle.actorSimConfig)
        TransactionSimConfigElement config = actorSimConfigManager.getSimConfigElement()
        config.setBool(TransactionSimConfigElement.SCHEMACHECK, false)
        actorSimConfigManager.save(simHandle.configAsset)

        and: '''Reload sim configuration'''
        def simHandle2 = SimUtils.open(simId1)
        def actorSimConfigManager2 = new ActorSimConfigManager(simHandle2.actorSimConfig)
        TransactionSimConfigElement config2 = actorSimConfigManager2.getSimConfigElement()

        then: '''Schema Check setting should have persisted'''
        !config2.getBool(TransactionSimConfigElement.SCHEMACHECK).value
    }

    Boolean getSchemaCheck(String actorXML) {
        def actor = new XmlSlurper().parseText(actorXML)
        def value = actor.transaction.find {
            it.@name == 'prb'
        }.settings.boolean.find {
            it.@name == 'schemaCheck'
        }.@value.text()
        value = new Boolean(value)
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

    def 'Verify getSchemaCheck and setSchemaCheck'() {
        when: '''Create a sim'''
        simHandle = SimUtils.create('docrec', simId2)
        def updater = new SimConfigUpdater()

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
        simHandle = SimUtils.create('docrec', simId2)
        def updater = new SimConfigUpdater()

        and: '''Get config (XML) through API'''
        def config = updater.getConfig(simHandle.simId)
        println 'Config as Read' + config

        and: '''Update config - set schemaCheck to false'''
        def config2 = setSchemaCheck(config, false)
        println 'Config as Updated' + config2

        then:  'In memory copy has been updated'
        !getSchemaCheck(config2)

        when: 'Save changes to model'
        SimulatorDAO dao = new SimulatorDAO()
        dao.updateModel(simHandle.actorSimConfig, config2)

        and: 'Get changes back in XML'
        String updatedConfig = dao.toXML(simHandle.actorSimConfig)

        then:
        !getSchemaCheck(updatedConfig)
    }

    def 'Update of settings should be saved in config'() {
        when: '''Create a sim'''
        SimUtils.delete(simId2)
        simHandle = SimUtils.create('docrec', simId2)
        def updater = new SimConfigUpdater()

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

}
