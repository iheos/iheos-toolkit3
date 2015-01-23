package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator

import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.AbstractRegistryObjectModel
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.hit.ds.utilities.xml.XmlUtil
import org.apache.axiom.om.OMElement

class TopAttsValidator extends ValComponentBase {
    SimHandle simHandle
    AbstractRegistryObjectModel model
    ValidationContext vc
    List<String> statusValues

    TopAttsValidator(SimHandle _simHandle, AbstractRegistryObjectModel _model,  ValidationContext _vc, List<String> _statusValues) {
        super(_simHandle.event)
        simHandle = _simHandle
        model = _model
        vc = _vc
        statusValues = _statusValues
    }

    @Validation(id = 'rota010', msg = 'Validate id attribute', ref = '')
    def rota010() {
        infoFound(model.identifyingString())
        new IdValidator(simHandle, vc, "entryUUID", model.id).asSelf(this).run()
    }

    boolean sqResponse() { vc.isSQ && vc.isResponse }
    boolean xc() { vc.isXC }

    @Guard(methodNames=['sqResponse'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'rota020', msg = 'Validate status', ref = '')
    def rota020() {
        if (model.status == null)
            fail("${model.identifyingString()} : availabilityStatus attribute (status attribute in XML) must be present")
        else {
            assertIn(statusValues, model.status)
        }

    }

    @Guard(methodNames=['sqResponse'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'rota030', msg = 'Validate lid attribute', ref = '')
    def rota030() {
        new IdValidator(simHandle, vc, "lid", model.lid).asSelf(this).run()
    }

    @Guard(methodNames=['sqResponse'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'rota040', msg = 'Validate presence of versionInfo attribute', ref = 'ebRIM Section 2.5.1')
    def rota040() {
        List<OMElement> versionInfos = XmlUtil.childrenWithLocalName(model.ro, "VersionInfo");
        assertFalse(versionInfos.size() == 0)
    }

    @Guard(methodNames=['sqResponse', 'xc'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'rota050', msg = 'homeCommunityId required on XC Query response', ref = 'ebRIM Section 2.5.1')
    def rota050() {
        new UrnOidValidator(simHandle, 'homeCommunityId', model.home).asSelf(this).run()
    }
}
