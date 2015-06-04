package gov.nist.hit.ds.ebIT

import gov.nist.hit.ds.simServlet.api.SimApi
import gov.nist.hit.ds.simServlet.restClient.CreateSimRest
import gov.nist.hit.ds.simServlet.restClient.RestSend
import gov.nist.hit.ds.simServlet.servlet.SimServlet
import gov.nist.hit.ds.simSupport.client.SimId
import groovy.util.logging.Log4j
import spock.lang.Specification

/**
 * There are three parts to the test environment.
 * 1) toolkit2 running on tomcat1(8080) hosting a DocRecipient
 * simulator.
 *
 * 2) engine running at localhost:9090/tk hosting a DocSrc simulator. Its
 * EC is /Users/bill/tmp/toolkit3.
 * This is started via
 *     cd sim-servlet-war
 *     mvn package tomcat7:run-war
 *
 * 3) This test acting as a client of the DocSrc simulator.
 *
 * The goal of the test is to have the DocSrc send a PnR to the DocRec.
 *
 * Created by bill on 6/3/15.
 */
@Log4j
class RestToDocSrcSendIT extends Specification {

    def clientUserName = 'RestToDocSrcSendIT'
    def clientSimName = 'DocSrc'
    def clientSimId = new SimId(clientSimName)

    def metadata = '''
<xdsb:ProvideAndRegisterDocumentSetRequest xmlns:xdsb="urn:ihe:iti:xds-b:2007">
    <lcm:SubmitObjectsRequest xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0">
        <rim:RegistryObjectList xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0">
        </rim:RegistryObjectList>
    </lcm:SubmitObjectsRequest>
</xdsb:ProvideAndRegisterDocumentSetRequest>
'''
    // This is the endpoint for the DocRec supplied by v2 toolkit
    def docRecEndpoint = 'http://localhost:8080/xdstools2/sim/09000c69-9a87-4a73-9231-be04f6886e12/rec/xdrpr'

    // TODO: needs to delete DocSrc sim in engine
    def setup() {
        new SimServlet().init()
        log.debug "Deleting sims for ${clientUserName}"
        SimApi.delete(clientUserName, clientSimId)  // necessary to make sure create actually creates new, default is to keep old if present
        log.debug "DocRec endpoint is ${docRecEndpoint}"
    }

    def 'Run'() {
        when: '''Tell engine, via REST, to create DocSrc sim'''
        log.debug "Creating DocSrc sim"
        def config = DocSrcUtils.createClientSimConfig(docRecEndpoint, "")
        def host = 'localhost'
        def port = '9090'  // this is where engine is running
        def service = 'tk' // again this is local instance of engine
        def result
        result = CreateSimRest.run(config, host, port, service, clientUserName, clientSimName)
        log.debug "Result from creating DocSrc sim is: \n${result}"

        then:
        result

        when: '''Tell engine, via REST, to send PnR from DocSrc to DocRec in v2 toolkit'''
        log.debug "Sending request to DocSrc sim"
        def request = DocSrcUtils.createClientRequest(clientUserName, clientSimName, metadata)
        log.debug "Request is ${request}"
        def uri = "http://${host}:${port}/${service}/rest/sim/client/${clientUserName}/${clientSimName}"
        log.debug "Sending it to REST service at ${uri}"
        def response = RestSend.run(uri, request)
        log.debug "Result from sending Pnr is: \n${response}"

        then:
        response
    }
}
