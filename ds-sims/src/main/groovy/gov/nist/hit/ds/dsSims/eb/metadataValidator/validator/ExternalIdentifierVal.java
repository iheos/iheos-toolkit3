package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator;

import gov.nist.hit.ds.dsSims.eb.client.ValidationContext;
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.ExternalIdentifierModel;
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder;

/**
 * Created by bmajur on 12/23/14.
 */
public class ExternalIdentifierVal extends AbstractRegistryObjectVal {
    ExternalIdentifierModel model;

    public ExternalIdentifierVal(ExternalIdentifierModel model) { this.model = model; }

    @Override
    public void validateSlotsLegal(ErrorRecorder er) {

    }

    @Override
    public void validateRequiredSlotsPresent(ErrorRecorder er, ValidationContext
            vc) {

    }

    @Override
    public void validateSlotsCodedCorrectly(ErrorRecorder er, ValidationContext vc) {

    }
}
