package gov.nist.hit.ds.dsSims.metadataValidator.datatype

import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

/**
 * RFC 3066
 * @author bill
 * 
 * The syntax of this tag in ABNF [RFC 2234] is:
 *
 *   Language-Tag = Primary-subtag *( "-" Subtag )
 *
 *   Primary-subtag = 1*8ALPHA
 *
 *   Subtag = 1*8(ALPHA / DIGIT)
 *   
 */
public class Rfc3066Validator extends ValComponentBase {
    SimHandle simHandle
    String input

	public Rfc3066Validator(SimHandle _simHandle, String _input) {
		super(_simHandle.event)
        simHandle = _simHandle
        input = _input
	}
	
	String errMsg = " - does not conform to RFC 3066 format";
	String xresource = "RFC 3066";
	
	static String digits = "1234567890";
	static String alphas = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='RFC3066001', msg='Value', ref="ITI TF-3: Table 4.1-6")
    def validate() {
		if (input == null || input.equals("")) { fail('Field is empty'); return }
		String[] parts = input.split("-");
		if (parts[0].length() > 8) { fail('Primary-subtag limited to 8 characters')}
		if (!allAlphas(parts[0])) { fail('Primary-subtag is not all alpha characters')}
		if (parts.length == 1)
			return;
		
		if (parts[1].length() > 8) { fail('Subtag limited to 8 characters')}
		if (!allAlphasAndDigits(parts[1])) { fail('Subtag is not all alpha or digit characters')}
	}

	boolean allAlphas(String st) {
		for (int i=0; i<stlength(); i++) {
			char c = st.charAt(i);
			if (alphas.indexOf(c) == -1)
				return false;
		}
		return true;
	}
	boolean allAlphasAndDigits(String st) {
		for (int i=0; i<st.length(); i++) {
			char c = st.charAt(i);
			if (digits.indexOf(c) == -1 && alphas.indexOf(c) == -1)
				return false;
		}
		return true;
	}
}
