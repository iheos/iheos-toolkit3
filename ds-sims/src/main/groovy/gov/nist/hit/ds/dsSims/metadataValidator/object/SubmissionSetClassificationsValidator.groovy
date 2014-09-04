package gov.nist.hit.ds.dsSims.metadataValidator.object

import gov.nist.hit.ds.dsSims.client.ValidationContext
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.metadata.MetadataSupport
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

/**
 * Created by bmajur on 9/2/14.
 */
class SubmissionSetClassificationsValidator extends ValComponentBase {
    SubmissionSetModel model
    SimHandle simHandle
    ValidationContext vc

    SubmissionSetClassificationsValidator(SimHandle _simHandle, SubmissionSetModel _model, ValidationContext _vc) {
        super(_simHandle.event)
        model = _model
        simHandle = _simHandle
        vc = _vc
    }

    // Guards
    def xdmXdrLimited() { vc.isXDM || vc.isXDRLimited}
    def xdrMinimal() { vc.isXDRMinimal}

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='SSClasLegal001', msg='submissionTime must be present', ref="ITI TF-3: Table 4.1-6")
    def submissionTimeSlotPresent() {
        if (!model.getSlot('submissionTime')?.size())
            fail('No value')
    }

    // legal
    MetadataSupport.XDSSubmissionSet_contentTypeCode_uuid ,
    MetadataSupport.XDSSubmissionSet_author_uuid

}
