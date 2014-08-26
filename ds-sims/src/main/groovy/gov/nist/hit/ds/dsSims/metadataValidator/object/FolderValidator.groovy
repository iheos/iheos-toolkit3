package gov.nist.hit.ds.dsSims.metadataValidator.object
import gov.nist.hit.ds.dsSims.client.ValidationContext
import gov.nist.hit.ds.dsSims.metadataValidator.datatype.DtmSubValidator
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode

//@groovy.transform.TypeChecked
public class FolderValidator extends AbstractRegistryObjectValidator {
	static public String table417 = "ITI TF-3: Table 4.1-7";
    FolderModel model

    FolderValidator(Event event, FolderModel model) {
        super(event, model)
        this.model = model
    }

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
			validateClassifications(er, vc, model.XDMclassificationDescription, table417);
		else
			validateClassifications(er, vc, model.classificationDescription, table417);

		if (vc.isXDM || vc.isXDRLimited)
			validateExternalIdentifiers(er, vc, model.XDMexternalIdentifierDescription, table417);
		else
			validateExternalIdentifiers(er, vc, model.externalIdentifierDescription, table417);

		verifyIdsUnique(er, knownIds);
	}

	public void validateSlotsCodedCorrectly(ErrorRecorder er, ValidationContext vc)  {

		//                    name				   multi	format                                                  resource
		validateSlot(er, 	"lastUpdateTime", 	   false, 	new DtmSubValidator(er, "Slot lastUpdateTime",            table417),  table417);
	}

	public void validateRequiredSlotsPresent(ErrorRecorder er, ValidationContext vc) {
		// Slots always required
		for (String slotName : model.requiredSlots) {
			if (model.getSlot(slotName) == null)
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": Slot " + slotName + " missing", this, table417);
		}
	}

	public void validateSlotsLegal(ErrorRecorder er)  {
		verifySlotsUnique(er);
		for (SlotModel slot : model.getSlots()) {
			if ( ! legal_slot_name(slot.getName()))
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": " + slot.getName() + " is not a legal slot name for a SubmissionSet",  this,  table417);

		}
	}

	boolean legal_slot_name(String name) {
		if (name == null) return false;
		if (name.startsWith("urn:")) return true;
		return model.definedSlots.contains(name);
	}

	public void validateTopAtts(ErrorRecorder er, ValidationContext vc) {
		validateTopAtts(er, vc, table417, model.statusValues);
	}


}
