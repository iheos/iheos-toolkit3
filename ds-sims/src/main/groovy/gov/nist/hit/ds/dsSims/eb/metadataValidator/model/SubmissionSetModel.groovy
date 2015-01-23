package gov.nist.hit.ds.dsSims.eb.metadataValidator.model

import gov.nist.hit.ds.ebMetadata.Metadata
import gov.nist.hit.ds.ebMetadata.MetadataSupport
import gov.nist.hit.ds.xdsExceptions.XdsInternalException
import org.apache.axiom.om.OMElement

public class SubmissionSetModel extends AbstractRegistryObjectModel  {

	public static List<String> statusValues =
			Arrays.asList(
					MetadataSupport.status_type_namespace + "Approved"
					);

    public static List<String> definedSlots =
			Arrays.asList(
					"intendedRecipient",
					"submissionTime"
					);

    public static List<String> requiredSlots =
			Arrays.asList(
					"submissionTime"
					);

    public static List<String> requiredSlotsMinimal =
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



	static public String table416 = "ITI TF-3: Table 4.1-6";

	public SubmissionSetModel(Metadata m, OMElement ro) throws XdsInternalException  {
		super(m, ro);
	}

	public SubmissionSetModel(String id) {
		super(id);
		internalClassifications.add(new InternalClassificationModel("cl" + id, id, MetadataSupport.XDSSubmissionSet_classification_uuid));
	}

	public boolean equals(SubmissionSetModel s)  {
		if (!id.equals(id)) 
			return false;
		return	super.equals(s);
	}

	public String identifyingString() {
		return "SubmissionSet-" + getId();
	}


	public OMElement toXml() throws XdsInternalException {
		ro = MetadataSupport.om_factory.createOMElement(MetadataSupport.registrypackage_qnamens);
		ro.addAttribute("id", id, null);
		if (status != null)
			ro.addAttribute("status", status, null);
		if (home != null)
			ro.addAttribute("home", home, null);

		addSlotsXml(ro);
		addNameToXml(ro);
		addDescriptionXml(ro);
		addClassificationsXml(ro);
		addAuthorsXml(ro);
		addExternalIdentifiersXml(ro);

		return ro;
	}

	public boolean isMetadataLimited() {
		return isClassifiedAs(MetadataSupport.XDSSubmissionSet_limitedMetadata_uuid);
	}


}
