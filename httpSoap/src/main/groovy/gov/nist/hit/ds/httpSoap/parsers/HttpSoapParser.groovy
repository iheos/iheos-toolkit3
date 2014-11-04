package gov.nist.hit.ds.httpSoap.parsers
import gov.nist.hit.ds.http.parser.HttpParserBa
import gov.nist.hit.ds.utilities.html.HttpMessageContent
// this is a non-judgemental parser, it tries to unwrap the message so processing decisions can be made. It is not
// a validator
public class HttpSoapParser {
    HttpMessageContent content
	HttpParserBa hparser = null;

    public HttpSoapParser(HttpMessageContent _content) {
        content = _content
    }

    def init() {
        if (!hparser) hparser = new HttpParserBa(content.bytes)
    }

    byte[] getSoapEnvelope() {
        init()
        if (hparser.isMultipart()) {
            return new MtomParser(hparser, content).getSoapEnvelope()
        } else {
            return new SimpleSoapParser(hparser, content).getSoapEnvelope()
        }
    }

    boolean isMultiPart() { init(); hparser.isMultipart() }

}
