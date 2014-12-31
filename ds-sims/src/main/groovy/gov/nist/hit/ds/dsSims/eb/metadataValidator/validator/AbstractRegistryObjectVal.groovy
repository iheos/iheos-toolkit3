package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator
import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.metadata.MetadataSupport
import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.AbstractFormatValidator
import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.CxFormatValidator
import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.OidFormatValidator
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.*
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
/**
 * Created by bmajur on 12/23/14.
 */
public abstract class AbstractRegistryObjectVal {

    AbstractRegistryObjectModel model;

    public AbstractRegistryObjectVal() { }

    public AbstractRegistryObjectVal(AbstractRegistryObjectModel model) {
        this.model = model;
    }

    public void validateSlot(AbstractRegistryObjectModel model, ErrorRecorder er, String slotName, boolean multivalue, AbstractFormatValidator validator, String resource) {
        SlotModel slot = model.getSlot(slotName);
        if (slot == null) return;
        new SlotValidator(simHandle, slot, multivalue, validator)
    }

    public void validateRequiredClassificationsPresent(ErrorRecorder er, ValidationContext vc, ClassAndIdDescription desc, String resource) {
        if (!(vc.isXDM || vc.isXDRLimited)) {
            for (String cScheme : desc.requiredSchemes) {
                List<ClassificationModel> cs = model.getClassificationsByClassificationScheme(cScheme);
                if (cs.size() == 0)
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": " + model.classificationDescription(desc, cScheme) + " is required but missing", this, resource);
            }
        }
    }

    public void validateClassificationsCodedCorrectly(ErrorRecorder er, ValidationContext vc) {
        for (ClassificationModel c : model.getClassifications())
            new ClassificationVal(c).validateStructure(er, vc);

        for (AuthorModel a : model.getAuthors())
            new AuthorVal(a).validateStructure(er, vc);
    }

    public void validateClassifications(ErrorRecorder er, ValidationContext vc, ClassAndIdDescription desc, String resource)  {
        er.challenge("Validating Classifications present are legal");
        validateClassificationsLegal(er, desc, resource);
        er.challenge("Validating Required Classifications present");
        validateRequiredClassificationsPresent(er, vc, desc, resource);
        er.challenge("Validating Classifications coded correctly");
        validateClassificationsCodedCorrectly(er, vc);
    }

    public void validateExternalIdentifiers(ErrorRecorder er, ValidationContext vc, ClassAndIdDescription desc, String resource) {
        er.challenge("Validating ExternalIdentifiers present are legal");
        validateExternalIdentifiersLegal(er, desc, resource);
        er.challenge("Validating Required ExternalIdentifiers present");
        validateRequiredExternalIdentifiersPresent(er, vc, desc, resource);
        er.challenge("Validating ExternalIdentifiers coded correctly");
        validateExternalIdentifiersCodedCorrectly(er, vc, desc, resource);
    }

//    public void validateRequiredExternalIdentifiersPresent(ErrorRecorder er, ValidationContext vc, ClassAndIdDescription desc, String resource)  {
//        for (String idScheme : desc.requiredSchemes) {
//            List<ExternalIdentifierModel> eis = model.getExternalIdentifiers(idScheme);
//            if (eis.size() == 0)
//                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": " + model.externalIdentifierDescription(desc, idScheme) + " is required but missing", this, resource);
//            if (eis.size() > 1)
//                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": " + model.externalIdentifierDescription(desc, idScheme) + " is specified multiple times, only one allowed", this, resource);
//        }
//    }


//    public void validateExternalIdentifiersLegal(ErrorRecorder er, ClassAndIdDescription desc, String resource) {
//        for (ExternalIdentifierModel ei : model.getExternalIdentifiers()) {
//            String idScheme = ei.getIdentificationScheme();
//            if (idScheme == null || idScheme.equals("") || !desc.definedSchemes.contains(idScheme))
//                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": " + ei.identifyingString() + " has an unknown identificationScheme attribute value: " + idScheme, this, resource);
//        }
//    }


    public void validateExternalIdentifiersCodedCorrectly(ErrorRecorder er, ValidationContext vc, ClassAndIdDescription desc, String resource) {
        for (ExternalIdentifierModel ei : model.getExternalIdentifiers()) {
            new ExternalIdentifierVal(ei).validateStructure(er, vc);
            if (MetadataSupport.XDSDocumentEntry_uniqueid_uuid.equals(ei.getIdentificationScheme())) {
                String[] parts = ei.getValue().split("\\^");
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

    public void validateClassificationsLegal(ErrorRecorder er, ClassAndIdDescription desc, String resource) {
        List<String> cSchemes = new ArrayList<String>();

        for (ClassificationModel c : model.getClassifications()) {
            String cScheme = c.getClassificationScheme();
            if (cScheme == null || cScheme.equals("") || !desc.definedSchemes.contains(cScheme)) {
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": " + c.identifyingString() + " has an unknown classificationScheme attribute value: " + cScheme, this, resource);
            } else {
                cSchemes.add(cScheme);
            }
        }

        Set<String> cSchemeSet = new HashSet<String>();
        cSchemeSet.addAll(cSchemes);
        for (String cScheme : cSchemeSet) {
            if (model.count(cSchemes, cScheme) > 1 && !desc.multipleSchemes.contains(cScheme))
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": " + model.classificationDescription(desc, cScheme) + " is specified multiple times, only one allowed", this, resource);
        }
    }




}
