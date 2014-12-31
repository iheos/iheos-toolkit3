package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype
import gov.nist.hit.ds.dsSims.eb.utility.ValidatorCommon
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.hit.ds.utilities.xml.XmlUtil
import org.apache.axiom.om.OMElement

public class SourcePatientInfoFormatValidator extends AbstractFormatValidator {
    OMElement slot

    String formatName() { return 'SourcePatientInfo' }

    SourcePatientInfoFormatValidator(SimHandle _simHandle, String context, OMElement _value) {
        super(_simHandle, context);
        slot = _value
    }

    @ErrorCode(code= XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rospi010', msg='SourcePatientInfo format', ref='ITI TF-3: Table 4.1-5: sourcePatientInfo')
    def rospi010() {
        infoFound("${context} root is ${slot.getLocalName()}")
    }

    @ErrorCode(code= XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rospi020', msg='SourcePatientInfo contents', ref='ITI TF-3: Table 4.1-5: sourcePatientInfo')
    def rospi020() {
		OMElement value_list = XmlUtil.firstChildWithLocalName(slot, "ValueList");
		int valueI = -1;
		for (OMElement value : XmlUtil.childrenWithLocalName(value_list, "Value")) {
			valueI++;
			String content = value.getText();
            infoFound("Slot value is ${content}")
            assertHasValue(content)
			if (content == null || content.equals("")) {
				continue;
			}
			String[] parts = content.split('|');
			
			if (parts.length == 0) {
				fail(twoPartsMsg(valueI))
				continue;
			}
			if (!parts[0].startsWith("PID-")) {
                fail('Must start with PID-')
                fail(twoPartsMsg(valueI))
				continue;
			}
			if (parts.length == 1 && parts[0].endsWith("|")) {
				// Example  PID-3|
				continue;
			}
			if (parts.length != 2) {
                fail('Value must follow |')
                fail(twoPartsMsg(valueI))
				continue;
			}
			if (parts[0].startsWith("PID-3")) {
				String msg = ValidatorCommon.validate_CX_datatype(parts[1]);
				if (msg != null)
					fail("Slot sourcePatientInfo#PID-3 must be valid Patient ID: " + msg)
			}
		}
	}

	String twoPartsMsg(int valueI) { 
		"Slot sourcePatientInfo Value (index ${valueI}) must have two parts separated by | and the first part must be formatted as PID-x where x is a number"
	}


}
