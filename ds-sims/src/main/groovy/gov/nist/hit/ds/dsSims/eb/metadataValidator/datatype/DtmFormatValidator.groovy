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
    @Validation(id='rodtm010', msg='HL7 V2 DateTime format (CX) format', ref='ITI TF-3: Table 4.2.3.1.7-2')
    def rodtm010() {
        infoFound("${context} is ${value}")
        parse()
    }

    def year
    def month
    def day
    def hour
    def minute
    def second

    def parse() {
        try {
            year = value.substring(0, 4)
            month = value.substring(4, 6)
            day = value.substring(6, 8)
            hour = value.substring(8, 10)
            minute = value.substring(10, 12)
            second = value.substring(12, 14)
        } catch (Exception e) {}
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rodtm020', msg='Valid number of characters', ref='ITI TF-3: Table 4.2.3.1.7-2')
    def rodtm020() {
        int size = value.length();
        infoFound("${size} characters")
        assertTrue(size == 4 || size == 6 || size == 8 || size == 10 || size == 12 || size == 14)
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rodtm030', msg='All characters must be digits', ref='ITI TF-3: Table 4.2.3.1.7-2')
    def rodtm030() {
        assertTrue(ValidatorCommon.isInt(value))
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rodtm040', msg='Year', ref='ITI TF-3: Table 4.2.3.1.7-2')
    def rodtm040() {
        assertTrue(year)
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rodtm050', msg='Month', ref='ITI TF-3: Table 4.2.3.1.7-2')
    def rodtm050() {
        if (!month) return
        def monthi = month.toInteger()
        assertTrue(monthi > 0 && monthi <= 12)
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rodtm060', msg='Day', ref='ITI TF-3: Table 4.2.3.1.7-2')
    def rodtm060() {
        if (!day) return
        def dayi = day.toInteger()
        assertTrue(dayi > 0 && dayi <= 31)
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rodtm070', msg='Hour', ref='ITI TF-3: Table 4.2.3.1.7-2')
    def rodtm070() {
        if (!hour) return
        def houri = hour.toInteger()
        assertTrue(houri > 0 && houri <= 24)
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rodtm080', msg='Minute', ref='ITI TF-3: Table 4.2.3.1.7-2')
    def rodtm080() {
        if (!minute) return
        def minutei = minute.toInteger()
        assertTrue(minutei > 0 && minutei <= 60)
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rodtm090', msg='Second', ref='ITI TF-3: Table 4.2.3.1.7-2')
    def rodtm090() {
        if (!second) return
        def secondi = second.toInteger()
        assertTrue(secondi > 0 && secondi <= 60)
    }
}
