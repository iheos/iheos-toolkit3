package gov.nist.hit.ds.dsSims.metadataValidator.object

import gov.nist.hit.ds.dsSims.client.ValidationContext
import gov.nist.hit.ds.dsSims.metadataValidator.datatype.*
import gov.nist.hit.ds.dsSims.metadataValidator.field.ValidatorCommon
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Optional
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

/**
 * Created by bmajur on 9/2/14.
 */
class DocumentEntrySlotsValidator extends ValComponentBase {
    DocumentEntryModel model
    SimHandle simHandle
    ValidationContext vc

    DocumentEntrySlotsValidator(SimHandle _simHandle, DocumentEntryModel _model, ValidationContext _vc) {
        super(_simHandle.event)
        model = _model
        simHandle = _simHandle
        vc = _vc
    }

    // Guards

    // /////////////////////////////////////////////////////////
    // creationTime
    //

    def hasCreationTime() {
        println "DE model is ${model}"
        SlotModel sm = model.getSlot('creationTime')
        println "Slot Model for creationTime is ${sm}"
        sm?.size()
    }

    def repositoryUniqueIdRequired() { return false }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot001', msg = 'creationTime must be present', ref = "ITI TF-3: Table 4.1-5")
    def creationTimePresent() {
        if (!hasCreationTime())
            fail('No value')
    }

    @Guard(methodNames = ['hasCreationTime'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot002', msg = 'creationTime must have single value', ref = "ITI TF-3: Table 4.1-5")
    def creationTimeSingleValue() {
        assertEquals(1, model.getSlot('creationTime').size())
    }

    @Guard(methodNames = ['hasCreationTime'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot003', msg = 'creationTime value', ref = "ITI TF-3: Table 4.1-5")
    def creationTimeValue() {
        infoFound(model.getSlot('creationTime').getValue(0))
    }

    @Guard(methodNames = ['hasCreationTime'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot004', msg = 'creationTime format validation follows', ref = "ITI TF-3: Table 4.1-5")
    def creationTimeFormat() {
        new DtmSubValidator(this, model.getSlot('creationTime').getValue(0)).asSelf().run()
    }

    // /////////////////////////////////////////////////////////
    // languageCode
    //

    def hasLanguageCode() { model.getSlot('languageCode')?.size() }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot011', msg = 'languageCode must be present', ref = "ITI TF-3: Table 4.1-5")
    def languageCodePresent() {
        if (!hasLanguageCode())
            fail('No value')
    }

    @Guard(methodNames = ['hasLanguageCode'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot012', msg = 'languageCode must have single value', ref = "ITI TF-3: Table 4.1-5")
    def languageCodeSingleValue() {
        assertEquals(1, model.getSlot('languageCode').size())
    }

    @Guard(methodNames = ['hasLanguageCode'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot013', msg = 'languageCode value', ref = "ITI TF-3: Table 4.1-5")
    def languageCodeValue() {
        for (int i = 0; i < model.getSlot('languageCode').size(); i++) {
            infoFound(model.getSlot('languageCode').getValue(i))
        }
    }

    @Guard(methodNames = ['hasLanguageCode'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot014', msg = 'languageCode format validation follows', ref = "ITI TF-3: Table 4.1-5")
    def languageCodeFormat() {
        new Rfc3066Validator(simHandle, model.getSlot('languageCode').getValue(0)).asSelf().run()
    }

    // /////////////////////////////////////////////////////////
    // sourcePatientId
    //

    def hasSourcePatientId() { model.getSlot('sourcePatientId')?.size() }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot021', msg = 'sourcePatientId must be present', ref = "ITI TF-3: Table 4.1-5")
    def sourcePatientIdPresent() {
        if (!hasSourcePatientId())
            fail('No value')
    }

    @Guard(methodNames = ['hasSourcePatientId'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot022', msg = 'sourcePatientId must have single value', ref = "ITI TF-3: Table 4.1-5")
    def sourcePatientIdSingleValue() {
        assertEquals(1, model.getSlot('sourcePatientId').size())
    }

    @Guard(methodNames = ['hasSourcePatientId'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot023', msg = 'sourcePatientId value', ref = "ITI TF-3: Table 4.1-5")
    def sourcePatientIdValue() {
        for (int i = 0; i < model.getSlot('sourcePatientId').size(); i++) {
            infoFound(model.getSlot('sourcePatientId').getValue(i))
        }
    }

    @Guard(methodNames = ['hasSourcePatientId'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot024', msg = 'sourcePatientId must be CX format', ref = "ITI TF-3: Table 4.1-5")
    def sourcePatientIdFormat() {
        String input = model.getSlot('sourcePatientId').getValue(0)
        String error = ValidatorCommon.validate_CX_datatype(input);
        if (error != null)
            fail(error, input)
    }

    // /////////////////////////////////////////////////////////
    // legalAuthenticator
    //

    def hasLegalAuthenticator() { model.getSlot('legalAuthenticator')?.size() }

    def notHasLegalAuthenticator() { !model.getSlot('legalAuthenticator')?.size() }

    def trueFunction() { true }

//    @Optional(methodNames = ['trueFunction'])
//    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
//    @Validation(id = 'DESlot031', msg = 'legalAuthenticator is present', ref = "ITI TF-3: Table 4.1-5")
//    def legalAuthenticatorPresent() {
//        if (!hasLegalAuthenticator())
//            fail('No value')
//    }

    @Guard(methodNames = ['hasLegalAuthenticator'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot032', msg = 'legalAuthenticator must have single value', ref = "ITI TF-3: Table 4.1-5")
    def legalAuthenticatorSingleValue() {
        assertEquals(1, model.getSlot('legalAuthenticator').size())
        for (int i = 0; i < model.getSlot('legalAuthenticator').size(); i++) {
            infoFound(model.getSlot('legalAuthenticator').getValue(i))
        }
    }

    @Guard(methodNames = ['hasLegalAuthenticator'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot033', msg = 'legalAuthenticator must be XCN format', ref = "ITI TF-3: Table 4.1-5")
    def legalAuthenticatorFormat() {
        new XcnSubValidator(this, model.getSlot('legalAuthenticator').getValue(0)).asSelf().run()
    }

    // /////////////////////////////////////////////////////////
    // serviceStartTime
    //

    def hasServiceStartTime() { model.getSlot('serviceStartTime')?.size() }

    def notHasServiceStartTime() { !model.getSlot('serviceStartTime')?.size() }

    @Optional(methodNames = ['notHasServiceStartTime'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot041', msg = 'serviceStartTime is present', ref = "ITI TF-3: Table 4.1-5")
    def serviceStartTimePresent() {
        if (!hasServiceStartTime())
            fail('No value')
    }

    @Guard(methodNames = ['hasServiceStartTime'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot042', msg = 'serviceStartTime must have single value', ref = "ITI TF-3: Table 4.1-5")
    def serviceStartTimeSingleValue() {
        assertEquals(1, model.getSlot('serviceStartTime').size())
        for (int i = 0; i < model.getSlot('serviceStartTime').size(); i++) {
            infoFound(model.getSlot('serviceStartTime').getValue(i))
        }
    }

    @Guard(methodNames = ['hasServiceStartTime'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot043', msg = 'serviceStartTime must be DTM format', ref = "ITI TF-3: Table 4.1-5")
    def serviceStartTimeFormat() {
        new DtmSubValidator(this, model.getSlot('serviceStartTime').getValue(0)).asSelf().run()
    }

    // /////////////////////////////////////////////////////////
    // serviceStopTime
    //

    def hasServiceStopTime() { model.getSlot('serviceStopTime')?.size() }

    def notHasServiceStopTime() { !model.getSlot('serviceStopTime')?.size() }

    @Optional(methodNames = ['notHasServiceStopTime'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot051', msg = 'serviceStopTime is present', ref = "ITI TF-3: Table 4.1-5")
    def serviceStopTimePresent() {
        if (!hasServiceStopTime())
            fail('No value')
    }

    @Guard(methodNames = ['hasServiceStopTime'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot052', msg = 'serviceStopTime must have single value', ref = "ITI TF-3: Table 4.1-5")
    def serviceStopTimeSingleValue() {
        assertEquals(1, model.getSlot('serviceStopTime').size())
        for (int i = 0; i < model.getSlot('serviceStopTime').size(); i++) {
            infoFound(model.getSlot('serviceStopTime').getValue(i))
        }
    }

    @Guard(methodNames = ['hasServiceStartTime'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot053', msg = 'serviceStopTime must be DTM format', ref = "ITI TF-3: Table 4.1-5")
    def serviceStopTimeFormat() {
        new DtmSubValidator(this, model.getSlot('serviceStopTime').getValue(0)).asSelf().run()
    }

    // /////////////////////////////////////////////////////////
    // hash
    //

    def hasHash() { model.getSlot('hash')?.size() }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot061', msg = 'hash is present', ref = "ITI TF-3: Table 4.1-5")
    def hashPresent() {
        if (!hasServiceStopTime())
            fail('No value')
    }

    @Guard(methodNames = ['hasHash'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot062', msg = 'hash must have single value', ref = "ITI TF-3: Table 4.1-5")
    def hashSingleValue() {
        assertEquals(1, model.getSlot('hash').size())
        for (int i = 0; i < model.getSlot('hash').size(); i++) {
            infoFound(model.getSlot('hash').getValue(i))
        }
    }

    @Guard(methodNames = ['hasHash'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot063', msg = 'hash must be hex format', ref = "ITI TF-3: Table 4.1-5")
    def hashFormat() {
        new HashValidator(simHandle, model.getSlot('hash').getValue(0)).asSelf().run()
    }

    // /////////////////////////////////////////////////////////
    // size
    //

    def hasSize() { model.getSlot('size')?.size() }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot071', msg = 'size is present', ref = "ITI TF-3: Table 4.1-5")
    def sizePresent() {
        if (!hasServiceStopTime())
            fail('No value')
    }

    @Guard(methodNames = ['hasSize'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot072', msg = 'size must have single value', ref = "ITI TF-3: Table 4.1-5")
    def sizeSingleValue() {
        assertEquals(1, model.getSlot('size').size())
        for (int i = 0; i < model.getSlot('size').size(); i++) {
            infoFound(model.getSlot('size').getValue(i))
        }
    }

    @Guard(methodNames = ['hasSize'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot073', msg = 'size must be hex format', ref = "ITI TF-3: Table 4.1-5")
    def sizeFormat() {
        new IntValidator(simHandle, model.getSlot('hash').getValue(0)).asSelf().run()
    }

    // /////////////////////////////////////////////////////////
    // URI
    //

    def hasURI() { model.getSlot('URI')?.size() }

//    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
//    @Validation(id = 'DESlot081', msg = 'URI is present', ref = "ITI TF-3: Table 4.1-5")
//    def uriPresent() {
//        if (!hasURI())
//            fail('No value')
//    }

    @Guard(methodNames = ['hasURI'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot082', msg = 'URI must have single value', ref = "ITI TF-3: Table 4.1-5")
    def uriSingleValue() {
        assertEquals(1, model.getSlot('URI').size())
        for (int i = 0; i < model.getSlot('URI').size(); i++) {
            infoFound(model.getSlot('URI').getValue(i))
        }
    }

    @Guard(methodNames = ['hasURI'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot083', msg = 'URI must be hex format', ref = "ITI TF-3: Table 4.1-5")
    def uriFormat() {
        new URIValidator(simHandle, model.getSlot('URI')).asSelf().run()
    }

    // /////////////////////////////////////////////////////////
    // repositoryUniqueId
    //

    def hasRepositoryUniqueId() { model.getSlot('repositoryUniqueId')?.size() }

    @Guard(methodNames = ['repositoryUniqueIdRequired'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot091', msg = 'repositoryUniqueId is present', ref = "ITI TF-3: Table 4.1-5")
    def repositoryUniqueIdPresent() {
        if (!hasRepositoryUniqueId())
            fail('No value')
    }

    @Guard(methodNames = ['hasRepositoryUniqueId'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot092', msg = 'repositoryUniqueId must have single value', ref = "ITI TF-3: Table 4.1-5")
    def repositoryUniqueIdSingleValue() {
        assertEquals(1, model.getSlot('repositoryUniqueId').size())
        for (int i = 0; i < model.getSlot('repositoryUniqueId').size(); i++) {
            infoFound(model.getSlot('repositoryUniqueId').getValue(i))
        }
    }

    @Guard(methodNames = ['hasRepositoryUniqueId'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot093', msg = 'repositoryUniqueId must be hex format', ref = "ITI TF-3: Table 4.1-5")
    def repositoryUniqueIdFormat() {
        new OidValidator(simHandle, model.getSlot('repositoryUniqueId').getValue(0)).asSelf().run()
    }

    // /////////////////////////////////////////////////////////
    // documentAvailability
    //

    def hasDocumentAvailability() { model.getSlot('documentAvailability')?.size() }

//    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
//    @Validation(id = 'DESlot101', msg = 'documentAvailability is present', ref = "ITI TF-3: Table 4.1-5")
//    def documentAvailabilityPresent() {
//        if (!hasDocumentAvailability())
//            fail('No value')
//    }

    @Guard(methodNames = ['hasDocumentAvailability'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot102', msg = 'documentAvailability must have single value', ref = "ITI TF-3: Table 4.1-5")
    def documentAvailabilitySingleValue() {
        assertEquals(1, model.getSlot('documentAvailability').size())
        for (int i = 0; i < model.getSlot('documentAvailability').size(); i++) {
            infoFound(model.getSlot('documentAvailability').getValue(i))
        }
    }

    @Guard(methodNames = ['hasDocumentAvailability'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot103', msg = 'documentAvailability must be hex format', ref = "ITI TF-3: Table 4.1-5")
    def documentAvailabilityFormat() {
        assertIn(['urn:ihe:iti:2010:DocumentAvailability:Online', 'urn:ihe:iti:2010:DocumentAvailability:Offline'], model.getSlot('documentAvailability').getValue(0))
    }

    // /////////////////////////////////////////////////////////
    // sourcePatientInfo
    //

    def hasSourcePatientInfo() { model.getSlot('sourcePatientInfo')?.size() }

    def notHasSourcePatientInfo() { !model.getSlot('sourcePatientInfo')?.size() }

    @Optional(methodNames = ['notHasSourcePatientInfo'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot111', msg = 'sourcePatientInfo is present', ref = "ITI TF-3: Table 4.1-5")
    def sourcePatientInfoPresent() {
        if (!hasSourcePatientInfo())
            fail('No value')
    }

//    @Guard(methodNames = ['hasSourcePatientInfo'])
//    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
//    @Validation(id = 'DESlot112', msg = 'sourcePatientInfo must have single value', ref = "ITI TF-3: Table 4.1-5")
//    def sourcePatientInfoSingleValue() {
//        assertEquals(1, model.getSlot('sourcePatientInfo').size())
//        for (int i = 0; i < model.getSlot('sourcePatientInfo').size(); i++) {
//            infoFound(model.getSlot('sourcePatientInfo').getValue(i))
//        }
//    }

    @Guard(methodNames = ['hasSourcePatientInfo'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DESlot113', msg = 'sourcePatientInfo must be CX format', ref = "ITI TF-3: Table 4.1-5")
    def sourcePatientInfoFormat() {
        new SourcePatientInfoValidator(simHandle, model.getSlot('sourcePatientInfo')).asSelf().run()
    }


}
