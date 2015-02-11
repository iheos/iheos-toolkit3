package gov.nist.hit.ds.dsSims.eb.metadataValidator.model

import gov.nist.hit.ds.ebMetadata.MetadataSupport
import gov.nist.hit.ds.utilities.xml.XmlUtil
import gov.nist.hit.ds.xdsExceptions.XdsInternalException
import org.apache.axiom.om.OMContainer
import org.apache.axiom.om.OMElement

public class SlotModel {
	String name = "";
	List<String> values = new ArrayList<String>();
	OMElement owner = null;
	OMElement myElement = null;
	
	public String toString() {
		return "Slot(" + name + ") = " + values;
	}
	
	public boolean equals(SlotModel s) {
		if (!name.equals(s.name)) 
			return false;
		if (!values.equals(s.values)) 
			return false;
		return true;
	}
	
	public OMElement toXML() {
		OMElement sl = MetadataSupport.om_factory.createOMElement(MetadataSupport.slot_qnamens);
		sl.addAttribute("name", name, null);
		OMElement valuelist = MetadataSupport.om_factory.createOMElement(MetadataSupport.valuelist_qnamens);
		sl.addChild(valuelist);
		
		for (String value : values) {
			OMElement valueEle = MetadataSupport.om_factory.createOMElement(MetadataSupport.value_qnamens);
			valueEle.setText(value);
			valuelist.addChild(valueEle);
		}
		myElement = sl;
		return sl;
	}
	
	public SlotModel(String name) {
		this.name = name;
	}
	
	public void addValue(String value) {
		values.add(value);
	}
	
	@SuppressWarnings("unchecked")
	public SlotModel(OMElement e) {
		myElement = e;
		OMContainer ownerContainer = e.getParent();
		if (ownerContainer instanceof OMElement)
			owner = (OMElement) ownerContainer;
		
		name = e.getAttributeValue(MetadataSupport.slot_name_qname);
		OMElement value_list = XmlUtil.firstChildWithLocalName(e, "ValueList");

		for (Iterator<OMElement> it=value_list.getChildElements(); it.hasNext(); ) {
			OMElement value = it.next();
			values.add(value.getText());
		}
	}

	public String getName() { return name; }
	public List<String> getValues() { return values; }
	
	public String getValue(int index) throws XdsInternalException {
		if (values.size() <= index)
			throw new XdsInternalException(ownerIdentifyingString() + ": Slot " + name + " does not have a " + index + "th value");
		return values.get(index);
	}
	
	public String getOwnerType() { 
		if (owner == null)
			return "";
		return owner.getLocalName();
	}
	
	public String getOwnerId() {
		if (owner == null)
			return "";
		return owner.getAttributeValue(MetadataSupport.id_qname);
	}
	
	String ownerIdentifyingString() {
		if (owner == null) 
			return "Unknown";
		return getOwnerType() + "(" + getOwnerId() + ")";
	}

}
