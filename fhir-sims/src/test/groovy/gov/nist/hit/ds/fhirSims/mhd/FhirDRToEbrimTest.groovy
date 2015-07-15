package gov.nist.hit.ds.fhirSims.mhd

import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.DocumentEntryModel
import FhirDRToEbrim
import gov.nist.hit.ds.ebMetadata.Metadata
import org.hl7.fhir.instance.model.DocumentReference
import org.hl7.fhir.instance.model.Resource
import spock.lang.Specification

/**
 * Created by bmajur on 9/8/14.
 */
class FhirDRToEbrimTest extends Specification {

    // TODO - testRun schema
    // TODO - testRun metadata validator
    def 'Minimal'() {
        setup:
        def res
        def url = getClass().classLoader.getResource('mhd/minimal_docref.xml')
        url.withInputStream {
            res = new XmlParser().parse(it)
        }

        when: ''

        then:
        res instanceof DocumentReference

        when:
        Resource resource = (Resource) res
        DocumentReference documentReference = (DocumentReference) resource
        def metadata = new Metadata()
        def converter = new FhirDRToEbrim(documentReference, 'Document1', metadata)
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
        def res
        def url = getClass().classLoader.getResource('mhd/full_docref.xml')
        url.withInputStream {
            res = new XmlParser().parse(it)
        }

        when: ''

        then:
        res != null
        res instanceof DocumentReference

        when:
        Resource resource = (Resource) res
        DocumentReference documentReference = (DocumentReference) resource
        def metadata = new Metadata()
        def converter = new FhirDRToEbrim(documentReference, 'Document1', metadata)
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
