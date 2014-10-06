package gov.nist.hit.ds.dsSims.generator

import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import spock.lang.Specification
/**
 * Created by bmajur on 7/14/14.
 */
class PatternGeneratorTest extends Specification {
    def setup() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setIgnoreComments(true)
        XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.ignoreAttributeOrder = true
    }

    def 'Del single att test'() {
        when:
        def spec1 = [
                type: 'DocumentEntry',
                attributes: [
                        [name: 'foo1', value: 'bar1'],
                        [name: 'foo2', value: 'bar2']
                ]
        ]
        def spec2 = [
                type: 'DocumentEntry',
                attributes: [
                        [name: 'foo2', value: 'bar2']
                ]
        ]
        def gen = new PatternGenerator()

        then:
        gen.delAtt(spec1, 'foo1') == spec2
    }

    def 'Del multiple att test'() {
        when:
        def spec1 = [
                type: 'DocumentEntry',
                attributes: [
                        [name: 'foo1', value: 'bar1'],
                        [name: 'foo2', value: 'bar2'],
                        [name: 'foo2', value: 'bar2']
                ]
        ]
        def spec2 = [
                type: 'DocumentEntry',
                attributes: [
                        [name: 'foo1', value: 'bar1']
                ]
        ]
        def gen = new PatternGenerator()

        then:
        gen.delAtt(spec1, 'foo2') == spec2
    }

    def 'Add single att test'() {
        when:
        def spec1 = [
                type: 'DocumentEntry',
                attributes: [
                        [name: 'foo1', value: 'bar1'],
                        [name: 'foo2', value: 'bar2']
                ]
        ]
        def spec2 = [
                type: 'DocumentEntry',
                attributes: [
                        [name: 'foo1', value: 'bar1']
                ]
        ]
        def gen = new PatternGenerator()

        then:
        gen.addAtt(spec2, [name: 'foo2', value: 'bar2']) == spec1
    }

    def 'Add to empty test'() {
        when:
        def gen = new PatternGenerator()
        def empty = gen.emptyDocumentEntry('de')
        def full = gen.addAtt(empty, [name: 'foo1', value: 'bar1'])

        then:
        full == [type: 'DocumentEntry',
                 id: 'de', mimeType: 'text/xml',
                 attributes: [[name: 'foo1', value: 'bar1']]
        ]
    }

    def 'Set header test'() {
        when:
        def gen = new PatternGenerator()
        def empty = gen.emptyDocumentEntry('de')
        def full = gen.setHeader(empty, 'mimeType', 'text/plain')

        then:
        full == [type: 'DocumentEntry',
                 id: 'de', mimeType: 'text/plain',
                 attributes: []
        ]
    }

    def 'Build DE submission'() {
        def expected = '''
<wrapper>
    <RegistryPackage id="ss" lid=""/>
    <Classification classifiedObject='ss' classificationNode='urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd' />
    <ExtrinsicObject id="de" lid="" mimeType="text/xml"
        objectType="urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1"/>
    <Association id="assoc"
        associationType="urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember"
        sourceObject="ss" targetObject="de"
        objectType="urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association"/>
</wrapper>
'''
        when:
        def gen = new PatternGenerator()
        def spec = []
        spec << gen.emptySubmissionSet('ss')
        spec << gen.emptyDocumentEntry('de')
        spec << gen.emptyAssociation('assoc', 'HasMember', 'ss', 'de')
        println spec
        def rimGen = new RimGenerator()
        def xmlTextList = rimGen.toRimXml(spec, 'wrapper')
        println xmlTextList

        then:
        Diff diff = XMLUnit.compareXML(expected, xmlTextList)

        then:
        diff.identical()
    }

}
