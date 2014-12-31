package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype

import gov.nist.hit.ds.dsSims.eb.utility.ValidatorCommon
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

public class DtmFormatValidator extends AbstractFormatValidator {

    String formatName() { return 'DTM' }

    public DtmFormatValidator(SimHandle _simHandle, String context) {
		super(_simHandle, context);
	}

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rodtm010', msg='HL7 V2 DateTime format (CX) format', ref='ITI TF-3: Table 4.1-3')
    def rodtm010() {
        infoFound("${context} is ${value}")
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rodtm020', msg='Valid number of characters', ref='ITI TF-3: Table 4.1-3')
    def rodtm020() {
        int size = value.length();
        infoFound("${size} characters")
        assertTrue(size == 4 || size == 6 || size == 8 || size == 10 || size == 12 || size == 14)
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rodtm030', msg='All characters must be digits', ref='ITI TF-3: Table 4.1-3')
    def rodtm030() {
        assertTrue(ValidatorCommon.isInt(value))
    }

}
