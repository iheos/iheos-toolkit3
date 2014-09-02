package gov.nist.hit.ds.dsSims.generator

import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
import groovy.util.logging.Log4j

/**
 * Created by bmajur on 7/13/14.
 */
@Log4j
class RimParser {
    def rimGenerator

    RimParser() {
        rimGenerator = new RimGenerator()
    }

    def parse(xmlString) {
        def xml = new XmlSlurper().parseText(xmlString)
        log.debug("Parsed xml is ${xml}")
        labelAllRegistryPackages(xml)
        parseToplevel(xml)
    }

    def parseToplevel(xml) {
        def data = []
        xml.children().each { major ->
            def type = major.name()
            if (type == 'ExtrinsicObject') { data << parseExtrinsicObject(major); return }
            if (type == 'RegistryPackage') { data << parseRegistryPackage(major); return }
            if (type == 'Association') { data << parseAssociation(major); return }
            throw new ToolkitRuntimeException("Major object type ${type} is undefined.")
        }
        return data
    }

    def parseExtrinsicObject(xml) {
        def data = [ : ]
        data['type'] = 'DocumentEntry'

        // fill in top level attributes
        data['id'] = xml.@id
        data['mimeType'] = xml.@mimeType
        String status = xml.@status
        if (status) data['status'] = status.tokenize(':').last()

        data['attributes'] = parseAttributes(xml)
        return data
    }

    def parseRegistryPackage(xml) {
        def data = [ : ]
        def id = xml.@id
        if (id in submissionSetIds) data['type'] = 'SubmissionSet'
        else if (id in folderIds) data['type'] = 'Folder'
        else throw new ToolkitRuntimeException("RegistryPackage with id=${id} is not understood.")

        // fill in top level attributes
        data['id'] = id

        data['attributes'] = parseAttributes(xml)
        return data
    }

    def parseAssociation(xml) {
        def data = [ : ]
        data['type'] = 'Association'

        // fill in top level attributes
        data['id'] = xml.@id
        data['source'] = xml.@sourceObject
        data['target'] = xml.@targetObject
        String type = xml.@associationType
        if (type) data['atype'] = type.tokenize(':').last()

        data['attributes'] = parseAttributes(xml)
        return data
    }

    def parseAttributes(xml) {
        def attributes = []
        attributes += parseSlots(xml)
        attributes += parseName(xml)
        attributes += parseDescription(xml)
        attributes += parseClassifications(xml)
        attributes += parseExternalIdentifiers(xml)
        return attributes
    }

    def parseName(xml) {
        log.debug("xml is ${xml}")
        def attributes = []
        xml.'Name'.each { name ->
            log.debug("Found Name")
            def atts = [name: 'name']
            def values = [name.LocalizedString.@value]
            atts['values'] = values
            attributes << atts
        }
        return attributes
    }

    def parseDescription(xml) {
        def attributes = []
        xml."*:Description".each { name ->
            def atts = [name: 'description']
            def values = [name.LocalizedString.@value]
            atts['values'] = values
            attributes << atts
        }
        return attributes
    }

    def parseSlots(xml) {
        def attributes = []
        xml."*:Slot".each { slot ->
            def atts = [ : ]
            atts['name'] = slot.@name
            def values = []
            slot."*:ValueList"."*:Value".each { value ->
                values << value.text()
            }
            atts['values'] = values
            attributes << atts
        }
        return attributes
    }

    def parseClassifications(xml) {
        def attributes = []
        xml."*:Classification".each { classification ->
            String classScheme = classification.@classificationScheme
            def classType = rimGenerator.classSchemesReverse[classScheme]
            if (!classType) throw new ToolkitRuntimeException("Classification scheme ${classScheme} unknown.")
            def atts = [ : ]
            atts['name'] = classType
            atts['code'] = classification.@nodeRepresentation
            atts['system'] = classification."*:Slot"."*:ValueList"."*:Value".text()
            atts['display'] = classification."*:Name"."*:LocalizedString".@value
            attributes << atts
        }
        return attributes
    }

    def parseExternalIdentifiers(xml) {
        def attributes = []
        xml."*:ExternalIdentifier".each { identifier ->
            String eiScheme = identifier.@identificationScheme
            def idType = rimGenerator.eiSchemesReverse[eiScheme]
            if (!idType) throw new ToolkitRuntimeException("ExternalIdentifier scheme ${eiScheme} unknown.")
            def atts = [ : ]
            atts['name'] = idType
            atts['value'] = identifier.@value
            attributes << atts
        }
        return attributes
    }

    def submissionSetIds = []
    def folderIds = []

    def labelAllRegistryPackages(xml) {
        submissionSetIds = labelRegistryPackages(xml, 'urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd')
        folderIds = labelRegistryPackages(xml, 'urn:uuid:d9d542f3-6cc4-48b6-8870-ea235fbc94c2')

        log.debug "SS: ${submissionSetIds}."
        log.debug "Fol: ${folderIds}."
    }

    def labelRegistryPackages(xml, classification) {
        def rpClassifications = []
        rpClassifications << xml."*:Classification".findAll {
            it.@classificationNode == classification
        }.collect { it.@classifiedObject }
        rpClassifications << xml."*:RegistryPackage"."*:Classification".findAll {
            it.@classificationNode == classification
        }.collect { it.@classifiedObject }
        return rpClassifications.flatten()
    }
}
