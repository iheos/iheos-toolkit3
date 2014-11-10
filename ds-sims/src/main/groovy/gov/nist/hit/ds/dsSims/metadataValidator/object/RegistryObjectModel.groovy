package gov.nist.hit.ds.dsSims.metadataValidator.object
import gov.nist.hit.ds.metadata.Metadata
import gov.nist.hit.ds.metadata.MetadataSupport
import gov.nist.hit.ds.utilities.xml.XmlUtil
import gov.nist.hit.ds.xdsException.MetadataException
import gov.nist.hit.ds.xdsException.XdsInternalException
import groovy.transform.ToString
import org.apache.axiom.om.OMElement
/**
 * Created by bmajur on 8/18/14.
 */

// TODO: Still includes some validator code
// TODO: Should be declared abstract
//@groovy.transform.TypeChecked
@ToString(includeNames=true, excludes="ro, m, owner")
class RegistryObjectModel {

//    abstract public OMElement toXml() throws XdsInternalException;

    String id = "";
    String status = null;
    // TODO - conflict - accessor does not reference or update
    String home = null;
    List<SlotModel> slots = new ArrayList<SlotModel>();
    String name = "";
    String description = "";
    List<ClassificationModel> classifications = new ArrayList<ClassificationModel>();
    List<InternalClassificationModel> internalClassifications = new ArrayList<InternalClassificationModel>();
    List<AuthorModel> authors = new ArrayList<AuthorModel>();
    List<ExternalIdentifierModel> externalIdentifiers = new ArrayList<ExternalIdentifierModel>();
    String version = "1.1";
    String lid;
    OMElement ro;
    Metadata m;
    OMElement owner = null;

     RegistryObjectModel(String id) {
        ro = null;
        this.id = id;
    }

    RegistryObjectModel() {
        ro = null
        m = null
    }

     RegistryObjectModel(Metadata m, OMElement ro) throws XdsInternalException  {
        this.m = m;
        this.ro = ro;

        if (ro == null)
            throw new XdsInternalException("Not a RegistryObject");

        id = ro.getAttributeValue(MetadataSupport.id_qname);
        if (id == null) id = "";

        lid = ro.getAttributeValue(MetadataSupport.lid_qname);
        if (lid == null) lid = "";

        status = ro.getAttributeValue(MetadataSupport.status_qname);
        home = ro.getAttributeValue(MetadataSupport.home_qname);

        for (OMElement slotEle : XmlUtil.childrenWithLocalName(ro, "Slot")) {
            SlotModel s = new SlotModel(m, slotEle);
            slots.add(s);
        }

         // TODO not ready for this yet
        name = getLocalizedString(XmlUtil.firstChildWithLocalName(ro, "Name"));
        if (name == null) name = "";
        description = getLocalizedString(XmlUtil.firstChildWithLocalName(ro, "Description"));
        if (description == null) description = "";

        OMElement vinfo = XmlUtil.firstChildWithLocalName(ro, "VersionInfo");
        if (vinfo != null)
            version = vinfo.getAttributeValue(MetadataSupport.versionname_qname);

        for (OMElement clEle : XmlUtil.childrenWithLocalName(ro, "Classification")) {
            if (AuthorModel.isAuthorClassification(clEle)) {
                AuthorModel aModel = new AuthorModel(m, clEle)
//                AuthorValidator a = new AuthorValidator(aModel);
                authors.add(aModel);
            } else if (InternalClassificationModel.isInternalClassification(clEle)) {
                internalClassifications.add(new InternalClassificationModel(m, clEle));
            } else {
                ClassificationModel c = new ClassificationModel(m, clEle);
                classifications.add(c);
            }
        }

        for (OMElement eiEle : XmlUtil.childrenWithLocalName(ro, "ExternalIdentifier")) {
            ExternalIdentifierModel ei = new ExternalIdentifierModel(m, eiEle);
            externalIdentifiers.add(ei);
        }
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


    public String getId() {
        return ro.getAttributeValue(MetadataSupport.id_qname);
    }

    public String getHome() {
        return ro.getAttributeValue(MetadataSupport.home_qname);
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    public List<OMElement> getSlotElements() throws MetadataException {
        return m.getSlots(getId());
    }

    public List<SlotModel> getSlots() { return slots; }

    public SlotModel getSlot(String name) {
        for (SlotModel slot : slots) {
            if (name.equals(slot.getName()))
                return slot;
        }
        return null;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public List<ClassificationModel> getClassificationsByClassificationScheme(String classificationScheme) {
        List<ClassificationModel> cls = new ArrayList<ClassificationModel>();
        for (ClassificationModel c : classifications) {
            if (c.getClassificationScheme().equals(classificationScheme))
                cls.add(c);
        }
        return cls;
    }

    public List<ClassificationModel> getClassifications() {
        return classifications;
    }

    public List<AuthorModel> getAuthors() {
        return authors;
    }

    public List<ExternalIdentifierModel> getExternalIdentifiers() {
        return externalIdentifiers;
    }

    public List<ExternalIdentifierModel> getExternalIdentifiers(String identificationScheme) {
        List<ExternalIdentifierModel> eis = new ArrayList<ExternalIdentifierModel>()
        for (ExternalIdentifierModel ei : externalIdentifiers) {
            if (ei.getIdentificationScheme().equals(identificationScheme)) {
                eis.add(ei)
            }
        }
        return eis;
    }


//    public void updateDone() throws XdsInternalException, MetadataException  {
//        ro = toXml();
//        m = MetadataParser.parseObject(ro);
//    }

    String getLocalizedString(OMElement attEle) {
        if (attEle != null) {
            OMElement nameLocStr =  XmlUtil.firstChildWithLocalName(attEle, "LocalizedString") ;
            if (nameLocStr != null) {
                return nameLocStr.getAttributeValue(MetadataSupport.value_qname);
            }
        }
        return null;
    }

    public boolean equals(RegistryObjectModel a) {
        if (status == null && a.status == null)
        {}
        else {
            if (status != null && !status.equals(a.status))
                return false;
            if (a.status != null && !a.status.equals(status))
                return false;
        }
        if (home == null && a.home == null)
        {}
        else {
            if (home != null && !home.equals(a.home))
                return false;
            if (a.home != null && !a.home.equals(home))
                return false;
        }

        if (!version.equals(a.version))
            return false;

        if (lid == null && a.lid == null)
        {}
        else {
            if (lid != null && !lid.equals(a.lid))
                return false;
            if (a.lid != null && !a.lid.equals(lid))
                return false;
        }

        if (a.slots.size() != slots.size()) return false;
        for (int i=0; i<slots.size(); i++)
            if (!a.slots.get(i).equals(slots.get(i))) return false;

        if (!a.name.equals(name)) return false;
        if (!a.description.equals(description))	return false;

        if (a.classifications.size() != classifications.size()) return false;
        for (int i=0; i<classifications.size(); i++)
            if (!a.classifications.get(i).equals(classifications.get(i))) return false;

        if (a.authors.size() != authors.size()) return false;
        for (int i=0; i<authors.size(); i++)
            if (!a.authors.get(i).equals(authors.get(i))) return false;

        if (a.externalIdentifiers.size() != externalIdentifiers.size()) return false;
        for (int i=0; i<externalIdentifiers.size(); i++)
            if (!a.externalIdentifiers.get(i).equals(externalIdentifiers.get(i))) return false;

        return true;
    }
     OMElement getElement() {
        return ro;
    }

     Metadata getMetadata() {
        return m;
    }

     boolean isClassifiedAs(String uuid) {
        for (InternalClassificationModel ic : internalClassifications) {
            if (ic.getClassificationNode().equals(uuid))
                return true;
        }
        return false;
    }
     void addSlot(SlotModel s) {
        slots.add(s);
    }

     SlotModel addSlot(String name, String value) {
        SlotModel s = new SlotModel(name);
        s.addValue(value);
        addSlot(s);
        return s;
    }

     void addClassification(ClassificationModel c) {
        classifications.add(c);
    }

     void addAuthor(AuthorModel a) {
        authors.add(a);
    }

     void addExternalIdentifier(ExternalIdentifierModel e) {
        externalIdentifiers.add(e);
    }

    String classificationDescription(ClassAndIdDescription desc, String cScheme) {
        return "Classification(" + cScheme + ")(" + desc.names.get(cScheme) + ")";
    }

    String externalIdentifierDescription(ClassAndIdDescription desc, String eiScheme) {
        return "ExternalIdentifier(" + eiScheme + ")(" + desc.names.get(eiScheme) + ")";
    }

     String identifyingString() {
        return "RegistryObject(" + getId() + ")";
    }


}
