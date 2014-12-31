package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

public class UuidFormatValidator extends AbstractFormatValidator {
    SimHandle simHandle

    String formatName() { return 'UUID' }

    public UuidFormatValidator(SimHandle _simHandle) {
        super(_simHandle.event)
        simHandle = _simHandle
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rouuid010', msg='Validate UUID format', ref='ITI TF-3: Table 4.1-3 and section 4.1.12.3')
	def rouuid010() {
        infoFound(value)
        assertTrue(isUuid(value))
	}

	// example: urn:uuid:488705e6-91e6-47f8-b567-8c06f8472e74
	boolean isUuid(String value)  {
		if (value == null)
			return false;
		if (!value.startsWith("urn:uuid:"))
			return false;
		
		if (value.length() != 45) 
			return false;
		
		String rest;
		rest = value.substring(9);
		
		if (!isLCHexString(rest.substring(0, 8)))
			return false;
		rest = rest.substring(8);
		
		if (!(rest.charAt(0) == '-'))
			return false;
		rest = rest.substring(1);

		if (!isLCHexString(rest.substring(0, 4)))
			return false;
		rest = rest.substring(4);
		
		if (!(rest.charAt(0) == '-'))
			return false;
		
		rest = rest.substring(1);
		if (!isLCHexString(rest.substring(0, 4)))
			return false;
		rest = rest.substring(4);
				
		if (!(rest.charAt(0) == '-'))
			return false;
		
		rest = rest.substring(1);
		if (!isLCHexString(rest.substring(0, 4)))
			return false;
		rest = rest.substring(4);
				
		if (!(rest.charAt(0) == '-'))
			return false;
		
		rest = rest.substring(1);
		if (!isLCHexString(rest.substring(0, 12)))
			return false;
		return true;
	}
	
	static public boolean isLCHexString(String value) {
		for (int i=0; i<value.length(); i++) {
			char c = value.charAt(i);
			switch (c) {
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '0':
			case 'a':
			case 'b':
			case 'c':
			case 'd':
			case 'e':
			case 'f':
				continue;
			default:
				return false;
			}
		}
		return true;
	}

	static public boolean isHexString(String value) {
		for (int i=0; i<value.length(); i++) {
			char c = value.charAt(i);
			switch (c) {
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '0':
			case 'a':
			case 'b':
			case 'c':
			case 'd':
			case 'e':
			case 'f':
			case 'A':
			case 'B':
			case 'C':
			case 'D':
			case 'E':
			case 'F':
				continue;
			default:
				return false;
			}
		}
		return true;
	}

}
