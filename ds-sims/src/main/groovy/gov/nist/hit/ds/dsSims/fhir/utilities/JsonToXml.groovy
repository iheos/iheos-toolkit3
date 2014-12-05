package gov.nist.hit.ds.dsSims.fhir.utilities

import gov.nist.hit.ds.utilities.io.Io
import org.hl7.fhir.instance.formats.JsonParser
import org.hl7.fhir.instance.formats.XmlComposer
import org.hl7.fhir.instance.model.Resource

/**
 * Created by bmajur on 12/5/14.
 */
class JsonToXml {
    String json

    JsonToXml(String _json) { json = _json }

    // TODO - this works for a single resource, not a feed. See JsonParser.parseGeneral
    String xml() {
        Resource resource = new JsonParser().parse(Io.stringToInputStream(json))
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        new XmlComposer().compose(baos, resource, true)
        return new String(baos.toByteArray())
    }
}
