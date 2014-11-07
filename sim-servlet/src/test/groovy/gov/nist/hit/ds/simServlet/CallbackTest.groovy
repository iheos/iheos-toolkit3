package gov.nist.hit.ds.simServlet
import gov.nist.hit.ds.simServlet.api.SimApi
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import spock.lang.Specification
/**
 * Created by bmajur on 11/6/14.
 */
class CallbackTest extends Specification {
    def header = '''
POST /sim/PnrSoapTest/docrec/pnr HTTP/1.1
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
        <wsa:To soapenv:mustUnderstand="true">http://localhost:9085/xdstools3/sim/PnrSoapTest/docrec/pnr</wsa:To>
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

    def simId = new SimId('PnrSoapTest')
    def simServlet

    def setup() {
        simServlet = new SimServlet()
        simServlet.init()
        def simApi = new SimApi()
        simApi.delete(simId)
        def simHandle = simApi.create('docrec', simId)
        SimUtils.close(simHandle)
    }

    def 'Parts should be retrievable'() {
        when:
        SimHandle simHandle = simServlet.runPost(simId, header, body.getBytes(), [], null)
        def reqHdr = simHandle.event.inOut.reqHdr
        def reqBody = simHandle.event.inOut.reqBody
        def respHdr = simHandle.event.inOut.respHdr
        def respBody = simHandle.event.inOut.respBody

        then:
        reqHdr
        reqBody
        respHdr
        respBody
    }

}
