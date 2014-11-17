package gov.nist.hit.ds.mhd

import gov.nist.hit.ds.dsSims.metadataValidator.datatype.HashValidator
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
    @Validation(id='mhd010', msg='classCode present', ref='')
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
    @Validation(id='mhd020', msg='comments present', ref='')
    def mhd020() { infoFound('Comments') }

    @Guard(methodNames=['commentPresent'])
    @Validation(id='mhd030', msg='comments have value', ref='')
    def mhd030() { comments().each { assertHasValue(it.valueString.@value.text())} }

    ///////////////////////////
    // ConfidentialityCode
    ///////////////////////////

    def confCodePresent() { dr.confidentiality.size() > 0}

    @Guard(methodNames=['confCodePresent'])
    @Validation(id='mhd040', msg='confidentialityCode present', ref='')
    def mhd040() {
        infoFound(true)
        dr.confidentiality.children().each { new CodingValidator(simHandle, it).asSelf().run() }}

    ///////////////////////////
    // CreationTime
    ///////////////////////////

    def creationTimePresent() { dr.created.size() > 0}

    @Guard(methodNames=['creationTimePresent'])
    @Validation(id='mhd045', msg='creationTime present', ref='')
    def mhd045() { infoFound('true')}

    ///////////////////////////
    // EventCodeList
    ///////////////////////////

    def eventCodePresent() { dr.context.event.size() > 0 }

    @Guard(methodNames=['eventCodePresent'])
    @Validation(id='mhd050', msg='eventCodeList present', ref='')
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
    @Validation(id='mhd060', msg='formatCode present', ref='')
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
    @Validation(id='mhd070', msg='hash present', ref='')
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
    @Validation(id='mhd080', msg='healthcareFacilityTypeCode present', ref='')
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
    @Validation(id='mhd090', msg='homeCommunityId present', ref='')
    def mhd090() { infoFound('homeCommunityId') }

    @Guard(methodNames=['homePresent'])
    @Validation(id='mhd100', msg='homeCommunityId has value', ref='')
    def mhd100() { homes().each { assertHasValue(it.valueString.@value.text())} }

    ///////////////////////////
    // languageCode
    ///////////////////////////

    def languageCodePresent() { dr.primaryLanguage.size() > 0 }

    @Guard(methodNames=['languageCodePresent'])
    @Validation(id='mhd110', msg='languageCode present', ref='')
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
    @Validation(id='mhd120', msg='legalAuthenticator present', ref='')
    def mhd120() { infoFound(true)}

    @Guard(methodNames=['authenticatorPresent'])
    @Validation(id='mhd130', msg='legalAuthenticator is local reference (starts with #)', ref='')
    def mhd130() { authenticatorRefValues().each { assertStartsWith(it, '#') } }

    @Guard(methodNames=['authenticatorPresent'])
    @Validation(id='mhd140', msg='legalAuthenticator references contained Practitioner', ref='')
    def mhd140() {
        authenticatorTags().each {
            infoFound("Practitioner id=${it}")
            containedPractitioners(it).each { practitioner ->
                infoFound("Pract")
                new PractitionerNameValidator(simHandle, practitioner.name).asSelf().run()
            }
        }
    }
}
