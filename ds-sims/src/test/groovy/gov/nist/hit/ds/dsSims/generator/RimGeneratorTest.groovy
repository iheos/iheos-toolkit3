package gov.nist.hit.ds.dsSims.generator

import groovy.xml.MarkupBuilder
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import spock.lang.Specification
/**
 * Created by bmajur on 7/13/14.
 */
class RimGeneratorTest extends Specification {

    def setup() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setIgnoreComments(true)
        XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.ignoreAttributeOrder = true
    }

    def 'Test XMLUnit different'() {
        when:
        def answer='<hi/>'
        def value='<bye/>'
        Diff diff = XMLUnit.compareXML(answer, value)

        then:
        !diff.identical()
    }

    def 'Test XMLUnit same'() {
        when:
        def answer='<hi/>'
        def value='<hi/>'
        Diff diff = XMLUnit.compareXML(answer, value)

        then:
        diff.identical()
    }

   def spec1 = '''
 [['name': 'languageCode', 'values':['en-us']]]
'''

    def rim1 = '''
        <Slot name="languageCode">
            <ValueList>
                <Value>en-us</Value>
            </ValueList>
        </Slot>
'''

    def 'Should generate Slot'() {
        when:
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        new RimGenerator().evalAttribute(xml, 'parent', Eval.me(spec1)[0])
        def rim = writer.toString()
        println rim

        then:
        Diff diff = XMLUnit.compareXML(rim1, rim)

        then:
        diff.identical()

    }

    def spec2 = '''
[[name: 'classCode', code: 'classCodeValue', system: 'XDS Affinity Domain Specific Value', display: 'classCodeName']]
'''

    def rim2 = '''
<Classification classificationScheme="urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a"
  classifiedObject="theDocument"
  id="ID-1"
  objectType="urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification"
    nodeRepresentation="classCodeValue">
    <Name>
       <LocalizedString value="classCodeName"/>
    </Name>
    <Slot name="codingScheme">
       <ValueList>
          <Value>XDS Affinity Domain Specific Value</Value>
       </ValueList>
    </Slot>
</Classification>'''

    def 'Should generate Classification'() {
        when:
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        new RimGenerator().evalAttribute(xml, 'theDocument', Eval.me(spec2)[0])
        def rim = writer.toString()
        println rim

        then:
        Diff diff = XMLUnit.compareXML(rim2, rim)

        then:
        diff.identical()
    }

    def spec3 = '''
[[name: 'de.patientId', value: '76cc765a442f410^^^&1.3.6.1.4.1.21367.2005.3.7&ISO']]
'''

    def rim3 = '''
<ExternalIdentifier identificationScheme="urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427"
            value="76cc765a442f410^^^&amp;1.3.6.1.4.1.21367.2005.3.7&amp;ISO"
            objectType="urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier"
            id="ID-1" registryObject="Document01">
     <Name>
           <LocalizedString value="XDSDocumentEntry.patientId"/>
     </Name>
</ExternalIdentifier>
'''

    def 'Should generate ExternalIdentifier'() {
        when:
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        new RimGenerator().evalAttribute(xml, 'Document01', Eval.me(spec3)[0])
        def rim = writer.toString()
        println rim

        then:
        Diff diff = XMLUnit.compareXML(rim3, rim)

        then:
        diff.identical()
    }


    def 'Should generate ExtrinsicObject with Description'() {
        def spec = '''
[
    [type: 'DocumentEntry',
         id: 'Document1', lid: '', mimeType: 'text/xml', status:'Approved',
    attributes:
        [
            [name: 'description', values: ['hello']]
        ]
    ]
]
'''

        def rimm = '''
<ExtrinsicObject id='Document1' mimeType='text/xml'
    status='urn:oasis:names:tc:ebxml-regrep:StatusType:Approved' lid=''
    objectType='urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1'>
<Description>
  <LocalizedString value="hello"/>
</Description>
</ExtrinsicObject>
'''
        when:
        List<String> rim=new RimGenerator().toRim(Eval.me(spec))
        println rim

        then:
        Diff diff = XMLUnit.compareXML(rimm, rim[0])

        then:
        diff.identical()

    }

    def 'Should generate Association'() {
        def spec = '''
[
    [type: 'Association',
         id: 'ID-1', source: 'ss1', target: 'doc1', atype: 'HasMember',
         attributes:
        [
            [name: 'SubmissionSetStatus', values: ['Original']]
        ]

    ]
]
'''

        def rimm = '''
<Association associationType="urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember"
        sourceObject="ss1" targetObject="doc1" id="ID-1"
        objectType="urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association">
        <Slot name="SubmissionSetStatus">
            <ValueList>
                <Value>Original</Value>
            </ValueList>
        </Slot>
    </Association>
'''
        when:
        List<String> rim=new RimGenerator().toRim(Eval.me(spec))
        println rim

        then:
        Diff diff = XMLUnit.compareXML(rimm, rim[0])

        then:
        diff.identical()

    }


    def 'Should generate Author'() {
        def spec3 = '''
[
    [   name: 'de.author',
        person: 'D12398^Doe^John^^^^^^&1.2.3.4.5.6.7.8. 9.1789.45.1&ISO',
        telecom: '^^Internet^john.doe@healthcare.example.org',
        institutions: ['Some Hospital^^^^^^^^^1.2.3.4.5.6.7.8.9.1789.45'],
        roles: ['name of role'],
        specialty: ['specialty of author']
    ]
]
'''

        def rim3 = '''
<Classification classificationScheme="urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d"
    classifiedObject="theDocument" id="ID-1"
    objectType="urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification"
    nodeRepresentation="" >
    <!-- nodeRepresentation intentionally left blank -->
    <Slot name="authorPerson">
        <!-- shall be single valued, includes
optional ID -->
        <ValueList>
            <Value>D12398^Doe^John^^^^^^&amp;1.2.3.4.5.6.7.8. 9.1789.45.1&amp;ISO</Value>
        </ValueList>
    </Slot>
    <Slot name="authorInstitution">
        <!-- may be multivalued -->
        <ValueList>
            <Value>Some Hospital^^^^^^^^^1.2.3.4.5.6.7.8.9.1789.45</Value>
        </ValueList>
    </Slot>
    <Slot name="authorRole">
        <!-- may be multivalued -->
        <ValueList>
            <Value>name of role</Value>
        </ValueList>
    </Slot>
    <Slot name="authorSpecialty">
        <!-- may be multivalued -->
        <ValueList>
            <Value>specialty of author</Value>
        </ValueList>
    </Slot>
    <Slot name="authorTelecommunication">
        <!-- shall be single valued -->
        <ValueList>
            <Value>^^Internet^john.doe@healthcare.example.org</Value>
        </ValueList>
    </Slot>
</Classification>
'''
        when:
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        new RimGenerator().evalAttribute(xml, 'theDocument', Eval.me(spec3)[0])
        def rim = writer.toString()
        println rim

        then:
        Diff diff = XMLUnit.compareXML(rim3, rim)

        then:
        diff.identical()
    }


    def 'Eval test'() {
        when:
        def code = '''
x = [['a': 1, 'b': 2], 3]
'''
        def y = Eval.me(code)

        then:
        y[1] == 3
        y[0].a == 1
    }

}
