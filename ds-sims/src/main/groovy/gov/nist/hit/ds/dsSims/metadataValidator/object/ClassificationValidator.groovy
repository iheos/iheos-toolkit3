package gov.nist.hit.ds.dsSims.metadataValidator.object
import gov.nist.hit.ds.dsSims.client.ValidationContext
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.metadata.MetadataSupport
import org.apache.axiom.om.OMElement

@groovy.transform.TypeChecked
public class ClassificationValidator extends AbstractRegistryObjectValidator {
    ClassificationModel model

    ClassificationValidator(Event event, ClassificationModel model) {
        super(event, model)
        this.model = model
    }

//	public ClassificationValidator(String id, String classificationScheme, String code, String codingScheme, String displayName) {
//		super(id);
//		classification_scheme = classificationScheme;
//		code_value = code;
//		addSlot("codingScheme", codingScheme);
//		//coding_scheme = codingScheme;
//		displayName = displayName;
//	}



	void validateStructure(ErrorRecorder er, ValidationContext vc) {
		validateId(er, vc, "entryUUID", model.id, "ITI TF-3: 4.1.12.2");
		OMElement parentEle = (OMElement) model.ro.getParent();
		String parentEleId =  ((parentEle == null) ? "null" :
			parentEle.getAttributeValue(MetadataSupport.id_qname));
		String classifiedObjectId = model.ro.getAttributeValue(MetadataSupport.classified_object_qname);

		if (parentEle != null && !parentEleId.equals(classifiedObjectId))
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": is a child of object " + parentEleId + " but the classifiedObject value is " +
					classifiedObjectId + ", they must match", this, "ITI TF-3: 4.1.12.2");

		if (model.getClassificationScheme() == null || model.getClassificationScheme().equals(""))
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": does not have a value for the classificationScheme attribute", this, "ebRIM 3.0 section 4.3.1");
		else if (!model.getClassificationScheme().startsWith("urn:uuid:"))
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": classificationScheme attribute value is not have urn:uuid: prefix", this, "ITI TF-3: 4.3.1");

		if (model.getCodeValue().equals(""))
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": nodeRepresentation attribute is missing or empty", this, "ebRIM 3.0 section 4.3.1");

		if (model.getCodeDisplayName().equals(""))
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": no displayName attribute", this, "ITI TF-3: 4.1.12.2");

		if (model.getCodeScheme().equals(""))
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": no codingScheme Slot", this, "ITI TF-3: 4.1.12.2");

	}

	void validateRequiredSlotsPresent(ErrorRecorder er,
			ValidationContext vc) {		
	}

	void validateSlotsCodedCorrectly(ErrorRecorder er,
			ValidationContext vc) {
	}

	void validateSlotsLegal(ErrorRecorder er) {
	}



}
