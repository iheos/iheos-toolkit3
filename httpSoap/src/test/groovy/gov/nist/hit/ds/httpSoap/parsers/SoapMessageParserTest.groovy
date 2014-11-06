package gov.nist.hit.ds.httpSoap.parsers

import gov.nist.hit.ds.httpSoap.components.parsers.SoapMessageParser
import gov.nist.hit.ds.httpSoap.datatypes.SoapMessage
import spock.lang.Specification
/**
 * Created by bmajur on 11/4/14.
 */
class SoapMessageParserTest extends Specification {

    def 'Field test'() {
        def msg = '''
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
        when:
        def soapMessageParser = new SoapMessageParser(msg)
        SoapMessage soapMessage = soapMessageParser.parse()

        then:
        soapMessage.getSoapAction() == 'urn:ihe:iti:2007:RegisterDocumentSet-b'
    }


}
