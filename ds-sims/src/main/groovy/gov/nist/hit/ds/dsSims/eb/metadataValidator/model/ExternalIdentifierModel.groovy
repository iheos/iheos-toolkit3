package gov.nist.hit.ds.dsSims.eb.metadataValidator.model

import gov.nist.hit.ds.ebMetadata.Metadata
import gov.nist.hit.ds.ebMetadata.MetadataSupport
import gov.nist.hit.ds.xdsException.XdsInternalException
import org.apache.axiom.om.OMElement

public class ExternalIdentifierModel extends AbstractRegistryObjectModel {
	public String identificationScheme = "";
	public String value = "";
	public OMElement owner;
	
	public boolean equals(ExternalIdentifierModel e) {
		if (!e.identificationScheme.equals(identificationScheme)) 
			return false;
		if (!e.value.equals(value)) 
			return false;
		return super.equals(e);
	}
	
	public ExternalIdentifierModel(String id, String identificationScheme, String name, String value) {
		super(id);
		this.identificationScheme = identificationScheme;
		this.name = name;
		this.value = value;
	}
	
	public ExternalIdentifierModel(Metadata m, OMElement ei) throws XdsInternalException {
		super(m, ei);
		identificationScheme = ei.getAttributeValue(MetadataSupport.identificationscheme_qname);
		value = ro.getAttributeValue(MetadataSupport.value_att_qname);
	}
	
	public OMElement toXml(OMElement owner) throws XdsInternalException  {
		ro = MetadataSupport.om_factory.createOMElement(MetadataSupport.externalidentifier_qnamens);
		ro.addAttribute("id", id, null);
		ro.addAttribute("identificationScheme", identificationScheme, null);
		ro.addAttribute("registryObject", owner.getAttributeValue(MetadataSupport.id_qname), null);
		ro.addAttribute("value", value, null);
		
		addSlotsXml(ro);
		addNameToXml(ro);
		addDescriptionXml(ro);
		addClassificationsXml(ro);
		addExternalIdentifiersXml(ro);

		return ro;
	}
	
	public String getIdentificationScheme() {
		return ro.getAttributeValue(MetadataSupport.identificationscheme_qname);
	}
	
	public String getValue() {
		return value;
	}

	public String identifyingString() {
		return "ExternalIdentifier(identificationScheme=" + identificationScheme + ", type=" + name + ")";
	}
	
	public OMElement toXml() throws XdsInternalException  {
		return toXml(null);
	}

}
