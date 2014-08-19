package gov.nist.hit.ds.dsSims.metadataValidator.object
import gov.nist.hit.ds.dsSims.client.ValidationContext
import gov.nist.hit.ds.dsSims.metadataValidator.datatype.DtmFormat
import gov.nist.hit.ds.dsSims.metadataValidator.datatype.XonXcnXtnFormat
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode

@groovy.transform.TypeChecked
public class SubmissionSetValidator extends AbstractRegistryObjectValidator implements TopLevelObject {
    SubmissionSetModel model

	static public String table416 = "ITI TF-3: Table 4.1-6";



    SubmissionSetValidator(SubmissionSetModel model) { super(model); this.model = model }

	public void validate(ErrorRecorder er, ValidationContext vc,
			Set<String> knownIds) {

		if (vc.skipInternalStructure)
			return;

		if (vc.isXDR)
			vc.isXDRLimited = model.isMetadataLimited();

		if (vc.isXDRLimited)
			er.sectionHeading("is labeled as Limited Metadata");

		if (vc.isXDRMinimal)
			er.sectionHeading("is labeled as Minimal Metadata (Direct)");

		validateTopAtts(er, vc);

		validateSlots(er, vc);

		if (vc.isXDM || vc.isXDRLimited)
			validateClassifications(er, vc, SubmissionSetModel.XDMclassificationDescription, table416);
		else if (vc.isXDRMinimal)
			validateClassifications(er, vc, SubmissionSetModel.MinimalclassificationDescription, table416);
		else
			validateClassifications(er, vc, SubmissionSetModel.classificationDescription, table416);

		if (vc.isXDM || vc.isXDRLimited)
			validateExternalIdentifiers(er, vc, SubmissionSetModel.XDMexternalIdentifierDescription, table416);
		else if (vc.isXDRMinimal)
			validateExternalIdentifiers(er, vc, SubmissionSetModel.MinimalexternalIdentifierDescription, table416);
		else
			validateExternalIdentifiers(er, vc, SubmissionSetModel.externalIdentifierDescription, table416);

		verifyIdsUnique(er, knownIds);
	}

	public void validateSlotsCodedCorrectly(ErrorRecorder er, ValidationContext vc)  {
		if (vc.isXDRMinimal) { 
			validateDirectSlotsCodedCorrectly(er, vc);
		} else {
			//                    name				   multi	format                                                  resource
			validateSlot(er, 	"submissionTime", 	   false, 	new DtmFormat(er, "Slot submissionTime",            table416),  table416);
			validateSlot(er, 	"intendedRecipient",   true, 	new XonXcnXtnFormat(er, "Slot intendedRecipient",      table416),  table416);
		}
	}

	public void validateDirectSlotsCodedCorrectly(ErrorRecorder er, ValidationContext vc)  {

		//                    name				   multi	format                                                  resource
		validateSlot(er, 	"submissionTime", 	   false, 	new DtmFormat(er, "Slot submissionTime",            table416),  table416);
		validateSlot(er, 	"intendedRecipient",   true, 	new XonXcnXtnFormat(er, "Slot intendedRecipient",     table416),  table416);
	}

	public void validateRequiredSlotsPresent(ErrorRecorder er, ValidationContext vc) {
		// Slots always required
		if (vc.isXDRMinimal) {
			for (String slotName : SubmissionSetModel.requiredSlotsMinimal) {
				if (model.getSlot(slotName) == null)
					er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": Slot " + slotName + " missing", this, table416);
			}
		} else {
			for (String slotName : SubmissionSetModel.requiredSlots) {
				if (model.getSlot(slotName) == null)
					er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": Slot " + slotName + " missing", this, table416);
			}
		}
	}

	public void validateSlotsLegal(ErrorRecorder er)  {
		verifySlotsUnique(er);
		for (Slot slot : model.getSlots()) {
			if ( ! legal_slot_name(slot.getName()))
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": " + slot.getName() + " is not a legal slot name for a SubmissionSet",  this,  table416);

		}
	}

	boolean legal_slot_name(String name) {
		if (name == null) return false;
		if (name.startsWith("urn:")) return true;
		return SubmissionSetModel.definedSlots.contains(name);
	}

	public void validateTopAtts(ErrorRecorder er, ValidationContext vc) {
		validateTopAtts(er, vc, table416, SubmissionSetModel.statusValues);
	}


}
