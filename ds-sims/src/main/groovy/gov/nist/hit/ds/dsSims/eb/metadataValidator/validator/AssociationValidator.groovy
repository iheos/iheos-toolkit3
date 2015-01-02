package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator
import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.metadata.MetadataSupport
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.AssociationModel
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.ClassificationModel
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.SlotModel
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
/**
 * Created by bmajur on 12/23/14.
 */
public class AssociationValidator extends AbstractRegistryObjectVal  {
    public AssociationModel model;
    SimHandle simHandle
    ValidationContext vc
    Set<String> knownIds

    AssociationValidator(SimHandle _simHandle, AssociationModel _model, ValidationContext _vc, Set<String> _knownIds) {
        super(_simHandle)
        model = _model
        vc = _vc
        knownIds = _knownIds
    }

    def run() {
        if (vc.skipInternalStructure) return;
        runValidationEngine()
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roas010', msg='Validate Association ID', ref='')
    def roas010() {
        new IdValidator(simHandle, vc, "entryUUID", model.id).asSelf(this).run()
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roas020', msg='Validate sourceObject', ref='')
    def roas020() {
        new IdValidator(simHandle, vc, "sourceObject", model.source).asSelf(this).run()
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roas030', msg='Validate targetObject', ref='')
    def roas030() {
        new IdValidator(simHandle, vc, "targetObject", model.target).asSelf(this).run()
    }

    boolean muReq() { vc.isMU && vc.isRequest }
    boolean notMuReq() { !muReq() }

    @Guard(methodNames=['muReq'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roas040', msg='Validate MetadataUpdate type', ref='ITI TF-3 Table 4.1-2.1')
    def roas040() {
        boolean basicType = AssociationModel.assocTypes.contains(model.type)
        boolean muType = AssociationModel.assocTypesMU.contains(model.type)
        if (!basicType && !muType)
            fail(model.identifyingString() + ": associationType " + model.type + " unknown. Known assocationTypes are " + AssociationModel.assocTypes + " and " + AssociationModel.assocTypesMU)
    }

    @Guard(methodNames=['notMuReq'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roas050', msg='Validate associationType', ref='ITI TF-3 Table 4.1-2.1')
    def roas050() {
        if (vc.isResponse) {
            if (!AssociationModel.assocTypes.contains(model.type) && !AssociationModel.assocTypesMU.contains(model.type))
                fail(model.identifyingString() + ": associationType " + model.type + " unknown. Known assocationTypes are " + AssociationModel.assocTypes + " and " + AssociationModel.assocTypesMU)
        } else if (!AssociationModel.assocTypes.contains(model.type))
            fail(model.identifyingString() + ": associationType " + model.type + " unknown. Known assocationTypes are " + AssociationModel.assocTypes)
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roas060', msg='Validate slots coded correctly', ref='ITI TF-3 Table 4.1-4.1')
    def roas060() {
        SlotModel s = model.getSlot(MetadataSupport.assoc_slot_submission_set_status);
        if (s == null)
            return;
        if (s.getValues().size() == 1) {
            String value = s.getValues().get(0);
            if ("Original".equals(value) || "Reference".equals(value))
            ;
            else
                fail(model.identifyingString() + ": SubmissionSetStatus Slot can only take value Original or Reference")
        } else {
            fail(model.identifyingString() + ": SubmissionSetStatus Slot must have only single value")
        }
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roas070', msg='Validate Classifications', ref='ITI TF-3 4.1.6.1')
    def roas070() {
        List<ClassificationModel> c = model.getClassificationsByClassificationScheme(MetadataSupport.XDSAssociationDocumentation_uuid);
        if (c.size() == 0)
        ;
        else if (c.size() > 1)
            fail(model.identifyingString() +
                    ": may contain only a single documentation classification (classificationScheme=" +
                    MetadataSupport.XDSAssociationDocumentation_uuid + ")")
        else {
            if (!assocs_with_documentation.contains(model.type))
                fail(model.identifyingString() +
                        ": documentation classification (classificationScheme=" +
                        MetadataSupport.XDSAssociationDocumentation_uuid +
                        ") may only be present on the following association types: " +
                        assocs_with_documentation)
        }
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roas080', msg='Validate ExternalIdentifiers', ref='ITI TF-3 4.1.6.1')
    def roas080() {
        new ExternalIdentifierValidator(simHandle, model, vc, AssociationModel.externalIdentifierDescription).asSelf(this).run()
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roas090', msg='Verify no self-references', ref='')
    def roas090() {
        if (model.source.equals(model.id))
            fail(model.identifyingString() + " sourceObject attribute references self")
        if (model.target.equals(model.id))
            fail(model.identifyingString() + " targetObject attribute references self")
    }
    static List<String> assocs_with_documentation =
            Arrays.asList(
                    MetadataSupport.assoctype_rplc,
                    MetadataSupport.assoctype_xfrm,
                    MetadataSupport.assoctype_apnd,
                    MetadataSupport.assoctype_xfrm_rplc
            );


    List<String> relationship_assocs =
            Arrays.asList(
                    MetadataSupport.assoctype_rplc,
                    MetadataSupport.assoctype_apnd,
                    MetadataSupport.assoctype_xfrm,
                    MetadataSupport.assoctype_xfrm_rplc
            );


    public void validateRequiredSlotsPresent(ErrorRecorder er,
                                             ValidationContext vc) {
//		Metadata m = getMetadata();
//		if (type.equals(MetadataSupport.assoctype_has_member) &&
//				m.isSubmissionSet(source) &&
//				m.isDocument(target)) {
//			if (getSlot(MetadataSupport.assoc_slot_submission_set_status) == null)
//				er.err(identifyingString() + ": SubmissionSet to DocumentEntry HasMember association must have a SubmissionSetStatus Slot", "ITI TF-3: 4.1.4.1");
//		} else {
//
//		}

    }
}
