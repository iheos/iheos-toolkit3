package gov.nist.hit.ds.dsSims.eb.metadataValidator.object
import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder

@groovy.transform.TypeChecked
public class InternalClassificationValidator extends AbstractRegistryObjectValidator {
    ClassificationModel model

    InternalClassificationValidator(Event event, ClassificationModel model) {
        super(event, model)
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
