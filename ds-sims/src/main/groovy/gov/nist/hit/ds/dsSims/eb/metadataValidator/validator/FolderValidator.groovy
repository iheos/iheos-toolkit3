package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator
import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.DtmFormatValidator
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.FolderModel
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.SlotModel
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
/**
 * Created by bmajur on 12/23/14.
 */
public class FolderValidator extends AbstractRegistryObjectVal {
    FolderModel model
    SimHandle simHandle
    ValidationContext vc
    Set<String> knownIds

    FolderValidator(SimHandle _simHandle, FolderModel _model, ValidationContext _vc, Set<String> _knownIds) {
        super(_simHandle)
        model = _model
        vc = _vc
        knownIds = _knownIds
    }

    @Override
    void run() {
        if (vc.skipInternalStructure) return;

        if (vc.isXDR) vc.isXDRLimited = model.isMetadataLimited();

        runValidationEngine()
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rofo010', msg='Identify options', ref='')
    def rofo010() {
        if (vc.isXDRLimited) infoFound("Labeled as Limited Metadata");
        if (vc.isXDRMinimal) infoFound("Labeled as Minimal Metadata (Direct)");
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rofo020', msg='Validate Folder top-level attributes', ref='ITI TF-3: Table 4.1-7')
    def rofo020() {
        new TopAttsValidator(simHandle, model, vc, FolderModel.statusValues)
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rofo030', msg='Validating that Slots present are legal', ref='ITI TF-3: Table 4.1-7')
    def rofo030() {
        new SlotsUniqueValidator(simHandle, model.slots).asSelf(this).run()
        for (SlotModel slot : model.getSlots()) {
            if ( ! legal_slot_name(slot.getName()))
                fail(model.identifyingString() + ": " + slot.getName() + " is not a legal slot name for a SubmissionSet")
        }
    }

    boolean xdrMinimal() { vc.isXDRMinimal  }
    boolean notXdrMinimal() { !vc.isXDRMinimal }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rofo040', msg='Validating required Slots present', ref='ITI TF-3: Table 4.1-6')
    def rofo040() {
        for (String slotName : FolderModel.requiredSlots) {
            if (!model.getSlot(slotName))
                fail(model.identifyingString() + ": Slot " + slotName + " missing")
        }
    }

    @Guard(methodNames=['xdrMinimal'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rofo060', msg='Validating Slots are coded correctly', ref='ITI TF-3: Table 4.1-7')
    def rofo060() {
            //                    name				   multi	format                                                  resource
            validateSlot(model, "lastUpdateTime", 	   false, 	new DtmFormatValidator(simHandle, "Slot lastUpdateTime"))
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rofo080', msg='Validating Classifications', ref='ITI TF-3: Table 4.1-7')
    def rofo080() {
        if (vc.isXDM || vc.isXDRLimited)
            new ClassificationValidator(simHandle, model, vc, FolderModel.XDMclassificationDescription).asSelf(this).run()
        else
            new ClassificationValidator(simHandle, model, vc, FolderModel.classificationDescription).asSelf(this).run()
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rofo090', msg='Validating ExternalIdentifiers', ref='ITI TF-3: Table 4.1-6')
    def rofo090() {

        if (vc.isXDM || vc.isXDRLimited)
            new ExternalIdentifierValidator(simHandle, model, vc, FolderModel.XDMexternalIdentifierDescription).asSelf(this).run()
        else
            new ExternalIdentifierValidator(simHandle, model, vc, FolderModel.externalIdentifierDescription).asSelf(this).run()

        new IdUniqueValidator(simHandle, model.id, knownIds).asSelf(this).run()
    }

    boolean legal_slot_name(String name) {
        if (name == null) return false;
        if (name.startsWith("urn:")) return true;
        return FolderModel.definedSlots.contains(name);
    }

    boolean equals(FolderModel f)  {
        if (!model.id.equals(model.id))
            return false;
        return	super.equals(f);
    }

}
