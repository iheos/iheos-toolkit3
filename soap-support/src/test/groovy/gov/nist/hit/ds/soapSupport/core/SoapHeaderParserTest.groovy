package gov.nist.hit.ds.soapSupport.core

import spock.lang.Specification

/**
 * Created by bmajur on 10/1/14.
 */
class SoapHeaderParserTest extends Specification {
    def envelope = '''
<soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
    xmlns:wsa="http://www.w3.org/2005/08/addressing">
    <soapenv:Header>
        <wsa:To>http://localhost:5000/axis2/services/xdsrepositoryb</wsa:To>
        <wsa:MessageID>urn:uuid:AFBE87CB65FD88AC4B1220879854302</wsa:MessageID>
        <wsa:Action soapenv:mustUnderstand="true">urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b</wsa:Action>
    </soapenv:Header>
    <soapenv:Body>
    </soapenv:Body>
</soapenv:Envelope>
     '''

    def 'Parser should extract msgId and Action'() {
        when:
        def parser = new SoapHeaderParser(envelope)
        def soapHeader = parser.parse()

        then:
        soapHeader.messageId == 'urn:uuid:AFBE87CB65FD88AC4B1220879854302'
        soapHeader.action == 'urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b'
    }

    def envelope2 = '''
<soapenv:Envelope
   xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope">
   <soapenv:Header
      xmlns:wsa="http://www.w3.org/2005/08/addressing">
      <wsa:To
         soapenv:mustUnderstand="true">http://localhost:8001/tf6/services/xdsregistryb</wsa:To>
      <wsa:MessageID
         soapenv:mustUnderstand="true">urn:uuid:CB37DFAD5CCB6A786D1415131427156</wsa:MessageID>
      <wsa:Action
         soapenv:mustUnderstand="true">urn:ihe:iti:2007:RegisterDocumentSet-b</wsa:Action>
   </soapenv:Header>
   <soapenv:Body>
   </soapenv:Body>
</soapenv:Envelope>
   '''


    def 'Parser should extract msgId and Action 2'() {
        when:
        def parser = new SoapHeaderParser(envelope2)
        def soapHeader = parser.parse()

        then:
        soapHeader.messageId == 'urn:uuid:CB37DFAD5CCB6A786D1415131427156'
        soapHeader.action == 'urn:ihe:iti:2007:RegisterDocumentSet-b'
    }


}
