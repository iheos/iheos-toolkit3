package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype
import gov.nist.hit.ds.dsSims.eb.utility.ValidatorCommon
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

public class XtnFormatValidator extends AbstractFormatValidator {
    String value

    String formatName() { return 'XTN' }

    XtnFormatValidator(SimHandle _simHandle, String context, String _value) {
        super(_simHandle, context);
        value = _value
    }

    @ErrorCode(code= XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roxtn010', msg='XTN format', ref='ITI TF-3: Table 4.1-3 (XTN)')
    def roxonxcn010() {
        infoFound("${context} is ${value}")
        parts = value.split('^')
    }

	String[] parts;

    @ErrorCode(code= XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roxtn020', msg='XTN.3 Type of telecommunications address must be "Internet"', ref='ITI TF-3: Table 4.1-3 (XTN)')
    def roxtn020() {
        assertEquals('Internet', xtn_3())
    }

    @ErrorCode(code= XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roxtn030', msg='XTN.4 Telecommunications address must be present', ref='ITI TF-3: Table 4.1-3 (XTN)')
    def roxtn030() {
        assertHasValue(xtn_4())
    }

    @ErrorCode(code= XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roxtn040', msg='XTN.4 Telecommunications address must be present', ref='ITI TF-3: Table 4.1-3 (XTN)')
    def roxtn040() {
        assertFalse(xtn_4().indexOf('@') == -1)
    }

	boolean isOid(String x) { return ValidatorCommon.is_oid(x, true); }
	
	boolean valued(String x) { return !x.equals(""); }
	
	String xtn_3()   { return get(2); }
	String xtn_4() { return get(3); }
	
	String get(int i) {
		if (parts.length <= i)
			return "";
		return parts[i];
	}
	
	String get(int i, int j) {
		String x = get(i);
		if (x.equals(""))
			return "";
		String[] xparts = x.split("&");
		if (xparts.length <= j)
			return "";
		return xparts[j];
	}

}
