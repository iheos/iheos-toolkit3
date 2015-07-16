package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator

import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.ClassificationModel
import gov.nist.hit.ds.ebMetadata.MetadataSupport
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.toolkit.valsupport.client.ValidationContext
import org.apache.axiom.om.OMElement

class ClassificationStructureValidator extends ValComponentBase {
    SimHandle simHandle
    ValidationContext vc
    ClassificationModel model

    ClassificationStructureValidator(SimHandle _simHandle, ValidationContext _vc, ClassificationModel _model) {
        super(_simHandle.event)
        simHandle = _simHandle
        model = _model
        vc = _vc
    }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'rocls010', msg = 'Validate Classification Structure ID', ref = '')
    def rocls010() {
        infoFound("Validate structure of ${model.identifyingString()}")
        new IdValidator(simHandle, vc, "entryUUID", model.id).asSelf(this).run()
    }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'rocls020', msg = 'Validate linkage to parent object', ref = 'ITI TF-3: 4.1.12.2')
    def rocls020() {
        infoFound("Validate structure of ${model.identifyingString()}")
        OMElement parentEle = (OMElement) model.ro.getParent();
        String parentEleId = ((parentEle == null) ? "null" :
                parentEle.getAttributeValue(MetadataSupport.id_qname));
        String classifiedObjectId = model.ro.getAttributeValue(MetadataSupport.classified_object_qname);

        if (parentEle != null && !parentEleId.equals(classifiedObjectId))
            fail("Child of object " + parentEleId + " but the classifiedObject value is " +
                    classifiedObjectId + ", they must match");
    }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'rocls030', msg = 'Has value for the classificationScheme attribute', ref = 'ebRIM 3.0 section 4.3.1')
    def rocls030() {
        infoFound("Validate structure of ${model.identifyingString()}")
        assertFalse(model.getClassificationScheme() == null || model.getClassificationScheme().equals(""))
    }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'rocls040', msg = 'classificationScheme attribute value is not have urn:uuid: prefix', ref = 'ebRIM 3.0 section 4.3.1')
    def rocls040() {
        infoFound("Validate structure of ${model.identifyingString()}")
        assertFalse(!model.getClassificationScheme().startsWith("urn:uuid:"))
    }


    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'rocls050', msg = 'nodeRepresentation attribute is required to not be empty', ref = 'ebRIM 3.0 section 4.3.1')
    def rocls050() {
        infoFound("Validate structure of ${model.identifyingString()}")
        assertFalse(model.getCodeValue().equals(""))
    }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'rocls060', msg = 'name attribute is required', ref = 'ITI TF-3: 4.1.12.2')
    def rocls060() {
        infoFound("Validate structure of ${model.identifyingString()}")
        assertFalse(model.getCodeDisplayName().equals(""))
    }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'rocls070', msg = 'codingScheme Slot is required', ref = 'ITI TF-3: 4.1.12.2')
    def rocls070() {
        assertFalse(model.getCodeScheme().equals(""))
    }
}
