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
 * simulator.  docRecEndpoint below holds this endpoint.
 *
 * 2) engine running at localhost:9090/tk hosting a DocSrc simulator. Its
 * EC is /Users/bill/tmp/toolkit3.
 * This is started via
 *     cd sim-servlet-war
 *     mvn package tomcat7:run-war
 *
 * 3) This test acting as a client of the DocSrc simulator.
 *
 * The goal of the test is to have the DocSrc send a PnR to the DocRec and
 * report back the results.
 *
 * Created by bill on 6/3/15.
 */
@Log4j
class RestToDocSrcSendIT extends Specification {

//    def clientUserName = 'RestToDocSrcSendIT'
//    def clientSimName = 'DocSrc'
    def clientUserName = 'ett'
    def clientSimName = '3'
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
    def docRecEndpoint = 'http://localhost:8080/xdstools2/sim/c727eaed-b41a-487a-b597-d17e3207b18e/rec/xdrpr'
//    def docRecEndpointTls = 'https://localhost:8443/xdstools2/sim/44547243-9a82-42ce-8390-b9a10bc898a4/rec/xdrpr'
    def docRecEndpointTls = 'https://transport-testing.nist.gov:12081/ttt/sim/ce45c84c-fc5f-430e-b1cd-aadf592a67ca/rec/xdrpr'

    // This is where the engine is running
//    def host = 'localhost'
//    def port = '9090'  // this is where engine is running
//    def service = 'tk' // again this is local instance of engine
    //http://hit-dev.nist.gov:11080
    def host = 'hit-dev.nist.gov'
    def port = '11080'  // this is where engine is running
    def service = 'xdstools3' // again this is local instance of engine

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
        def config = DocSrcUtils.createClientSimConfig(docRecEndpoint, docRecEndpointTls)
        def result
        result = CreateSimRest.run(config, host, port, service, clientUserName, clientSimName)
        log.debug "Result from creating DocSrc sim is: \n${result}"

        then:
        result

        when: '''Tell engine, via REST, to send PnR from DocSrc to DocRec in v2 toolkit'''
        log.debug "Sending request to DocSrc sim"
        def request = DocSrcUtils.createClientRequest(clientUserName, clientSimName, metadata, false)
        log.debug "Request is ${request}"
        def uri = "http://${host}:${port}/${service}/rest/sim/client/${clientUserName}/${clientSimName}"
        log.debug "Sending it to REST service at ${uri}"
        def response = RestSend.run(uri, request)
        log.debug "Result from sending Pnr is: \n${response}"

        then:
        response
    }

    def 'Run with TLS'() {
        when: '''Tell engine, via REST, to create DocSrc sim'''
        log.info "Creating DocSrc sim"
        def config = DocSrcUtils.createClientSimConfig(docRecEndpoint, docRecEndpointTls)
        def result
        result = CreateSimRest.run(config, host, port, service, clientUserName, clientSimName)
        log.info "Result from creating DocSrc sim is: \n${result}"

        then:
        result

        when: '''Tell engine, via REST, to send PnR from DocSrc to DocRec in v2 toolkit'''
        log.info "Sending request to DocSrc sim"
        def request = DocSrcUtils.createClientRequest(clientUserName, clientSimName, metadata, true)
        log.info "Request is ${request}"
        def uri = "http://${host}:${port}/${service}/rest/sim/client/${clientUserName}/${clientSimName}"
        log.info "Sending it to REST service at ${uri}"
        def response = RestSend.run(uri, request)
        log.info "Result from sending Pnr is: \n${response}"

        then:
        response
    }

    def 'Run bad REST address'() {
        when: '''Tell engine, via REST, to create DocSrc sim'''
        log.debug "Creating DocSrc sim"
        def config = DocSrcUtils.createClientSimConfig(docRecEndpoint, "")
        def result
        result = CreateSimRest.run(config, host, port, service, clientUserName, clientSimName)
        log.debug "Result from creating DocSrc sim is: \n${result}"

        then:
        result

        when: '''Tell engine, via REST, to send PnR from DocSrc to DocRec in v2 toolkit'''
        log.debug "Sending request to DocSrc sim"
        def request = DocSrcUtils.createClientRequest(clientUserName, clientSimName, metadata, false)
        log.debug "Request is ${request}"
        def uri = "http://${host}:${port}/${service}/rest/sim/client/${clientUserName}/${clientSimName}xxx"
        log.debug "Sending it to REST service at ${uri}"
        def response = RestSend.run(uri, request)
        log.debug "Result from sending Pnr is: \n${response}"

        then:
        response
    }

    def 'Edge Request'() {
        when: '''Tell engine, via REST, to create DocSrc sim'''
        log.info "Creating DocSrc sim"
        def config = DocSrcUtils.createClientSimConfig(docRecEndpoint, docRecEndpointTls)
        def result
        result = CreateSimRest.run(config, host, port, service, clientUserName, clientSimName)
        log.info "Result from creating DocSrc sim is: \n${result}"

        then:
        result

        when: '''Tell engine, via REST, to send PnR from DocSrc to DocRec in v2 toolkit'''
        log.info "Sending request to DocSrc sim"
        def request = getClass().getResource('/edgeRequest.xml').text
        log.info "Request is ${request}"
        def uri = "http://${host}:${port}/${service}/rest/sim/client/${clientUserName}/${clientSimName}"
        log.info "Sending it to REST service at ${uri}"
        def response = RestSend.run(uri, request)
        log.info "Result from sending Pnr is: \n${response}"

        then:
        response
    }

}
