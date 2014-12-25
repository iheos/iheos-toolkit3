package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator;

import gov.nist.hit.ds.dsSims.eb.client.ValidationContext;
import gov.nist.hit.ds.dsSims.eb.metadata.MetadataSupport;
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.AbstractRegistryObjectModel;
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.AuthorModel;
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.xdsException.XdsInternalException;
import org.apache.axiom.om.OMElement;

/**
 * Created by bmajur on 12/23/14.
 */
public class AuthorVal extends AbstractRegistryObjectVal {
    AuthorModel model;

    public AuthorVal(AuthorModel model) { this.model = model; }

    public void validateStructure(ErrorRecorder er, ValidationContext vc)  {
        validateId(er, vc, "entryUUID", model.id, null);
        OMElement parentEle = (OMElement) model.ro.getParent();
        String parentEleId =  ((parentEle == null) ? "null" :
                parentEle.getAttributeValue(MetadataSupport.id_qname));
        String classifiedObjectId = model.ro.getAttributeValue(MetadataSupport.classified_object_qname);

        if (parentEle != null && !parentEleId.equals(classifiedObjectId))
            er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": is a child of object " + parentEleId + " but the classifiedObject value is " +
                    classifiedObjectId + ", they must match", this, "ITI TF-3: 4.1.12.2");

        try {
            if (model.getClassificationScheme() == null || model.getClassificationScheme().equals(""))
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": does not have a value for the classificationScheme attribute", this, "ebRIM 3.0 section 4.3.1");
            else if (!model.getClassificationScheme().startsWith("urn:uuid:"))
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": classificationScheme attribute value is not have urn:uuid: prefix", this, "ITI TF-3: 4.3.1");
        } catch (XdsInternalException e) {
            er.err(XdsErrorCode.Code.XDSRegistryMetadataError, e);
        }
    }

    @Override
    public void validateSlotsLegal(AbstractRegistryObjectModel model, ErrorRecorder er) {

    }

    @Override
    public void validateRequiredSlotsPresent(AbstractRegistryObjectModel model, ErrorRecorder er, ValidationContext
            vc) {

    }

    @Override
    public void validateSlotsCodedCorrectly(AbstractRegistryObjectModel model, ErrorRecorder er, ValidationContext vc) {

    }


}
