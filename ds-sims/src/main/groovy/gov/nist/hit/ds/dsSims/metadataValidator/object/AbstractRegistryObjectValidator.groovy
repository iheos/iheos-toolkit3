package gov.nist.hit.ds.dsSims.metadataValidator.object
import gov.nist.hit.ds.dsSims.client.ValidationContext
import gov.nist.hit.ds.dsSims.metadataValidator.datatype.CxFormat
import gov.nist.hit.ds.dsSims.metadataValidator.datatype.FormatValidator
import gov.nist.hit.ds.dsSims.metadataValidator.datatype.OidFormat
import gov.nist.hit.ds.dsSims.metadataValidator.datatype.UuidFormat
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.metadata.MetadataSupport
import gov.nist.hit.ds.utilities.xml.XmlUtil
import org.apache.axiom.om.OMElement

@groovy.transform.TypeChecked
public abstract class AbstractRegistryObjectValidator {
    RegistryObjectModel model

//	abstract public String identifyingString();
	abstract public void validateSlotsLegal(ErrorRecorder er);
	abstract public void validateRequiredSlotsPresent(ErrorRecorder er, ValidationContext vc);
	abstract public void validateSlotsCodedCorrectly(ErrorRecorder er, ValidationContext vc);

    public AbstractRegistryObjectValidator(RegistryObjectModel model) { this.model = model }

	public List<ExternalIdentifier> getExternalIdentifiers(String identificationScheme) {
		List<ExternalIdentifier> eis = new ArrayList<ExternalIdentifier>();
		for (ExternalIdentifier ei : model.externalIdentifiers) {
			if (ei.getIdentificationScheme().equals(identificationScheme))
				eis.add(ei);
		}
		return eis;
	}

	protected int count(List<String> strings, String target) {
		int i=0;
	
		for (String s : strings)
			if (s.equals(target))
				i++;
	
		return i;
	}

// TODO: remove since copied to RegistryObjectValidator.groovy

    // should be utility called by validators
    public void validateSlot(ErrorRecorder er, String slotName, boolean multivalue, FormatValidator validator, String resource) {
        Slot slot = model.getSlot(slotName);
        if (slot == null) {
            return;
        }

        slot.validate(er, multivalue, validator, resource);
    }

    public void validateTopAtts(ErrorRecorder er, ValidationContext vc, String tableRef, List<String> statusValues) {
        validateId(er, vc, "entryUUID", model.id, null);

        if (vc.isSQ && vc.isResponse) {
            if (model.status == null)
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": availabilityStatus attribute (status attribute in XML) must be present", this, tableRef);
            else {
                if (!statusValues.contains(model.status))
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": availabilityStatus attribute must take on one of these values: " + statusValues + ", found " + model.status, this, "ITI TF-2a: 3.18.4.1.2.3.6");
            }

            validateId(er, vc, "lid", model.lid, null);

            List<OMElement> versionInfos = XmlUtil.childrenWithLocalName(model.ro, "VersionInfo");
            if (versionInfos.size() == 0) {
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": VersionInfo attribute missing", this, "ebRIM Section 2.5.1");
            }
        }

        if (vc.isSQ && vc.isXC && vc.isResponse) {
            validateHome(er, tableRef);

        }
    }

    // break up into validateId (utility), class, author, EI
    public void validateId(ErrorRecorder er, ValidationContext vc, String attName, String attValue, String resource) {
        String defaultResource = "ITI TF-3: 4.1.12.3";
        if (attValue == null || attValue.equals("")) {
            er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": " + attName + " attribute empty or missing", this, (resource!=null) ? resource : defaultResource);
        } else {
            if (vc.isSQ && vc.isResponse) {
                new UuidFormat(er, model.identifyingString() + " " + attName + " attribute must be a UUID", (resource!=null) ? resource : defaultResource).validate(model.id);
            } else if(model.id.startsWith("urn:uuid:")) {
                new UuidFormat(er, model.identifyingString() + " " + attName + " attribute", (resource!=null) ? resource : defaultResource).validate(model.id);
            }
        }

        for (Classification c : model.classifications)
            c.validateId(er, vc, "entryUUID", c.model.getId(), resource);

        for (Author a : model.authors)
            a.validateId(er, vc, "entryUUID", a.model.getId(), resource);

        for (ExternalIdentifier ei : model.externalIdentifiers)
            ei.validateId(er, vc, "entryUUID", ei.model.getId(), resource);

    }
    public void verifyIdsUnique(ErrorRecorder er, Set<String> knownIds) {
        if (model.id != null) {
            if (knownIds.contains(model.id))
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": entryUUID " + model.id + "  identifies multiple objects", this, "ITI TF-3: 4.1.12.3 and ebRS 5.1.2");
            knownIds.add(model.id);
        }

        for (Classification c : model.classifications)
            c.verifyIdsUnique(er, knownIds);

        for (Author a : model.authors)
            a.verifyIdsUnique(er, knownIds);

        for (ExternalIdentifier ei : model.externalIdentifiers)
            ei.verifyIdsUnique(er, knownIds);
    }

    // guarded
    public void validateHome(ErrorRecorder er, String resource) {
        if (model.home == null)
            er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": homeCommunityId attribute must be present", this, resource);
        else {
            if (model.home.length() > 64)
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": homeCommunityId is limited to 64 characters, found " + model.home.length(), this, resource);

            String[] parts = model.home.split(":");
            if (parts.length < 3 || !parts[0].equals("urn") || !parts[1].equals("oid"))
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": homeCommunityId must begin with urn:oid: prefix, found [" + model.home + "]", this, resource);
            new OidFormat(er, model.identifyingString() + " homeCommunityId", resource).validate(parts[parts.length-1]);
        }
    }
    public void validateClassificationsLegal(ErrorRecorder er, ClassAndIdDescription desc, String resource) {
        List<String> cSchemes = new ArrayList<String>();

        for (Classification c : model.getClassifications()) {
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
            if (count(cSchemes, cScheme) > 1 && !desc.multipleSchemes.contains(cScheme))
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": " + model.classificationDescription(desc, cScheme) + " is specified multiple times, only one allowed", this, resource);
        }
    }
    public void validateClassificationsCodedCorrectly(ErrorRecorder er, ValidationContext vc) {
        for (Classification c : model.getClassifications())
            c.validateStructure(er, vc);

        for (Author a : model.getAuthors())
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
        for (ExternalIdentifier ei : model.getExternalIdentifiers()) {
            ei.validateStructure(er, vc);
            if (MetadataSupport.XDSDocumentEntry_uniqueid_uuid.equals(ei.getIdentificationScheme())) {
                String[] parts = ei.getValue().split("\\^");
                new OidFormat(er, model.identifyingString() + ": " + ei.identifyingString(), model.externalIdentifierDescription(desc, ei.getIdentificationScheme()))
                        .validate(parts[0]);
                if (parts[0].length() > 64)
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": " + ei.identifyingString() + " OID part of DocumentEntry uniqueID is limited to 64 digits", this, resource);
                if (parts.length > 1 && parts[1].length() > 16) {
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": " + ei.identifyingString() + " extension part of DocumentEntry uniqueID is limited to 16 characters", this, resource);
                }

            } else if (MetadataSupport.XDSDocumentEntry_patientid_uuid.equals(ei.getIdentificationScheme())){
                new CxFormat(er, model.identifyingString() + ": " + ei.identifyingString(), "ITI TF-3: Table 4.1.7")
                        .validate(ei.getValue());
            }
        }
    }



    public void validateRequiredExternalIdentifiersPresent(ErrorRecorder er, ValidationContext vc, ClassAndIdDescription desc, String resource)  {
        for (String idScheme : desc.requiredSchemes) {
            List<ExternalIdentifier> eis = getExternalIdentifiers(idScheme);
            if (eis.size() == 0)
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": " + model.externalIdentifierDescription(desc, idScheme) + " is required but missing", this, resource);
            if (eis.size() > 1)
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": " + model.externalIdentifierDescription(desc, idScheme) + " is specified multiple times, only one allowed", this, resource);
        }
    }


    public void validateExternalIdentifiersLegal(ErrorRecorder er, ClassAndIdDescription desc, String resource) {
        for (ExternalIdentifier ei : model.getExternalIdentifiers()) {
            String idScheme = ei.getIdentificationScheme();
            if (idScheme == null || idScheme.equals("") || !desc.definedSchemes.contains(idScheme))
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": " + ei.identifyingString() + " has an unknown identificationScheme attribute value: " + idScheme, this, resource);
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
                List<Classification> cs = model.getClassificationsByClassificationScheme(cScheme);
                if (cs.size() == 0)
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": " + model.classificationDescription(desc, cScheme) + " is required but missing", this, resource);
            }
        }
    }
    public boolean verifySlotsUnique(ErrorRecorder er) {
        boolean ok = true;
        List<String> names = new ArrayList<String>();
        for (Slot slot : model.slots) {
            if (names.contains(slot.getName()))
                if (er != null) {
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": Slot " + slot.getName() + " is multiply defined", this, "ebRIM 3.0 section 2.8.2");
                    ok = false;
                }
                else
                    names.add(slot.getName());
        }
        return ok;
    }



}
