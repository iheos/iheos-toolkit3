package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype

import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

public class HashValidator extends ValComponentBase {
    SimHandle simHandle
    String input

	public HashValidator(SimHandle _simHandle, String _input) {
		super(_simHandle.event)
        simHandle = _simHandle
        input = _input
	}

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='Hex001', msg='Hex format required', ref="ITI TF-3: Table 4.1-5")
	public void validate() {
		if (!UuidFormat.isHexString(input)) { fail ('Bad Format') }
	}

}
