package gov.nist.hit.ds.simServlet
import gov.nist.hit.ds.simServlet.api.SimApi
import gov.nist.hit.ds.simServlet.servlet.SimServlet
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.toolkit.Toolkit
import groovy.xml.StreamingMarkupBuilder
import spock.lang.Specification
/**
 * Created by bmajur on 1/21/15.
 */
class DocSrcTest extends Specification {
    def simId = new SimId('docsrc1')
    SimHandle simHandle

    def setup() {
        new SimServlet().init()
        SimApi.delete(simId)  // necessary to make sure create actually creates new, default is to keep old if present
        simHandle = SimApi.create('docsrc', simId)
    }

    def 'Create test'() {
        when:
        def configStr = SimApi.getConfig(simHandle.simId)
        def config = new XmlSlurper().parse(new StringReader(configStr))

        then: config.@type.text() == 'docsrc'
    }

    def 'Install environment test'() {
        expect: 'confirm external cache is initialized'
        Toolkit.externalCacheFile.isDirectory()

        when: 'get default config so it can be updated'
        String configStr = SimApi.getConfig(simHandle.simId)
        def config = new XmlSlurper().parse(new StringReader(configStr))

        and: 'set environemnt name into config xml and store'
        def environment = new XmlSlurper().parseText('<environment name="NA2015"/>')
        config.appendNode(environment)
        def outputBuilder = new StreamingMarkupBuilder()
        configStr = outputBuilder.bind{ mkp.yield config }

        and: 'parse updated xml'
        config = new XmlSlurper().parseText(configStr)

        then: 'confirm update to xml'
        config.name() == 'actor'
        config.environment[0].@name.text() == 'NA2015'

        when: 'update the config from the xml'
        SimApi.updateConfig(simId, configStr)

        and: 'retrieve config'
        configStr = SimApi.getConfig(simId)
        config = new XmlSlurper().parse(new StringReader(configStr))

        then: 'verify environment name saved'
        config.environment[0].@name.text() == 'NA2015'
    }
}
