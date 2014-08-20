package gov.nist.hit.ds.dsSims.metadataValidator.object
import gov.nist.hit.ds.dsSims.client.ValidationContext
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder

public class InternalClassificationValidator extends AbstractRegistryObjectValidator {
    ClassificationModel model

    InternalClassificationValidator(ClassificationModel model) {
        super(model)
        this.model = model
    }

	public void validateRequiredSlotsPresent(ErrorRecorder er,
			ValidationContext vc) {
	}

	public void validateSlotsCodedCorrectly(ErrorRecorder er,
			ValidationContext vc) {
	}

	public void validateSlotsLegal(ErrorRecorder er) {
	}

}
