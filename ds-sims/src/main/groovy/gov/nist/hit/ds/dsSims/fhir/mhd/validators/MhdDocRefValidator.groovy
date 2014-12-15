package gov.nist.hit.ds.dsSims.fhir.mhd.validators
import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.HashValidator
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
/**
 * Created by bmajur on 11/15/14.
 */
class MhdDocRefValidator extends ValComponentBase {
    SimHandle simHandle
    def dr

    // dr is XmlSlurper representation of a DocumentReference
    MhdDocRefValidator(SimHandle _simHandle, def _dr) {
        super(_simHandle.event)
        simHandle = _simHandle
        dr = _dr
    }

    ///////////////////////////
    // ClassCode
    ///////////////////////////
    def classCodePresent() { dr.class.size() > 0 }

    @Guard(methodNames=['classCodePresent'])
    @Validation(id='mhd010', msg='Validating classCode', ref='')
    def mhd010() {
        infoFound(true)
        dr.class.children().each { new CodingValidator(simHandle, it).asSelf().run() }
    }

    ///////////////////////////
    // comment
    ///////////////////////////

    def comments() {
        dr.extension.findAll {
            it.@url.text() == 'http://ihe.net/fhir/Profile/XDS/extensions#comments'
        }
    }
    def commentPresent() {
        comments().size() > 0
    }

    @Guard(methodNames=['commentPresent'])
    @Validation(id='mhd020', msg='Validating comments', ref='')
    def mhd020() { infoFound('Comments') }

    @Guard(methodNames=['commentPresent'])
    @Validation(id='mhd030', msg='comments have value', ref='')
    def mhd030() { comments().each { assertHasValue(it.valueString.@value.text())} }

    ///////////////////////////
    // ConfidentialityCode
    ///////////////////////////

    def confCodePresent() { dr.confidentiality.size() > 0}

    @Guard(methodNames=['confCodePresent'])
    @Validation(id='mhd040', msg='Validating confidentialityCode', ref='')
    def mhd040() {
        infoFound(true)
        dr.confidentiality.children().each { new CodingValidator(simHandle, it).asSelf().run() }}

    ///////////////////////////
    // CreationTime
    ///////////////////////////

    def creationTimePresent() { dr.created.size() > 0}

    @Guard(methodNames=['creationTimePresent'])
    @Validation(id='mhd045', msg='Validating creationTime', ref='')
    def mhd045() { infoFound('true')}

    ///////////////////////////
    // EventCodeList
    ///////////////////////////

    def eventCodePresent() { dr.context.event.size() > 0 }

    @Guard(methodNames=['eventCodePresent'])
    @Validation(id='mhd050', msg='Validating eventCodeList', ref='')
    def mhd050() {
        infoFound(true)
        dr.context.event.children().each {
            new CodingValidator(simHandle, it).asSelf().run()
        }
    }

    ///////////////////////////
    // FormatCode
    ///////////////////////////

    def formatCodePresent() { dr.format.size() > 0 }

    @Guard(methodNames=['formatCodePresent'])
    @Validation(id='mhd060', msg='Validating formatCode', ref='')
    def mhd060() {
        infoFound(true)
        dr.format.each {
            assertHasValue(it.@value.text())
        }
    }

    ///////////////////////////
    // hash
    ///////////////////////////

    def hashPresent() { dr.hash.size() > 0 }

    @Guard(methodNames=['hashPresent'])
    @Validation(id='mhd070', msg='Validating hash', ref='')
    def mhd070() {
        infoFound(true)
        dr.hash.each {
            def value = it.@value.text()
            assertHasValue(value)
            new HashValidator(simHandle, value).asSelf().run()
        }
    }

    ///////////////////////////
    // healthcareFacilityTypeCode
    ///////////////////////////

    def facilityTypePresent() { dr.context.facilityType.size() > 0 }

    @Guard(methodNames=['facilityTypePresent'])
    @Validation(id='mhd080', msg='Validating healthcareFacilityTypeCode', ref='')
    def mhd080() {
        infoFound(true)
        dr.context.facilityType.children().each {
            new CodingValidator(simHandle, it).asSelf().run()
        }
    }

    ///////////////////////////
    // homeCommunityId
    ///////////////////////////

    def homes() {
        dr.extension.findAll {
            it.@url.text() == 'http://ihe.net/fhir/Profile/XDS/extensions#homeCommunityId'
        }
    }
    def homePresent() {
        homes().size() > 0
    }

    @Guard(methodNames=['homePresent'])
    @Validation(id='mhd090', msg='Validating homeCommunityId', ref='')
    def mhd090() { infoFound('homeCommunityId') }

    @Guard(methodNames=['homePresent'])
    @Validation(id='mhd100', msg='homeCommunityId has value', ref='')
    def mhd100() { homes().each { assertHasValue(it.valueString.@value.text())} }

    ///////////////////////////
    // languageCode
    ///////////////////////////

    def languageCodePresent() { dr.primaryLanguage.size() > 0 }

    @Guard(methodNames=['languageCodePresent'])
    @Validation(id='mhd110', msg='Validating languageCode', ref='')
    def mhd110() {
        infoFound(true)
        dr.primaryLanguage.each {
            def value = it.@value.text()
            assertHasValue(value)
        }
    }

    ///////////////////////////
    // legalAuthenticator
    ///////////////////////////

    def authenticatorPresent() { dr.authenticator.size() > 0 }

    def authenticatorRefValues() {
        dr.authenticator.collect { it.reference.@value.text() }
    }

    def authenticatorTags() { authenticatorRefValues().collect { it.substring(1)}}

    def containedPractitioners() {
        dr.contained.findAll {
            it.Practitioner.size() > 0
        }.collect { it.Practitioner }
    }

    def containedPractitioners(id) {
        containedPractitioners().findAll { it.@id.text() == id}
    }

    @Guard(methodNames=['authenticatorPresent'])
    @Validation(id='mhd120', msg='Validating legalAuthenticator', ref='')
    def mhd120() { infoFound(true)}

    @Guard(methodNames=['authenticatorPresent'])
    @Validation(id='mhd130', msg='legalAuthenticator is local reference (starts with #)', ref='')
    def mhd130() { authenticatorRefValues().each { assertStartsWith(it, '#') } }

    @Guard(methodNames=['authenticatorPresent'])
    @Validation(id='mhd140', msg='legalAuthenticator references contained Practitioner', ref='')
    def mhd140() {
        authenticatorTags().each {
            containedPractitioners(it).each { practitioner ->
                new PractitionerNameValidator(simHandle, practitioner.name).asSelf().run()
            }
        }
    }

    ///////////////////////////
    // mimeType
    ///////////////////////////

    def mimeTypePresent() { dr.mimeType.size() > 0 }

    @Guard(methodNames=['mimeTypePresent'])
    @Validation(id='mhd150', msg='Validating mimeType', ref='')
    def mhd150() {
        infoFound(true)
        dr.mimeType.each {
            assertHasValue(it.@value.text())
        }
    }

    ///////////////////////////
    // patientId/subject
    ///////////////////////////

    def subjectPresent() { dr.subject.size() > 0 }

    @Guard(methodNames=['subjectPresent'])
    @Validation(id='mhd160', msg='Validating subject', ref='')
    def mhd160() {
        infoFound(true)
        new SubjectValidator(simHandle, dr).asSelf().run()
    }

    ///////////////////////////
    // serviceStartTime
    ///////////////////////////

    def startTimePresent() { dr.context.period.start.size() > 0 }

    @Guard(methodNames=['startTimePresent'])
    @Validation(id='mhd190', msg='Validating startTime', ref='')
    def mhd190() { assertHasValue(dr.context.period.start.@value.text())}

    ///////////////////////////
    // serviceStopTime
    ///////////////////////////

    def endTimePresent() { dr.context.period.end.size() > 0 }

    @Guard(methodNames=['endTimePresent'])
    @Validation(id='mhd200', msg='Validating endTime', ref='')
    def mhd200() { assertHasValue(dr.context.period.end.@value.text())}

    ///////////////////////////
    // sourcePatient
    ///////////////////////////

    def sourcePatientExtensions() {
        dr.extension.findAll {
            it.@url.text() == 'http://ihe.net/fhir/Profile/XDS/extensions#sourcePatient'
        }
    }
    def sourcePatientsPresent() { sourcePatientExtensions().size() > 0 }

    def sourcePatientRefValues() {
        sourcePatientExtensions().collect { it.valueResource.reference.@value.text() }
    }

    def sourcePatientRefTags() { sourcePatientRefValues().collect { it.substring(1)}}

    def sourcePatients() {
        def tags = sourcePatientRefTags()
        def subVal = new SubjectValidator(simHandle, dr)
        subVal.containedPatients().findAll { it.@id.text() in tags }
    }

    @Guard(methodNames=['sourcePatientsPresent'])
    @Validation(id='mhd210', msg='Validating sourcePatient', ref='')
    def mhd210() {
        sourcePatients().each { patient ->
            new PatientIdentifierValidator(simHandle, patient.identifier).asSelf().run()

        }
    }

    ///////////////////////////
    // practiceSettingCode
    ///////////////////////////

    def practiceSettingCodeExtensions() {
        dr.extension.findAll {
            it.@url.text() == 'http://ihe.net/fhir/Profile/XDS/extensions#practiceSettingCode'
        }
    }

    def practiceSettingCodesPresent() { practiceSettingCodeExtensions().size() > 0}

    @Guard(methodNames=['practiceSettingCodesPresent'])
    @Validation(id='mhd220', msg='Validating practiceSettingCode', ref='')
    def mhd220() {
        infoFound(true)
        practiceSettingCodeExtensions().each {
            new CodingValidator(simHandle, it.valueCoding, 'valueCoding').asSelf().run()
        }
    }

    ///////////////////////////
    // typeCode
    ///////////////////////////

    def typeCodePresent() { dr.type.size() > 0}

    @Guard(methodNames=['typeCodePresent'])
    @Validation(id='mhd230', msg='Validating typeCode', ref='')
    def mhd230() {
        infoFound(true)
        dr.type.each {
            new CodingValidator(simHandle, it.coding).asSelf().run()
        }
    }

    ///////////////////////////
    // uniqueId/masterIdentifier
    ///////////////////////////

    def uniqueIdPresent() { dr.masterIdentifier.size() > 0}

    @Guard(methodNames=['uniqueIdPresent'])
    @Validation(id='mhd240', msg='Validating masterIdentifier', ref='')
    def mhd240() {
        infoFound(true)
        dr.masterIdentifier.each {
            new MasterIdentifierValidator(simHandle, dr).asSelf().run()
        }
    }

}
