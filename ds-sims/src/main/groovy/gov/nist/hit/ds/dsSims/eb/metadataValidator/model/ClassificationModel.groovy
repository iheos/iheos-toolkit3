package gov.nist.hit.ds.dsSims.eb.metadataValidator.model

import gov.nist.hit.ds.ebMetadata.Metadata
import gov.nist.hit.ds.ebMetadata.MetadataSupport
import gov.nist.hit.ds.xdsException.XdsInternalException
import org.apache.axiom.om.OMElement

public class ClassificationModel extends AbstractRegistryObjectModel {
	String classification_scheme = "";
	String code_value = "";
	String classification_node = "";

	public ClassificationModel(String id, String classificationScheme, String code, String codingScheme,
                               String displayName) {
		super(id);
		classification_scheme = classificationScheme;
		code_value = code;
		addSlot("codingScheme", codingScheme);
		name = displayName;
	}

	public boolean equals(ClassificationModel c) {
		if (!c.classification_scheme.equals(classification_scheme)) 
			return false;
		if (!c.code_value.equals(code_value)) 
			return false;
		if (!c.name.equals(name)) 
			return false;
		if (!c.classification_node.equals(classification_node))
			return false;
		return super.equals(c);
	}

	public OMElement toXml(OMElement parent) throws XdsInternalException {
		ro = MetadataSupport.om_factory.createOMElement(MetadataSupport.classification_qnamens);
		ro.addAttribute(MetadataSupport.id_qname.getLocalPart(), id, null);
		ro.addAttribute(MetadataSupport.classificationscheme_qname.getLocalPart(), classification_scheme, null);
		ro.addAttribute(MetadataSupport.classified_object_qname.getLocalPart(), parent.getAttributeValue(MetadataSupport.id_qname), null);
		ro.addAttribute(MetadataSupport.noderepresentation_qname.getLocalPart(), code_value, null);

		addSlotsXml(ro);
		addNameToXml(ro);
		addDescriptionXml(ro);
		addClassificationsXml(ro);
		addExternalIdentifiersXml(ro);

		return ro;
	}

	public ClassificationModel(Metadata m, OMElement cl)   {
		super(m, cl);
		parse();
	}

	void parse()  {
		classification_scheme = ro.getAttributeValue(MetadataSupport.classificationscheme_qname);
		classification_node = ro.getAttributeValue(MetadataSupport.classificationnode_qname);
		if (classification_node == null) classification_node = "";
		code_value = ro.getAttributeValue(MetadataSupport.noderepresentation_qname);
	}

	public String getCodeValue() { return code_value; }
	public String getCodeDisplayName() { return name; }
	public String getCodeScheme()  {
		try {
			return getSlot("codingScheme").getValue(0);
		} catch (Exception e) {
			return "";
		}
	}
	public String getClassificationScheme() { return classification_scheme; }
	public String getClassificationNode() { return classification_node; }

	public String identifyingString() {
		return identifying_string();
	}

	public String identifying_string() {
		String cs = "";
		try {
			cs = getCodeScheme();
		} catch (Exception e) {}
		return "Classification (classificationScheme=" + classification_scheme + " codingScheme=" + cs + ") of object " + parent_id(); 
	}

	public String parent_id() {
		OMElement parent = (OMElement) ro.getParent();
		if (parent == null) return "Unknown";
		return parent.getAttributeValue(MetadataSupport.id_qname);
	}

	public OMElement toXml() throws XdsInternalException  {
		return toXml(null);
	}




}
