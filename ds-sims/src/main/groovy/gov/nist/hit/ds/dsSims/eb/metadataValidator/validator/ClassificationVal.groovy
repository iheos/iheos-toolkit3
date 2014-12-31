package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator;

import gov.nist.hit.ds.dsSims.eb.client.ValidationContext;
import gov.nist.hit.ds.dsSims.eb.metadata.MetadataSupport;
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.ClassificationModel;
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode;
import org.apache.axiom.om.OMElement;

/**
 * Created by bmajur on 12/23/14.
 */
public class ClassificationVal extends AbstractRegistryObjectVal {
    public ClassificationModel model;

    public ClassificationVal(ClassificationModel model) { this.model = model; }

    public void validateStructure(ErrorRecorder er, ValidationContext vc) {
        new IdValidator(simHandle, vc, "entryUUID", model.id).asSelf(this).run()
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
            er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": no name attribute", this, "ITI TF-3: 4.1.12.2");

        if (model.getCodeScheme().equals(""))
            er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": no codingScheme Slot", this, "ITI TF-3: 4.1.12.2");

    }

    @Override
    public void validateSlotsLegal(ErrorRecorder er) {

    }

    @Override
    public void validateRequiredSlotsPresent(ErrorRecorder er, ValidationContext vc) {

    }

    @Override
    public void validateSlotsCodedCorrectly(ErrorRecorder er, ValidationContext vc) {

    }
}
