package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator
import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.metadata.MetadataSupport
import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.CxFormatValidator
import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.OidFormatValidator
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.AbstractRegistryObjectModel
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.ClassAndIdDescription
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.ExternalIdentifierModel
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

class ExternalIdentifierValidator extends ValComponentBase {
    SimHandle simHandle
    ValidationContext vc
    ClassAndIdDescription desc
    AbstractRegistryObjectModel model

    ExternalIdentifierValidator(SimHandle _simHandle, AbstractRegistryObjectModel _model, ValidationContext _vc, ClassAndIdDescription _desc) {
        super(_simHandle.event)
        simHandle = _simHandle
        model = _model
        vc = _vc
        desc = _desc
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'roei010', msg = 'Validating ExternalIdentifiers present are legal', ref = '')
    def roei010() {
        for (ExternalIdentifierModel ei : model.getExternalIdentifiers()) {
            String idScheme = ei.getIdentificationScheme();
            infoFound("Identifier Scheme ${idScheme} found (${model.identifyingString()})")
            assertHasValue(idScheme)
            assertIn(desc.definedSchemes, idScheme)
        }

    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'roei020', msg = 'Validating Required ExternalIdentifiers present', ref = '')
    def roei020() {
        for (String idScheme : desc.requiredSchemes) {
            infoFound("Identifier Scheme ${idScheme} found (${model.identifyingString()})")
            List<ExternalIdentifierModel> eis = model.getExternalIdentifiers(idScheme);
            if (eis.size() == 0)
                fail(model.identifyingString() + ": " + model.externalIdentifierDescription(desc, idScheme) + " is required but missing")
            if (eis.size() > 1)
                fail(model.identifyingString() + ": " + model.externalIdentifierDescription(desc, idScheme) + " is specified multiple times, only one allowed")
        }
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'roei030', msg = 'Validating ExternalIdentifiers structure', ref = '')
    def roei030() {
        for (ExternalIdentifierModel ei : model.getExternalIdentifiers()) {
            String idScheme = ei.identificationScheme
            infoFound("Identifier Scheme ${idScheme} found (${ei.identifyingString()})")
            new ExternalIdentifierStructureValidator(simHandle, ei).asSelf(this).run()
        }
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'roei040', msg = 'Validating Required ExternalIdentifiers present', ref = '')
    def roei040() {
        for (ExternalIdentifierModel ei : model.getExternalIdentifiers()) {
            if (MetadataSupport.XDSDocumentEntry_uniqueid_uuid.equals(ei.getIdentificationScheme())) {
                String[] parts = ei.getValue().split('^');
                new OidFormatValidator(er, model.identifyingString() + ": " + ei.identifyingString(), model.externalIdentifierDescription(desc, ei.getIdentificationScheme()))
                        .validate(parts[0]);
                if (parts[0].length() > 64)
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": " + ei.identifyingString() + " OID part of DocumentEntry uniqueID is limited to 64 digits", this, resource);
                if (parts.length > 1 && parts[1].length() > 16) {
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": " + ei.identifyingString() + " extension part of DocumentEntry uniqueID is limited to 16 characters", this, resource);
                }

            } else if (MetadataSupport.XDSDocumentEntry_patientid_uuid.equals(ei.getIdentificationScheme())){
                new CxFormatValidator(er, model.identifyingString() + ": " + ei.identifyingString(), "ITI TF-3: Table 4.1.7")
                        .validate(ei.getValue());
            }
        }
    }
}