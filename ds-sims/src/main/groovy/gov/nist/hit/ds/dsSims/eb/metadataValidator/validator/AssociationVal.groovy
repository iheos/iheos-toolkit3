package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator
import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.metadata.MetadataSupport
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.AssociationModel
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.ClassificationModel
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.SlotModel
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
/**
 * Created by bmajur on 12/23/14.
 */
public class AssociationVal extends AbstractRegistryObjectVal  implements TopLevelObjectVal {
    public AssociationModel model;

    public AssociationVal(AssociationModel model) { this.model = model; }

    public void validate(ErrorRecorder er, ValidationContext vc,
                         Set<String> knownIds) {
        if (vc.skipInternalStructure)
            return;

        validateTopAtts(er, vc);

        validateSlots(er, vc);

        validateClassifications(er, vc);

        validateExternalIdentifiers(er, vc, AssociationModel.externalIdentifierDescription, "ITI TF-3 4.1.3");

        new IdUniqueValidator(simHandle, model.id, knownIds).asSelf(this).run()

        verifyNotReferenceSelf(er);
    }

    void verifyNotReferenceSelf(ErrorRecorder er) {
        if (model.source.equals(model.id))
            er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + " sourceObject attribute references self", this, "???");
        if (model.target.equals(model.id))
            er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + " targetObject attribute references self", this, "???");
    }

    static List<String> assocs_with_documentation =
            Arrays.asList(
                    MetadataSupport.assoctype_rplc,
                    MetadataSupport.assoctype_xfrm,
                    MetadataSupport.assoctype_apnd,
                    MetadataSupport.assoctype_xfrm_rplc
            );

    public void validateClassifications(ErrorRecorder er, ValidationContext vc) {

        er.challenge("Classifications present are legal");

        List<ClassificationModel> c = model.getClassificationsByClassificationScheme(MetadataSupport.XDSAssociationDocumentation_uuid);
        if (c.size() == 0)
            ;
        else if (c.size() > 1)
            er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() +
                    ": may contain only a single documentation classification (classificationScheme=" +
                    MetadataSupport.XDSAssociationDocumentation_uuid + ")", this, "ITI TF-3 4.1.6.1");
        else {
            if (!assocs_with_documentation.contains(model.type))
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() +
                        ": documentation classification (classificationScheme=" +
                        MetadataSupport.XDSAssociationDocumentation_uuid +
                        ") may only be present on the following association types: " +
                        assocs_with_documentation, this, "ITI TF-3 4.1.6.1");
        }
        er.challenge("Required Classifications present");
        er.challenge("Classifications coded correctly");
    }

    List<String> relationship_assocs =
            Arrays.asList(
                    MetadataSupport.assoctype_rplc,
                    MetadataSupport.assoctype_apnd,
                    MetadataSupport.assoctype_xfrm,
                    MetadataSupport.assoctype_xfrm_rplc
            );

    public void validateTopAtts(ErrorRecorder er, ValidationContext vc) {
        new IdValidator(simHandle, vc, "entryUUID", model.id).asSelf(this).run()

        new IdValidator(simHandle, vc, "sourceObject", model.source).asSelf(this).run()
        new IdValidator(simHandle, vc, "targetObject", model.target).asSelf(this).run()

        boolean muReq = vc.isMU && vc.isRequest;
        boolean basicType = AssociationModel.assocTypes.contains(model.type);
        boolean muType = AssociationModel.assocTypesMU.contains(model.type);

        if (muReq) {
            if (basicType == false && muType == false)
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": associationType " + model.type + " unknown. Known assocationTypes are " + AssociationModel.assocTypes + " and " + AssociationModel.assocTypesMU, this, "ITI TF-3 Table 4.1-2.1");
        }

        else if (vc.isResponse) {
            if (!AssociationModel.assocTypes.contains(model.type) && !AssociationModel.assocTypesMU.contains(model.type))
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": associationType " + model.type + " unknown. Known assocationTypes are " + AssociationModel.assocTypes + " and " + AssociationModel.assocTypesMU, this, "ITI TF-3 Table 4.1-2.1");
        }

        else if (!AssociationModel.assocTypes.contains(model.type))
            er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": associationType " + model.type + " unknown. Known assocationTypes are " + AssociationModel.assocTypes, this, "ITI TF-3 Table 4.1-2.1");


    }

    @Override
    public void validateRequiredSlotsPresent(ErrorRecorder er,
                                             ValidationContext vc) {
//		Metadata m = getMetadata();
//		if (type.equals(MetadataSupport.assoctype_has_member) &&
//				m.isSubmissionSet(source) &&
//				m.isDocument(target)) {
//			if (getSlot(MetadataSupport.assoc_slot_submission_set_status) == null)
//				er.err(identifyingString() + ": SubmissionSet to DocumentEntry HasMember association must have a SubmissionSetStatus Slot", "ITI TF-3: 4.1.4.1");
//		} else {
//
//		}

    }

    @Override
    public void validateSlotsCodedCorrectly(ErrorRecorder er,
                                            ValidationContext vc) {
        SlotModel s = model.getSlot(MetadataSupport.assoc_slot_submission_set_status);
        if (s == null)
            return;
        if (s.getValues().size() == 1) {
            String value = s.getValues().get(0);
            if ("Original".equals(value) || "Reference".equals(value))
                ;
            else
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": SubmissionSetStatus Slot can only take value Original or Reference", this, "ITI TF-3: 4.1.4.1");
        } else {
            er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": SubmissionSetStatus Slot must have only single value", this, "ITI TF-3: 4.1.4.1");
        }
    }

    @Override
    public void validateSlotsLegal(ErrorRecorder er) {
        // work done by validateRequiredSlotsPresent
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
