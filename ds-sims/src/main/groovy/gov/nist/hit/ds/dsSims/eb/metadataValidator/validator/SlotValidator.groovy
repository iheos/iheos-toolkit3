package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator

import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.AbstractFormatValidator
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.SlotModel
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

class SlotValidator extends ValComponentBase {
    SimHandle simHandle
    SlotModel model
    boolean multivalue
    AbstractFormatValidator contentValidator

    SlotValidator(SimHandle _simHandle, SlotModel _model, boolean _multivalue, AbstractFormatValidator _contentValidator) {
        super(_simHandle.event)
        simHandle = _simHandle
        model = _model
        multivalue = _multivalue
        contentValidator = _contentValidator
    }

    @Validation(id = 'roslot010', msg = 'Validate Slot', ref = '')
    def roslot010() {
        infoFound("Slot ${model.name} containing value type ${contentValidator.formatName()}")
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'roslot020', msg = 'Validate number of values in Slot', ref = '')
    def roslot020() {
        assertFalse(!multivalue && model.values.size() > 1)
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'roslot030', msg = 'Validate value datatype', ref = '')
    def roslot030() {
        model.values.each {
            contentValidator.validate(it)
        }
    }
}