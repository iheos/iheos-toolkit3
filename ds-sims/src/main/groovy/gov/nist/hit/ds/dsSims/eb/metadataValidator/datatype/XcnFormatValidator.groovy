package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype

import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

public class XcnFormatValidator extends AbstractFormatValidator {
    String value

    String formatName() { return 'XCN' }

    XcnFormatValidator(SimHandle _simHandle, String context, String _value) {
        super(_simHandle, context);
        value = _value
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roxcn010', msg='XCN format', ref='ITI TF-3: Table 4.1-3 (XCN)')
    def roxcn010() {
        infoFound("${context} is ${value}")
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roxcn020', msg='Either name or an identifier shall be present', ref='ITI TF-3: Table 4.1-3 (XCN)')
    def roxcn020() {
        parts = input.split('^');
        assertTrue(!id().equals("") || ( !lastName().equals("")  && !firstName().equals("")))
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roxcn030', msg='Validate id and Assigning Authority if present', ref='ITI TF-3: Table 4.1-3 (XCN)')
    def roxcn030() {
        if ((!id().equals("") && !aa().equals(""))) {
            new CxFormatValidator(simHandle, "", id() + '^^^' + aa()).asSelf(this).run()
        }
    }

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
	
	String[] parts;

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
