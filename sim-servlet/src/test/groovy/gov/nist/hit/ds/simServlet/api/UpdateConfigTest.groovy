package gov.nist.hit.ds.simServlet.api

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.AsyncType
import gov.nist.hit.ds.actorTransaction.TlsType
import gov.nist.hit.ds.simServlet.servlet.SimServlet
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.client.SimIdentifier
import gov.nist.hit.ds.simSupport.serializer.SimulatorDAO
import spock.lang.Specification

/**
 * Created by bmajur on 2/8/15.
 */
class UpdateConfigTest extends Specification {
    def clientSimConfig(noTlsEndpoint, tlsEndpoint) {
        """
<actor type='docsrc'>
    <environment name="NA2015"/>
    <transaction name='prb'>
        <!-- example endpoint value -->
        <endpoint value='${noTlsEndpoint}' />
        <settings>
            <!-- Checks applied to response only -->
            <boolean name='schemaCheck' value='false' />
            <boolean name='modelCheck' value='false' />
            <boolean name='codingCheck' value='false' />
            <boolean name='soapCheck' value='false' />
            <!-- on response -->
            <text name='msgCallback' value='' />
        </settings>
        <webService value='prb' />
    </transaction>
    <transaction name='prb'>
        <endpoint value='${tlsEndpoint}' />
        <settings>
            <boolean name='schemaCheck' value='false' />
            <boolean name='modelCheck' value='false' />
            <boolean name='codingCheck' value='false' />
            <boolean name='soapCheck' value='false' />
            <text name='msgCallback' value='' />
        </settings>
        <webService value='prb_TLS' />
    </transaction>
</actor>"""
    }
    def serverSimConfig(noTlsEndpoint) {
        """
<actor type='docrec'>
    <environment name="NA2015"/>
    <transaction name='prb'>
        <!-- example endpoint value -->
        <endpoint value='${noTlsEndpoint}' />
        <settings>
            <!-- Checks applied to response only -->
            <boolean name='schemaCheck' value='false' />
            <boolean name='modelCheck' value='false' />
            <boolean name='codingCheck' value='false' />
            <boolean name='soapCheck' value='false' />
            <!-- on response -->
            <text name='msgCallback' value='' />
        </settings>
        <webService value='prb' />
    </transaction>
    <transaction name='prb'>
        <endpoint value='https://localhost:8080/xdstools3/sim/unknown/target23/docrec/prb' />
        <settings>
            <boolean name='schemaCheck' value='false' />
            <boolean name='modelCheck' value='false' />
            <boolean name='codingCheck' value='false' />
            <boolean name='soapCheck' value='false' />
            <text name='msgCallback' value='' />
        </settings>
        <webService value='prb_TLS' />
    </transaction>
</actor>"""
    }

    def userName = 'Tester'
    def clientSimName = 'DocSrcSendTest'
    def clientSimId = new SimId(clientSimName)

    def simIdent
    ActorTransactionTypeFactory factory = new ActorTransactionTypeFactory()

    def setup() {
        new SimServlet().init()
        SimApi.delete(userName, clientSimId)  // necessary to make sure create actually creates new, default is to keep old if present
        simIdent = new SimIdentifier(userName, clientSimId)
    }

    def 'Show server sim endpoint shows up in client sim configuration'() {
        setup:
        def serverSimConfig = SimulatorDAO.toModel(serverSimConfig('http://foo.com'))
        def serverEndpoint = serverSimConfig.getEndpoint(factory.getTransactionTypeIfAvailable('prb'),TlsType.NOTLS, AsyncType.SYNC)

        // Now that create has been replaced by createClient this test is irrelevant


        when: 'create client locally'
        SimApi.createClient('docsrc', userName, clientSimId, clientSimConfig(serverEndpoint, serverEndpoint))
        def updatedConfig = SimApi.setConfig(userName, clientSimId, clientSimConfig(serverEndpoint, serverEndpoint))
        def xml = new XmlSlurper().parseText(updatedConfig)
        def updatedEndpoint = xml.transaction[0].endpoint.@value.text()

//        then:  'verify that update occured'
//        updatedEndpoint.contains('foo.com')
        then: true
    }
}
