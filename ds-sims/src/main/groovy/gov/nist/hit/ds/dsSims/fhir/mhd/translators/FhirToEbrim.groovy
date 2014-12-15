package gov.nist.hit.ds.dsSims.fhir.mhd.translators

import gov.nist.hit.ds.dsSims.eb.metadata.Metadata
import gov.nist.hit.ds.dsSims.eb.metadata.MetadataSupport
import gov.nist.hit.ds.utilities.xml.OMFormatter
import org.apache.axiom.om.OMElement
import org.hl7.fhir.instance.model.*

/**
 * Created by bmajur on 9/8/14.
 */
class FhirToEbrim {
    DocumentReference dr
    OMElement extrinsicObject
    Metadata m
    String logicalId
    def contains = [:]    // id -> value

    FhirToEbrim(DocumentReference _documentReference, String _logicalId, Metadata _metadata) {
        dr = _documentReference
        m = _metadata
        logicalId = _logicalId
    }

    Metadata run() {
        extrinsicObject = m.mkExtrinsicObject(logicalId, dr.getMimeTypeSimple())
        dr.getContained().each { contained(it) }
        println "Contains ${contains}"
        parseExtensions()
        parseStatus()
        parseCreated()
        parsePrimaryLanguage()
        parseSize()
        parseDescription()
        parse(dr.type, MetadataSupport.XDSDocumentEntry_typeCode_uuid)
        parse(dr.class_, MetadataSupport.XDSDocumentEntry_classCode_uuid)
        parse(dr.confidentiality, MetadataSupport.XDSDocumentEntry_confCode_uuid)
        parse(dr.context.facilityType, MetadataSupport.XDSDocumentEntry_hcftCode_uuid)
        parse(dr.context.event, MetadataSupport.XDSDocumentEntry_eventCode_uuid)
        parseAuthor()
        parseFormat()
        parseMasterIdentifier()
        parseSubject()
        println new OMFormatter(m.getExtrinsicObject(0)).toString()
        return m
    }

    def parseExtensions() {
        def extensions = dr.getExtensions()
        extensions.each { Extension extension ->
            def url = extension.url.value
            def extensionName = noHash(url)
            if (extensionName.indexOf('#') != -1) extensionName = url.substring(url.indexOf('#') +1)
            parseExtension(extension, extensionName)
        }
    }

    def parseExtension(Extension extension, extensionName) {
        println "parseExtension - ${extensionName}"
        List<Property> childrenList = extension.children()
        Property valueProperty = childrenList.find { it.name == 'value[x]'}
        if (!valueProperty.hasValues()) return
        def type = valueProperty.values[0]
        parse(type, extensionName)
    }

    def parse(String_ x, String y) {
        println "Parse ${x} ${y}"
        if (y == 'homeCommunityId')
            extrinsicObject.addAttribute('home', x.value, null)
    }

    def parse(ResourceReference reference, String extensionName) {
        println "extensionName is ${extensionName}"
        def ref = noHash(reference.referenceSimple)
        println "References ${ref}"
        Resource contained = contains[ref]
        assert contained
        parse(contained, extensionName)
    }

    def parse(Coding coding, String extensionName) {
        def codeType = null
        if (extensionName == 'practiceSettingCode') codeType = MetadataSupport.XDSDocumentEntry_psCode_uuid
        assert codeType
        addCoding(coding, codeType)
    }

    def contained(Resource resource) {
        def id = resource.xmlId
        contains[id] = resource
        println "Contains Resource ${resource}"
    }

    def parse(Patient patient, extensionName) {
        println "Parse Patient ${extensionName}"
        if (extensionName == 'sourcePatient')
            parseSourcePatient(patient)
    }

    def parseSourcePatient(Patient patient) {
        def id = patient.xmlId
        Identifier identifier = (Identifier) patient?.properties['identifier'][0]
        println "Patient ${id} = ${identifier.valueSimple} : ${noUrn(identifier.systemSimple)}"
        def value = "${identifier.valueSimple}^^^&${noUrn(identifier.systemSimple)}&ISO"
        m.addSlot(extrinsicObject, 'sourcePatientId', value)
    }

    def contained(Patient patient) {
        def id = patient.xmlId
        contains[id] = patient
    }

    def parseMasterIdentifier() {
        Identifier identifier = dr.masterIdentifier
        String value = noUrn(identifier.valueSimple)
        m.addExternalId(extrinsicObject, MetadataSupport.XDSDocumentEntry_uniqueid_uuid, value, MetadataSupport.XDSDocumentEntry_uniqueid_name)
    }

    def parseSubject() {
        ResourceReference rr = dr.subject
        String reference = noHash(rr.referenceSimple)
        println "reference is ${reference}"
        String value = contains[reference]
        println "Subject: ${value}"
        m.addExternalId(extrinsicObject, MetadataSupport.XDSDocumentEntry_patientid_uuid, value, MetadataSupport.XDSDocumentEntry_patientid_name)
    }

    def parse(List<CodeableConcept> concepts, codeType) {
        concepts.each { parse(it, codeType)}
    }

    def parse(CodeableConcept concept, codeType) {
        def codings = concept.coding
        assert codings
        assert codings.size() > 0
        Coding coding = codings.first()
        addCoding(coding, codeType)
    }

    def addCoding(Coding coding, codeType) {
        def system = coding.systemSimple
        def code = coding.codeSimple
        def display = coding.displaySimple
        m.addExtClassification(extrinsicObject, codeType, system, display, code)
    }

    def parseCreated() {
        DateTime created = dr.created
        DateAndTime dt = created.value
        String value = "${dt.year}${formatTime(dt.month)}${formatTime(dt.day)}${formatTime(dt.hour)}${formatTime(dt.minute)}${formatTime(dt.second)}"
        m.addSlot(extrinsicObject, 'creationTime', value)
    }

    def parseSize() {
        String value = dr.size?.stringValue
        if (value)
            m.addSlot(extrinsicObject, 'size', value)
    }

    def parseDescription() {
        def description = dr.descriptionSimple
        m.setTitleValue(extrinsicObject, description)
    }


    def parseStatus() {
        DocumentReference.DocumentReferenceStatus status = dr.statusSimple
        def drvalue = status.toString()
        def value = drvalue
        if (drvalue == 'current') value = MetadataSupport.statusType_approved
        else if (drvalue == 'superceeded') value = MetadataSupport.statusType_deprecated
        m.setStatus(extrinsicObject, value)
    }

    def parsePrimaryLanguage() {
        Code lang = dr.primaryLanguage
        m.addSlot(extrinsicObject, 'languageCode', lang.value)
    }

    class DSCode {
        String system
        String code
        String display
        DSCode(String _code, String _system, String _display) { code = _code; system = _system; display = _display }
    }

    def formatCodeMap = [
            'urn:ihe:pcc:xds-ms:2007': new DSCode('urn:ihe:pcc:xds-ms:2007', 'IHE PCC', 'Medical Summaries')
    ]

    def parseFormat() {
        List<Uri> formats = dr.format
        assert formats.size() > 0
        String uri = formats.first().value
        DSCode dsc = formatCodeMap[uri]
        if (!dsc) dsc = new DSCode(uri, 'Unknown', uri)
        m.addExtClassification(extrinsicObject, MetadataSupport.XDSDocumentEntry_formatCode_uuid, dsc.system, dsc.display, dsc.code)
    }

    def parseAuthor() {
        List<ResourceReference> resRefs = dr.author
        resRefs.each { ResourceReference rr ->
            String ref = rr.referenceSimple
            ref = noHash(ref)
            println "ref is ${ref}"
            Resource resource = contains[ref]
            assert resource
            assert resource instanceof Resource
            parseAuthor(resource)
        }
    }

    def parseAuthor(Practitioner practitioner) {
        def identifier = practitioner?.identifier
        def identifierValue = ''
        def assigningAuthority = ''
        def lastName = ''
        def firstName = ''
        def prefix = ''
        if (identifier && !identifier.empty) {
            identifierValue = identifier.first().getValueSimple()
            assigningAuthority = identifier.first().systemSimple
        }
        def name = practitioner.name
        if (name) {
            lastName = name.family?.first()?.value
            firstName = name.given?.first()?.value
            prefix = name.prefix?.first()?.value
        }
        if (assigningAuthority) {
            assigningAuthority = "&${noURN(assigningAuthority)}&ISO"
        }
        def value = "${identifierValue}^${lastName}^${firstName}^^^${prefix}^^^${assigningAuthority}"
        OMElement author = m.addIntClassification(extrinsicObject, MetadataSupport.XDSDocumentEntry_author_uuid)
        m.addSlot(author, 'authorPerson', value)
    }

    def parseAuthor(Patient patient) {
        assert false
    }

    def parseAuthor(RelatedPerson relatedPerson) {
        assert false
    }

    def parseAuthor(Device device) {
        assert false
    }

    def noUrn(String item) {
        if (!item) return item
        def parts = item.split(':')
        parts.last()
    }

    def noHash(String item) {
        if (!item) return item
        if (!item.charAt(0) == '#') return item
        return item.substring(1)
    }

    def formatTime(Integer time) { formatTime(time.toString())}

    def formatTime(java.lang.Integer time) { formatTime(time.toString())}

    def formatTime(String time) {
        if (!time) return time
        if (time.length() == 2) return time
        return "0${time}"
    }

}
