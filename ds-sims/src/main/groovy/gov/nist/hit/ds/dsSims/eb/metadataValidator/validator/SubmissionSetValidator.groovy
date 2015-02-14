package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator
import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.DtmFormatValidator
import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.XonXcnXtnFormatValidator
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.SlotModel
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.SubmissionSetModel
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
/**
 * Created by bmajur on 12/23/14.
 */
public class SubmissionSetValidator extends AbstractRegistryObjectVal {
    SubmissionSetModel model
    ValidationContext vc
    Set<String> knownIds

    SubmissionSetValidator(SimHandle _simHandle, SubmissionSetModel _model, ValidationContext _vc, Set<String> _knownIds) {
        super(_simHandle)
        model = _model
        vc = _vc
        knownIds = _knownIds
    }

    void run() {
//        if (vc.skipInternalStructure) return;

        if (vc.isXDR) vc.isXDRLimited = model.isMetadataLimited();

        runValidationEngine()
    }

    @Validation(id='ross001', msg='Is Enabled', ref='')
    def ross001() {
        if (vc.skipInternalStructure) {
            infoMsg('Skipping validation of internal structure')
            quit()
        }  // run no more rules
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='ross010', msg='Identify options', ref='')
    def ross010() {
        if (vc.isXDRLimited) infoMsg("Labeled as Limited Metadata");
        if (vc.isXDRMinimal) infoMsg("Labeled as Minimal Metadata (Direct)");
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='ross020', msg='Validate SubmissionSet top-level attributes', ref='ITI TF-3: Table 4.1-6')
    def ross020() {
        new TopAttsValidator(simHandle, model, vc, SubmissionSetModel.statusValues)
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='ross025', msg='Validating that Slots are unique', ref='ITI TF-3: Table 4.1-6')
    def ross025() {
        new SlotsUniqueValidator(simHandle, model.slots).asSelf(this).run()
    }

        @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='ross030', msg='Validating that Slots present are legal', ref='ITI TF-3: Table 4.1-6')
    def ross030() {
        for (SlotModel slot : model.getSlots()) {
            infoMsg('  ')
            infoFound("${slot.getName()}")
            if (!legal_slot_name(slot.getName())) {
                fail(model.identifyingString() + ": " + slot.getName() + " is not a legal slot name for a SubmissionSet")
            }
        }
    }

    boolean xdrMinimal() { vc.isXDRMinimal  }
    boolean notXdrMinimal() { !vc.isXDRMinimal }

    @Guard(methodNames=['xdrMinimal'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='ross040', msg='Validating required Slots present (XDR Minimal)', ref='ITI TF-3: Table 4.1-6')
    def ross040() {
        for (String slotName : SubmissionSetModel.requiredSlotsMinimal) {
            if (!model.getSlot(slotName))
                fail(model.identifyingString() + ": Slot " + slotName + " missing")
        }
    }

    @Guard(methodNames=['notXdrMinimal'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='ross050', msg='Validating required Slots present', ref='ITI TF-3: Table 4.1-6')
    def ross050() {

        for (String slotName : SubmissionSetModel.requiredSlots) {
            if (!model.getSlot(slotName))
                fail(model.identifyingString() + ": Slot " + slotName + " missing");
        }
    }

    @Guard(methodNames=['xdrMinimal'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='ross060', msg='Validating Slots are coded correctly (XDR Minimal)', ref='ITI TF-3: Table 4.1-6')
    def ross060() {
        //                             name				   multi	format
        validateSlot(model, "submissionTime", 	   false, 	new DtmFormatValidator(simHandle, "Slot submissionTime"));
        validateSlot(model, "intendedRecipient",   true, 	new XonXcnXtnFormatValidator(simHandle, "Slot intendedRecipient"));
    }

    @Guard(methodNames=['notXdrMinimal'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='ross070', msg='Validating Slots are coded correctly', ref='ITI TF-3: Table 4.1-6')
    def ross070() {
        //                           name				   multi	format
        validateSlot(model, "submissionTime", 	   false, 	new DtmFormatValidator(simHandle, "Slot submissionTime"));
        validateSlot(model, "intendedRecipient",   true, 	new XonXcnXtnFormatValidator(simHandle, "Slot intendedRecipient"));
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='ross080', msg='Validating Classifications', ref='ITI TF-3: Table 4.1-6')
    def ross080() {
        if (vc.isXDM || vc.isXDRLimited)
            new ClassificationValidator(simHandle, model, vc, SubmissionSetModel.XDMclassificationDescription).asSelf(this).run()
        else if (vc.isXDRMinimal)
            new ClassificationValidator(simHandle, model, vc, SubmissionSetModel.MinimalclassificationDescription).asSelf(this).run()
        else
            new ClassificationValidator(simHandle, model, vc, SubmissionSetModel.classificationDescription).asSelf(this).run()
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='ross090', msg='Validating ExternalIdentifiers', ref='ITI TF-3: Table 4.1-6')
    def ross090() {

        if (vc.isXDM || vc.isXDRLimited)
            new ExternalIdentifierValidator(simHandle, model, vc, SubmissionSetModel.XDMexternalIdentifierDescription).asSelf(this).run()
        else if (vc.isXDRMinimal)
            new ExternalIdentifierValidator(simHandle, model, vc, SubmissionSetModel.MinimalexternalIdentifierDescription).asSelf(this).run()
        else
            new ExternalIdentifierValidator(simHandle, model, vc, SubmissionSetModel.externalIdentifierDescription).asSelf(this).run()

        new IdUniqueValidator(simHandle, model.id, knownIds).asSelf(this).run()
    }

    boolean legal_slot_name(String name) {
        if (name == null) return false;
        if (name.startsWith("urn:")) return true;
        return SubmissionSetModel.definedSlots.contains(name);
    }


}
