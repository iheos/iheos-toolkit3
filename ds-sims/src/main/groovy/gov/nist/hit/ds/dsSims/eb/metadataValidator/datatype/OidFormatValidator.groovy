package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype
import gov.nist.hit.ds.dsSims.eb.utility.ValidatorCommon
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

public class OidFormatValidator extends AbstractFormatValidator {
    SimHandle simHandle
    String attName

    String formatName() { return 'OID' }

    public OidFormatValidator(SimHandle _simHandle, String _attName) {
		super(_simHandle.event)
        simHandle = _simHandle
        attName = _attName
	}

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rooid010', msg='Verify OID format', ref='')
	def rooid010() {
        infoFound("${attName} is ${value}")
        assertTrue(ValidatorCommon.is_oid(value, true))
	}

}
