package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype

import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
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
public class Rfc3066FormatValidator extends AbstractFormatValidator {

    String formatName() { return 'RFC 3066' }

    Rfc3066FormatValidator(SimHandle _simHandle, String context) {
        super(_simHandle, context);
    }

    String[] parts

    boolean hasParts() { parts.length > 0 }
    boolean has2Parts() { parts.length > 1 }

    @Validation(id='ro3066005', msg='RFC 3066', ref='RFC 3066')
    def ro3066005() {
        infoFound("${context} is ${value}")
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='ro3066010', msg='Integer format', ref='')
    def ro3066010() {
        assertHasValue(value)
        if (!value) return
        parts = input.split("-");
    }

    @Guard(methodNames=['hasParts'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='ro3066020', msg='Primary-subtag limited to 8 characters', ref='')
    def ro3066020() {
        assertTrue(parts[0].length() <= 8)
    }

    @Guard(methodNames=['hasParts'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='ro3066030', msg='Primary-subtag is not all alpha characters', ref='')
    def ro3066030() {
        assertFalse(allAlphas(parts[0]))
    }

    @Guard(methodNames=['has2Parts'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='ro3066040', msg='Subtag limited to 8 characters', ref='')
    def ro3066040() {
        assertFalse(parts[1].length() > 8)
    }

    @Guard(methodNames=['has2Parts'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='ro3066050', msg='Subtag is not all alpha or digit characters', ref='')
    def ro3066050() {
        assertFalse(allAlphasAndDigits(parts[1]))
    }

	static String digits = "1234567890";
	static String alphas = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	boolean allAlphas(String val) {
		for (int i=0; i<val.length(); i++) {
			char c = val.charAt(i);
			if (alphas.indexOf(c) == -1)
				return false;
		}
		return true;
	}
	boolean allAlphasAndDigits(String val) {
		for (int i=0; i<val.length(); i++) {
			char c = val.charAt(i);
			if (digits.indexOf(c) == -1 && alphas.indexOf(c) == -1)
				return false;
		}
		return true;
	}
}
