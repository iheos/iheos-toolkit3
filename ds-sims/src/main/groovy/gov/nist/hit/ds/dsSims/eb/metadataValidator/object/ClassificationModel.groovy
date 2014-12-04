package gov.nist.hit.ds.dsSims.eb.metadataValidator.object

import gov.nist.hit.ds.dsSims.eb.metadata.Metadata
import gov.nist.hit.ds.dsSims.eb.metadata.MetadataSupport
import gov.nist.hit.ds.xdsException.XdsInternalException
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 8/19/14.
 */

@groovy.transform.TypeChecked
class ClassificationModel extends RegistryObjectModel {
    String classification_scheme = "";
    String code_value = "";
    String classification_node = "";

    ClassificationModel(Metadata m, OMElement cl) throws XdsInternalException  {
        super(m, cl);
        parse();
    }

    void parse()  {
        classification_scheme = ro.getAttributeValue(MetadataSupport.classificationscheme_qname);
        classification_node = ro.getAttributeValue(MetadataSupport.classificationnode_qname);
        if (classification_node == null) classification_node = "";
        code_value = ro.getAttributeValue(MetadataSupport.noderepresentation_qname);
    }

    boolean equals(ClassificationModel c) {
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

     String getCodeValue() { return code_value; }
     String getCodeDisplayName() { return name; }
     String getCodeScheme()  {
        try {
            return getSlot("codingScheme").getValue(0);
        } catch (Exception e) {
            return "";
        }
    }
    String getClassificationScheme() { return classification_scheme; }
    String getClassificationNode() { return classification_node; }

    String identifyingString() {
        return identifying_string();
    }

    String identifying_string() {
        String cs = "";
        try {
            cs = getCodeScheme();
        } catch (Exception e) {}
        return "Classification (classificationScheme=" + classification_scheme + " codingScheme=" + cs + ") of object " + parent_id();
    }

    String parent_id() {
        OMElement parent = (OMElement) ro.getParent();
        if (parent == null) return "Unknown";
        return parent.getAttributeValue(MetadataSupport.id_qname);
    }

}
