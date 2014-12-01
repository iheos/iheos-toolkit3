package gov.nist.hit.ds.dsSims.eb.metadataValidator.object

import gov.nist.hit.ds.dsSims.eb.metadata.Metadata
import gov.nist.hit.ds.dsSims.eb.metadata.MetadataSupport
import gov.nist.hit.ds.xdsException.XdsInternalException
import groovy.transform.ToString
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 8/18/14.
 */
@groovy.transform.TypeChecked
@ToString(includeNames=true,includeSuper=true)
class SubmissionSetModel extends RegistryObjectModel {
    static List<String> statusValues =
            Arrays.asList(
                    MetadataSupport.status_type_namespace + "Approved"
            );

    static List<String> definedSlots =
            Arrays.asList(
                    "intendedRecipient",
                    "submissionTime"
            );

    static List<String> requiredSlots =
            Arrays.asList(
                    "submissionTime"
            );

    static List<String> requiredSlotsMinimal =
            Arrays.asList(
                    "intendedRecipient"
            );

    static public ClassAndIdDescription classificationDescription = new ClassAndIdDescription();
    static {
        classificationDescription.definedSchemes =
                Arrays.asList(
                        MetadataSupport.XDSSubmissionSet_contentTypeCode_uuid ,
                        MetadataSupport.XDSSubmissionSet_author_uuid
                );
        classificationDescription.requiredSchemes =
                Arrays.asList(
                        MetadataSupport.XDSSubmissionSet_contentTypeCode_uuid
                );
        classificationDescription.multipleSchemes =
                Arrays.asList(
                        MetadataSupport.XDSSubmissionSet_author_uuid
                );
        classificationDescription.names = new HashMap<String, String>();
        classificationDescription.names.put(MetadataSupport.XDSSubmissionSet_contentTypeCode_uuid, "Content Type Code");
        classificationDescription.names.put(MetadataSupport.XDSSubmissionSet_author_uuid, "Author");
    }

    static public ClassAndIdDescription XDMclassificationDescription = new ClassAndIdDescription();
    static {
        XDMclassificationDescription.definedSchemes =
                Arrays.asList(
                        MetadataSupport.XDSSubmissionSet_contentTypeCode_uuid ,
                        MetadataSupport.XDSSubmissionSet_author_uuid
                );
        XDMclassificationDescription.requiredSchemes =
                Arrays.asList(
                );
        XDMclassificationDescription.multipleSchemes =
                Arrays.asList(
                        MetadataSupport.XDSSubmissionSet_author_uuid
                );
        XDMclassificationDescription.names = new HashMap<String, String>();
        XDMclassificationDescription.names.put(MetadataSupport.XDSSubmissionSet_contentTypeCode_uuid, "Content Type Code");
        XDMclassificationDescription.names.put(MetadataSupport.XDSSubmissionSet_author_uuid, "Author");
    }

    static public ClassAndIdDescription MinimalclassificationDescription = new ClassAndIdDescription();
    static {
        MinimalclassificationDescription.definedSchemes =
                Arrays.asList(
                        MetadataSupport.XDSSubmissionSet_contentTypeCode_uuid ,
                        MetadataSupport.XDSSubmissionSet_author_uuid
                );
        MinimalclassificationDescription.requiredSchemes =
                Arrays.asList(
                );
        MinimalclassificationDescription.multipleSchemes =
                Arrays.asList(
                        MetadataSupport.XDSSubmissionSet_author_uuid
                );
        MinimalclassificationDescription.names = new HashMap<String, String>();
        //MinimalclassificationDescription.names.put(MetadataSupport.XDSSubmissionSet_contentTypeCode_uuid, "Content Type Code");
        MinimalclassificationDescription.names.put(MetadataSupport.XDSSubmissionSet_author_uuid, "Author");
    }

    static public ClassAndIdDescription externalIdentifierDescription = new ClassAndIdDescription();
    static {
        externalIdentifierDescription.definedSchemes =
                Arrays.asList(
                        MetadataSupport.XDSSubmissionSet_patientid_uuid,
                        MetadataSupport.XDSSubmissionSet_uniqueid_uuid,
                        MetadataSupport.XDSSubmissionSet_sourceid_uuid
                );

        externalIdentifierDescription.requiredSchemes =
                Arrays.asList(
                        MetadataSupport.XDSSubmissionSet_patientid_uuid,
                        MetadataSupport.XDSSubmissionSet_uniqueid_uuid,
                        MetadataSupport.XDSSubmissionSet_sourceid_uuid
                );
        externalIdentifierDescription.multipleSchemes = new ArrayList<String>();

        externalIdentifierDescription.names = new HashMap<String, String>();
        externalIdentifierDescription.names.put(MetadataSupport.XDSSubmissionSet_patientid_uuid, "Patient ID");
        externalIdentifierDescription.names.put(MetadataSupport.XDSSubmissionSet_uniqueid_uuid, "Unique ID");
        externalIdentifierDescription.names.put(MetadataSupport.XDSSubmissionSet_sourceid_uuid, "Source ID");
    }

    static public ClassAndIdDescription XDMexternalIdentifierDescription = new ClassAndIdDescription();
    static {
        XDMexternalIdentifierDescription.definedSchemes =
                Arrays.asList(
                        MetadataSupport.XDSSubmissionSet_patientid_uuid,
                        MetadataSupport.XDSSubmissionSet_uniqueid_uuid,
                        MetadataSupport.XDSSubmissionSet_sourceid_uuid
                );

        XDMexternalIdentifierDescription.requiredSchemes =
                Arrays.asList(
                        MetadataSupport.XDSSubmissionSet_uniqueid_uuid,
                        MetadataSupport.XDSSubmissionSet_sourceid_uuid
                );
        XDMexternalIdentifierDescription.multipleSchemes = new ArrayList<String>();

        XDMexternalIdentifierDescription.names = new HashMap<String, String>();
        XDMexternalIdentifierDescription.names.put(MetadataSupport.XDSSubmissionSet_patientid_uuid, "Patient ID");
        XDMexternalIdentifierDescription.names.put(MetadataSupport.XDSSubmissionSet_uniqueid_uuid, "Unique ID");
        XDMexternalIdentifierDescription.names.put(MetadataSupport.XDSSubmissionSet_sourceid_uuid, "Source ID");
    }

    static public ClassAndIdDescription MinimalexternalIdentifierDescription = new ClassAndIdDescription();
    static {
        MinimalexternalIdentifierDescription.definedSchemes =
                Arrays.asList(
                        MetadataSupport.XDSSubmissionSet_patientid_uuid,
                        MetadataSupport.XDSSubmissionSet_uniqueid_uuid,
                        MetadataSupport.XDSSubmissionSet_sourceid_uuid
                );

        MinimalexternalIdentifierDescription.requiredSchemes =
                Arrays.asList(
                        MetadataSupport.XDSSubmissionSet_uniqueid_uuid,
                        MetadataSupport.XDSSubmissionSet_sourceid_uuid
                );
        MinimalexternalIdentifierDescription.multipleSchemes = new ArrayList<String>();

        MinimalexternalIdentifierDescription.names = new HashMap<String, String>();
        //MinimalexternalIdentifierDescription.names.put(MetadataSupport.XDSSubmissionSet_patientid_uuid, "Patient ID");
        MinimalexternalIdentifierDescription.names.put(MetadataSupport.XDSSubmissionSet_uniqueid_uuid, "Unique ID");
        MinimalexternalIdentifierDescription.names.put(MetadataSupport.XDSSubmissionSet_sourceid_uuid, "Source ID");
    }

    public SubmissionSetModel(Metadata m, OMElement ro) throws XdsInternalException  {
        super(m, ro);
    }

    public SubmissionSetModel(String id) {
        super(id);
        internalClassifications.add(new InternalClassificationModel("cl" + id, id, MetadataSupport.XDSSubmissionSet_classification_uuid));
    }

    public boolean equals(SubmissionSetValidator s)  {
        if (!id.equals(id))
            return false;
        return	super.equals(s);
    }

    public String identifyingString() {
        return "SubmissionSet(" + getId() + ")";
    }

    public boolean isMetadataLimited() {
        return isClassifiedAs(MetadataSupport.XDSSubmissionSet_limitedMetadata_uuid);
    }


}
