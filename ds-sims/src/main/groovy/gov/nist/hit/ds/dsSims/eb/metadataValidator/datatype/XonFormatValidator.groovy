package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype
import gov.nist.hit.ds.dsSims.eb.utility.ValidatorCommon
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

public class XonFormatValidator extends AbstractFormatValidator {
    String value

    String formatName() { return 'XON' }

    XonFormatValidator(SimHandle _simHandle, String context, String _value) {
        super(_simHandle, context);
        value = _value
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roxon010', msg='XON format', ref='ITI TF-3: Table 4.1-3 (XON)')
    def roxon010() {
        infoFound("${context} is ${value}")
        parts = input.split('^');
    }

	String[] parts;

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roxon020', msg='XON.1 Organization Name required', ref='ITI TF-3: Table 4.1-3 (XON)')
    def roxon020() {
        infoFound("XON.1 is ${x1()}")
        assertHasValue(x1())
    }

    String x10

    @Validation(id='roxon030', msg='XON.10', ref='ITI TF-3: Table 4.1-3 (XON)')
    def roxon030() {
        x10 = x10();
        infoFound("XON.10 is ${x10}")
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roxon040', msg='XON 6.2 Assigning Authority Universal Id is required if XON.10 is valued and not an OID', ref='ITI TF-3: Table 4.1-3 (XON)')
    def roxon040() {
        if (valued(x10) && !isOid(x10)) {
            assertHasValue(x6_2())
        }
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roxon050', msg='XON 6.3 Assigning Authority Universal Id Type is required since XON.10 is valued and not an OID - it must have value ISO', ref='ITI TF-3: Table 4.1-3 (XON)')
    def roxon050() {
        if (valued(x10) && !isOid(x10)) {
            assertEquals('ISO', x6_3())
        }
    }

	boolean isOid(String x) { return ValidatorCommon.is_oid(x, true); }
	
	boolean valued(String x) { return !x.equals(""); }
	
	String x1()   { return get(0); }
	String x6_2() { return get(6,2); }
	String x6_3() { return get(6,3); }
	String x10()  { return get(10); }
	
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
