package gov.nist.hit.ds.httpSoap.parsers

import gov.nist.hit.ds.http.parser.HttpParserBa
import gov.nist.hit.ds.utilities.html.HttpMessageContent

/**
 * Created by bmajur on 10/9/14.
 */
// TODO: Implement
class SimpleSoapParser {
    HttpParserBa httpParserBa
    HttpMessageContent content

    SimpleSoapParser(HttpParserBa _httpParserBa, HttpMessageContent _content) {
        httpParserBa = _httpParserBa
        content = _content
    }

    byte[] getSoapEnvelope() {
        return content.body
    }
}
