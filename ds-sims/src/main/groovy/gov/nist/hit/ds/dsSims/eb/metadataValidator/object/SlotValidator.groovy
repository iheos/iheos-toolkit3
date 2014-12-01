package gov.nist.hit.ds.dsSims.eb.metadataValidator.object
import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.FormatValidator
import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.FormatValidatorCalledIncorrectlyException
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.xdsException.XdsInternalException

@Deprecated
public class SlotValidator {
    SlotModel model

    SlotValidator(SlotModel model) {
        super(model)
        this.model = model
    }

	public void validate(ErrorRecorder er, boolean multivalue, FormatValidator formatValidator, String resource) {
		if (!multivalue && values.size() > 1)
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, getOwnerType() + "(" + getOwnerId() + ") has Slot " + name + " which is required to have a single value, " + values.size() + "  values found", this, resource);
		try {
			for (String value : values) {
				formatValidator.validate(value);
			}
		} catch (FormatValidatorCalledIncorrectlyException e) {
			// oops - can't call with individual slot values, needs Slot
			try {
				formatValidator.validate(myElement);
			} catch (FormatValidatorCalledIncorrectlyException e1) {
				// hmmm - I guess we give up here
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new XdsInternalException("Slot#validate: the validator " + formatValidator.getClass().getName() + " implements no validate methods"));
			}
		}
	}


}
