package gov.nist.hit.ds.mhd

import gov.nist.hit.ds.dsSims.metadataValidator.object.DocumentEntryModel
import gov.nist.hit.ds.metadata.Metadata
import org.hl7.fhir.instance.model.DocumentReference
import org.hl7.fhir.instance.model.Resource
import spock.lang.Specification

/**
 * Created by bmajur on 9/8/14.
 */
class FhirToEbrimTest extends Specification {

    // TODO - testRun schema
    // TODO - testRun metadata validator
    def 'Minimal'() {
        setup:
        def url = getClass().classLoader.getResource('mhd/minimal_docref.xml')
        Resource resource
        url.withInputStream {
            resource = new org.hl7.fhir.instance.formats.XmlParser().parse(it)
        }

        when: ''

        then:
        resource != null
        resource instanceof DocumentReference

        when:
        DocumentReference documentReference = (DocumentReference) resource
        def metadata = new Metadata()
        def converter = new FhirToEbrim(documentReference, 'Document1', metadata)
        converter.run()
        DocumentEntryModel de = new DocumentEntryModel(metadata, metadata.getExtrinsicObject(0))

        then:
        de.classCode()
        !de.confCodes().empty
        de.creationTime()
        de.entryUUID().length() != 0
        de.formatCode()
        de.healthcareFacilityTypeCode()
        de.languageCode()
        de.mimeType.length() != 0
        de.patientId()
        de.practiceSettingCode()
        de.sourcePatientId()
        de.typeCode()
        de.uniqueId()
    }

    // TODO - testRun schema
    // TODO - testRun metadata validator
    def 'Full'() {
        setup:
        def url = getClass().classLoader.getResource('mhd/full_docref.xml')
        Resource resource
        url.withInputStream {
            resource = new org.hl7.fhir.instance.formats.XmlParser().parse(it)
        }

        when: ''

        then:
        resource != null
        resource instanceof DocumentReference

        when:
        DocumentReference documentReference = (DocumentReference) resource
        def metadata = new Metadata()
        def converter = new FhirToEbrim(documentReference, 'Document1', metadata)
        converter.run()
        DocumentEntryModel de = new DocumentEntryModel(metadata, metadata.getExtrinsicObject(0))

        then: 'Minimal coding requirements (R)'
        de.classCode()
        !de.confCodes()?.empty
        de.creationTime()
        de.entryUUID().length() != 0
        de.formatCode()
        de.healthcareFacilityTypeCode()
        de.languageCode()
        de.mimeType?.length() != 0
        de.patientId()
        de.practiceSettingCode()
        de.sourcePatientId()
        de.typeCode()
        de.uniqueId()

        then: 'Beyond minimal Coding'
        !de.availabilityStatus()?.empty
        de.eventCodes()
        de.hash()
        de.homeCommunityId()
        de.legalAuthenticator()
        de.repositoryUniqueId()
        de.size()
        de.sourcePatientInfo()
        de.title()
    }

}
