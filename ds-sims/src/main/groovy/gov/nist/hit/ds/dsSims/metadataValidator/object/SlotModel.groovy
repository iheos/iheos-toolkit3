package gov.nist.hit.ds.dsSims.metadataValidator.object
import gov.nist.hit.ds.metadata.Metadata
import gov.nist.hit.ds.metadata.MetadataSupport
import gov.nist.hit.ds.utilities.xml.XmlUtil
import gov.nist.hit.ds.xdsException.XdsInternalException
import org.apache.axiom.om.OMContainer
import org.apache.axiom.om.OMElement
/**
 * Created by bmajur on 8/19/14.
 */
@groovy.transform.TypeChecked
class SlotModel extends RegistryObjectModel {
    String name = "";
    List<String> values = new ArrayList<String>();
    OMElement owner = null;

    SlotModel(Metadata m, OMElement ro) throws XdsInternalException  {
        super(m, ro)
        parse(ro)
    }

    SlotModel(String name) {
        this.name = name
    }

    void parse(OMElement e) {
        OMContainer ownerContainer = e.getParent();
        if (ownerContainer instanceof OMElement)
            owner = (OMElement) ownerContainer;

        name = e.getAttributeValue(MetadataSupport.slot_name_qname);
        OMElement value_list = XmlUtil.firstChildWithLocalName(e, "ValueList");

        for (Iterator<OMElement> it=value_list.getChildElements(); it.hasNext(); ) {
            OMElement value = (OMElement) it.next();
            values.add(value.getText());
        }
    }

    String toString() {
        return "Slot(" + name + ") = " + values;
    }

     boolean equals(SlotModel s) {
        if (!name.equals(s.name))
            return false;
        if (!values.equals(s.values))
            return false;
        return true;
    }

     void addValue(String value) {
        values.add(value);
    }

     String getName() { return name; }
     List<String> getValues() { return values; }
    int size() { values.size() }

     String getValue(int index) throws  XdsInternalException {
        if (values.size() <= index)
            throw new XdsInternalException(ownerIdentifyingString() + ": Slot " + name + " does not have a " + index + "th value");
        return values.get(index);
    }

     String getOwnerType() {
        if (owner == null)
            return "";
        return owner.getLocalName();
    }

     String getOwnerId() {
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
