package gov.nist.hit.ds.dsSims.metadataValidator.object
import gov.nist.hit.ds.dsSims.metadataValidator.datatype.DtmSubValidator
import gov.nist.hit.ds.dsSims.metadataValidator.datatype.XonXcnXtnSubValidator
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.DependsOn
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
/**
 * Created by bmajur on 8/25/14.
 */
class SubmissionSetSlotsValidator extends ValComponentBase {
    SubmissionSetModel model
    SimHandle simHandle

    SubmissionSetSlotsValidator(SimHandle simHandle, SubmissionSetModel model) {
        super(simHandle.event)
        this.model = model
        this.simHandle = simHandle
    }

    // Guards
    def hasSubmissionTime() { model.getSlot('submissionTime')?.size() }
    def hasIntendedRecipient() { model.getSlot('intendedRecipient')?.size() }
    // TODO: This must be adjusted for Direct
    def intendedRecipientRequired() { false }


    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='SSSlot001', msg='submissionTime must be present and have single value', ref="ITI TF-3: Table 4.1-6")
    def submissionTimeSlotSingleValue() {
        if (!model.getSlot('submissionTime')?.size())
            fail('No value')
        else {
            assertEquals(1, model.getSlot('submissionTime').size())
            infoFound(model.getSlot('submissionTime').getValue(0))
        }
    }

    @Guard(methodNames = ['hasSubmissionTime'])
    @DependsOn(ids=['SSSlot001'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='SSSlot002', msg='submissionTime must be DTM format', ref="ITI TF-3: Table 4.1-6")
    def submissionTimeSlotFormat() {
        new DtmSubValidator(this, model.getSlot('submissionTime').getValue(0)).asSelf()run()
    }

    // TODO: When is intendedRecipient required
    @Guard(methodNames = ['intendedRecipientRequired'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='SSSlot003', msg='intendedRecipient present', ref="ITI TF-3: Table 4.1-6")
    def intendedRecipientCheck() {
        assertNotNull(model.getSlot('intendedRecipient'))
    }

    @Guard(methodNames = ['hasIntendedRecipient'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='SSSlot004', msg='hasIntendedRecipient must be XON or XCN or XTN format', ref="ITI TF-3: Table 4.1-6")
    def intendedRecipientFormat() {
        new XonXcnXtnSubValidator(this, model.getSlot('intendedRecipient')).asSelf().run()
    }


}
