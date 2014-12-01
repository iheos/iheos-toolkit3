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
class LidSubValidator extends ValComponentBase {
    ValComponentBase base
    RegistryObjectModel model
    ValidationContext vc

    LidSubValidator(ValComponentBase base, RegistryObjectModel model, ValidationContext vc) {
        super(base.event)
        this.base = base
        this.model = model
        this.vc = vc
    }

    // Guards
    def sqResponse() { vc.isSQ && vc.isResponse }
    def notSqResponse() { !sqResponse() }
    def hasLid() { model.lid != null && !model.lid.equals("") }
    def isUUID() { model.lid.startsWith("urn:uuid:") }

    @Guard(methodNames = ['sqResponse'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'Lid001', msg = 'lid must be present', ref='ITI TF-3: 4.1.12.3')
    def lidExistsCheck() { if (!hasLid()) fail('Not present') }

    @Guard(methodNames = ['sqResponse'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'Lid002', msg = 'Response lid must be UUID', ref='ITI TF-3: 4.1.12.3')
    def responseLidIsUUIDCheck() { new UuidFormat(this).validate(model.lid) }

    @Guard(methodNames = ['hasLid', 'notSqResponse', 'isUUID'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'Lid003', msg = 'UUID has valid format', ref='ITI TF-3: 4.1.12.3')
    def ifUUIDUUIDCheck() { new UuidFormat(this).validate(model.lid) }

}