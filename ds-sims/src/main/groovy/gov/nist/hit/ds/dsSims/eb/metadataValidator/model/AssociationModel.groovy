package gov.nist.hit.ds.dsSims.eb.metadataValidator.model

import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.ebMetadata.Metadata
import gov.nist.hit.ds.ebMetadata.MetadataSupport
import gov.nist.hit.ds.xdsExceptions.XdsInternalException
import org.apache.axiom.om.OMElement

public class AssociationModel extends AbstractRegistryObjectModel {
	public String source = "";
    public String target = "";
    public String type = "";
    public ValidationContext vc;

    public static List<String> assocTypes =
		Arrays.asList(
				MetadataSupport.assoctype_has_member,
				MetadataSupport.assoctype_rplc,
				MetadataSupport.assoctype_xfrm,
				MetadataSupport.assoctype_apnd,
				MetadataSupport.assoctype_xfrm_rplc,
				MetadataSupport.assoctype_signs,
				MetadataSupport.assoctype_isSnapshotOf
		);

    public static List<String> assocTypesMU =
		Arrays.asList(
				MetadataSupport.assoctype_update_availabilityStatus,
				MetadataSupport.assoctype_submitAssociation
				);

    public static ClassAndIdDescription externalIdentifierDescription = new ClassAndIdDescription();
	static {
		externalIdentifierDescription.definedSchemes = new ArrayList<String>();

		externalIdentifierDescription.requiredSchemes = new ArrayList<String>();
		externalIdentifierDescription.multipleSchemes = new ArrayList<String>(); 

		externalIdentifierDescription.names = new HashMap<String, String>();
	}


	public AssociationModel(Metadata m, OMElement ro, ValidationContext vc) throws XdsInternalException {
		super(m, ro);
		source = ro.getAttributeValue(MetadataSupport.source_object_qname);
		target = ro.getAttributeValue(MetadataSupport.target_object_qname);
		type = ro.getAttributeValue(MetadataSupport.association_type_qname);
		normalize();
		this.vc = vc;
	}
	
	public AssociationModel(String id, String type, String source, String target) {
		super(id);
		this.type = type;
		this.source = source;
		this.target = target;
		normalize();
	}
	
	void normalize() {
		if (source == null) source="";
		if (target == null) target = "";
		if (type == null) type = "";
	}

    String getSimpleType() {
        String[] parts = type.split(':')
        if (!parts || parts.size() == 0) return type
        return parts[parts.size()-1]
    }

	public String identifyingString() {
		return "Association-${simpleType}-${getId()}"
	}

	public boolean equals(AssociationModel a) {
		if (!id.equals(a.id))
			return false;
		if (!source.equals(a.source))
			return false;
		if (!target.equals(a.target))
			return false;
		if (!type.equals(a.type))
			return false;
		return super.equals(a);

	}

	public OMElement toXml() throws XdsInternalException  {
		ro = MetadataSupport.om_factory.createOMElement(MetadataSupport.association_qnamens);
		ro.addAttribute("id", id, null);
		ro.addAttribute("sourceObject", source, null);
		ro.addAttribute("targetObject", target, null);
		ro.addAttribute("associationType", type, null);
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
