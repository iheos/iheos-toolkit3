package gov.nist.hit.ds.dsSims.metadataValidator.datatype
import gov.nist.hit.ds.dsSims.metadataValidator.field.ValidatorCommon
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

public class OidValidator extends ValComponentBase {
    SimHandle simHandle
    String input

	public OidValidator(SimHandle _simHandle, String _input) {
		super(_simHandle.event)
        simHandle = _simHandle
        input = _input
	}

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='OID001', msg='OID format required', ref="ITI TF-3: Table 4.1-5")
    public void validate() {
        assertTrue(ValidatorCommon.is_oid(input, true))
	}

}
