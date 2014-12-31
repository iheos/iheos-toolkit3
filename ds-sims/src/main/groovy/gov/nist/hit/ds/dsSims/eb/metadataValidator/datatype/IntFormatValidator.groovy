package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype
import gov.nist.hit.ds.dsSims.eb.utility.ValidatorCommon
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

public class IntFormatValidator extends AbstractFormatValidator {
    String value

    String formatName() { return 'Integer' }

    IntFormatValidator(SimHandle _simHandle, String context, String _value) {
        super(_simHandle, context);
        value = _value
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roint010', msg='Integer format', ref='')
    def roint010() {
        infoFound("${context} is ${value}")
        assertTrue(ValidatorCommon.isInt(value))
    }
}
