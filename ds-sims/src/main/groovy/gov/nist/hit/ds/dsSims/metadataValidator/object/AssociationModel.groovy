package gov.nist.hit.ds.dsSims.metadataValidator.object

import gov.nist.hit.ds.metadata.Metadata
import gov.nist.hit.ds.metadata.MetadataSupport
import gov.nist.hit.ds.xdsException.XdsInternalException
import org.apache.axiom.om.OMElement
/**
 * Created by bmajur on 8/18/14.
 */
@groovy.transform.TypeChecked
class AssociationModel extends RegistryObjectModel {
    String source = "";
    String target = "";
    String type = "";

    static List<String> assocTypes =
            Arrays.asList(
                    MetadataSupport.assoctype_has_member,
                    MetadataSupport.assoctype_rplc,
                    MetadataSupport.assoctype_xfrm,
                    MetadataSupport.assoctype_apnd,
                    MetadataSupport.assoctype_xfrm_rplc,
                    MetadataSupport.assoctype_signs,
                    MetadataSupport.assoctype_isSnapshotOf
            );

    static List<String> assocTypesMU =
            Arrays.asList(
                    MetadataSupport.assoctype_update_availabilityStatus,
                    MetadataSupport.assoctype_submitAssociation
            );

    static public ClassAndIdDescription externalIdentifierDescription = new ClassAndIdDescription();
    static {
        externalIdentifierDescription.definedSchemes = new ArrayList<String>();

        externalIdentifierDescription.requiredSchemes = new ArrayList<String>();
        externalIdentifierDescription.multipleSchemes = new ArrayList<String>();

        externalIdentifierDescription.names = new HashMap<String, String>();
    }

    static List<String> assocs_with_documentation =
            Arrays.asList(
                    MetadataSupport.assoctype_rplc,
                    MetadataSupport.assoctype_xfrm,
                    MetadataSupport.assoctype_apnd,
                    MetadataSupport.assoctype_xfrm_rplc
            );

    List<String> relationship_assocs =
            Arrays.asList(
                    MetadataSupport.assoctype_rplc,
                    MetadataSupport.assoctype_apnd,
                    MetadataSupport.assoctype_xfrm,
                    MetadataSupport.assoctype_xfrm_rplc
            );

    public AssociationModel(Metadata m, OMElement ro) throws XdsInternalException  {
        super(m, ro);
        source = ro.getAttributeValue(MetadataSupport.source_object_qname);
        target = ro.getAttributeValue(MetadataSupport.target_object_qname);
        type = ro.getAttributeValue(MetadataSupport.association_type_qname);
        normalize();
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

    public String identifyingString() {
        return "Association(" + getId() + ", " + type + ")";
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
}
