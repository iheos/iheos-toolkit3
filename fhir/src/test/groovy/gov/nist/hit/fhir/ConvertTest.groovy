package gov.nist.hit.fhir

import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import org.hl7.fhir.instance.formats.JsonComposer
import org.hl7.fhir.instance.formats.JsonParser
import org.hl7.fhir.instance.formats.XmlComposer
import org.hl7.fhir.instance.model.Resource
import spock.lang.Specification
/**
 * Created by bmajur on 9/6/14.
 */
class ConvertTest extends Specification {

    def 'DocRef should translate from XML to JSON'() {
        when:
        def url = getClass().classLoader.getResource('mhd/minimal_docref.xml')
        Resource resource
        url.withInputStream {
            resource = new org.hl7.fhir.instance.formats.XmlParser().parse(it)
        }
        println new JsonComposer().compose(System.out, resource, true)

        then:  '''Something'''
        true
    }

    def 'DocRef should translate from JSON to XML'() {
        when:
        def url = getClass().classLoader.getResource('mhd/minimal_docref.json')
        Resource resource
        url.withInputStream {
            resource = new JsonParser().parse(it)
        }
        println new XmlComposer().compose(System.out, resource, true)

        then:  '''Something'''
        true
    }

    def 'DocRef should translate from XML to in-memory Resource back to XML'() {
        when:  'Load XML file of DocumentReference object'
        def url = getClass().classLoader.getResource('mhd/minimal_docref.xml')
        String xml = url.getText()

        and: 'Parse XML into DocumentReference resource'
        def resource = new org.hl7.fhir.instance.formats.XmlParser().parse(new ByteArrayInputStream(xml.bytes))

        and: ''
        def baos2 = new ByteArrayOutputStream()
        new XmlComposer().compose(baos2, resource, true)
        String xml2 = baos2.toString()
        println xml2

        and: 'Compare generated XML to orginal XML'
        Diff diff = XMLUnit.compareXML(xml, xml2)
        println diff.toString()

        then:
        diff.similar()

    }

    def 'DocRef should translate from XML to JSON back to XML'() {
        setup:  'Load XML file of DocumentReference object'
        def url = getClass().classLoader.getResource('mhd/minimal_docref.xml')
        String xml = url.getText()

        when: 'Parse XML into DocumentReference resource'
        def resource = new org.hl7.fhir.instance.formats.XmlParser().parse(new ByteArrayInputStream(xml.bytes))

        and: 'Generate JSON form (in memory)'
        def baos = new ByteArrayOutputStream()
        new JsonComposer().compose(baos, resource, true)

        and: 'Parse JSON into resource'
        def bais = new ByteArrayInputStream(baos.toByteArray())
        def resource2 = new JsonParser().parse(bais)

        and: ''
        def baos2 = new ByteArrayOutputStream()
        new XmlComposer().compose(baos2, resource2, true)
        String xml2 = baos2.toString()
        println xml2

        new File('/Users/bmajur/tmp/fhir/de.xml').newPrintWriter().print(xml2)

        Diff diff = XMLUnit.compareXML(xml, xml2)

        then:  '''Something'''
        diff.identical()
    }

}
