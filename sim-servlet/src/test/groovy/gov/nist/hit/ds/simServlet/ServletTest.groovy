package gov.nist.hit.ds.simServlet

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.AsyncType
import gov.nist.hit.ds.actorTransaction.TlsType
import gov.nist.hit.ds.actorTransaction.TransactionType
import gov.nist.hit.ds.eventLog.testSupport.EventAccess
import gov.nist.hit.ds.simServlet.servlet.SimServlet
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.client.SimIdentifier
import gov.nist.hit.ds.simSupport.config.TransactionSimConfigElement
import gov.nist.hit.ds.simSupport.manager.ActorSimConfigManager
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.utilities.SimEventAccess
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import spock.lang.Specification


/**
 * Created by bmajur on 10/6/14.
 */
class ServletTest extends Specification {
    String simIdString = 'target23'
    SimIdentifier realSimId
    def repoName = SimUtils.defaultRepoName
    def username = repoName

    def header = '''
POST /axis2/services/repositoryBonedoc HTTP/1.1
Content-Type: multipart/related; boundary=MIMEBoundaryurn_uuid_806D8FD2D542EDCC2C1199332890718; type="application/xop+xml"; start="0.urn:uuid:806D8FD2D542EDCC2C1199332890719@apache.org"; start-info="application/soap+xml"; action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"
User-Agent: Axis2
Host: localhost:9085'''
    def badEndpointBody = '''
<soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
    xmlns:wsa="http://www.w3.org/2005/08/addressing">
    <soapenv:Header>
        <wsa:To soapenv:mustUnderstand="true">http://localhost:9085/axis2/services/repositoryBonedoc</wsa:To>
        <wsa:MessageID>urn:uuid:806D8FD2D542EDCC2C1199332890651</wsa:MessageID>
        <wsa:Action>urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b</wsa:Action>
    </soapenv:Header>
    <soapenv:Body>
        <xdsb:ProvideAndRegisterDocumentSetRequest xmlns:xdsb="urn:ihe:iti:xds-b:2007">
            <lcm:SubmitObjectsRequest xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0">
            </lcm:SubmitObjectsRequest>
        </xdsb:ProvideAndRegisterDocumentSetRequest>
    </soapenv:Body>
</soapenv:Envelope>'''

    def goodEndpointBody = """
<soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
    xmlns:wsa="http://www.w3.org/2005/08/addressing">
    <soapenv:Header>
        <wsa:To soapenv:mustUnderstand="true">http://localhost:9085/xdstools3/sim/${username}/docrec/pnr</wsa:To>
        <wsa:MessageID>urn:uuid:806D8FD2D542EDCC2C1199332890651</wsa:MessageID>
        <wsa:Action>urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b</wsa:Action>
    </soapenv:Header>
    <soapenv:Body>
        <xdsb:ProvideAndRegisterDocumentSetRequest xmlns:xdsb="urn:ihe:iti:xds-b:2007">
            <lcm:SubmitObjectsRequest xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0">
            </lcm:SubmitObjectsRequest>
        </xdsb:ProvideAndRegisterDocumentSetRequest>
    </soapenv:Body>
</soapenv:Envelope>"""

    def noSimEndpointBody = '''
<soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
    xmlns:wsa="http://www.w3.org/2005/08/addressing">
    <soapenv:Header>
        <wsa:To soapenv:mustUnderstand="true">http://localhost:9085/xdstools3/sim/NoSim/docrec/pnr</wsa:To>
        <wsa:MessageID>urn:uuid:806D8FD2D542EDCC2C1199332890651</wsa:MessageID>
        <wsa:Action>urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b</wsa:Action>
    </soapenv:Header>
    <soapenv:Body>
        <xdsb:ProvideAndRegisterDocumentSetRequest xmlns:xdsb="urn:ihe:iti:xds-b:2007">
            <lcm:SubmitObjectsRequest xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0">
            </lcm:SubmitObjectsRequest>
        </xdsb:ProvideAndRegisterDocumentSetRequest>
    </soapenv:Body>
</soapenv:Envelope>'''

    def realEndpointBody = '''
<soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
    xmlns:wsa="http://www.w3.org/2005/08/addressing">
    <soapenv:Header>
        <wsa:To soapenv:mustUnderstand="true">http://localhost:9085/xdstools3/sim/target23/docreg/rb</wsa:To>
        <wsa:MessageID>urn:uuid:806D8FD2D542EDCC2C1199332890651</wsa:MessageID>
        <wsa:Action>urn:ihe:iti:2007:RegisterDocumentSet-b</wsa:Action>
    </soapenv:Header>
    <soapenv:Body>
            <lcm:SubmitObjectsRequest xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0">
            </lcm:SubmitObjectsRequest>
    </soapenv:Body>
</soapenv:Envelope>'''

    def badTransactionBody = '''
<soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
    xmlns:wsa="http://www.w3.org/2005/08/addressing">
    <soapenv:Header>
        <wsa:To soapenv:mustUnderstand="true">http://localhost:9085/xdstools3/sim/target23/docrec/xyzzy</wsa:To>
        <wsa:MessageID>urn:uuid:806D8FD2D542EDCC2C1199332890651</wsa:MessageID>
        <wsa:Action>xyzzy</wsa:Action>
    </soapenv:Header>
    <soapenv:Body>
        <xdsb:ProvideAndRegisterDocumentSetRequest xmlns:xdsb="urn:ihe:iti:xds-b:2007">
            <lcm:SubmitObjectsRequest xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0">
            </lcm:SubmitObjectsRequest>
        </xdsb:ProvideAndRegisterDocumentSetRequest>
    </soapenv:Body>
</soapenv:Envelope>'''

    SimServlet simServlet
    SimHandle simHandle
    ActorTransactionTypeFactory factory = new ActorTransactionTypeFactory()

    def setup() {
        simServlet = new SimServlet()
        simServlet.init()
        realSimId = new SimIdentifier(repoName, simIdString)
        simHandle = SimUtils.create('docrec', realSimId)
        // Cancel all message validation
        def actorSimConfigManager = new ActorSimConfigManager(simHandle.actorSimConfig)
        List<TransactionSimConfigElement> configs = actorSimConfigManager.getSimConfigElements()
        configs.each { config ->
            config.setBool(TransactionSimConfigElement.SCHEMACHECK, false)
            config.setBool(TransactionSimConfigElement.MODELCHECK, false)
            config.setBool(TransactionSimConfigElement.CODINGCHECK, false)
            config.setBool(TransactionSimConfigElement.SOAPCHECK, false)
        }
        actorSimConfigManager.save(simHandle.configAsset)
    }

    def cleanup() {
//        SimUtils.delete(realSimId)
    }

    def 'Extract simId from URI'() {
        when:
        TransactionType ttype = factory.getTransactionTypeIfAvailable('prb')

        then:
        ttype

        when:
        def uri = simHandle.actorSimConfig.getEndpoint(ttype, TlsType.NOTLS, AsyncType.SYNC).requestURI()

        then:
        simServlet.parseSimIdentFromURI(uri) == realSimId
    }

    def 'Extract username from URI'() {
        when:
        TransactionType ttype = factory.getTransactionTypeIfAvailable('prb')

        then:
        ttype

        when:
        def uri = simHandle.actorSimConfig.getEndpoint(ttype, TlsType.NOTLS, AsyncType.SYNC).requestURI()

        then:
        simServlet.parseUserFromURI(uri) == repoName
    }

    def 'Request to real sim should not fault'() {
        when:
        SimHandle simHandle = simServlet.runPost(realSimId, header, realEndpointBody.getBytes(), [], null)
        def fault = simHandle.getEvent().getFault()
        def eventAccess = new SimEventAccess(simHandle.simIdentifier, simHandle.event)

        then:
        fault == null
        eventAccess.respBodyFile().exists()
    }

    def 'Message to non-existent sim should report'() {
        when:
        def simHandle = simServlet.runPost(new SimIdentifier(SimUtils.defaultRepoName, 'NoSim'), header, noSimEndpointBody.getBytes(), [], null)
        def fault = simHandle.getEvent().getFault()
        def eventAccess = new SimEventAccess(simHandle.simIdentifier, simHandle.event)

        then:
        fault != null
        eventAccess.faultFile().exists()
        eventAccess.reqBodyFile().exists()
        eventAccess.respBodyFile().exists()
        fault.faultMsg.contains('does not exist')
    }


    def 'SimId mismatch should fail with fault'() {
        when:
        def simHandle = simServlet.runPost(new SimIdentifier(SimUtils.defaultRepoName, 'fake'), header, realEndpointBody.getBytes(), [], null)
        def fault = simHandle.getEvent().getFault()
        def eventAccess = new SimEventAccess(simHandle.simIdentifier, simHandle.event)

        then:
        fault != null
        fault.faultMsg.contains('SimId mismatch: URI has')
    }

    def 'Request for un-implemented transaction'() {
        when: '''Run body with bad wsa:Action field'''
        def simHandle = simServlet.runPost(realSimId, header, badTransactionBody.getBytes(), [], null)
        def fault = simHandle.getEvent().getFault()
        def eventAccess = new SimEventAccess(simHandle.simIdentifier, simHandle.event)

        then:
        fault != null
        fault.faultMsg.contains('Unknown wsa:Action')
        eventAccess.respBodyFile().exists()
    }

    def 'Force fault through options'() {
        when:
        def options = ['fault']
        def simHandle = simServlet.runPost(realSimId, header, realEndpointBody.getBytes(), options, null)
        def fault = simHandle.getEvent().getFault()

        then:
        fault != null
        fault.faultMsg.contains('Forced Fault')
    }

}
