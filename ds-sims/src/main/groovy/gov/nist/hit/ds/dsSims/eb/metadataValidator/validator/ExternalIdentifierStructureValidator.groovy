package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator

import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.ExternalIdentifierModel
import gov.nist.hit.ds.ebMetadata.MetadataSupport
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import org.apache.axiom.om.OMElement

class ExternalIdentifierStructureValidator extends ValComponentBase {
    SimHandle simHandle
    String registryObject
    OMElement parentEle
    String parentEleId
    ExternalIdentifierModel ei
    ValidationContext vc

    ExternalIdentifierStructureValidator(SimHandle _simHandle, ValidationContext _vc, ExternalIdentifierModel _ei) {
        super(_simHandle.event)
        simHandle = _simHandle
        ei = _ei
        vc = _vc

        parentEle = (OMElement) ei.ro.getParent();
        parentEleId = ((parentEle == null) ? "null" :
                parentEle.getAttributeValue(MetadataSupport.id_qname));
        registryObject = ei.ro.getAttributeValue(MetadataSupport.registry_object_qname);

    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'roeis010', msg = 'Validating ExternalIdentifier', ref = '')
    def roeis010() {
        infoFound(ei.identifyingString())
        new IdValidator(simHandle, vc, "entryUUID", ei.id).asSelf(this).run()
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'roeis020', msg = 'ExternalIdentifier.registryObject must point to parent', ref = 'ITI TF-3: 4.1.12.5')
    def roeis020() {
        assertEquals(parentEleId, registryObject)
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'roeis030', msg = 'ExternalIdentifier value must not be empty', ref = 'ebRIM 3.0 section 2.11.1')
    def roeis030() {
        assertHasValue(ei.value)
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'roeis040', msg = 'ExternalIdentifier display name (Name element) must not be empty', ref = 'ITI TF-3: 4.1.12.5')
    def roeis040() {
        assertHasValue(ei.getName())
    }
}