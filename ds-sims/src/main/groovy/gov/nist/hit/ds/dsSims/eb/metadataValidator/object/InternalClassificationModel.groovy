package gov.nist.hit.ds.dsSims.eb.metadataValidator.object
import gov.nist.hit.ds.dsSims.eb.metadata.Metadata
import gov.nist.hit.ds.dsSims.eb.metadata.MetadataSupport
import gov.nist.hit.ds.xdsException.XdsInternalException
import org.apache.axiom.om.OMElement
/**
 * Created by bmajur on 8/19/14.
 */
@groovy.transform.TypeChecked
class InternalClassificationModel extends RegistryObjectModel {
    String classifiedObjectId;
    String classificationNode;

    InternalClassificationModel(Metadata m, OMElement ro) throws XdsInternalException  {
        super(m, ro);
        classifiedObjectId = ro.getAttributeValue(MetadataSupport.classified_object_qname);
        classificationNode = ro.getAttributeValue(MetadataSupport.classificationnode_qname);
    }

    public InternalClassificationModel(String id, String classifiedObject, String classificationNode) {
        super(id);
        this.classifiedObjectId = classifiedObject;
        this.classificationNode = classificationNode;
    }

     boolean equals(InternalClassificationModel ic) {
        if (ic == null)
            return false;
        if (!id.equals(ic.id))
            return false;
        if (!classifiedObjectId.equals(ic.classifiedObjectId))
            return false;
        if (!classificationNode.equals(ic.classificationNode))
            return false;
        return super.equals(ic);
    }

     String getClassificationNode() {
        return classificationNode;
    }

    static  boolean isInternalClassification(OMElement ele) {
        return ele.getAttribute(MetadataSupport.classificationnode_qname) != null;
    }

     String identifyingString() {
        return "Classification(" + getId() + ")";
    }
}
