package gov.nist.hit.ds.dsSims.eb.metadataValidator.object

import gov.nist.hit.ds.dsSims.eb.metadata.Metadata
import gov.nist.hit.ds.dsSims.eb.metadata.MetadataSupport
import gov.nist.hit.ds.xdsExceptions.MetadataException
import gov.nist.hit.ds.xdsExceptions.XdsInternalException
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 8/19/14.
 */
@groovy.transform.TypeChecked
class AuthorModel extends RegistryObjectModel {
    String classificationScheme = null;

    AuthorModel(Metadata m, OMElement ro) throws XdsInternalException  {
        super(m, ro);
        classificationScheme = ro.getAttributeValue(MetadataSupport.classificationscheme_qname);
    }

     void addInstitution(String institution) {
        SlotModel s = getSlot("authorInstitution");
        if (s == null)
            addSlot("authorInstitution", institution);
        else
            s.addValue(institution);
    }

     void addRole(String roleName) {
        SlotModel s = getSlot("authorRole");
        if (s == null)
            addSlot("authorRole", roleName);
        else
            s.addValue(roleName);
    }

     void addSpecialty(String specialtyName) {
        SlotModel s = getSlot("authorSpecialty");
        if (s == null)
            addSlot("authorSpecialty", specialtyName);
        else
            s.addValue(specialtyName);
    }

     boolean equals(AuthorModel a) {
        try {
            if (!getClassificationScheme().equals(a.getClassificationScheme()))
                return false;
        } catch (XdsInternalException e) {
            return false;
        }
        return super.equals(a);
    }

     String getAuthorPerson() throws MetadataException {
        try {
            String value = getSlot("authorPerson").getValue(0);
            if (value == null)
                return "";
            return value;
        } catch (Exception e) {
            return "";
        }
    }

     List<String> getAuthorInstitutions() {
        try {
            return getSlot("authorInstitution").getValues();
        } catch (Exception e) {
            return new ArrayList<String>();
        }
    }

     List<String> getAuthorRoles() {
        try {
            return getSlot("authorRole").getValues();
        } catch (Exception e) {
            return new ArrayList<String>();
        }
    }

     List<String> getAuthorSpecialties() {
        try {
            return getSlot("authorSpecialty").getValues();
        } catch (Exception e) {
            return new ArrayList<String>();
        }
    }

    String getPerson() {
        try {
            return getAuthorPerson();
        } catch (MetadataException e) {
            return "";
        }
    }

    static  boolean isAuthorClassification(OMElement ele) {
        String classificationScheme = ele.getAttributeValue(MetadataSupport.classificationscheme_qname);
        return isAuthorClassification(classificationScheme);
    }

    static  boolean isAuthorClassification(String classificationScheme) {
        if (MetadataSupport.XDSDocumentEntry_author_uuid.equals(classificationScheme))
            return true;
        if (MetadataSupport.XDSSubmissionSet_author_uuid.equals(classificationScheme))
            return true;
        return false;
    }

     String identifyingString() {
        return "Author (" + getPerson() + ")";
    }

     String getClassificationScheme() throws XdsInternalException  {
        return getClassificationScheme(null);
    }

     String getClassificationScheme(OMElement parent) throws XdsInternalException {
        if (parent != null) {
            if ("ExtrinsicObject".equals(parent.getLocalName()))
                return MetadataSupport.XDSDocumentEntry_author_uuid;
            if ("RegistryPackage".equals(parent.getLocalName()))
                return MetadataSupport.XDSSubmissionSet_author_uuid;
        }

        if (classificationScheme != null)
            return classificationScheme;
        if (owner == null)
            throw new XdsInternalException("Cannot determine proper classificationScheme for Author, no owner specified to infer classificationScheme from");
        if ("ExtrinsicObject".equals(owner.getLocalName()))
            return MetadataSupport.XDSDocumentEntry_author_uuid;
        if ("RegistryPackage".equals(owner.getLocalName()))
            return MetadataSupport.XDSSubmissionSet_author_uuid;
        throw new XdsInternalException("Cannot determine proper classificationScheme for Author, owner element type " + owner.getLocalName() + " is not understood");
    }
}
