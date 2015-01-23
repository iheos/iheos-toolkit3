package gov.nist.hit.ds.dsSims.eb.metadataValidator.model

import gov.nist.hit.ds.ebMetadata.Metadata
import gov.nist.hit.ds.ebMetadata.MetadataSupport
import gov.nist.hit.ds.utilities.xml.XmlUtil
import gov.nist.hit.ds.xdsExceptions.MetadataException
import gov.nist.hit.ds.xdsExceptions.XdsInternalException
import org.apache.axiom.om.OMElement

public abstract class AbstractRegistryObjectModel {

	abstract public String identifyingString();
	abstract public OMElement toXml() throws XdsInternalException;

	public OMElement ro;
    public List<SlotModel> slots = new ArrayList<SlotModel>();
    public String status = null;
    public String home = null;
    public String name = "";
    public String description = "";
    public List<ClassificationModel> classifications = new ArrayList<ClassificationModel>();
    public List<InternalClassificationModel> internalClassifications = new ArrayList<InternalClassificationModel>();
    public List<AuthorModel> authors = new ArrayList<AuthorModel>();
    public List<ExternalIdentifierModel> externalIdentifiers = new ArrayList<ExternalIdentifierModel>();
    public Metadata m;
    public OMElement owner = null;
    public String id = "";
    public String lid;
    public String version = "1.1";

    public AbstractRegistryObjectModel(String id) {
        ro = null;
        this.id = id;
    }

    public OMElement getElement() {
		return ro;
	}
	
	public Metadata getMetadata() {
		return m;
	}
	
	public boolean isClassifiedAs(String uuid) {
		for (InternalClassificationModel ic : internalClassifications) {
			if (ic.getClassificationNode().equals(uuid))
				return true;
		}
		return false;
	}

	public void updateDone() throws XdsInternalException, MetadataException {
		ro = toXml();
		m = MetadataParser.parseObject(ro);
	}

	public boolean equals(AbstractRegistryObjectModel a) {
		if (!(status == null && a.status == null)) {
			if (status != null && !status.equals(a.status))
				return false;
			if (a.status != null && !a.status.equals(status))
				return false;
		}
		if (!(home == null && a.home == null)) {
			if (home != null && !home.equals(a.home))
				return false;
			if (a.home != null && !a.home.equals(home))
				return false;
		}
		
		if (!version.equals(a.version))
			return false;
		
		if (!(lid == null && a.lid == null)) {
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

	public void addSlot(SlotModel s) {
		slots.add(s);
	}

	public SlotModel addSlot(String name, String value) {
		SlotModel s = new SlotModel(name);
		s.addValue(value);
		addSlot(s);
		return s;
	}

	public void addClassification(ClassificationModel c) {
		classifications.add(c);
	}

	public void addAuthor(AuthorModel a) {
		authors.add(a);
	}

	public void addExternalIdentifier(ExternalIdentifierModel e) {
		externalIdentifiers.add(e);
	}

	public void addNameToXml(OMElement parent) {

		if (name != null && !name.equals("")) {
			OMElement n = XmlUtil.om_factory.createOMElement(MetadataSupport.name_qnamens);
			OMElement locstr = MetadataSupport.om_factory.createOMElement(MetadataSupport.localizedstring_qnamens);
			n.addChild(locstr);
			locstr.addAttribute("value", name, null);
			parent.addChild(n);
		}
	}

	public void addDescriptionXml(OMElement parent) {

		if (description != null && !description.equals("")) {
			OMElement n = MetadataSupport.om_factory.createOMElement(MetadataSupport.description_qnamens);
			OMElement locstr = MetadataSupport.om_factory.createOMElement(MetadataSupport.localizedstring_qnamens);
			n.addChild(locstr);
			locstr.addAttribute("value", description, null);
			parent.addChild(n);
		}
	}
	
	public void addVersionXml(OMElement parent) {
		OMElement n = MetadataSupport.om_factory.createOMElement(MetadataSupport.versioninfo_qnamens);
		n.addAttribute("versionName", version, null);
		parent.addChild(n);
	}

	public void addSlotsXml(OMElement parent) {
		for (SlotModel s : slots) {
			parent.addChild(s.toXML());
		}
	}

	public void addClassificationsXml(OMElement parent) throws XdsInternalException  {
		for (ClassificationModel c : classifications) {
			OMElement cl = c.toXml(parent);
			parent.addChild(cl);

		}
		if (internalClassifications != null) {
			for (InternalClassificationModel ic : internalClassifications)
				parent.addChild(ic.toXml());
		}
	}
	
	public void addAuthorsXml(OMElement parent) throws XdsInternalException  {
		for (AuthorModel a : authors) {
			OMElement ele = a.toXml(parent);
			parent.addChild(ele);
		}
	}

	public void addExternalIdentifiersXml(OMElement parent) throws XdsInternalException  {
		for (ExternalIdentifierModel ei : externalIdentifiers) {
			OMElement ele = ei.toXml(parent);
			parent.addChild(ele);
		}
	}
	
	public AbstractRegistryObjectModel(Metadata m, OMElement ro)   {
		this.m = m;
		this.ro = ro;

        assert m
        assert ro

		id = ro.getAttributeValue(MetadataSupport.id_qname);
		if (id == null) id = "";
		
		lid = ro.getAttributeValue(MetadataSupport.lid_qname);
		if (lid == null) lid = "";
		
		status = ro.getAttributeValue(MetadataSupport.status_qname);
		home = ro.getAttributeValue(MetadataSupport.home_qname);

		for (OMElement slotEle : XmlUtil.childrenWithLocalName(ro, "Slot")) {
			SlotModel s = new SlotModel(slotEle);
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
			if (AuthorModel.isAuthorClassification(clEle)) {
				AuthorModel a = new AuthorModel(m, clEle);
				authors.add(a);
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


	String getLocalizedString(OMElement attEle) {
		if (attEle != null) {
			OMElement nameLocStr =  XmlUtil.firstChildWithLocalName(attEle, "LocalizedString") ;
			if (nameLocStr != null) {
				return nameLocStr.getAttributeValue(MetadataSupport.value_qname);
			}
		}
		return null;
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

	public ExternalIdentifierModel getExternalIdentifier(String identificationScheme) {
		for (ExternalIdentifierModel ei : externalIdentifiers) {
			if (ei.getIdentificationScheme().equals(identificationScheme))
				return ei;
		}
		return null;
	}

	public List<ExternalIdentifierModel> getExternalIdentifiers(String identificationScheme) {
		List<ExternalIdentifierModel> eis = new ArrayList<ExternalIdentifierModel>();
		for (ExternalIdentifierModel ei : externalIdentifiers) {
			if (ei.getIdentificationScheme().equals(identificationScheme))
				eis.add(ei);
		}
		return eis;
	}

	public int count(List<String> strings, String target) {
		int i=0;
	
		for (String s : strings)
			if (s.equals(target))
				i++;
	
		return i;
	}
	

	public String classificationDescription(ClassAndIdDescription desc, String cScheme) {
		return "Classification(" + cScheme + ")(" + desc.names.get(cScheme) + ")";
	}
	
	public String externalIdentifierDescription(ClassAndIdDescription desc, String eiScheme) {
		return "ExternalIdentifier(" + eiScheme + ")(" + desc.names.get(eiScheme) + ")";
	}



}
