package gov.nist.hit.ds.dsSims.metadataValidator.datatype

import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

public class XcnSubValidator extends ValComponentBase {
    String input
    String[] parts;
    String xresource = "ITI TF-3: Table 4.1-3 (XCN)";

	public XcnSubValidator(ValComponentBase base, String input) {
		super(base.event);
        this.input = input
	}

    @Override
    void runBefore() {
        parts = input.split("\\^");
    }

    // Guards
    def hasIdAA() { (!id().equals("") && !aa().equals("")) }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'XCN001', msg = 'XCN - Either displayName or an identifier shall be present', ref = "ITI TF-3: Table 4.1-3 (XCN)")
    def nameCheck() {
        assertFalse((id().equals("") && ( lastName().equals("")  || firstName().equals(""))))
    }

    @Guard(methodNames = ['hasIdAA'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'XCN002', msg = 'CX Format', ref = "ITI TF-3: Table 4.1-3 (XCN)")
    def cxCheck() {
        new CxSubValidator(this, id() + "^^^" + aa()).asSelf().run()
    }


//	public void validate(String input) {
//		parts = input.split("\\^");
//
//		if ((id().equals("") && ( lastName().equals("")  || firstName().equals(""))))
//			err(input, "Either displayName or an identifier shall be present", xresource);
//
//		if ((!id().equals("") && !aa().equals(""))) {
//			new CxSubValidator(this, id() + "^^^" + aa()).asSelf().run()
//		}
//	}

    // parts
    // 1 - ID
    // 2 - Last Name
    // 3 - First Name
    // 4 - Second and Further Given Names
    // 5 - Suffix
    // 6 - Prefix
    // 7 -
    // 8 -
    // 9 - Assigning Authority

	String id()              { String x = get(1); if (x == null) x = ""; return x; }
	String lastName()        { String x = get(2); if (x == null) x = ""; return x; }
	String firstName()       { String x = get(3); if (x == null) x = ""; return x; }
	String otherGivenNames() { String x = get(4); if (x == null) x = ""; return x; }
	String suffix()          { String x = get(5); if (x == null) x = ""; return x; }
	String prefix()          { String x = get(6); if (x == null) x = ""; return x; }
	String aa()              { String x = get(9); if (x == null) x = ""; return x; }

	
	// i is the index (1 is first)
	String get(int i) {
		i--;
		if (i < parts.length) return parts[i];
		return null;
	}
}
