package gov.nist.hit.ds.dsSims.metadataValidator.datatype
import gov.nist.hit.ds.dsSims.metadataValidator.field.ValidatorCommon
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

public class DtmSubValidator  extends ValComponentBase {
    ValComponentBase base
    String input

    public DtmSubValidator(ValComponentBase base, String input) {
        super(base.event);
        this.input = input
    }

//    public void validate(String input) {
//        int size = input.length();
//        if (!(size == 4 || size == 6 || size == 8 || size == 10 || size == 12 || size == 14))
//            er.err(XdsErrorCode.Code.XDSRegistryMetadataError, context + ": " + input + " is not in HL7 V2 DateTime format: it has an invalid number of characters", this, getResource("ITI TF-3: Table 4.1-3"));
//        if (!ValidatorCommon.isInt(input))
//            er.err(XdsErrorCode.Code.XDSRegistryMetadataError, context + ": " + input + " is not in HL7 V2 DateTime format: all characters must be digits", this, getResource("ITI TF-3: Table 4.1-3"));
//    }

    @Override
    void run() {
        runValidationEngine()
    }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DTM001', msg = 'HL7 V2 DateTime format - length', ref = "ITI TF-3: Table 4.1-6")
    def lengthCheck() {
        int size = input.length();
        assertTrue(size == 4 || size == 6 || size == 8 || size == 10 || size == 12 || size == 14)
    }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'DTM002', msg = 'HL7 V2 DateTime format - all digits', ref = "ITI TF-3: Table 4.1-6")
    def formatCheck() {
        assertTrue(ValidatorCommon.isInt(input))
    }
}