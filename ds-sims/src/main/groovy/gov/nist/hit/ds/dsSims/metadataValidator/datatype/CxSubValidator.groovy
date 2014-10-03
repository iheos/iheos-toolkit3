package gov.nist.hit.ds.dsSims.metadataValidator.datatype
import gov.nist.hit.ds.dsSims.metadataValidator.field.ValidatorCommon
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

public class CxSubValidator extends ValComponentBase  {
    String input

	public CxSubValidator(ValComponentBase base, String input) {
        super(base.event);
        this.input = input
	}

    @Override
    void run() {
        runValidationEngine()
    }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'CX001', msg = 'XCN - Either name or an identifier shall be present', ref = "ITI TF-3: Table 4.1-3 (XCN)")
    def nameCheck() {
        assertFalse((id().equals("") && ( lastName().equals("")  || firstName().equals(""))))
    }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='CX002', msg = 'Validate CX datatype', ref = "ITI TF-3: Table 4.1-3 (CX)")
    def checkCX() {
		String error = ValidatorCommon.validate_CX_datatype(input);
		if (error != null)
            fail(error, input)
	}

}
