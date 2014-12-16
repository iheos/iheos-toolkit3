package gov.nist.hit.ds.dsSims.eb.generator

import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
import groovy.xml.MarkupBuilder

/**
 * Created by bmajur on 7/13/14.
 */
class RimGenerator {

    RimGenerator() {
        init()
    }

    String toRimXml(spec, wrapperEleName) {
        def eleList = toRim(spec)
        def x = eleList.join('\n')
        "<${wrapperEleName}>\n${x}\n</${wrapperEleName}>"
    }

    List<String> toRim(spec) {
        return spec.collect { evalObjects(it) }
    }

    def slots = ['creationTime', 'hash', 'languageCode',
                 'legalAuthenticator', 'repositoryUniqueId', 'serviceStartTime',
                 'serviceStopTime', 'size', 'sourcePatientId',
                 'sourcePatientInfo', 'URI', 'submissionTime',
                 'lastUpdateTime', 'SubmissionSetStatus']
    def classifications = ['classCode', 'eventCode', 'formatCode',
                           'hcft', 'practiceSettingCode', 'typeCode', 'contentTypeCode',
                           'codeList']
    def externalIds = ['de.patientId', 'de.uniqueId',
                       'ss.patientId', 'sourceId', 'ss.uniqueId', 'fol.patientId',
                       'fol.uniqueId']
    def classSchemes = [
            classCode: 'urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a',
            eventCode: 'urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4',
            formatCode: 'urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d',
            hcft: 'urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1',
            practiceSettingCode: 'urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead',
            typeCode: 'urn:uuid:f0306f51-975f-434e-a61c-c59651d33983',
            contentTypeCode: 'urn:uuid:aa543740-bdda-424e-8c96-df4873be8500',
            codeList: 'urn:uuid:1ba97051-7806-41a8-a48b-8fce7af683c5'
    ]

    def classSchemesReverse = [ : ]

    def authors = ['de.author']

    def authorSchemes = [
            'de.author': 'urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d'
    ]

    def authorSchemesReverse = [ : ]

    def eiSchemes = ['de.patientId': 'urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427',
                     'de.uniqueId': 'urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab',
                     'ss.patientId': 'urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446',
                     sourceId: 'urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832',
                     'ss.uniqueId': 'urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8',
                     'fol.patientId': 'urn:uuid:f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a',
                     'fol.uniqueId': 'urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a']

    def eiSchemesReverse = [ : ]

    def init() {
        classSchemes.each { classSchemesReverse[it.value] = it.key }
        eiSchemes.each { eiSchemesReverse[it.value] = it.key }
        authorSchemes.each { authorSchemesReverse[it.value] = it.key }
    }

    def evalObjects(spec) {
            if (spec.type == 'DocumentEntry') return evalDocumentEntry(spec)
            if (spec.type == 'SubmissionSet') return evalSubmissionSet(spec)
            if (spec.type == 'Folder') return evalFolder(spec)
            if (spec.type == 'Association') return evalAssociation(spec)
            throw new ToolkitRuntimeException("Do not understand ${spec.type}.")
    }

    def evalDocumentEntry(spec) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        def status = (spec.status) ? "urn:oasis:names:tc:ebxml-regrep:StatusType:${spec.status}" : null
        if (status) {
            xml.ExtrinsicObject(id: spec.id, mimeType: spec.mimeType, lid: spec.lid,
                    'status': status,
                    objectType: 'urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1')
                    {
                        spec.attributes.each { evalAttribute(xml, spec.id, it) }
                    }
        } else {
            xml.ExtrinsicObject(id: spec.id, mimeType: spec.mimeType, lid: spec.lid,
                    objectType: 'urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1')
                    {
                        spec.attributes.each { evalAttribute(xml, spec.id, it) }
                    }
        }
        return writer.toString()
    }

    def evalSubmissionSet(spec) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        def status = (spec.status) ? "urn:oasis:names:tc:ebxml-regrep:StatusType:${spec.status}" : null
        if (status) {
            xml.RegistryPackage(id: spec.id, lid: spec.lid,
                    status: "urn:oasis:names:tc:ebxml-regrep:StatusType:${spec.status}")
                    {
                        spec.attributes.each { evalAttribute(xml, spec.id, it) }
                    }
        } else {
            xml.RegistryPackage(id: spec.id, lid: spec.lid,)
                    {
                        spec.attributes.each { evalAttribute(xml, spec.id, it) }
                    }
        }
        xml.Classification(classifiedObject:spec.id, classificationNode:"urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd")
        return writer.toString()
    }

    def evalFolder(spec) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        def status = (spec.status) ? "urn:oasis:names:tc:ebxml-regrep:StatusType:${spec.status}" : null
        if (status) {
            xml.RegistryPackage(id: spec.id,
                    status: "urn:oasis:names:tc:ebxml-regrep:StatusType:${spec.status}") {
                spec.attributes.each { evalAttribute(xml, spec.id, it) }
            }
        } else {
            xml.RegistryPackage(id: spec.id) {
                spec.attributes.each { evalAttribute(xml, spec.id, it) }
            }
        }
        return writer.toString()
    }

    def evalAssociation(spec) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.Association(id: spec.id, associationType: "urn:oasis:names:tc:ebxml-regrep:AssociationType:${spec.atype}",
            sourceObject: spec.source, targetObject: spec.target,
            objectType: 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association') {
            spec.attributes.each { evalAttribute(xml, spec.id, it) }
        }
        return writer.toString()
    }

    def evalAttribute(xml, parent, spec) {
        String name = spec.name
        if (name in slots) return mkSlot(xml, spec)
        if (name in classifications) return mkClassification(xml, parent, spec)
        if (name in externalIds) return mkExternalIdentifier(xml, parent, spec)
        if (name in authors) return mkAuthor(xml, parent, spec)
        if (name == 'description') return mkDescription(xml, spec)
        if (name == 'name') return mkName(xml, spec)
        if (name == 'version') return mkVersion(xml, spec)
        throw new ToolkitRuntimeException("Cannot eval ${name}.")
    }

    def mkSlot(xml, spec) {
        xml.Slot(name: spec.name) {
            ValueList {
                spec.values.each { Value(it) }
            }
        }
    }

    def mkVersion(xml, spec) {
        xml.VersionInfo(versionName: spec.value)
    }

    def mkClassification(xml, parent, spec) {
        def scheme = classSchemes.get(spec.name)
        if (scheme == null) throw new ToolkitRuntimeException("No Classification Scheme defined for ${spec.name}.")
        xml.Classification(id: newId(), classificationScheme: scheme, classifiedObject: parent, objectType: 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification', nodeRepresentation: spec.code) {
            Name {
                LocalizedString(value: spec.display)
            }
            Slot(name: 'codingScheme') {
                ValueList {
                    Value(spec.system)
                }
            }
        }
    }

    def mkAuthor(xml, parent, spec) {
        println "person is ${spec.person}"
        def scheme = authorSchemes.get(spec.name)
        if (scheme == null) throw new ToolkitRuntimeException("No Classification Scheme defined for ${spec.name}.")
        xml.Classification(id: newId(),
                classificationScheme: scheme,
                classifiedObject: parent,
                objectType: 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification',
                nodeRepresentation: '') {
            if (spec.person) {
                xml.Slot(name: 'authorPerson') {
                    ValueList {
                        Value(spec.person)
                    }
                }
            }
            if (spec.institutions) {
                xml.Slot(name: 'authorInstitution') {
                    ValueList {
                        spec.institutions.each {
                            Value(it)
                        }
                    }
                }
            }
            if (spec.roles) {
                xml.Slot(name: 'authorRole') {
                    ValueList {
                        spec.roles.each {
                            Value ( it )
                        }
                    }
                }
            }
            if (spec.specialty) {
                xml.Slot(name: 'authorSpecialty') {
                    ValueList {
                        spec.specialty.each {
                            Value ( it )
                        }
                    }
                }
            }
            if (spec.telecom) {
                xml.Slot(name: 'authorTelecommunication') {
                    ValueList {
                        Value (spec.telecom)
                    }
                }
            }
        }
    }

    def mkExternalIdentifier(xml, parent, spec) {
        def scheme = eiSchemes.get(spec.name)
        if (scheme == null) throw new ToolkitRuntimeException("No ExternalIdentifier Scheme defined for ${spec.name}.")
        xml.ExternalIdentifier(id: newId(), identificationScheme: scheme, registryObject: parent, objectType: 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier', value: spec.value) {
            Name {
                LocalizedString(value: 'XDSDocumentEntry.patientId')
            }
        }
    }

    def mkDescription(xml, spec) {
        xml.Description {
                LocalizedString(value: spec.values[0])
        }
    }

    def mkName(xml, spec) {
        xml.Name {
            LocalizedString(value: spec.values[0])
        }
    }

    def id = 1
    def newId() { "ID-${id++}" }

}
