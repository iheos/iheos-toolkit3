package gov.nist.hit.ds.simServlet

import gov.nist.hit.ds.eventLog.testSupport.EventAccess
import spock.lang.Specification

/**
 * Created by bmajur on 10/6/14.
 */
class ServletTest extends Specification {
    def header = '''
POST /axis2/services/repositoryBonedoc HTTP/1.1
Content-Type: multipart/related; boundary=MIMEBoundaryurn_uuid_806D8FD2D542EDCC2C1199332890718; type="application/xop+xml"; start="0.urn:uuid:806D8FD2D542EDCC2C1199332890719@apache.org"; start-info="application/soap+xml"; action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"
User-Agent: Axis2
Host: localhost:9085'''
    def body = '''
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
                <rim:RegistryObjectList xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0">
                    <rim:ExtrinsicObject id="Document01" mimeType="text/plain"
                        objectType="urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1">
                    </rim:ExtrinsicObject>
                    <rim:RegistryPackage id="SubmissionSet01"
                        objectType="urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage">
                    </rim:RegistryPackage>
                    <rim:Association
                        associationType="urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember"
                        sourceObject="SubmissionSet01" targetObject="Document01" id="ID_12928619_2"
                        objectType="urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association">
                        <rim:Slot name="SubmissionSetStatus">
                            <rim:ValueList>
                                <rim:Value>Original</rim:Value>
                            </rim:ValueList>
                        </rim:Slot>
                    </rim:Association>
                </rim:RegistryObjectList>
            </lcm:SubmitObjectsRequest>
            <xdsb:Document id="Document01">
                <xop:Include href="cid:1.urn:uuid:806D8FD2D542EDCC2C1199332890776@apache.org"
                    xmlns:xop="http://www.w3.org/2004/08/xop/include"/>
            </xdsb:Document>
        </xdsb:ProvideAndRegisterDocumentSetRequest>
    </soapenv:Body>
</soapenv:Envelope>'''

    def 'Test1'() {
        when:
        def simServlet = new SimServlet()
        simServlet.init()
        def simHandle = simServlet.runPost(header, body.getBytes(), null)
        def fault = simHandle.getEvent().getFault()
        def eventAccess = new EventAccess(simHandle.simId.id, simHandle.event)

        then:
        fault != null
        eventAccess.faultFile().exists()
        eventAccess.reqBodyFile().exists()
    }
}
