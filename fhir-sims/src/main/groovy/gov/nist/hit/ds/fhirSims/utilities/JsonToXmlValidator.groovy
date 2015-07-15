package gov.nist.hit.ds.fhirSims.utilities

import SimHandle
import ValComponentBase
import Validation
import Io
import JsonParser
import ParserBase
import XmlComposer
import AtomFeed
import Resource

/**
 * Created by bmajur on 12/5/14.
 */
class JsonToXmlValidator extends ValComponentBase {
    String json
    String xml

    JsonToXmlValidator(SimHandle simHandle, String _json) { super(simHandle.event); json = _json }

    @Validation(id='json010', msg='JSON to XML', ref='')
    def json010() {
        ResourceOrFeed rof = new JsonParser().parseGeneral(Io.stringToInputStream(json))
        AtomFeed feed = rof.feed
        Resource resource = rof.resource
//        Resource resource = new JsonParser().parse(Io.stringToInputStream(json))
        if (resource) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream()
            new XmlComposer().compose(baos, resource, true)
            xml = new String(baos.toByteArray())
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream()
            new XmlComposer().compose(baos, feed, true)
            xml = new String(baos.toByteArray())
        }
    }

    // TODO - this works for a single resource, not a feed. See JsonParser.parseGeneral
    // This should be called after the validator is run
    String xml() {
        return xml
    }
}
