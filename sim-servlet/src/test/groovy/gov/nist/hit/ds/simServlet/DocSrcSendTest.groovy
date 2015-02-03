package gov.nist.hit.ds.simServlet

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.TransactionType
import gov.nist.hit.ds.dsSims.eb.transactionSupport.EbSendRequest
import gov.nist.hit.ds.dsSims.eb.transactionSupport.EbSendRequestDAO
import gov.nist.hit.ds.simServlet.api.SimApi
import gov.nist.hit.ds.simServlet.servlet.SimServlet
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.client.SimIdentifier
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import spock.lang.Specification

/**
 * Created by bmajur on 2/3/15.
 */
class DocSrcSendTest extends Specification {
    def requestXml = '''
<sendRequest>
    <simReference>user/simid</simReference>
    <transactionName>prb</transactionName>
    <tls value="true"/>
    <metadata>meta content</metadata>
    <document id="Document01" mimeType="text/plain">doc content</document>
</sendRequest>'''
    def simConfig = '''
<actor type='docsrc'>
    <transaction name='prb'>
        <!-- example endpoint value -->
        <endpoint value='http://localhost:8080/xdstools3/sim/unknown/target23/docrec/prb' />
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
</actor>'''

    def repoName = 'Tester'
    def simId = new SimId('DocSrcSendTest')
    def simIdent
    ActorTransactionTypeFactory factory = new ActorTransactionTypeFactory()

    def setup() {
        new SimServlet().init()
        SimApi.delete(repoName, simId)  // necessary to make sure create actually creates new, default is to keep old if present
        simIdent = new SimIdentifier(repoName, simId)
    }

    def 'Send'() {
        when:
        SimHandle simHandle = SimApi.create('docsrc', repoName, simId)
        SimApi.updateConfig(repoName, simId, simConfig)
        EbSendRequest request = EbSendRequestDAO.toModel(requestXml)
        SimApi.send(simIdent, request)

        then: true
    }
}
