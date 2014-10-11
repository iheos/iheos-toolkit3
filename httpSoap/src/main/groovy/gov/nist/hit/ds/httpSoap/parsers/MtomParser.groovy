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
        MultipartParserBa mp = hp.getMultipartParser()
        if (mp.getPartCount() == 0) throw new Exception('No parts found in multipart when parsing MTOM format')
//        def partIds = []
//        for (int i=0; i<mp.getPartCount(); i++)
//            partIds << mp.getPart(i).getContentId()
//        msg("Part IDs found are ${partIds}")
        PartBa startPart = mp.getStartPart();
//        msg("Start part is ${startPart?.getContentId()}")

        if (startPart == null) throw new Exception("Start part [${mp.getStartPartId()}] not found")

        return startPart.getBody()

        // See V2 SoapMessageValidator.java
    }

}
