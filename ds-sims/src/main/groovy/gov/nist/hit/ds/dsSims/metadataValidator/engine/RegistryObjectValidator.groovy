package gov.nist.hit.ds.dsSims.metadataValidator.engine

import gov.nist.hit.ds.dsSims.client.ValidationContext
import gov.nist.hit.ds.dsSims.metadataValidator.datatype.CxFormat
import gov.nist.hit.ds.dsSims.metadataValidator.datatype.FormatValidator
import gov.nist.hit.ds.dsSims.metadataValidator.datatype.OidFormat
import gov.nist.hit.ds.dsSims.metadataValidator.datatype.UuidFormat
import gov.nist.hit.ds.dsSims.metadataValidator.object.Author
import gov.nist.hit.ds.dsSims.metadataValidator.object.ClassAndIdDescription
import gov.nist.hit.ds.dsSims.metadataValidator.object.Classification
import gov.nist.hit.ds.dsSims.metadataValidator.object.ExternalIdentifier
import gov.nist.hit.ds.dsSims.metadataValidator.object.Slot
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.metadata.MetadataSupport
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 8/4/14.
 */
class RegistryObjectValidator {

    public void validateSlot(ErrorRecorder er, String slotName, boolean multivalue, FormatValidator validator, String resource) {
        Slot slot = getSlot(slotName);
        if (slot == null) {
            return;
        }

        slot.validate(er, multivalue, validator, resource);
    }

    public void validateTopAtts(ErrorRecorder er, ValidationContext vc, String tableRef, List<String> statusValues) {
        validateId(er, vc, "entryUUID", id, null);

        if (vc.isSQ && vc.isResponse) {
            if (status == null)
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, identifyingString() + ": availabilityStatus attribute (status attribute in XML) must be present", this, tableRef);
            else {
                if (!statusValues.contains(status))
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, identifyingString() + ": availabilityStatus attribute must take on one of these values: " + statusValues + ", found " + status, this, "ITI TF-2a: 3.18.4.1.2.3.6");
            }

            validateId(er, vc, "lid", lid, null);

            List<OMElement> versionInfos = MetadataSupport.childrenWithLocalName(ro, "VersionInfo");
            if (versionInfos.size() == 0) {
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, identifyingString() + ": VersionInfo attribute missing", this, "ebRIM Section 2.5.1");
            }
        }

        if (vc.isSQ && vc.isXC && vc.isResponse) {
            validateHome(er, tableRef);

        }
    }
    public void validateId(ErrorRecorder er, ValidationContext vc, String attName, String attValue, String resource) {
        String defaultResource = "ITI TF-3: 4.1.12.3";
        if (attValue == null || attValue.equals("")) {
            er.err(XdsErrorCode.Code.XDSRegistryMetadataError, identifyingString() + ": " + attName + " attribute empty or missing", this, (resource!=null) ? resource : defaultResource);
        } else {
            if (vc.isSQ && vc.isResponse) {
                new UuidFormat(er, identifyingString() + " " + attName + " attribute must be a UUID", (resource!=null) ? resource : defaultResource).validate(id);
            } else if(id.startsWith("urn:uuid:")) {
                new UuidFormat(er, identifyingString() + " " + attName + " attribute", (resource!=null) ? resource : defaultResource).validate(id);
            }
        }

        for (Classification c : classifications)
            c.validateId(er, vc, "entryUUID", c.getId(), resource);

        for (Author a : authors)
            a.validateId(er, vc, "entryUUID", a.getId(), resource);

        for (ExternalIdentifier ei : externalIdentifiers)
            ei.validateId(er, vc, "entryUUID", ei.getId(), resource);

    }
    public void verifyIdsUnique(ErrorRecorder er, Set<String> knownIds) {
        if (id != null) {
            if (knownIds.contains(id))
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, identifyingString() + ": entryUUID " + id + "  identifies multiple objects", this, "ITI TF-3: 4.1.12.3 and ebRS 5.1.2");
            knownIds.add(id);
        }

        for (Classification c : classifications)
            c.verifyIdsUnique(er, knownIds);

        for (Author a : authors)
            a.verifyIdsUnique(er, knownIds);

        for (ExternalIdentifier ei : externalIdentifiers)
            ei.verifyIdsUnique(er, knownIds);
    }
    public void validateHome(ErrorRecorder er, String resource) {
        if (home == null)
            er.err(XdsErrorCode.Code.XDSRegistryMetadataError, identifyingString() + ": homeCommunityId attribute must be present", this, resource);
        else {
            if (home.length() > 64)
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, identifyingString() + ": homeCommunityId is limited to 64 characters, found " + home.length(), this, resource);

            String[] parts = home.split(":");
            if (parts.length < 3 || !parts[0].equals("urn") || !parts[1].equals("oid"))
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, identifyingString() + ": homeCommunityId must begin with urn:oid: prefix, found [" + home + "]", this, resource);
            new OidFormat(er, identifyingString() + " homeCommunityId", resource).validate(parts[parts.length-1]);
        }
    }
    public void validateClassificationsLegal(ErrorRecorder er, ClassAndIdDescription desc, String resource) {
        List<String> cSchemes = new ArrayList<String>();

        for (Classification c : getClassifications()) {
            String cScheme = c.getClassificationScheme();
            if (cScheme == null || cScheme.equals("") || !desc.definedSchemes.contains(cScheme)) {
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, identifyingString() + ": " + c.identifyingString() + " has an unknown classificationScheme attribute value: " + cScheme, this, resource);
            } else {
                cSchemes.add(cScheme);
            }
        }

        Set<String> cSchemeSet = new HashSet<String>();
        cSchemeSet.addAll(cSchemes);
        for (String cScheme : cSchemeSet) {
            if (count(cSchemes, cScheme) > 1 && !desc.multipleSchemes.contains(cScheme))
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, identifyingString() + ": " + classificationDescription(desc, cScheme) + " is specified multiple times, only one allowed", this, resource);
        }
    }
    public void validateClassificationsCodedCorrectly(ErrorRecorder er, ValidationContext vc) {
        for (Classification c : getClassifications())
            c.validateStructure(er, vc);

        for (Author a : getAuthors())
            a.validateStructure(er, vc);
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

    public void validateExternalIdentifiersCodedCorrectly(ErrorRecorder er, ValidationContext vc, ClassAndIdDescription desc, String resource) {
        for (ExternalIdentifier ei : getExternalIdentifiers()) {
            ei.validateStructure(er, vc);
            if (MetadataSupport.XDSDocumentEntry_uniqueid_uuid.equals(ei.getIdentificationScheme())) {
                String[] parts = ei.getValue().split("\\^");
                new OidFormat(er, identifyingString() + ": " + ei.identifyingString(), externalIdentifierDescription(desc, ei.getIdentificationScheme()))
                        .validate(parts[0]);
                if (parts[0].length() > 64)
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, identifyingString() + ": " + ei.identifyingString() + " OID part of DocumentEntry uniqueID is limited to 64 digits", this, resource);
                if (parts.length > 1 && parts[1].length() > 16) {
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, identifyingString() + ": " + ei.identifyingString() + " extension part of DocumentEntry uniqueID is limited to 16 characters", this, resource);
                }

            } else if (MetadataSupport.XDSDocumentEntry_patientid_uuid.equals(ei.getIdentificationScheme())){
                new CxFormat(er, identifyingString() + ": " + ei.identifyingString(), "ITI TF-3: Table 4.1.7")
                        .validate(ei.getValue());
            }
        }
    }



    public void validateRequiredExternalIdentifiersPresent(ErrorRecorder er, ValidationContext vc, ClassAndIdDescription desc, String resource)  {
        for (String idScheme : desc.requiredSchemes) {
            List<ExternalIdentifier> eis = getExternalIdentifiers(idScheme);
            if (eis.size() == 0)
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, identifyingString() + ": " + externalIdentifierDescription(desc, idScheme) + " is required but missing", this, resource);
            if (eis.size() > 1)
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, identifyingString() + ": " + externalIdentifierDescription(desc, idScheme) + " is specified multiple times, only one allowed", this, resource);
        }
    }


    public void validateExternalIdentifiersLegal(ErrorRecorder er, ClassAndIdDescription desc, String resource) {
        for (ExternalIdentifier ei : getExternalIdentifiers()) {
            String idScheme = ei.getIdentificationScheme();
            if (idScheme == null || idScheme.equals("") || !desc.definedSchemes.contains(idScheme))
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, identifyingString() + ": " + ei.identifyingString() + " has an unknown identificationScheme attribute value: " + idScheme, this, resource);
        }
    }

    public void validateSlots(ErrorRecorder er, ValidationContext vc) {
        er.challenge("Validating that Slots present are legal");
        validateSlotsLegal(er);
        er.challenge("Validating required Slots present");
        validateRequiredSlotsPresent(er, vc);
        er.challenge("Validating Slots are coded correctly");
        validateSlotsCodedCorrectly(er, vc);
    }

    public void validateRequiredClassificationsPresent(ErrorRecorder er, ValidationContext vc, ClassAndIdDescription desc, String resource) {
        if (!(vc.isXDM || vc.isXDRLimited)) {
            for (String cScheme : desc.requiredSchemes) {
                List<Classification> cs = getClassificationsByClassificationScheme(cScheme);
                if (cs.size() == 0)
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, identifyingString() + ": " + classificationDescription(desc, cScheme) + " is required but missing", this, resource);
            }
        }
    }
    public boolean verifySlotsUnique(ErrorRecorder er) {
        boolean ok = true;
        List<String> names = new ArrayList<String>();
        for (Slot slot : slots) {
            if (names.contains(slot.getName()))
                if (er != null) {
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, identifyingString() + ": Slot " + slot.getName() + " is multiply defined", this, "ebRIM 3.0 section 2.8.2");
                    ok = false;
                }
                else
                    names.add(slot.getName());
        }
        return ok;
    }
}
