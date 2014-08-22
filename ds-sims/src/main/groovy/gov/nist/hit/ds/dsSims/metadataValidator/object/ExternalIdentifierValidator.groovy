package gov.nist.hit.ds.dsSims.metadataValidator.object
import gov.nist.hit.ds.dsSims.client.ValidationContext
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.metadata.MetadataSupport
import org.apache.axiom.om.OMElement

@groovy.transform.TypeChecked
public class ExternalIdentifierValidator extends AbstractRegistryObjectValidator {
    ExternalIdentifierModel model

    ExternalIdentifierValidator(Event event, ExternalIdentifierModel model) {
        super(event, model)
        this.model = model
    }

	public void validateStructure(ErrorRecorder er, ValidationContext vc) {
		validateId(er, vc, "entryUUID", model.id, null);
		OMElement parentEle = (OMElement) model.ro.getParent();
		String parentEleId = ((parentEle == null) ? "null" :
			parentEle.getAttributeValue(MetadataSupport.id_qname));
		String registryObject = model.ro.getAttributeValue(MetadataSupport.registry_object_qname);
		
		if (parentEle != null && !parentEleId.equals(registryObject))
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": is a child of object " + parentEleId + " but the registryObject value is " +
					registryObject + ", they must match", this, "ITI TF-3: 4.1.12.5");
		
		if (model.value == null || model.value.equals(""))
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": value attribute missing or empty", this, "ebRIM 3.0 section 2.11.1");
		
		if (model.getName() == null || model.getName().equals(""))
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": display name (Name element) missing or empty", this, "ITI TF-3: 4.1.12.5");
	}

	public void validateRequiredSlotsPresent(ErrorRecorder er,
			ValidationContext vc) {
	}

	public void validateSlotsCodedCorrectly(ErrorRecorder er,
			ValidationContext vc) {
	}

	public void validateSlotsLegal(ErrorRecorder er) {
	}
}
