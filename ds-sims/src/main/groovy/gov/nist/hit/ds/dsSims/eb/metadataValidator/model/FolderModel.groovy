package gov.nist.hit.ds.dsSims.eb.metadataValidator.model

import gov.nist.hit.ds.ebMetadata.Metadata
import gov.nist.hit.ds.ebMetadata.MetadataSupport
import gov.nist.hit.ds.xdsException.XdsInternalException
import org.apache.axiom.om.OMElement

public class FolderModel extends AbstractRegistryObjectModel {

	public static List<String> statusValues =
		Arrays.asList(
				MetadataSupport.status_type_namespace + "Approved"
		);

	public static List<String> definedSlots =
		Arrays.asList(
				"lastUpdateTime"
		);

	public static List<String> requiredSlots = new ArrayList<String>();

	public static ClassAndIdDescription classificationDescription = new ClassAndIdDescription();
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
	
	public static ClassAndIdDescription XDMclassificationDescription = new ClassAndIdDescription();
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

	static public String table417 = "ITI TF-3: Table 4.1-7";

	public String identifyingString() {
		return "Folder(" + getId() + ")";	
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



}
