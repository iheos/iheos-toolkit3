package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator
import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.*
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.DocumentEntryModel
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.SlotModel
import gov.nist.hit.ds.ebMetadata.MetadataSupport
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.hit.ds.xdsExceptions.MetadataException
/**
 * Created by bmajur on 12/23/14.
 */
public class DocumentEntryValidator extends AbstractRegistryObjectVal {
    public DocumentEntryModel model;
    ValidationContext vc
    Set<String> knownIds

    DocumentEntryValidator(SimHandle _simHandle, DocumentEntryModel _model, ValidationContext _vc, Set<String> _knownIds) {
        super(_simHandle)
        model = _model
        vc = _vc
        knownIds = _knownIds
    }

    void run() {
        if (vc.skipInternalStructure) return;

        if (vc.isXDR) vc.isXDRLimited = model.isMetadataLimited();

        runValidationEngine()
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rode010', msg='Identify options', ref='')
    def rosde010() {
        if (vc.isXDRLimited)
            infoFound("Labeled as Limited Metadata");

        if (vc.isXDRMinimal)
            infoFound("Labeled as Minimal Metadata (Direct)");
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rode020', msg='Validate DocumentEntry top-level attributes', ref='ITI TF-3: Table 4.1-5')
    def rode020() {
        new TopAttsValidator(simHandle, model, vc, DocumentEntryModel.statusValues)
        if (!MetadataSupport.XDSDocumentEntry_objectType_uuid.equals(model.objectType))
            fail(model.identifyingString() + ": objectType must be " + MetadataSupport.XDSDocumentEntry_objectType_uuid + " (found " + model.objectType + ")")

        if (model.mimeType == null || model.mimeType.equals(""))
            fail(model.identifyingString() + ": mimeType attribute missing or empty")
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rode030', msg='Validating that Slots present are legal', ref='ITI TF-3: Table 4.1-5')
    def rode030() {
        new SlotsUniqueValidator(simHandle, model.slots).asSelf(this).run()
        for (SlotModel slot : model.getSlots()) {
            if ( ! legal_slot_name(slot.getName()))
                fail(model.identifyingString() + ": " + slot.getName() + " is not a legal slot name for a SubmissionSet")
        }
    }

    boolean xdrMinimal() { vc.isXDRMinimal  }
    boolean notXdrMinimal() { !vc.isXDRMinimal }

    @Guard(methodNames=['xdrMinimal'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rode040', msg='Validating required Slots present (XDR Minimal)', ref='ITI TF-3: Table 4.1-5')
    def rode040() {
        for (String slotName : DocumentEntryModel.directRequiredSlots) {
            if (!model.getSlot(slotName))
                fail(model.identifyingString() + ": Slot " + slotName + " missing")
        }
    }

    @Guard(methodNames=['notXdrMinimal'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rode050', msg='Validating required Slots present', ref='ITI TF-3: Table 4.1-5')
    def rode050() {

        for (String slotName : DocumentEntryModel.requiredSlots) {
            if (!model.getSlot(slotName))
                fail(model.identifyingString() + ": Slot " + slotName + " missing");
        }
    }

    @Guard(methodNames=['notXdrMinimal'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rode053', msg='Validating required Slots present', ref='ITI TF-3: Table 4.1-5')
    def rode053() {

        for (String slotName : DocumentEntryModel.requiredSlots) {
            if (!model.getSlot(slotName))
                fail(model.identifyingString() + ": Slot " + slotName + " missing");
        }
    }

    boolean notnot() { !vc.isXDRMinimal && !vc.isXDM && !vc.isXDRLimited }

    @Guard(methodNames=['notnot'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rode055', msg='Validating required Slots present (plain case)', ref='ITI TF-3: Table 4.1-5')
    def rode055() {
        for (String slotName : DocumentEntryModel.requiredSlots) {
            if (model.getSlot(slotName) == null)
                fail(model.identifyingString() + ": Slot " + slotName + " missing")
        }
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rode057', msg='Validating optional Slots', ref='ITI TF-3: Table 4.1-5')
    def rode057() {
        //  Optional Slots required by this transaction
        if (vc.hashRequired() && model.getSlot("hash") == null)
            fail(model.identifyingString() + ": Slot hash required in this context")

        if (vc.sizeRequired() && model.getSlot("size") == null)
            fail(model.identifyingString() + ": Slot size required in this context")

        if (vc.repositoryUniqueIdRequired() && model.getSlot("repositoryUniqueId") == null)
            fail(model.identifyingString() + ": Slot repositoryUniqueId required in this context")

        if (vc.uriRequired() && model.getSlot("URI") == null)
            fail(model.identifyingString() + ": Slot URI required in this context")
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rode060', msg='Validating Slots are coded correctly', ref='ITI TF-3: Table 4.1-5')
    def rode060() {
        //                    name				   multi	format
        validateSlot(model, "creationTime", 	   false, 	new DtmFormatValidator(simHandle, "Slot creationTime"))
        validateSlot(model, "languageCode",		   false, 	new Rfc3066FormatValidator(simHandle, "Slot languageCode"))
        validateSlot(model, "legalAuthenticator",  false, 	new XcnFormatValidator(simHandle, "Slot legalAuthenticator"))
        validateSlot(model, "serviceStartTime",	   false, 	new DtmFormatValidator(simHandle, "Slot serviceStartTime"))
        validateSlot(model, "serviceStopTime",	   false, 	new DtmFormatValidator(simHandle, "Slot serviceStopTime"))
        validateSlot(model, "sourcePatientInfo",   true, 	new SourcePatientInfoFormatValidator(simHandle, "Slot sourcePatientInfo"))
        validateSlot(model, "sourcePatientId",     false, 	new CxFormatValidator(simHandle, "Slot sourcePatientId"))
        validateSlot(model, "hash",			 	   false, 	new HashFormatValidator(simHandle, "Slot hash"))
        validateSlot(model, "size",				   false, 	new IntFormatValidator(simHandle, "Slot size"))
        validateSlot(model, "URI",				   true, 	new AnyFormatValidator(simHandle, "Slot URI"))
        validateSlot(model, "repositoryUniqueId",	false, 	new OidFormatValidator(simHandle, "Slot repositoryUniqueId"))

        if ( model.getSlot("URI") != null ) {
            try {
                model.m.getURIAttribute(model.ro, !vc.isXDM);
            } catch (MetadataException e) {
                fail("Slot URI: " + e.getMessage());
            }
        }

        SlotModel docAvail = model.getSlot("documentAvailability");
        if (docAvail != null) {
            if (docAvail.getValues().size() > 1)
                fail("Slot documentAvailability shall have a single value");
            String val;
            try {
                val = docAvail.getValue(0);
                if (MetadataSupport.documentAvailability_offline.equals(val)   ||
                        MetadataSupport.documentAvailability_online.equals(val)) {

                } else {
                    fail("Slot documentAvailability must have one of two values: " + MetadataSupport.documentAvailability_offline + " or " +
                            MetadataSupport.documentAvailability_online + ". Found instead " + val
                    );
                }
            } catch (Exception e) {
                fail(e.message);
            }
        }
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rode080', msg='Validating Classifications', ref='ITI TF-3: Table 4.1-5')
    def rode080() {
        if (vc.isXDRMinimal)
            new ClassificationValidator(simHandle, model, vc, DocumentEntryModel.directClassificationDescription).asSelf(this).run()
        else
            new ClassificationValidator(simHandle, model, vc, DocumentEntryModel.classificationDescription).asSelf(this).run()
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rode090', msg='Validating ExternalIdentifiers', ref='ITI TF-3: Table 4.1-6')
    def rode090() {
        if (vc.isXDRMinimal)
            new ExternalIdentifierValidator(simHandle, model, vc, DocumentEntryModel.directExternalIdentifierDescription).asSelf(this).run()
        else if (vc.isXDM || vc.isXDRLimited)
            new ExternalIdentifierValidator(simHandle, model, vc, DocumentEntryModel.XDMexternalIdentifierDescription).asSelf(this).run()
        else
            new ExternalIdentifierValidator(simHandle, model, vc, DocumentEntryModel.externalIdentifierDescription).asSelf(this).run()

        new IdUniqueValidator(simHandle, model.id, knownIds).asSelf(this).run()
    }

    boolean legal_slot_name(String name) {
        if (name == null) return false;
        if (name.startsWith("urn:")) return true;
        return DocumentEntryModel.definedSlots.contains(name);
    }
}
