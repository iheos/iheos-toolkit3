package gov.nist.hit.ds.dsSims.eb.metadataValidator.object
import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.dsSims.eb.metadata.MetadataSupport

@groovy.transform.TypeChecked
class AssociationValidator extends AbstractRegistryObjectValidator {
	ValidationContext vc;
    AssociationModel model

    AssociationValidator(Event event, AssociationModel model, ValidationContext vc) {
        super(event, model)
        this.model = model
        this.vc = vc
    }

     void validate(ErrorRecorder er, ValidationContext vc,
			Set<String> knownIds) {
		if (vc.skipInternalStructure)
			return;
		
		validateTopAtts(er, vc);

		validateSlots(er, vc);

		validateClassifications(er, vc);

		validateExternalIdentifiers(er, vc, model.externalIdentifierDescription, "ITI TF-3 4.1.3");

		verifyIdsUnique(er, knownIds);
		
		verifyNotReferenceSelf(er);
	}
	
	void verifyNotReferenceSelf(ErrorRecorder er) {
		if (model.source.equals(model.id))
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + " sourceObject attribute references self", this, "???");
		if (model.target.equals(model.id))
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + " targetObject attribute references self", this, "???");
	}


	 void validateClassifications(ErrorRecorder er, ValidationContext vc) {

		er.challenge("Classifications present are legal");

		List<ClassificationModel> c = model.getClassificationsByClassificationScheme(MetadataSupport.XDSAssociationDocumentation_uuid);
		if (c.size() == 0)
        {}
		else if (c.size() > 1)
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() +
					": may contain only a single documentation classification (classificationScheme=" + 
					MetadataSupport.XDSAssociationDocumentation_uuid + ")", this, "ITI TF-3 4.1.6.1");
		else {
			if (!model.assocs_with_documentation.contains(model.type))
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() +
						": documentation classification (classificationScheme=" + 
						MetadataSupport.XDSAssociationDocumentation_uuid + 
						") may only be present on the following association types: " +
                        model.assocs_with_documentation, this, "ITI TF-3 4.1.6.1");
		}
		er.challenge("Required Classifications present");
		er.challenge("Classifications coded correctly");
	}
	

	 void validateTopAtts(ErrorRecorder er, ValidationContext vc) {
		validateId(er, vc, "entryUUID", model.id, null);

		validateId(er, vc, "sourceObject", model.source, null);
		validateId(er, vc, "targetObject", model.target, null);
		
		
		boolean muReq = vc.isMU && vc.isRequest;
		boolean basicType = model.assocTypes.contains(model.type);
		boolean muType = model.assocTypesMU.contains(model.type);
				
		if (muReq) {
			if (basicType == false && muType == false)
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": associationType " + model.type + " unknown. Known assocationTypes are " + model.assocTypes + " and " + model.assocTypesMU, this, "ITI TF-3 Table 4.1-2.1");
		}
				
		else if (vc.isResponse) {
			if (!model.assocTypes.contains(model.type) && !model.assocTypesMU.contains(model.type))
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": associationType " + model.type + " unknown. Known assocationTypes are " + model.assocTypes + " and " + model.assocTypesMU, this, "ITI TF-3 Table 4.1-2.1");
		}
		
		else if (!model.assocTypes.contains(model.type))
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": associationType " + model.type + " unknown. Known assocationTypes are " + model.assocTypes, this, "ITI TF-3 Table 4.1-2.1");
	}

	 void validateRequiredSlotsPresent(ErrorRecorder er,
			ValidationContext vc) {
	}

	 void validateSlotsCodedCorrectly(ErrorRecorder er,
			ValidationContext vc) {
		SlotModel s = model.getSlot(MetadataSupport.assoc_slot_submission_set_status);
		if (s == null)
			return;
		if (s.getValues().size() == 1) {
			String value = s.getValues().get(0);
			if ("Original".equals(value) || "Reference".equals(value))
            {}
			else
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": SubmissionSetStatus Slot can only take value Original or Reference", this, "ITI TF-3: 4.1.4.1");
		} else {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": SubmissionSetStatus Slot must have only single value", this, "ITI TF-3: 4.1.4.1");
		}
	}

	 void validateSlotsLegal(ErrorRecorder er) {
		// work done by validateRequiredSlotsPresent
	}

}
