package gov.nist.hit.ds.dsSims.eb.metadataValidator.object

import gov.nist.hit.ds.dsSims.eb.metadata.Metadata
import gov.nist.hit.ds.dsSims.eb.metadata.MetadataSupport
import gov.nist.hit.ds.xdsExceptions.XdsInternalException
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 8/18/14.
 */
@groovy.transform.TypeChecked
class DocumentEntryModel extends RegistryObjectModel {
    static List<String> definedSlots =
            Arrays.asList(
                    "creationTime",
                    "languageCode",
                    "sourcePatientId",
                    "sourcePatientInfo",
                    "legalAuthenticator",
                    "serviceStartTime",
                    "serviceStopTime",
                    "hash",
                    "size",
                    "URI",
                    "repositoryUniqueId",
                    "documentAvailability"
            );

    static List<String> requiredSlots =
            Arrays.asList(
                    "creationTime",
                    "languageCode",
                    "sourcePatientId"
            );

    static List<String> directRequiredSlots =
            Arrays.asList(
            );


    static public ClassAndIdDescription classificationDescription = new ClassAndIdDescription();
    static {
        classificationDescription.definedSchemes =
                Arrays.asList(
                        MetadataSupport.XDSDocumentEntry_classCode_uuid ,
                        MetadataSupport.XDSDocumentEntry_confCode_uuid ,
                        MetadataSupport.XDSDocumentEntry_eventCode_uuid ,
                        MetadataSupport.XDSDocumentEntry_formatCode_uuid ,
                        MetadataSupport.XDSDocumentEntry_hcftCode_uuid ,
                        MetadataSupport.XDSDocumentEntry_psCode_uuid ,
                        MetadataSupport.XDSDocumentEntry_typeCode_uuid,
                        MetadataSupport.XDSDocumentEntry_author_uuid
                );
        classificationDescription.requiredSchemes =
                Arrays.asList(
                        MetadataSupport.XDSDocumentEntry_classCode_uuid,
                        MetadataSupport.XDSDocumentEntry_confCode_uuid,
                        MetadataSupport.XDSDocumentEntry_formatCode_uuid,
                        MetadataSupport.XDSDocumentEntry_hcftCode_uuid,
                        MetadataSupport.XDSDocumentEntry_psCode_uuid,
                        MetadataSupport.XDSDocumentEntry_typeCode_uuid
                );
        classificationDescription.multipleSchemes =
                Arrays.asList(
                        MetadataSupport.XDSDocumentEntry_author_uuid,
                        MetadataSupport.XDSDocumentEntry_confCode_uuid,
                        MetadataSupport.XDSDocumentEntry_eventCode_uuid
                );
        classificationDescription.names = new HashMap<String, String>();
        classificationDescription.names.put(MetadataSupport.XDSDocumentEntry_classCode_uuid, "Class Code");
        classificationDescription.names.put(MetadataSupport.XDSDocumentEntry_confCode_uuid, "Confidentiality Code");
        classificationDescription.names.put(MetadataSupport.XDSDocumentEntry_eventCode_uuid, "Event Codelist");
        classificationDescription.names.put(MetadataSupport.XDSDocumentEntry_formatCode_uuid, "Format Code");
        classificationDescription.names.put(MetadataSupport.XDSDocumentEntry_hcftCode_uuid, "Healthcare Facility Type Code");
        classificationDescription.names.put(MetadataSupport.XDSDocumentEntry_psCode_uuid, "Practice Setting Code");
        classificationDescription.names.put(MetadataSupport.XDSDocumentEntry_typeCode_uuid, "Type Code");
        classificationDescription.names.put(MetadataSupport.XDSDocumentEntry_author_uuid, "Author");
    }

    static public ClassAndIdDescription directClassificationDescription = new ClassAndIdDescription();
    static {
        directClassificationDescription.definedSchemes =
                Arrays.asList(
                        MetadataSupport.XDSDocumentEntry_classCode_uuid ,
                        MetadataSupport.XDSDocumentEntry_confCode_uuid ,
                        MetadataSupport.XDSDocumentEntry_eventCode_uuid ,
                        MetadataSupport.XDSDocumentEntry_formatCode_uuid ,
                        MetadataSupport.XDSDocumentEntry_hcftCode_uuid ,
                        MetadataSupport.XDSDocumentEntry_psCode_uuid ,
                        MetadataSupport.XDSDocumentEntry_typeCode_uuid,
                        MetadataSupport.XDSDocumentEntry_author_uuid
                );
        directClassificationDescription.requiredSchemes =
                Arrays.asList(
                );
        directClassificationDescription.multipleSchemes =
                Arrays.asList(
                        MetadataSupport.XDSDocumentEntry_author_uuid,
                        MetadataSupport.XDSDocumentEntry_confCode_uuid,
                        MetadataSupport.XDSDocumentEntry_eventCode_uuid
                );
        directClassificationDescription.names = new HashMap<String, String>();
        directClassificationDescription.names.put(MetadataSupport.XDSDocumentEntry_classCode_uuid, "Class Code");
        directClassificationDescription.names.put(MetadataSupport.XDSDocumentEntry_confCode_uuid, "Confidentiality Code");
        directClassificationDescription.names.put(MetadataSupport.XDSDocumentEntry_eventCode_uuid, "Event Codelist");
        directClassificationDescription.names.put(MetadataSupport.XDSDocumentEntry_formatCode_uuid, "Format Code");
        directClassificationDescription.names.put(MetadataSupport.XDSDocumentEntry_hcftCode_uuid, "Healthcare Facility Type Code");
        directClassificationDescription.names.put(MetadataSupport.XDSDocumentEntry_psCode_uuid, "Practice Setting Code");
        directClassificationDescription.names.put(MetadataSupport.XDSDocumentEntry_typeCode_uuid, "Type Code");
        directClassificationDescription.names.put(MetadataSupport.XDSDocumentEntry_author_uuid, "Author");
    }

    static public ClassAndIdDescription externalIdentifierDescription = new ClassAndIdDescription();
    static {
        externalIdentifierDescription.definedSchemes =
                Arrays.asList(
                        MetadataSupport.XDSDocumentEntry_patientid_uuid,
                        MetadataSupport.XDSDocumentEntry_uniqueid_uuid
                );

        externalIdentifierDescription.requiredSchemes =
                Arrays.asList(
                        MetadataSupport.XDSDocumentEntry_patientid_uuid,
                        MetadataSupport.XDSDocumentEntry_uniqueid_uuid
                );
        externalIdentifierDescription.multipleSchemes = new ArrayList<String>();

        externalIdentifierDescription.names = new HashMap<String, String>();
        externalIdentifierDescription.names.put(MetadataSupport.XDSDocumentEntry_patientid_uuid, "Patient ID");
        externalIdentifierDescription.names.put(MetadataSupport.XDSDocumentEntry_uniqueid_uuid, "Unique ID");
    }

    static public ClassAndIdDescription XDMexternalIdentifierDescription = new ClassAndIdDescription();
    static {
        XDMexternalIdentifierDescription.definedSchemes =
                Arrays.asList(
                        MetadataSupport.XDSDocumentEntry_patientid_uuid,
                        MetadataSupport.XDSDocumentEntry_uniqueid_uuid
                );

        XDMexternalIdentifierDescription.requiredSchemes =
                Arrays.asList(
                        MetadataSupport.XDSDocumentEntry_uniqueid_uuid
                );
        XDMexternalIdentifierDescription.multipleSchemes = new ArrayList<String>();

        XDMexternalIdentifierDescription.names = new HashMap<String, String>();
        XDMexternalIdentifierDescription.names.put(MetadataSupport.XDSDocumentEntry_patientid_uuid, "Patient ID");
        XDMexternalIdentifierDescription.names.put(MetadataSupport.XDSDocumentEntry_uniqueid_uuid, "Unique ID");
    }

    static public ClassAndIdDescription directExternalIdentifierDescription = new ClassAndIdDescription();
    static {
        directExternalIdentifierDescription.definedSchemes =
                Arrays.asList(
                        MetadataSupport.XDSDocumentEntry_patientid_uuid,
                        MetadataSupport.XDSDocumentEntry_uniqueid_uuid
                );

        directExternalIdentifierDescription.requiredSchemes =
                Arrays.asList(
                        MetadataSupport.XDSDocumentEntry_uniqueid_uuid
                );
        directExternalIdentifierDescription.multipleSchemes = new ArrayList<String>();

        directExternalIdentifierDescription.names = new HashMap<String, String>();
        directExternalIdentifierDescription.names.put(MetadataSupport.XDSDocumentEntry_patientid_uuid, "Patient ID");
        directExternalIdentifierDescription.names.put(MetadataSupport.XDSDocumentEntry_uniqueid_uuid, "Unique ID");
    }

    static List<String> statusValues =
            Arrays.asList(
                    MetadataSupport.status_type_namespace + "Approved",
                    MetadataSupport.status_type_namespace + "Deprecated"
            );

    String mimeType = "";
    String objectType = "";

    public DocumentEntryModel(String id, String mimeType) {
        super(id);
        this.mimeType = mimeType;
        this.objectType = MetadataSupport.XDSDocumentEntry_objectType_uuid;
    }

    public DocumentEntryModel(Metadata m, OMElement de) throws XdsInternalException  {
        super(m, de);
        mimeType = de.getAttributeValue(MetadataSupport.mime_type_qname);
        objectType = de.getAttributeValue(MetadataSupport.object_type_qname);
    }

    public boolean equals(DocumentEntryModel d) {
        if (!d.mimeType.equals(mimeType))
            return false;
        if (!id.equals(d.id))
            return false;
        return super.equals(d);
    }

    public String identifyingString() {
        return "DocumentEntry(" + getId() + ")";
    }

    public boolean isMetadataLimited() {
        return isClassifiedAs(MetadataSupport.XDSDocumentEntry_limitedMetadata_uuid);
    }

    String entryUUID() { id }

    ExternalIdentifierModel uniqueId() {
        externalIdentifiers.find { ExternalIdentifierModel it -> it.identificationScheme == MetadataSupport.XDSDocumentEntry_uniqueid_uuid}
    }

    ExternalIdentifierModel patientId() {
        externalIdentifiers.find { ExternalIdentifierModel it -> it.identificationScheme == MetadataSupport.XDSDocumentEntry_patientid_uuid}
    }

    Collection<ClassificationModel> authors() {
        classifications.findAll { ClassificationModel it -> it.classification_scheme == MetadataSupport.XDSDocumentEntry_author_uuid}
    }

    ClassificationModel classCode() {
        classifications.find { ClassificationModel it -> it.classification_scheme ==  MetadataSupport.XDSDocumentEntry_classCode_uuid}
    }

    Collection<ClassificationModel> confCodes() {
        classifications.findAll { ClassificationModel it -> it.classification_scheme ==  MetadataSupport.XDSDocumentEntry_formatCode_uuid}
    }

    Collection<ClassificationModel> eventCodes() {
        classifications.findAll { ClassificationModel it -> it.classification_scheme == MetadataSupport.XDSDocumentEntry_eventCode_uuid}
    }

    ClassificationModel formatCode() {
        classifications.find { ClassificationModel it -> it.classification_scheme == MetadataSupport.XDSDocumentEntry_formatCode_uuid}
    }

    ClassificationModel healthcareFacilityTypeCode() {
        classifications.find { ClassificationModel it -> it.classification_scheme == MetadataSupport.XDSDocumentEntry_hcftCode_uuid}
    }

    ClassificationModel practiceSettingCode() {
        classifications.find { ClassificationModel it -> it.classification_scheme == MetadataSupport.XDSDocumentEntry_psCode_uuid}
    }

    ClassificationModel typeCode() {
        classifications.find { ClassificationModel it -> it.classification_scheme == MetadataSupport.XDSDocumentEntry_typeCode_uuid}
    }

    SlotModel creationTime() {
        slots.find { SlotModel it -> it.name = 'creationTime'}
    }

    SlotModel hash() {
        slots.find { SlotModel it -> it.name = 'hash'}
    }

    SlotModel languageCode() {
        slots.find { SlotModel it -> it.name = 'languageCode'}
    }

    SlotModel legalAuthenticator() {
        slots.find { SlotModel it -> it.name = 'legalAuthenticator'}
    }

    SlotModel repositoryUniqueId() {
        slots.find { SlotModel it -> it.name = 'repositoryUniqueId'}
    }

    SlotModel serviceStartTime() {
        slots.find { SlotModel it -> it.name = 'serviceStartTime'}
    }

    SlotModel serviceStopTime() {
        slots.find { SlotModel it -> it.name = 'serviceStopTime'}
    }

    SlotModel size() {
        slots.find { SlotModel it -> it.name = 'size'}
    }

    SlotModel sourcePatientId() {
        slots.find { SlotModel it -> it.name = 'sourcePatientId'}
    }

    SlotModel sourcePatientInfo() {
        slots.find { SlotModel it -> it.name = 'sourcePatientInfo'}
    }

    SlotModel URI() {
        slots.find { SlotModel it -> it.name = 'URI'}
    }

    String title() { name }

    String availabilityStatus() { status }

    String homeCommunityId() { home }
}
