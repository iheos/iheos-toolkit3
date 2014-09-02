package gov.nist.hit.ds.dsSims.metadataValidator.object
import gov.nist.hit.ds.dsSims.client.ValidationContext
import gov.nist.hit.ds.dsSims.metadataValidator.datatype.DtmSubValidator
import gov.nist.hit.ds.dsSims.metadataValidator.datatype.XonXcnXtnSubValidator
import gov.nist.hit.ds.dsSims.metadataValidator.field.IdSubValidator
import gov.nist.hit.ds.dsSims.metadataValidator.field.LidSubValidator
import gov.nist.hit.ds.dsSims.metadataValidator.field.TopAttsSubValidator
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Setup
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
//@groovy.transform.TypeChecked
public class SubmissionSetValidator extends AbstractRegistryObjectValidator {
    SubmissionSetModel model
    Set<String> knownIds
    ValidationContext vc
    SimHandle simHandle

	static public String table416 = "ITI TF-3: Table 4.1-6";



    SubmissionSetValidator(SimHandle simHandle, SubmissionSetModel model, ValidationContext vc, Set<String> knownIds) {
        super(simHandle.event, model);
        this.simHandle = simHandle
        this.model = model
        this.vc = vc
        this.knownIds = knownIds
    }

    void runBefore() {
        if (vc.isXDR)
            vc.isXDRLimited = model.isMetadataLimited();
    }

    void runAfter() {
        new IdSubValidator(this, model, vc).asSelf().run()
        new LidSubValidator(this, model, vc).asSelf().run()

        new SubmissionSetSlotsValidator(simHandle, model).asPeer().run()
    }

    // Guards
    def xdrLimited() { vc.isXDRLimited }

    @Setup
    @Validation(id='Setup', msg='Setup', ref='')
    def setup() {
        if (vc.isXDRLimited)
            msg("Labeled as Limited Metadata");
        else if (vc.isXDRMinimal)
            msg("Labeled as Minimal Metadata (Direct)");
        else
            msg ('Labeled as Full Metadata')
        new TopAttsSubValidator(this, model, vc, SubmissionSetModel.statusValues).asSelf().run()
    }

//    @Validation(id='Top', msg='Top attributes', ref='')
//    def validateTopAtts() {
//        new TopAttsSubValidator(this, model, vc, SubmissionSetModel.statusValues).asSelf().run()
//    }


    public void validate(ErrorRecorder er, ValidationContext vc,
			Set<String> knownIds) {

		validateTopAtts(er, vc);

		validateSlots(er, vc);

		if (vc.isXDM || vc.isXDRLimited)
			validateClassifications(er, vc, SubmissionSetModel.XDMclassificationDescription, table416);
		else if (vc.isXDRMinimal)
			validateClassifications(er, vc, SubmissionSetModel.MinimalclassificationDescription, table416);
		else
			validateClassifications(er, vc, SubmissionSetModel.classificationDescription, table416);

		if (vc.isXDM || vc.isXDRLimited)
			validateExternalIdentifiers(er, vc, SubmissionSetModel.XDMexternalIdentifierDescription, table416);
		else if (vc.isXDRMinimal)
			validateExternalIdentifiers(er, vc, SubmissionSetModel.MinimalexternalIdentifierDescription, table416);
		else
			validateExternalIdentifiers(er, vc, SubmissionSetModel.externalIdentifierDescription, table416);

		verifyIdsUnique(er, knownIds);
	}

	public void validateSlotsCodedCorrectly(ErrorRecorder er, ValidationContext vc)  {
		if (vc.isXDRMinimal) { 
			validateDirectSlotsCodedCorrectly(er, vc);
		} else {
			//                    name				   multi	format                                                  resource
			validateSlot(er, 	"submissionTime", 	   false, 	new DtmSubValidator(er, "Slot submissionTime",            table416),  table416);
			validateSlot(er, 	"intendedRecipient",   true, 	new XonXcnXtnSubValidator(er, "Slot intendedRecipient",      table416),  table416);
		}
	}

	public void validateDirectSlotsCodedCorrectly(ErrorRecorder er, ValidationContext vc)  {

		//                    name				   multi	format                                                  resource
		validateSlot(er, 	"submissionTime", 	   false, 	new DtmSubValidator(er, "Slot submissionTime",            table416),  table416);
		validateSlot(er, 	"intendedRecipient",   true, 	new XonXcnXtnSubValidator(er, "Slot intendedRecipient",     table416),  table416);
	}

	public void validateRequiredSlotsPresent(ErrorRecorder er, ValidationContext vc) {
		// Slots always required
		if (vc.isXDRMinimal) {
			for (String slotName : SubmissionSetModel.requiredSlotsMinimal) {
				if (model.getSlot(slotName) == null)
					er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": Slot " + slotName + " missing", this, table416);
			}
		} else {
			for (String slotName : SubmissionSetModel.requiredSlots) {
				if (model.getSlot(slotName) == null)
					er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": Slot " + slotName + " missing", this, table416);
			}
		}
	}

	public void validateSlotsLegal(ErrorRecorder er)  {
		verifySlotsUnique(er);
		for (SlotModel slot : model.getSlots()) {
			if ( ! legal_slot_name(slot.getName()))
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": " + slot.getName() + " is not a legal slot name for a SubmissionSet",  this,  table416);

		}
	}

	boolean legal_slot_name(String name) {
		if (name == null) return false;
		if (name.startsWith("urn:")) return true;
		return SubmissionSetModel.definedSlots.contains(name);
	}



}
