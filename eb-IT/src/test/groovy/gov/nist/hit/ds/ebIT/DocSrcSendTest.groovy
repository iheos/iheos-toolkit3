package gov.nist.hit.ds.ebIT

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.AsyncType
import gov.nist.hit.ds.actorTransaction.TlsType
import gov.nist.hit.ds.dsSims.eb.transactionSupport.EbSendRequest
import gov.nist.hit.ds.dsSims.eb.transactionSupport.EbSendRequestDAO
import gov.nist.hit.ds.simServlet.api.SimApi
import gov.nist.hit.ds.simServlet.restClient.CreateSimRest
import gov.nist.hit.ds.simServlet.servlet.SimServlet
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.client.SimIdentifier
import gov.nist.hit.ds.simSupport.serializer.SimulatorDAO
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import spock.lang.Specification

/**
 * Created by bmajur on 2/3/15.
 */
class DocSrcSendTest extends Specification {
    def metadata = '''
<xdsb:ProvideAndRegisterDocumentSetRequest xmlns:xdsb="urn:ihe:iti:xds-b:2007">
    <lcm:SubmitObjectsRequest xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0">
        <rim:RegistryObjectList xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0">
        </rim:RegistryObjectList>
    </lcm:SubmitObjectsRequest>
</xdsb:ProvideAndRegisterDocumentSetRequest>
'''
    def requestXml = """
<sendRequest>
    <simReference>user/simid</simReference>
    <transactionName>prb</transactionName>
    <tls value="false"/>
    <metadata>${metadata}</metadata>
    <document id="Document01" mimeType="text/plain">doc content</document>
</sendRequest>
"""
    def clientSimConfig(noTlsEndpoint) {
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

    def repoName = 'Tester'
    def clientSimId = new SimId('DocSrcSendTest')
    def serverSimId = new SimId('DocRec1')
    def simIdent
    ActorTransactionTypeFactory factory = new ActorTransactionTypeFactory()

    def setup() {
        new SimServlet().init()
        SimApi.delete(repoName, clientSimId)  // necessary to make sure create actually creates new, default is to keep old if present
        simIdent = new SimIdentifier(repoName, clientSimId)
    }

    def 'Send'() {
        when: 'setup server via rest interface'
        def configXml = CreateSimRest.run(serverSimConfig('http://example.com'), 'localhost', '9080', repoName, serverSimId.id)
        def serverSimConfig = SimulatorDAO.toModel(configXml)
        def serverEndpoint = serverSimConfig.getEndpoint(factory.getTransactionTypeIfAvailable('prb'),TlsType.NOTLS, AsyncType.SYNC)

        then:
        serverEndpoint
        serverEndpoint.value.startsWith('http')

        when: 'create client locally'
        SimApi.create('docsrc', repoName, clientSimId)
        def updatedConfig = SimApi.updateConfig(repoName, clientSimId, clientSimConfig(serverEndpoint))
        def xml = new XmlSlurper().parseText(updatedConfig)
        def updatedEndpoint = xml.transaction[0].endpoint.@value.text()

        then:  'verify that update occured'
        updatedEndpoint.contains('DocRec')

        when:
        EbSendRequest request = EbSendRequestDAO.toModel(requestXml)
        SimHandle simHandle = SimApi.send(simIdent, request)

        then:
        simHandle.event.fault == null
    }
}
