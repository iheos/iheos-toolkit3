package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator
import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.DtmFormatValidator
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.FolderModel
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.SlotModel
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
/**
 * Created by bmajur on 12/23/14.
 */
public class FolderVal  extends AbstractRegistryObjectVal  implements TopLevelObjectVal {
    public FolderModel model;

    public FolderVal(FolderModel model) { this.model = model; }

    public void validate(ErrorRecorder er, ValidationContext vc,
                         Set<String> knownIds) {

        if (vc.skipInternalStructure)
            return;

        if (vc.isXDR)
            vc.isXDRLimited = model.isMetadataLimited();

        if (vc.isXDRLimited)
            er.sectionHeading("Limited Metadata");

        validateTopAtts(er, vc);

        validateSlots(er, vc);

        if (vc.isXDM || vc.isXDRLimited)
            validateClassifications(er, vc, FolderModel.XDMclassificationDescription, FolderModel.table417);
        else
            validateClassifications(er, vc, FolderModel.classificationDescription, FolderModel.table417);

        if (vc.isXDM || vc.isXDRLimited)
            validateExternalIdentifiers(er, vc, FolderModel.XDMexternalIdentifierDescription, FolderModel.table417);
        else
            validateExternalIdentifiers(er, vc, FolderModel.externalIdentifierDescription, FolderModel.table417);

        new IdUniqueValidator(simHandle, model.id, knownIds).asSelf(this).run()
    }

    public void validateSlotsCodedCorrectly(ErrorRecorder er, ValidationContext vc)  {

        //                    name				   multi	format                                                  resource
        validateSlot(model, er, 	"lastUpdateTime", 	   false, 	new DtmFormatValidator(er, "Slot lastUpdateTime",            FolderModel.table417),  FolderModel.table417);
    }

    public void validateRequiredSlotsPresent(ErrorRecorder er, ValidationContext vc) {
        // Slots always required
        for (String slotName : FolderModel.requiredSlots) {
            if (model.getSlot(slotName) == null)
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": Slot " + slotName + " missing", this, FolderModel.table417);
        }
    }

    public void validateSlotsLegal(ErrorRecorder er)  {
        new SlotsUniqueValidator(simHandle, model.slots).asSelf(this).run()
        for (SlotModel slot : model.getSlots()) {
            if ( ! legal_slot_name(slot.getName()))
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": " + slot.getName() + " is not a legal slot name for a SubmissionSet",  this,  FolderModel.table417);

        }
    }

    boolean legal_slot_name(String name) {
        if (name == null) return false;
        if (name.startsWith("urn:")) return true;
        return FolderModel.definedSlots.contains(name);
    }

    public void validateTopAtts(ErrorRecorder er, ValidationContext vc) {
        new TopAttsValidator(simHandle, model, vc, FolderModel.statusValues)
    }

    public boolean equals(FolderModel f)  {
        if (!model.id.equals(model.id))
            return false;
        return	super.equals(f);
    }

    public void validateSlots(ErrorRecorder er, ValidationContext vc) {
        er.challenge("Validating that Slots present are legal");
        validateSlotsLegal(er);
        er.challenge("Validating required Slots present");
        validateRequiredSlotsPresent(er, vc);
        er.challenge("Validating Slots are coded correctly");
        validateSlotsCodedCorrectly(er, vc);
    }

}
