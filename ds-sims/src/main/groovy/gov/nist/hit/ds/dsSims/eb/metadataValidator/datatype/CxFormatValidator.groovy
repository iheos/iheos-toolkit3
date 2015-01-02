package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype

import gov.nist.hit.ds.dsSims.eb.utility.ValidatorCommon
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

public class CxFormatValidator extends AbstractFormatValidator {
    String formatName() { return 'CX' }

    public CxFormatValidator(SimHandle _simHandle, String context) {
		super(_simHandle, context);
	}

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rocx010', msg='CX format', ref='')
    def rocx010() {
        infoFound("${context} is ${value}")
        def error = ValidatorCommon.validate_CX_datatype(value)
        assertFalse(error)
    }
}
