package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype

import gov.nist.hit.ds.dsSims.eb.metadataValidator.field.ValidatorCommon
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

public class IntValidator extends ValComponentBase {
    SimHandle simHandle
    String input

	public IntValidator(SimHandle _simHandle, String _input) {
		super(_simHandle.event)
        simHandle = _simHandle
        input = _input
	}

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='Int001', msg='Int format required', ref="ITI TF-3: Table 4.1-5")
	def validate() {
		if (!ValidatorCommon.isInt(input)) { fail('Not an Int') }
	}

}
