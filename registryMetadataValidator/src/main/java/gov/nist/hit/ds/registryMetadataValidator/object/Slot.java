package gov.nist.hit.ds.registryMetadataValidator.object;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.registryMetadataValidator.datatype.FormatValidator;
import gov.nist.hit.ds.registryMetadataValidator.datatype.FormatValidatorCalledIncorrectlyException;
import gov.nist.hit.ds.registrysupport.MetadataSupport;
import gov.nist.hit.ds.utilities.xml.XmlUtil;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.axiom.om.OMContainer;
import org.apache.axiom.om.OMElement;

public class Slot {
	String name = "";
	List<String> values = new ArrayList<String>();
	OMElement owner = null;
	OMElement myElement = null;
	
	public String toString() {
		return "Slot(" + name + ") = " + values;
	}
	
	public boolean equals(Slot s) {
		if (!name.equals(s.name)) 
			return false;
		if (!values.equals(s.values)) 
			return false;
		return true;
	}
	
	public OMElement toXML() {
		OMElement sl = XmlUtil.om_factory.createOMElement(MetadataSupport.slot_qnamens);
		sl.addAttribute("name", name, null);
		OMElement valuelist = XmlUtil.om_factory.createOMElement(MetadataSupport.valuelist_qnamens);
		sl.addChild(valuelist);
		
		for (String value : values) {
			OMElement valueEle = XmlUtil.om_factory.createOMElement(MetadataSupport.value_qnamens);
			valueEle.setText(value);
			valuelist.addChild(valueEle);
		}
		myElement = sl;
		return sl;
	}
	
	public Slot(String name) {
		this.name = name;
	}
	
	public void addValue(String value) {
		values.add(value);
	}
	
	@SuppressWarnings("unchecked")
	public Slot(OMElement e) {
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
	
	public String getValue(int index) throws  XdsInternalException {
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
	
	public void validate(IAssertionGroup er, boolean multivalue, FormatValidator validator, String resource) {
		if (!multivalue && values.size() > 1)
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(getOwnerType() + "(" + getOwnerId() + ") has Slot " + name + " which is required to have a single value, " + values.size() + "  values found", resource), this);
		try {
			for (String value : values) {
				validator.validate(value);
			}
		} catch (FormatValidatorCalledIncorrectlyException e) {
			// oops - can't call with individual slot values, needs Slot
			try {
				validator.validate(myElement);
			} catch (FormatValidatorCalledIncorrectlyException e1) {
				// hmmm - I guess we give up here
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new XdsInternalException("Slot#validate: the validator " + validator.getClass().getName() + " implements no validate methods"));
			}
		}
	}


}
