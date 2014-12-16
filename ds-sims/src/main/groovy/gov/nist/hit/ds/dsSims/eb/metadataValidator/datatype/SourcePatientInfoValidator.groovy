package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype

import gov.nist.hit.ds.dsSims.eb.metadataValidator.field.ValidatorCommon
import gov.nist.hit.ds.dsSims.eb.metadataValidator.object.SlotModel
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

public class SourcePatientInfoValidator extends ValComponentBase {
    SimHandle simHandle
    SlotModel input

	public SourcePatientInfoValidator(SimHandle _simHandle, SlotModel _input) {
		super(_simHandle.event)
        simHandle = _simHandle
        input = _input
	}

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='SSSlot001', msg='submissionTime must be present', ref="ITI TF-3: Table 4.1-5")
    def validate() {
        for (int valueI=0; valueI<input.size(); valueI++) {
            String content = input.getValue(valueI)
			if (content == null || content.equals("")) { fail("Slot sourcePatientInfo has empty Slot value (index ${valueI})"); continue }
			String[] parts = content.split("\\|");
			
			if (parts.length == 0) { fail(twoPartsMsg(valueI)); continue }
			if (!parts[0].startsWith("PID-")) { fail(twoPartsMsg(valueI)); continue }
			if (parts.length == 1 && parts[0].endsWith("|")) {
				// Example  PID-3|
				continue;
			}
			if (parts.length != 2) { fail(twoPartsMsg(valueI)); continue }
			if (parts[0].startsWith("PID-3")) {
				String msg = ValidatorCommon.validate_CX_datatype(parts[1]);
				if (msg != null) fail("Slot sourcePatientInfo#PID-3 must be valid Patient ID: ${msg}")
			}
		}
	}

	private String twoPartsMsg(int valueI) {
		"Slot sourcePatientInfo Value (index ${valueI}) must have two parts separated by | and the first part must be formatted as PID-x where x is a number"
	}


}
