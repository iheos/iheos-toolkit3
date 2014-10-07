package gov.nist.hit.ds.simServlet

import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.client.configElementTypes.TransactionSimConfigElement
import gov.nist.hit.ds.simSupport.manager.ActorSimConfigManager
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import spock.lang.Specification

/**
 * Created by bmajur on 10/7/14.
 */
class ConfigEditTest extends Specification {
    def simId = new SimId('ConfigEditTest')
    def simServlet
    def simHandle

    def setup() {
    }

    def cleanup() {
        SimUtils.delete(simId)
    }

    def 'Sim Config edits should be persist'() {
        when: '''Create a sim'''
        simServlet = new SimServlet()
        simServlet.init()
        simHandle = SimUtils.create('docrec', simId)

        and: '''Change the Sim's SCHEMACHECK setting.'''
        // set no message validation options
        def actorSimConfigManager = new ActorSimConfigManager(simHandle.actorSimConfig)
        TransactionSimConfigElement config = actorSimConfigManager.getSimConfigElement()
        config.setBool(TransactionSimConfigElement.SCHEMACHECK, false)
        actorSimConfigManager.save(simHandle.configAsset)

        and: '''Reload sim configuration'''
        def simHandle2 = SimUtils.open(simId)
        def actorSimConfigManager2 = new ActorSimConfigManager(simHandle2.actorSimConfig)
        TransactionSimConfigElement config2 = actorSimConfigManager2.getSimConfigElement()

        then: '''Schema Check setting should have persisted'''
        !config2.getBool(TransactionSimConfigElement.SCHEMACHECK).value
    }
}
