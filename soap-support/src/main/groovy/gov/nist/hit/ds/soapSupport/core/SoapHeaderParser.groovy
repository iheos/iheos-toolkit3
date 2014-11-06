package gov.nist.hit.ds.soapSupport.core

/**
 * Created by bmajur on 10/1/14.
 */
class SoapHeaderParser {
    String soapEnvelope

    def SoapHeaderParser(String _soapEnvelope) {
        soapEnvelope = _soapEnvelope
    }

    SoapHeader parse() {
        def soapHeader = new SoapHeader()
        def envelope = new XmlSlurper().parseText(soapEnvelope)
        def header = envelope.Header
        soapHeader.messageId = header.MessageID.text()
        soapHeader.action = header.Action.text()
        soapHeader.to = header.To.text()
        return soapHeader
    }

}
