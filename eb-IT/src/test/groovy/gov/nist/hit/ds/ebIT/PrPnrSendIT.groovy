package gov.nist.hit.ds.ebIT
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.dsSims.eb.transactionSupport.EbSendRequest
import gov.nist.hit.ds.dsSims.eb.transactionSupport.EbSendRequestDAO
import gov.nist.hit.ds.simServlet.api.SimApi
import gov.nist.hit.ds.simServlet.servlet.SimServlet
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.client.SimIdentifier
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import spock.lang.Shared
import spock.lang.Specification
/**
 * Created by bmajur on 1/13/15.
 */
class PrPnrSendIT extends Specification {
    // @Shared - Shared between feature methods
    @Shared
    def userName = 'Tester'
    @Shared
    def clientSimName = 'PRSendTest'
    @Shared
    def clientSimId = new SimId(clientSimName)

    def clientSimConfig(endpoint, tlsEndpoint) {
        """
<actor type='docsrc'>
    <environment name="NA2015"/>
    <transaction name='prb'>
        <!-- example endpoint value -->
        <endpoint value='${endpoint}' />
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

    @Shared
    def simIdent
    @Shared
    ActorTransactionTypeFactory factory = new ActorTransactionTypeFactory()
    @Shared
    def endpoint = 'http://ihexds.nist.gov:12090/tf6/services/xdsrepositoryb'
    @Shared
    def tlsEndpoint = 'https://ihexds.nist.gov:12091/tf6/services/xdsrepositoryb'
    @Shared
    String metadata
    @Shared
    def documentId
    @Shared
    def document

    def setupSpec() {
        new SimServlet().init()
        SimApi.delete(userName, clientSimId)  // necessary to make sure create actually creates new, default is to keep old if present
        simIdent = new SimIdentifier(userName, clientSimId)
        metadata = getClass().classLoader.getResource('PnR1Doc.xml').text
        documentId = 'Document01'
        document = getClass().classLoader.getResource('hello.txt').text
    }


    def 'Test against PR without TLS'() {
        setup:
        def requestXml = """
<sendRequest>
    <simReference>${userName}/${clientSimName}</simReference>
    <transactionName>prb</transactionName>
    <tls value="false"/>
    <metadata>${metadata}</metadata>
    <extraHeaders><foo/><bar/></extraHeaders>
    <document id="${documentId}" mimeType="text/plain">${document}</document>
</sendRequest>
"""

        when: 'create client locally'
        SimApi.createClient('docsrc', userName, clientSimId, clientSimConfig(endpoint, tlsEndpoint))
//        SimApi.setConfig(userName, clientSimId, clientSimConfig(endpoint, tlsEndpoint))

        and: 'send PnR to PR'
        EbSendRequest request = EbSendRequestDAO.toModel(requestXml)
        SimHandle simHandle = SimApi.send(simIdent, request)

        then: 'basically successful'
        simHandle.event.fault == null
    }

    def 'Test against PR without TLS - custom messageId'() {
        setup:
        def requestXml = """
<sendRequest>
    <simReference>${userName}/${clientSimName}</simReference>
    <transactionName>prb</transactionName>
    <tls value="false"/>
    <messageId>HiFrodo</messageId>
    <metadata>${metadata}</metadata>
    <extraHeaders><foo/><bar/></extraHeaders>
    <document id="${documentId}" mimeType="text/plain">${document}</document>
</sendRequest>
"""

        when: 'create client locally'
        SimApi.createClient('docsrc', userName, clientSimId, clientSimConfig(endpoint, tlsEndpoint))
//        SimApi.setConfig(userName, clientSimId, clientSimConfig(endpoint, tlsEndpoint))

        and: 'send PnR to PR'
        EbSendRequest request = EbSendRequestDAO.toModel(requestXml)
        println "custom messageId is ${request.messageId}"
        SimHandle simHandle = SimApi.send(simIdent, request)

        then: 'basically successful'
        simHandle.event.fault == null
    }

    def 'Test against PR with TLS'() {
        setup:
        def requestXml = """
<sendRequest>
    <simReference>${userName}/${clientSimName}</simReference>
    <transactionName>prb</transactionName>
    <tls value="true"/>
    <metadata>${metadata}</metadata>
    <extraHeaders><foo/><bar/></extraHeaders>
    <document id="${documentId}" mimeType="text/plain">${document}</document>
</sendRequest>
"""

        when: 'create client locally'
        SimApi.createClient('docsrc', userName, clientSimId, clientSimConfig(endpoint, tlsEndpoint))
//        SimApi.setConfig(userName, clientSimId, clientSimConfig(endpoint, tlsEndpoint))

        and: 'send PnR to PR'
        EbSendRequest request = EbSendRequestDAO.toModel(requestXml)
        SimHandle simHandle = SimApi.send(simIdent, request)

        then: 'basically successful'
        simHandle.event.fault == null
    }


    def 'Environment check'() {
        when:
        def file = new File(getClass().classLoader.getResource('NA2015').toURI())

        then:
        file.exists()
        file.isDirectory()


        when:
        def keystoreDir = new File(file, 'keystore')

        then:
        keystoreDir.exists()
        keystoreDir.isDirectory()

        when:
        def keystoreFile = new File(keystoreDir, 'keystore')
        def propertiesFile = new File(keystoreDir, 'keystore.properties')

        then:
        keystoreFile.exists()
        propertiesFile.exists()
    }
}

