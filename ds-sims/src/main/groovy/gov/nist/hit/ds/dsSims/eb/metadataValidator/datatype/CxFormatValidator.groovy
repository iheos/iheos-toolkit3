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
    @Validation(id='rocx010', msg='Patient ID', ref='ITI TF-3: Table 4.2.3.1.7-2')
    def rocx010() {
//        infoFound(value)
        found(value)
        expected('CX format')
        def error = ValidatorCommon.validate_CX_datatype(value)
        assertFalse(error)
    }
}
