package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator
import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.metadata.MetadataSupport
import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.*
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.DocumentEntryModel
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.SlotModel
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.xdsException.MetadataException
/**
 * Created by bmajur on 12/23/14.
 */
public class DocumentEntryVal extends AbstractRegistryObjectVal  implements TopLevelObjectVal {
    public DocumentEntryModel model;

    public DocumentEntryVal(DocumentEntryModel model) { this.model = model; }

    public void validate(ErrorRecorder er, ValidationContext vc, Set<String> knownIds) {
        if (vc.skipInternalStructure)
            return;

        if (vc.isXDR)
            vc.isXDRLimited = model.isMetadataLimited();

        if (vc.isXDRLimited)
            er.sectionHeading("is labeled as Limited Metadata");

        validateTopAtts(er, vc);

        validateSlots(er, vc);

        if (vc.isXDRMinimal)
            validateClassifications(er, vc, DocumentEntryModel.directClassificationDescription, table415);
        else
            validateClassifications(er, vc, DocumentEntryModel.classificationDescription, table415);

        if (vc.isXDRMinimal)
            validateExternalIdentifiers(er, vc, DocumentEntryModel.directExternalIdentifierDescription, table415);
        else if (vc.isXDM || vc.isXDRLimited)
            validateExternalIdentifiers(er, vc, DocumentEntryModel.XDMexternalIdentifierDescription, table415);
        else
            validateExternalIdentifiers(er, vc, DocumentEntryModel.externalIdentifierDescription, table415);

        new IdUniqueValidator(simHandle, model.id, knownIds).asSelf(this).run()
    }

    static public String table415 = "ITI TF-3: Table 4.1-5, TF-2b: Table 3.41.4.1.2-2";

    // this takes in two circumstances:
    //	Slots always required
    //  Optional Slots required by this transaction

    @Override
    public void validateRequiredSlotsPresent(ErrorRecorder er, ValidationContext vc) {
        // Slots always required

        if (vc.isXDRMinimal) {
            for (String slotName : DocumentEntryModel.directRequiredSlots) {
                if (model.getSlot(slotName) == null)
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": Slot " + slotName + " missing", this, table415);
            }
        }
        else if (!(vc.isXDM || vc.isXDRLimited)) {
            for (String slotName : DocumentEntryModel.requiredSlots) {
                if (model.getSlot(slotName) == null)
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": Slot " + slotName + " missing", this, table415);
            }
        }

        //  Optional Slots required by this transaction
        if (vc.hashRequired() && model.getSlot("hash") == null)
            er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": Slot hash required in this context", this, table415);

        if (vc.sizeRequired() && model.getSlot("size") == null)
            er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": Slot size required in this context", this, table415);

        if (vc.repositoryUniqueIdRequired() && model.getSlot("repositoryUniqueId") == null)
            er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": Slot repositoryUniqueId required in this context", this, table415);

        if (vc.uriRequired() && model.getSlot("URI") == null)
            er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": Slot URI required in this context", this, table415);

    }

    /**
     * Validate all slots present are legal for DocumentEntry
     * @param er
     */

    @Override
    public void validateSlotsLegal(ErrorRecorder er)  {
        new SlotsUniqueValidator(simHandle, model.slots).asSelf(this).run()
        for (SlotModel slot : model.getSlots()) {
            if ( ! legal_slot_name(slot.getName()))
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": " + slot.getName() + " is not a legal slot name for a DocumentEntry",   this, table415);

        }
    }

    boolean legal_slot_name(String name) {
        if (name == null) return false;
        if (name.startsWith("urn:")) return true;
        return DocumentEntryModel.definedSlots.contains(name);
    }

    @Override
    public void validateSlotsCodedCorrectly(ErrorRecorder er, ValidationContext vc)  {

        //                    name				   multi	format                                                  resource
        validateSlot(model, er, 	"creationTime", 	   false, 	new DtmFormatValidator(er, "Slot creationTime",      table415),  table415);
        validateSlot(model, er, 	"languageCode",		   false, 	new Rfc3066FormatValidator(er, "Slot languageCode",      table415),  table415);
        validateSlot(model, er, 	"legalAuthenticator",  false, 	new XcnFormatValidator(er, "Slot legalAuthenticator",table415),  table415);
        validateSlot(model, er, 	"serviceStartTime",	   false, 	new DtmFormatValidator(er, "Slot serviceStartTime",  table415),  table415);
        validateSlot(model, er, 	"serviceStopTime",	   false, 	new DtmFormatValidator(er, "Slot serviceStopTime",   table415),  table415);
        validateSlot(model, er, 	"sourcePatientInfo",   true, 	new SourcePatientInfoFormatValidator(er, "Slot sourcePatientInfo", table415),  table415);
        validateSlot(model, er, 	"sourcePatientId",     false, 	new CxFormatValidator(er, "Slot sourcePatientId",   table415),  table415);
        validateSlot(model, er, 	"hash",			 	   false, 	new HashFormatValidator(er, "Slot hash",   null), 		        table415);
        validateSlot(model, er, 	"size",				   false, 	new IntFormatValidator(er, "Slot size",   table415),             table415);
        validateSlot(model, er, 	"URI",				   true, 	new AnyFormatValidator(er, "Slot URI",   table415),   table415);
        validateSlot(model, er, 	"repositoryUniqueId",	false, 	new OidFormatValidator(er, "Slot repositoryUniqueId",   table415),   table415);


        if ( model.getSlot("URI") != null ) {
            try {
                model.m.getURIAttribute(model.ro, !vc.isXDM);
            } catch (MetadataException e) {
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, "Slot URI: " + e.getMessage(), this, table415);
            }
        }

        SlotModel docAvail = model.getSlot("documentAvailability");
        if (docAvail != null) {
            if (docAvail.getValues().size() > 1)
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, "Slot documentAvailability shall have a single value", this, table415);
            String val;
            try {
                val = docAvail.getValue(0);
                if (MetadataSupport.documentAvailability_offline.equals(val)   ||
                        MetadataSupport.documentAvailability_online.equals(val)) {

                } else {
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, "Slot documentAvailability must have one of two values: " + MetadataSupport.documentAvailability_offline + " or " +
                                    MetadataSupport.documentAvailability_online + ". Found instead " + val, this, table415
                    );
                }
            } catch (Exception e) {
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, e);
            }
        }
    }

    public void validateTopAtts(ErrorRecorder er, ValidationContext vc) {
        if (!MetadataSupport.XDSDocumentEntry_objectType_uuid.equals(model.objectType))
            er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": objectType must be " + MetadataSupport.XDSDocumentEntry_objectType_uuid + " (found " + model.objectType + ")", this, table415);

        if (model.mimeType == null || model.mimeType.equals(""))
            er.err(XdsErrorCode.Code.XDSRegistryMetadataError, model.identifyingString() + ": mimeType attribute missing or empty", this, table415);

        new TopAttsValidator(simHandle, model, vc, DocumentEntryModel.statusValues)

    }

    public void validateSlots(ErrorRecorder er, ValidationContext vc) {
        er.challenge("Validating that Slots present are legal");
        validateSlotsLegal(er);
        er.challenge("Validating required Slots present");
        validateRequiredSlotsPresent(er, vc);
        er.challenge("Validating Slots are coded correctly");
        validateSlotsCodedCorrectly(er, vc);
    }

}
