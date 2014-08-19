package gov.nist.hit.ds.dsSims.metadataValidator.object

import gov.nist.hit.ds.metadata.Metadata
import gov.nist.hit.ds.metadata.MetadataParser
import gov.nist.hit.ds.metadata.MetadataSupport
import gov.nist.hit.ds.utilities.xml.XmlUtil
import gov.nist.hit.ds.xdsException.MetadataException
import gov.nist.hit.ds.xdsException.XdsInternalException
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 8/18/14.
 */
@groovy.transform.TypeChecked
class RegistryObjectModel {

//    abstract public OMElement toXml() throws XdsInternalException;

    List<Slot> slots = new ArrayList<Slot>();
    String name = "";
    String description = "";
    List<Classification> classifications = new ArrayList<Classification>();
    List<InternalClassification> internalClassifications = new ArrayList<InternalClassification>();
    List<Author> authors = new ArrayList<Author>();
    List<ExternalIdentifier> externalIdentifiers = new ArrayList<ExternalIdentifier>();
    String version = "1.1";
    String status = null;
    String home = null;
    String id = "";
    String lid;
    OMElement ro;
    Metadata m;
    OMElement owner = null;

    public RegistryObjectModel(String id) {
        ro = null;
        this.id = id;
    }

    public RegistryObjectModel(Metadata m, OMElement ro) throws XdsInternalException  {
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
            Slot s = new Slot(slotEle);
            slots.add(s);
        }

        name = getLocalizedString(XmlUtil.firstChildWithLocalName(ro, "Name"));
        if (name == null) name = "";
        description = getLocalizedString(XmlUtil.firstChildWithLocalName(ro, "Description"));
        if (description == null) description = "";

        OMElement vinfo = XmlUtil.firstChildWithLocalName(ro, "VersionInfo");
        if (vinfo != null)
            version = vinfo.getAttributeValue(MetadataSupport.versionname_qname);

        for (OMElement clEle : XmlUtil.childrenWithLocalName(ro, "Classification")) {
            if (Author.isAuthorClassification(clEle)) {
                Author a = new Author(m, clEle);
                authors.add(a);
            } else if (InternalClassification.isInternalClassification(clEle)) {
                internalClassifications.add(new InternalClassification(m, clEle));
            } else {
                Classification c = new Classification(m, clEle);
                classifications.add(c);
            }
        }

        for (OMElement eiEle : XmlUtil.childrenWithLocalName(ro, "ExternalIdentifier")) {
            ExternalIdentifier ei = new ExternalIdentifier(m, eiEle);
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
        return ro.getAttributeValue(MetadataSupport.home_community_id_qname);
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    public List<OMElement> getSlotElements() throws MetadataException {
        return m.getSlots(getId());
    }

    public List<Slot> getSlots() { return slots; }

    public Slot getSlot(String name) {
        for (Slot slot : slots) {
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

    public List<Classification> getClassificationsByClassificationScheme(String classificationScheme) {
        List<Classification> cls = new ArrayList<Classification>();
        for (Classification c : classifications) {
            if (c.getClassificationScheme().equals(classificationScheme))
                cls.add(c);
        }
        return cls;
    }

    public List<Classification> getClassifications() {
        return classifications;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public List<ExternalIdentifier> getExternalIdentifiers() {
        return externalIdentifiers;
    }

    public ExternalIdentifier getExternalIdentifier(String identificationScheme) {
        for (ExternalIdentifier ei : externalIdentifiers) {
            if (ei.getIdentificationScheme().equals(identificationScheme))
                return ei;
        }
        return null;
    }


    public void updateDone() throws XdsInternalException, MetadataException  {
        ro = toXml();
        m = MetadataParser.parseObject(ro);
    }

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
    public OMElement getElement() {
        return ro;
    }

    public Metadata getMetadata() {
        return m;
    }

    public boolean isClassifiedAs(String uuid) {
        for (InternalClassification ic : internalClassifications) {
            if (ic.getClassificationNode().equals(uuid))
                return true;
        }
        return false;
    }
    public void addSlot(Slot s) {
        slots.add(s);
    }

    public Slot addSlot(String name, String value) {
        Slot s = new Slot(name);
        s.addValue(value);
        addSlot(s);
        return s;
    }

    public void addClassification(Classification c) {
        classifications.add(c);
    }

    public void addAuthor(Author a) {
        authors.add(a);
    }

    public void addExternalIdentifier(ExternalIdentifier e) {
        externalIdentifiers.add(e);
    }

    String classificationDescription(ClassAndIdDescription desc, String cScheme) {
        return "Classification(" + cScheme + ")(" + desc.names.get(cScheme) + ")";
    }

    String externalIdentifierDescription(ClassAndIdDescription desc, String eiScheme) {
        return "ExternalIdentifier(" + eiScheme + ")(" + desc.names.get(eiScheme) + ")";
    }

    public String identifyingString() {
        return "RegistryObject(" + getId() + ")";
    }


}
