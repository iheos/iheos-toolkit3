package gov.nist.hit.ds.dsSims.eb.metadataValidator.object
import gov.nist.hit.ds.dsSims.eb.metadata.Metadata
import gov.nist.hit.ds.dsSims.eb.metadata.MetadataSupport
import gov.nist.hit.ds.xdsException.XdsInternalException
import org.apache.axiom.om.OMElement
/**
 * Created by bmajur on 8/19/14.
 */
@groovy.transform.TypeChecked
class ExternalIdentifierModel extends RegistryObjectModel {
    String identificationScheme = ""
    String value = ""

    ExternalIdentifierModel(Metadata m, OMElement ro) throws XdsInternalException  {
        super(m, ro);
        identificationScheme = ro.getAttributeValue(MetadataSupport.identificationscheme_qname);
        value = ro.getAttributeValue(MetadataSupport.value_att_qname);
    }

//    public ExternalIdentifierValidator(String id, String identificationScheme, String displayName, String value) {
//        super(id);
//        this.identificationScheme = identificationScheme;
//        this.displayName = displayName;
//        this.value = value;
//    }

     boolean equals(ExternalIdentifierModel e) {
        if (!e.identificationScheme.equals(identificationScheme))
            return false;
        if (!e.value.equals(value))
            return false;
        return Object.equals(e);
    }

     String getIdentificationScheme() {
        return ro.getAttributeValue(MetadataSupport.identificationscheme_qname);
    }

     String getValue() {
        return value;
    }

     String identifyingString() {
        return "ExternalIdentifier(identificationScheme=" + identificationScheme + ", type=" + name + ")";
    }

}
