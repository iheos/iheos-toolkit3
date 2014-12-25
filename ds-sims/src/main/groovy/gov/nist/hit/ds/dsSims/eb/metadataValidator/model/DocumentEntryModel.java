package gov.nist.hit.ds.dsSims.eb.metadataValidator.model;

import gov.nist.hit.ds.dsSims.eb.metadata.Metadata;
import gov.nist.hit.ds.dsSims.eb.metadata.MetadataSupport;
import gov.nist.hit.ds.xdsException.XdsInternalException;
import org.apache.axiom.om.OMElement;

import java.util.*;

public class DocumentEntryModel extends AbstractRegistryObjectModel {
    public static List<String> definedSlots =
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

    public static List<String> requiredSlots =
		Arrays.asList(
				"creationTime",
				"languageCode",
				"sourcePatientId"
		);

    public static List<String> directRequiredSlots =
			Arrays.asList(
			);


    public static ClassAndIdDescription classificationDescription = new ClassAndIdDescription();
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

	public static List<String> statusValues =
		Arrays.asList(
				MetadataSupport.status_type_namespace + "Approved",
				MetadataSupport.status_type_namespace + "Deprecated"
		);

	public String mimeType = "";
	public String objectType = "";

	public DocumentEntryModel(String id, String mimeType) {
		super(id);
		this.mimeType = mimeType;
		this.objectType = MetadataSupport.XDSDocumentEntry_objectType_uuid;
	}

	public DocumentEntryModel(Metadata m, OMElement de) throws XdsInternalException {
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

	public OMElement toXml() throws XdsInternalException  {
		ro = MetadataSupport.om_factory.createOMElement(MetadataSupport.extrinsicobject_qnamens);
		ro.addAttribute("id", id, null);
		ro.addAttribute("mimeType", mimeType, null);
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

	public String identifyingString() {
		return "DocumentEntry(" + getId() + ")";
	}
	
	public boolean isMetadataLimited() {
		return isClassifiedAs(MetadataSupport.XDSDocumentEntry_limitedMetadata_uuid);
	}



}
