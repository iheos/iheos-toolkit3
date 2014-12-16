package gov.nist.hit.ds.dsSims.eb.metadataValidator.object

import gov.nist.hit.ds.dsSims.eb.metadata.Metadata
import gov.nist.hit.ds.dsSims.eb.metadata.MetadataSupport
import gov.nist.hit.ds.xdsException.XdsInternalException
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 8/18/14.
 */
@groovy.transform.TypeChecked
class FolderModel extends RegistryObjectModel {
    static List<String> statusValues =
            Arrays.asList(
                    MetadataSupport.status_type_namespace + "Approved"
            );

    static List<String> definedSlots =
            Arrays.asList(
                    "lastUpdateTime"
            );

    static List<String> requiredSlots = new ArrayList<String>();

    static public ClassAndIdDescription classificationDescription = new ClassAndIdDescription();
    static {
        classificationDescription.definedSchemes =
                Arrays.asList(
                        MetadataSupport.XDSFolder_codeList_uuid
                );
        classificationDescription.requiredSchemes =
                Arrays.asList(
                        MetadataSupport.XDSFolder_codeList_uuid
                );
        classificationDescription.multipleSchemes =
                Arrays.asList(
                        MetadataSupport.XDSFolder_codeList_uuid
                );
        classificationDescription.names = new HashMap<String, String>();
        classificationDescription.names.put(MetadataSupport.XDSFolder_codeList_uuid, "Code List");
    }

    static public ClassAndIdDescription XDMclassificationDescription = new ClassAndIdDescription();
    static {
        XDMclassificationDescription.definedSchemes =
                Arrays.asList(
                        MetadataSupport.XDSFolder_codeList_uuid
                );
        XDMclassificationDescription.requiredSchemes =
                Arrays.asList(
                );
        XDMclassificationDescription.multipleSchemes =
                Arrays.asList(
                        MetadataSupport.XDSFolder_codeList_uuid
                );
        XDMclassificationDescription.names = new HashMap<String, String>();
        XDMclassificationDescription.names.put(MetadataSupport.XDSFolder_codeList_uuid, "Code List");
    }

    static public ClassAndIdDescription externalIdentifierDescription = new ClassAndIdDescription();
    static {
        externalIdentifierDescription.definedSchemes =
                Arrays.asList(
                        MetadataSupport.XDSFolder_patientid_uuid,
                        MetadataSupport.XDSFolder_uniqueid_uuid
                );

        externalIdentifierDescription.requiredSchemes =
                Arrays.asList(
                        MetadataSupport.XDSFolder_patientid_uuid,
                        MetadataSupport.XDSFolder_uniqueid_uuid
                );
        externalIdentifierDescription.multipleSchemes = new ArrayList<String>();

        externalIdentifierDescription.names = new HashMap<String, String>();
        externalIdentifierDescription.names.put(MetadataSupport.XDSFolder_patientid_uuid, "Patient ID");
        externalIdentifierDescription.names.put(MetadataSupport.XDSFolder_uniqueid_uuid, "Unique ID");
    }

    static public ClassAndIdDescription XDMexternalIdentifierDescription = new ClassAndIdDescription();
    static {
        XDMexternalIdentifierDescription.definedSchemes =
                Arrays.asList(
                        MetadataSupport.XDSFolder_patientid_uuid,
                        MetadataSupport.XDSFolder_uniqueid_uuid
                );

        XDMexternalIdentifierDescription.requiredSchemes =
                Arrays.asList(
                        MetadataSupport.XDSFolder_uniqueid_uuid
                );
        XDMexternalIdentifierDescription.multipleSchemes = new ArrayList<String>();

        XDMexternalIdentifierDescription.names = new HashMap<String, String>();
        XDMexternalIdentifierDescription.names.put(MetadataSupport.XDSFolder_patientid_uuid, "Patient ID");
        XDMexternalIdentifierDescription.names.put(MetadataSupport.XDSFolder_uniqueid_uuid, "Unique ID");
    }

    public FolderModel(Metadata m, OMElement ro) throws XdsInternalException  {
        super(m, ro);
    }

    public FolderModel(String id) {
        super(id);
        internalClassifications.add(new InternalClassificationModel("cl" + id, id, MetadataSupport.XDSFolder_classification_uuid));

    }

    public boolean isMetadataLimited() {
        return isClassifiedAs(MetadataSupport.XDSFolder_limitedMetadata_uuid);
    }

    public String identifyingString() {
        return "Folder(" + getId() + ")";
    }

    public boolean equals(FolderValidator f)  {
        if (!id.equals(id))
            return false;
        return	super.equals(f);
    }
}
