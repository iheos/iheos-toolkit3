package gov.nist.hit.ds.httpSoap.parsers
import gov.nist.hit.ds.http.parser.HttpParserBa
import gov.nist.hit.ds.http.parser.MultipartParserBa
import gov.nist.hit.ds.http.parser.PartBa
import gov.nist.hit.ds.utilities.html.HttpMessageContent
/**
 * Created by bmajur on 10/8/14.
 */
// TODO: Finish coding
class MtomParser {
    HttpParserBa httpParserBa
    HttpMessageContent content

    MtomParser(HttpParserBa _httpParserBa, HttpMessageContent _content) {
        httpParserBa = _httpParserBa
        content = _content
    }

    byte[] getSoapEnvelope() throws Exception {
        MultipartParserBa mp = httpParserBa.getMultipartParser()
        if (mp.getPartCount() == 0) throw new Exception('No parts found in multipart when parsing MTOM format')
        PartBa startPart = mp.getStartPart();

        if (startPart == null) throw new Exception("Start part [${mp.getStartPartId()}] not found")

        return startPart.getBody()
    }

}
