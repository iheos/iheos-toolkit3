package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator
import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.AuthorModel
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import org.apache.axiom.om.OMElement
/**
 * Created by bmajur on 12/23/14.
 */
public class AuthorStructureValidator extends ValComponentBase {
    SimHandle simHandle
    ValidationContext vc
    AuthorModel model;

    public AuthorStructureValidator(SimHandle _simHandle, ValidationContext _vc, AuthorModel model) {
        super(_simHandle.event)
        simHandle = _simHandle
        vc = _vc
        this.model = model;
    }

    // TODO: does not check for all the possible structure

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'roas010', msg = 'Validate Author Structure ID', ref = '')
    def roas010() {
        infoFound("Validate structure of ${model.identifyingString()}")
        new IdValidator(simHandle, vc, "entryUUID", model.id).asSelf(this).run()
    }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'roas020', msg = 'Validate linkage to parent object', ref = 'ITI TF-3: 4.1.12.2')
    def roas020() {
        infoFound("Validate structure of ${model.identifyingString()}")
        OMElement parentEle = (OMElement) model.ro.getParent();
        String parentEleId =  ((parentEle == null) ? "null" :
                parentEle.getAttributeValue(MetadataSupport.id_qname));
        String classifiedObjectId = model.ro.getAttributeValue(MetadataSupport.classified_object_qname);

        if (parentEle != null && !parentEleId.equals(classifiedObjectId))
            fail(model.identifyingString() + ": is a child of object " + parentEleId + " but the classifiedObject value is " +
                    classifiedObjectId + ", they must match");
    }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'roas030', msg = 'Must have a value for the classificationScheme attribute', ref = 'ebRIM 3.0 section 4.3.1')
    def roas030() {
        infoFound("Validate structure of ${model.identifyingString()}")
        assertHasValue(model.getClassificationScheme())
        if (!model.getClassificationScheme()?.startsWith("urn:uuid:"))
            fail(model.identifyingString() + ": classificationScheme attribute value is not have urn:uuid: prefix");
    }
}
