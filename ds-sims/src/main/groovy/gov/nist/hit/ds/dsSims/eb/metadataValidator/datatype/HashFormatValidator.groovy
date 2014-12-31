package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype

import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

public class HashFormatValidator extends AbstractFormatValidator {

    String formatName() { return 'SHA1 Hash' }

    public HashFormatValidator(SimHandle _simHandle, String context) {
        super(_simHandle, context);
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rohash010', msg='Hex format', ref='ITI TF-3: Table 4.1-3 (SHA1)')
    def rohash010() {
        infoFound("${context} is ${value}")
        assertTrue(UuidFormatValidator.isHexString(value))
    }
}
