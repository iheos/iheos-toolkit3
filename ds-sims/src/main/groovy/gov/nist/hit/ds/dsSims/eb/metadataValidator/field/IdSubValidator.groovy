package gov.nist.hit.ds.dsSims.eb.metadataValidator.field

import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.UuidFormat
import gov.nist.hit.ds.dsSims.eb.metadataValidator.object.RegistryObjectModel
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

/**
 * Created by bmajur on 8/22/14.
 */
class IdSubValidator extends ValComponentBase {
    ValComponentBase base
    RegistryObjectModel model
    ValidationContext vc

    IdSubValidator(ValComponentBase base, RegistryObjectModel model, ValidationContext vc) {
        super(base.event)
        this.base = base
        this.model = model
        this.vc = vc
    }

    // Guards
    def sqResponse() { vc.isSQ && vc.isResponse }
    def notSqResponse() { !sqResponse() }
    def hasId() { model.id != null &&  !model.id.equals("") }
    def isUUID() { model.id.startsWith("urn:uuid:") }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'Id001', msg = 'id must be present', ref='ITI TF-3: 4.1.12.3')
    def idExistsCheck() { assertHasValue(model.id) }

    @Guard(methodNames = ['hasId', 'sqResponse'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'Id002', msg = 'Response id must be UUID', ref='ITI TF-3: 4.1.12.3')
    def responseIdIsUUIDCheck() { new UuidFormat(this).validate(model.id) }

    @Guard(methodNames = ['hasId', 'notSqResponse', 'isUUID'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'Id003', msg = 'UUID has valid format', ref='ITI TF-3: 4.1.12.3')
    def ifUUIDUUIDCheck() { new UuidFormat(this).validate(model.id) }

}