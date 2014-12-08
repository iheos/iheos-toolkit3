package gov.nist.hit.ds.dsSims.fhir.mhd.validators

import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.OidValidator
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

/**
 * Created by bmajur on 12/7/14.
 */
class MhdDocManValidator extends ValComponentBase {
    SimHandle simHandle
    def dr

    // dr is XmlSlurper representation of a DocumentManifest
    MhdDocManValidator(SimHandle _simHandle, def _dr) {
        super(_simHandle.event)
        simHandle = _simHandle
        dr = _dr
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
    @Validation(id='mhdn020', msg='Validating comments', ref='')
    def mhd020() { infoFound('Comments') }

    @Guard(methodNames=['commentPresent'])
    @Validation(id='mhdn030', msg='comments have value', ref='')
    def mhd030() { comments().each { assertHasValue(it.valueString.@value.text())} }



    ///////////////////////////
    // ContentTypeCode
    ///////////////////////////

    def contentTypeCodePresent() { dr.type.size() > 0}

    @Guard(methodNames=['contentTypeCodePresent'])
    @Validation(id='mhd040', msg='Validating contentTypeCode', ref='')
    def mhdm040() {
        infoFound(true)
        dr.type.children().each { new CodingValidator(simHandle, it).asSelf().run() }}

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
    @Validation(id='mhdm090', msg='Validating homeCommunityId', ref='')
    def mhd090() { infoFound('homeCommunityId') }

    @Guard(methodNames=['homePresent'])
    @Validation(id='mhdm100', msg='homeCommunityId has value', ref='')
    def mhd100() { homes().each { assertHasValue(it.valueString.@value.text())} }

    ///////////////////////////
    // patientId/subject
    ///////////////////////////

    def subjectPresent() { dr.subject.size() > 0 }

    @Guard(methodNames=['subjectPresent'])
    @Validation(id='mhdm160', msg='Validating subject', ref='')
    def mhdm160() {
        infoFound(true)
        new SubjectValidator(simHandle, dr).asSelf().run()
    }

    ///////////////////////////
    // sourceId
    ///////////////////////////

    def sourceIdPresent() { dr.source.size() > 0 }

    @Guard(methodNames=['sourceIdPresent'])
    @Validation(id='mhdm070', msg='Validating hash', ref='')
    def mhdm070() {
        infoFound(true)
        dr.hash.each {
            def value = it.@value.text()
            assertHasValue(value)
            new OidValidator(simHandle, value).asSelf().run()
        }
    }

    ///////////////////////////
    // submissionTime
    ///////////////////////////

    def submissionTimePresent() { dr.created.size() > 0 }

    @Guard(methodNames=['submissionTimePresent'])
    @Validation(id='mhdm200', msg='Validating endTime', ref='')
    def mhdm200() { assertHasValue(dr.created.@value.text())}


    ///////////////////////////
    // uniqueId/masterIdentifier
    ///////////////////////////

    def uniqueIdPresent() { dr.masterIdentifier.size() > 0}

    @Guard(methodNames=['uniqueIdPresent'])
    @Validation(id='mhdm500', msg='masterIdentifier present', ref='')
    def mhdm500() {
        infoFound(true)
        dr.masterIdentifier.each {
            new MasterIdentifierValidator(simHandle, dr).asSelf().run()
        }
    }

}
