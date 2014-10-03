package gov.nist.hit.ds.dsSims.metadataValidator.datatype
import gov.nist.hit.ds.dsSims.metadataValidator.field.ValidatorCommon
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

public class XonSubValidator extends ValComponentBase {
    String input
    String[] parts;
    String msg

	public XonSubValidator(ValComponentBase base, String input, String msg) {
        super(base.event);
        this.input = input
        this.msg = msg
	}

    @Override
    void run() {
        parts = input.split("\\^");

        runValidationEngine()
    }

    // Guards
    def x10Valued() { valued(x10()) }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'XON001', msg = 'XON.1 Organization Name required', ref = "ITI TF-3: Table 4.1-3 (XON)")
    def nameCheck() {
        assertFalse(x1().equals("")).msg(msg)
    }

    @Guard(methodNames = ['x10Valued'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'XON002', msg = 'XON 6.2 Assigning Authority Universal Id is required if XON.10 is valued and not an OID', ref = "ITI TF-3: Table 4.1-3 (XON)")
    def _62Check() {
        if (!isOid(x10())) {
            if (x6_2().equals(""))
                fail("XON 6.2 Assigning Authority Universal Id is required if XON.10 is valued and not an OID").msg(msg)
        }
    }

    @Guard(methodNames = ['x10Valued'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'XON003', msg = 'XON 6.3 Assigning Authority Universal Id Type is required since XON.10 is valued and not an OID - it must have value ISO', ref = "ITI TF-3: Table 4.1-3 (XON)")
    def _63Check() {
        if (!isOid(x10())) {
            if (!x6_3().equals("ISO"))
                fail("XON 6.3 Assigning Authority Universal Id Type is required since XON.10 is valued and not an OID - it must have value ISO", x6_3()).msg(msg)
        }
    }

//    String xresource = "ITI TF-3: Table 4.1-3 (XON)";
//
//	public void validate(String input) {
//		parts = input.split("\\^");
//		if (x1().equals(""))
//			err(input, "XON.1 Organization Name required", xresource);
//		String x10 = x10();
//		if (valued(x10) && !isOid(x10)) {
//			if (x6_2().equals(""))
//				err(input, "XON 6.2 Assigning Authority Universal Id is required if XON.10 is valued and not an OID", xresource);
//		if (valued(x10) && !isOid(x10))
//			if (!x6_3().equals("ISO"))
//				err(input, "XON 6.3 Assigning Authority Universal Id Type is required since XON.10 is valued and not an OID - it must have value ISO - found [" + x6_3() + "] instead", xresource);
//		}
//	}
	
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
