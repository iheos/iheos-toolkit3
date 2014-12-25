package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator;

import gov.nist.hit.ds.dsSims.eb.client.ValidationContext;
import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.DtmFormat;
import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.XonXcnXtnFormat;
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.SlotModel;
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.SubmissionSetModel;
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode;

import java.util.Set;

/**
 * Created by bmajur on 12/23/14.
 */
public class SubmissionSetVal extends AbstractRegistryObjectVal  implements TopLevelObjectVal {
    public SubmissionSetModel model;

    public SubmissionSetVal(SubmissionSetModel model) { this.model = model; }

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
            validateClassifications(er, vc, SubmissionSetModel.XDMclassificationDescription, SubmissionSetModel.table416);
        else if (vc.isXDRMinimal)
            validateClassifications(er, vc, SubmissionSetModel.MinimalclassificationDescription, SubmissionSetModel.table416);
        else
            validateClassifications(er, vc, SubmissionSetModel.classificationDescription, SubmissionSetModel.table416);

        if (vc.isXDM || vc.isXDRLimited)
            validateExternalIdentifiers(er, vc, SubmissionSetModel.XDMexternalIdentifierDescription, SubmissionSetModel.table416);
        else if (vc.isXDRMinimal)
            validateExternalIdentifiers(er, vc, SubmissionSetModel.MinimalexternalIdentifierDescription, SubmissionSetModel.table416);
        else
            validateExternalIdentifiers(er, vc, SubmissionSetModel.externalIdentifierDescription, SubmissionSetModel.table416);

        verifyIdsUnique(model, er, knownIds);
    }

    @Override
    public void validateSlotsCodedCorrectly(ErrorRecorder er, ValidationContext vc)  {
        if (vc.isXDRMinimal) {
            validateDirectSlotsCodedCorrectly(er, vc);
        } else {
            //                    name				   multi	format                                                  resource
            validateSlot(model, er, 	"submissionTime", 	   false, 	new DtmFormat(er, "Slot submissionTime",            SubmissionSetModel.table416),  SubmissionSetModel.table416);
            validateSlot(model, er, 	"intendedRecipient",   true, 	new XonXcnXtnFormat(er, "Slot intendedRecipient",      SubmissionSetModel.table416),  SubmissionSetModel.table416);
        }
    }

    public void validateDirectSlotsCodedCorrectly(ErrorRecorder er, ValidationContext vc)  {

        //                    name				   multi	format                                                  resource
        validateSlot(model, er, 	"submissionTime", 	   false, 	new DtmFormat(er, "Slot submissionTime",            SubmissionSetModel.table416),  SubmissionSetModel.table416);
        validateSlot(model, er, 	"intendedRecipient",   true, 	new XonXcnXtnFormat(er, "Slot intendedRecipient",     SubmissionSetModel.table416),  SubmissionSetModel.table416);
    }

    @Override
    public void validateRequiredSlotsPresent(ErrorRecorder er, ValidationContext vc) {
        // Slots always required
        if (vc.isXDRMinimal) {
            for (String slotName : SubmissionSetModel.requiredSlotsMinimal) {
                if (model.getSlot(slotName) == null)
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": Slot " + slotName + " missing", this, SubmissionSetModel.table416);
            }
        } else {
            for (String slotName : SubmissionSetModel.requiredSlots) {
                if (model.getSlot(slotName) == null)
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": Slot " + slotName + " missing", this, SubmissionSetModel.table416);
            }
        }
    }

    @Override
    public void validateSlotsLegal(ErrorRecorder er)  {
        verifySlotsUnique(model, er);
        for (SlotModel slot : model.getSlots()) {
            if ( ! legal_slot_name(slot.getName()))
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": " + slot.getName() + " is not a legal slot name for a SubmissionSet",  this,  SubmissionSetModel.table416);

        }
    }

    boolean legal_slot_name(String name) {
        if (name == null) return false;
        if (name.startsWith("urn:")) return true;
        return SubmissionSetModel.definedSlots.contains(name);
    }

    public void validateTopAtts(ErrorRecorder er, ValidationContext vc) {
        validateTopAtts(model, er, vc, SubmissionSetModel.table416, SubmissionSetModel.statusValues);
    }


}
