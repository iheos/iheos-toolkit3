package gov.nist.hit.ds.simServlet

import gov.nist.hit.ds.eventLog.testSupport.EventAccess
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.config.TransactionSimConfigElement
import gov.nist.hit.ds.simSupport.manager.SimConfigManager
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import spock.lang.Specification

/**
 * Created by bmajur on 11/2/14.
 */
class ErrorResponseTest extends Specification {
    def header = '''
POST /sim/ErrorResponseTest/docrec/pnr/error/XDSRegistryError HTTP/1.1
Content-Type: multipart/related; boundary=MIMEBoundaryurn_uuid_806D8FD2D542EDCC2C1199332890718; type="application/xop+xml"; start="0.urn:uuid:806D8FD2D542EDCC2C1199332890719@apache.org"; start-info="application/soap+xml"; action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"
User-Agent: Axis2
Host: localhost:9085'''

    def body = '''
--MIMEBoundaryurn_uuid_806D8FD2D542EDCC2C1199332890718
Content-Type: application/xop+xml; charset=UTF-8; type="application/soap+xml"
Content-Transfer-Encoding: binary
Content-ID: <0.urn:uuid:806D8FD2D542EDCC2C1199332890719@apache.org>

<soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
    xmlns:wsa="http://www.w3.org/2005/08/addressing">
    <soapenv:Header>
        <wsa:To soapenv:mustUnderstand="true">http://localhost:9085/xdstools3/sim/ErrorResponseTest/docrec/pnr</wsa:To>
        <wsa:MessageID>urn:uuid:806D8FD2D542EDCC2C1199332890651</wsa:MessageID>
        <wsa:Action>urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b</wsa:Action>
    </soapenv:Header>
    <soapenv:Body>
        <xdsb:ProvideAndRegisterDocumentSetRequest xmlns:xdsb="urn:ihe:iti:xds-b:2007">
        </xdsb:ProvideAndRegisterDocumentSetRequest>
    </soapenv:Body>
</soapenv:Envelope>

--MIMEBoundaryurn_uuid_806D8FD2D542EDCC2C1199332890718
Content-Type: text/plain
Content-Transfer-Encoding: binary
Content-ID: <1.urn:uuid:806D8FD2D542EDCC2C1199332890776@apache.org>

This is my document.

It is great!


--MIMEBoundaryurn_uuid_806D8FD2D542EDCC2C1199332890718--'''

    def simId = new SimId('ErrorResponseTest')
    def simServlet

    def setup() {
        simServlet = new SimServlet()
        simServlet.init()
        def simHandle = SimUtils.create('docrec', simId)
        // Cancel everything but SOAP validation
        def actorSimConfigManager = new SimConfigManager(simHandle.actorSimConfig)
        TransactionSimConfigElement config = actorSimConfigManager.getSimConfigElement()
        config.setBool(TransactionSimConfigElement.SCHEMACHECK, false)
        config.setBool(TransactionSimConfigElement.MODELCHECK, false)
        config.setBool(TransactionSimConfigElement.CODINGCHECK, false)
        config.setBool(TransactionSimConfigElement.SOAPCHECK, true)
        actorSimConfigManager.save(simHandle.configAsset)
    }

    def cleanup() {
//        SimUtils.delete(simId)
    }

    def 'URI should force error'() {
        when:
        def simHandle = simServlet.runPost(simId, header, body.getBytes(), ['error', 'XDSRegistryError'], null)
        def fault = simHandle.getEvent().getFault()
        def eventAccess = new EventAccess(simHandle.simId.id, simHandle.event)

        then:
        fault == null
        eventAccess.respBodyFile().exists()
    }

}
