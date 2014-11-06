package gov.nist.hit.ds.dsSims.metadataValidator.datatype

import gov.nist.hit.ds.dsSims.metadataValidator.field.ValidatorCommon
import gov.nist.hit.ds.dsSims.metadataValidator.object.SlotModel
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.DependsOn
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
/**
 * Created by bmajur on 9/3/14.
 */
class URIValidator extends ValComponentBase {
    SimHandle simHandle
    SlotModel slotModel

    URIValidator(SimHandle _simHandle, SlotModel _slotModel) {
       super(_simHandle.event)
        simHandle = _simHandle
        slotModel = _slotModel
    }

    // Guards
    def multivalue() { slotModel.size() > 1}
    def singleValue() { slotModel.size() == 1 }

    @Guard(methodNames = '[singleValue]')
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='URI001', msg='Single value format', ref="ITI TF-3: Table 4.1-5")
    def single() {
        infoFound('Format')
    }

    @Guard(methodNames = '[multiValue]')
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='URI002', msg='Multi value format', ref="ITI TF-3: Table 4.1-5")
    def multi() {
        infoFound('Format')
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='URI003', msg='http or file prefix', ref="ITI TF-3: Table 4.1-5")
    def prefix() {
        assertIn(['http://', '1|http://', 'file://', '1|file://'], slotModel.getValue(0))
    }

    @Guard(methodNames = '[multiValue]')
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='URI004', msg='Multi value format parts must contain separator', ref="ITI TF-3: Table 4.1-5")
    def multiValueSeparator() {
        slotModel.getValues().each {
            if (it.contains('|')) infoFound("Separator found in ${it}")
            else fail("Separator not found in ${it}")
        }
    }

    @Guard(methodNames = '[multiValue]')
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='URI005', msg='Multi value format parts must contain separator', ref="ITI TF-3: Table 4.1-5")
    def multiValueOrdered() {
        slotModel.getValues().each {
            if (it.contains('|')) infoFound("Separator found in ${it}")
            else fail("Separator not found in ${it}")
        }
    }

    @DependsOn(ids = '[URI005]')
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='URI006', msg='Multi value format prefix must be integer', ref="ITI TF-3: Table 4.1-5")
    def prefixesIntegers() {
        prefixes().each {
            if (ValidatorCommon.isInt(it)) infoFound("Found prefix ${it}")
            else fail("Prefix ${it} is not an int")
        }
    }

    def prefixes() {
        slotModel.getValues().collect {
            def parts = it.split('|')
            parts[0]
        }
    }
}
