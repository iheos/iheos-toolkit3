package gov.nist.hit.ds.dsSims.generator

import spock.lang.Specification

/**
 * Created by bmajur on 7/13/14.
 */
class RimParserTest extends Specification {

    def 'SS and Fol Assocs should be parsed' () {
        def rim = '''
<wrapper xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0">
  <rim:ExtrinsicObject id="eo1"/>
  <rim:RegistryPackage id="ss"/>
  <rim:Classification classifiedObject="ss"
        classificationNode="urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd" id="ID_1216346_1"
        objectType="urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification"/>
  <rim:Association
      sourceObject="ss"
      targetObject="eo1"
      type="urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember"/>
  <rim:RegistryPackage id="ss2">
     <rim:Classification classifiedObject="ss2"
        classificationNode="urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd" id="ID_7"
        objectType="urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification"/>
  </rim:RegistryPackage>
  <rim:RegistryPackage id="fol">
     <rim:Classification classifiedObject="fol"
        classificationNode="urn:uuid:d9d542f3-6cc4-48b6-8870-ea235fbc94c2" id="ID_8"
        objectType="urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification"/>
  </rim:RegistryPackage>
</wrapper>
'''
        when:
        def parser = new RimParser()
        parser.labelAllRegistryPackages(new XmlSlurper().parseText(rim))

        then:
        parser.submissionSetIds as Set == ['ss2', 'ss'] as Set
        parser.folderIds as Set == ['fol'] as Set
    }

    def 'Parse Slots in EO'() {
        def rim = '''
<wrapper xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0">
  <rim:ExtrinsicObject id="eo1">
        <rim:Slot name="creationTime">
            <rim:ValueList>
                <rim:Value>20051224</rim:Value>
            </rim:ValueList>
        </rim:Slot>
        <rim:Slot name="languageCode">
            <rim:ValueList>
                <rim:Value>en-us</rim:Value>
                <rim:Value>en-uk</rim:Value>
            </rim:ValueList>
        </rim:Slot>
  </rim:ExtrinsicObject>
</wrapper>
'''
        when:
        def parser = new RimParser()
        def data = parser.parse(rim)
        println data

        then:
        data[0].type == 'DocumentEntry'
        data[0].attributes[0].name == 'creationTime'
        data[0].attributes[0].values == ['20051224']
        data[0].attributes[1].name == 'languageCode'
        data[0].attributes[1].values == ['en-us', 'en-uk']
    }

    def 'Parse Name and Description in EO'() {
        def rim = '''
<wrapper xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0">
  <rim:ExtrinsicObject id="eo1">
        <rim:Name>
            <rim:LocalizedString value="Physical"/>
        </rim:Name>
        <rim:Description>
            <rim:LocalizedString value="Description"/>
        </rim:Description>
  </rim:ExtrinsicObject>
</wrapper>
'''
        when:
        def parser = new RimParser()
        def data = parser.parse(rim)
        println data

        then:
        data[0].type == 'DocumentEntry'
        data[0].attributes[0].name == 'name'
        data[0].attributes[0].values == ['Physical']
        data[0].attributes[1].name == 'description'
        data[0].attributes[1].values == ['Description']
    }

    def 'Parse Classification in EO'() {
        def rim = '''
<wrapper xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0">
  <rim:ExtrinsicObject id="eo1">
        <rim:Classification classificationScheme="urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a"
            classifiedObject="Document01" nodeRepresentation="History and Physical"
            objectType="urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification"
            id="id_3">
            <rim:Slot name="codingScheme">
                <rim:ValueList>
                    <rim:Value>Connect-a-thon classCodes</rim:Value>
                </rim:ValueList>
            </rim:Slot>
            <rim:Name>
                <rim:LocalizedString value="History and Physical Display"/>
            </rim:Name>
        </rim:Classification>
  </rim:ExtrinsicObject>
</wrapper>
'''
        when:
        def parser = new RimParser()
        def data = parser.parse(rim)
        println data

        then:
        data[0].attributes[0].name == 'classCode'
        data[0].attributes[0].code == 'History and Physical'
        data[0].attributes[0].system == 'Connect-a-thon classCodes'
        data[0].attributes[0].display == 'History and Physical Display'
    }

    def 'Parse ExternalIdentifier in EO'() {
        String rim = '''
<wrapper xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0">
  <rim:ExtrinsicObject id="eo1">
        <rim:ExternalIdentifier identificationScheme="urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab"
            value="2009.9.1.2455"
            objectType="urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier"
            id="id_10" registryObject="Document01">
            <rim:Name>
                <rim:LocalizedString value="XDSDocumentEntry.uniqueId"/>
            </rim:Name>
        </rim:ExternalIdentifier>
  </rim:ExtrinsicObject>
</wrapper>
'''
        when:
        def parser = new RimParser()
        def data = parser.parse(rim)
        println data

        then:
        data[0].attributes[0].name == 'de.uniqueId'
        data[0].attributes[0].value == '2009.9.1.2455'
    }

    def 'Parse EO'() {
        def rim = '''
<wrapper xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0">
  <rim:ExtrinsicObject id="Document01" mimeType="text/plain"
        objectType="urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1"
        status="urn:oasis:names:tc:ebxml-regrep:StatusType:Approved">
        <rim:Slot name="creationTime">
            <rim:ValueList>
                <rim:Value>20051224</rim:Value>
            </rim:ValueList>
        </rim:Slot>
        <rim:Slot name="languageCode">
            <rim:ValueList>
                <rim:Value>en-us</rim:Value>
                <rim:Value>en-uk</rim:Value>
            </rim:ValueList>
        </rim:Slot>
  </rim:ExtrinsicObject>
</wrapper>
'''
        when:
        def parser = new RimParser()
        def data = parser.parse(rim)
        println data

        then:
        data[0].type == 'DocumentEntry'
        data[0].id == 'Document01'
        data[0].mimeType == 'text/plain'
        data[0].status == 'Approved'
    }
}
