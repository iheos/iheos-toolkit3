package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator

import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.OidFormatValidator
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

class UrnOidValidator extends ValComponentBase {
    SimHandle simHandle
    String attName
    String attValue

    UrnOidValidator(SimHandle _simHandle, String _attName, String _attValue) {
        super(_simHandle.event)
        simHandle = _simHandle
        attName = _attName
        attValue = _attValue
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'rouo010', msg = 'Must be URN format OID', ref = '')
    def rouo010() {
        infoFound("${attName} is ${attValue}")
        assertNotNull(attValue)
        infoFound("length <= 64")
        assertTrue(attValue.length() <= 64)
        String[] parts = model.home.split(":");
        assertStartsWith(attValue, 'urn:oid:')
        new OidFormatValidator(simHandle, attName, attValue.substring(8)).asSelf(this).run()
    }
}
