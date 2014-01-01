package gov.nist.hit.ds.registryMetadataValidator.object;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.registryMetadata.MetadataParser;
import gov.nist.hit.ds.registryMetadataValidator.datatype.CxFormat;
import gov.nist.hit.ds.registryMetadataValidator.datatype.FormatValidator;
import gov.nist.hit.ds.registryMetadataValidator.datatype.OidFormat;
import gov.nist.hit.ds.registryMetadataValidator.datatype.UuidFormat;
import gov.nist.hit.ds.registrysupport.MetadataSupport;
import gov.nist.hit.ds.utilities.xml.XmlUtil;
import gov.nist.hit.ds.valSupport.client.ValidationContext;
import gov.nist.hit.ds.xdsException.MetadataException;
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.axiom.om.OMElement;

public abstract class AbstractRegistryObject {

	abstract public String identifyingString();
	abstract public OMElement toXml() throws ToolkitRuntimeException;
	abstract public void validateSlotsLegal(IAssertionGroup er);
	abstract public void validateRequiredSlotsPresent(IAssertionGroup er, ValidationContext vc);
	abstract public void validateSlotsCodedCorrectly(IAssertionGroup er, ValidationContext vc);

	OMElement ro;
	List<Slot> slots = new ArrayList<Slot>();
	String status = null;
	String home = null;
	String name = "";
	String description = "";
	List<Classification> classifications = new ArrayList<Classification>();
	List<InternalClassification> internalClassifications = new ArrayList<InternalClassification>();
	List<Author> authors = new ArrayList<Author>();
	List<ExternalIdentifier> externalIdentifiers = new ArrayList<ExternalIdentifier>();
	Metadata m;
	OMElement owner = null;
	String id = "";
	String lid;
	String version = "1.1";
	
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

	public void updateDone() throws ToolkitRuntimeException, MetadataException  {
		ro = toXml();
		m = MetadataParser.parseObject(ro);
	}

	public boolean equals(AbstractRegistryObject a) {
		if (status == null && a.status == null)
			;
		else {
			if (status != null && !status.equals(a.status))
				return false;
			if (a.status != null && !a.status.equals(status))
				return false;
		}
		if (home == null && a.home == null)
			;
		else {
			if (home != null && !home.equals(a.home))
				return false;
			if (a.home != null && !a.home.equals(home))
				return false;
		}
		
		if (!version.equals(a.version))
			return false;
		
		if (lid == null && a.lid == null)
			;
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

	public void addNameToXml(OMElement parent) {

		if (name != null && !name.equals("")) {
			OMElement n = XmlUtil.om_factory.createOMElement(MetadataSupport.name_qnamens);
			OMElement locstr = XmlUtil.om_factory.createOMElement(MetadataSupport.localizedstring_qnamens);
			n.addChild(locstr);
			locstr.addAttribute("value", name, null);
			parent.addChild(n);
		}
	}

	public void addDescriptionXml(OMElement parent) {

		if (description != null && !description.equals("")) {
			OMElement n = XmlUtil.om_factory.createOMElement(MetadataSupport.description_qnamens);
			OMElement locstr = XmlUtil.om_factory.createOMElement(MetadataSupport.localizedstring_qnamens);
			n.addChild(locstr);
			locstr.addAttribute("value", description, null);
			parent.addChild(n);
		}
	}
	
	public void addVersionXml(OMElement parent) {
		OMElement n = XmlUtil.om_factory.createOMElement(MetadataSupport.versioninfo_qnamens);
		n.addAttribute("versionName", version, null);
		parent.addChild(n);
	}

	public void addSlotsXml(OMElement parent) {
		for (Slot s : slots) {
			parent.addChild(s.toXML());
		}
	}

	public void addClassificationsXml(OMElement parent) throws ToolkitRuntimeException  {
		for (Classification c : classifications) {
			OMElement cl = c.toXml(parent);
			parent.addChild(cl);

		}
		if (internalClassifications != null) {
			for (InternalClassification ic : internalClassifications)
				parent.addChild(ic.toXml());
		}
	}
	
	public void addAuthorsXml(OMElement parent) throws ToolkitRuntimeException  {
		for (Author a : authors) {
			OMElement ele = a.toXml(parent);
			parent.addChild(ele);
		}
	}

	public void addExternalIdentifiersXml(OMElement parent) throws ToolkitRuntimeException  {
		for (ExternalIdentifier ei : externalIdentifiers) {
			OMElement ele = ei.toXml(parent);
			parent.addChild(ele);
		}
	}
	
	public AbstractRegistryObject(String id) {
		ro = null;
		this.id = id;
	}

	public AbstractRegistryObject(Metadata m, OMElement ro) throws ToolkitRuntimeException  {
		this.m = m;
		this.ro = ro;
		
		if (ro == null)
			throw new ToolkitRuntimeException("Not a RegistryObject");

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

	public List<ExternalIdentifier> getExternalIdentifiers(String identificationScheme) {
		List<ExternalIdentifier> eis = new ArrayList<ExternalIdentifier>();
		for (ExternalIdentifier ei : externalIdentifiers) {
			if (ei.getIdentificationScheme().equals(identificationScheme))
				eis.add(ei);
		}
		return eis;
	}

	public void validateSlot(IAssertionGroup er, String slotName, boolean multivalue, FormatValidator validator, String resource) {
		Slot slot = getSlot(slotName);
		if (slot == null) {
			return;
		}

		slot.validate(er, multivalue, validator, resource);
	}

	public boolean verifySlotsUnique(IAssertionGroup er) {
		boolean ok = true;
		List<String> names = new ArrayList<String>();
		for (Slot slot : slots) {
			if (names.contains(slot.getName())) 
				if (er != null) {
					er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + ": Slot " + slot.getName() + " is multiply defined", "ebRIM 3.0 section 2.8.2"), this);
					ok = false;
				}
				else
					names.add(slot.getName());
		}
		return ok;
	}
	
	public void validateTopAtts(IAssertionGroup er, ValidationContext vc, String tableRef, List<String> statusValues) {
		validateId(er, vc, "entryUUID", id, null);
		
		if (vc.isSQ && vc.isResponse) {
			if (status == null) 
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + ": availabilityStatus attribute (status attribute in XML) must be present", tableRef), this);
			else {
				if (!statusValues.contains(status))
					er.err(XdsErrorCode.Code.XDSRegistryMetadataError, 
							new ErrorContext(identifyingString() + ": availabilityStatus attribute must take on one of these values: " + statusValues + ", found " + status, "ITI TF-2a: 3.18.4.1.2.3.6"), this);
			}
			
			validateId(er, vc, "lid", lid, null);
			
			List<OMElement> versionInfos = XmlUtil.childrenWithLocalName(ro, "VersionInfo");
			if (versionInfos.size() == 0) {
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + ": VersionInfo attribute missing", "ebRIM Section 2.5.1"), this);
			}
		}

		if (vc.isSQ && vc.isXC && vc.isResponse) {
			validateHome(er, tableRef);

		}
	}

	public void validateId(IAssertionGroup er, ValidationContext vc, String attName, String attValue, String resource) {
		String defaultResource = "ITI TF-3: 4.1.12.3";
		if (attValue == null || attValue.equals("")) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + ": " + attName + " attribute empty or missing", (resource!=null) ? resource : defaultResource), this);
		} else {
			if (vc.isSQ && vc.isResponse) {
				new UuidFormat(er, identifyingString() + " " + attName + " attribute must be a UUID", (resource!=null) ? resource : defaultResource).validate(id);
			} else if(id.startsWith("urn:uuid:")) {
				new UuidFormat(er, identifyingString() + " " + attName + " attribute", (resource!=null) ? resource : defaultResource).validate(id);
			}
		}
		
		for (Classification c : classifications)
			c.validateId(er, vc, "entryUUID", c.getId(), resource);
		
		for (Author a : authors)
			a.validateId(er, vc, "entryUUID", a.getId(), resource);
		
		for (ExternalIdentifier ei : externalIdentifiers) 
			ei.validateId(er, vc, "entryUUID", ei.getId(), resource);
		
	}
	
	public void verifyIdsUnique(IAssertionGroup er, Set<String> knownIds) {
		if (id != null) {
			if (knownIds.contains(id))
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + ": entryUUID " + id + "  identifies multiple objects", "ITI TF-3: 4.1.12.3 and ebRS 5.1.2"), this);
			knownIds.add(id);
		}
		
		for (Classification c : classifications)
			c.verifyIdsUnique(er, knownIds);
		
		for (Author a : authors)
			a.verifyIdsUnique(er, knownIds);
		
		for (ExternalIdentifier ei : externalIdentifiers) 
			ei.verifyIdsUnique(er, knownIds);
		
		
	}
	public void validateHome(IAssertionGroup er, String resource) {
		if (home == null) 
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + ": homeCommunityId attribute must be present", resource), this);
		else {
			if (home.length() > 64)
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + ": homeCommunityId is limited to 64 characters, found " + home.length(), resource), this);
			
			String[] parts = home.split(":");
			if (parts.length < 3 || !parts[0].equals("urn") || !parts[1].equals("oid"))
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + ": homeCommunityId must begin with urn:oid: prefix, found [" + home + "]", resource), this);
			new OidFormat(er, identifyingString() + " homeCommunityId", resource).validate(parts[parts.length-1]);
		}
	}
	protected int count(List<String> strings, String target) {
		int i=0;
	
		for (String s : strings)
			if (s.equals(target))
				i++;
	
		return i;
	}
	
	public void validateClassificationsLegal(IAssertionGroup er, ClassAndIdDescription desc, String resource) {
		List<String> cSchemes = new ArrayList<String>();

		for (Classification c : getClassifications()) {
			String cScheme = c.getClassificationScheme();
			if (cScheme == null || cScheme.equals("") || !desc.definedSchemes.contains(cScheme)) {
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + ": " + c.identifyingString() + " has an unknown classificationScheme attribute value: " + cScheme, resource), this);
			} else {
				cSchemes.add(cScheme);
			}
		}

		Set<String> cSchemeSet = new HashSet<String>();
		cSchemeSet.addAll(cSchemes);
		for (String cScheme : cSchemeSet) {
			if (count(cSchemes, cScheme) > 1 && !desc.multipleSchemes.contains(cScheme))
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + ": " + classificationDescription(desc, cScheme) + " is specified multiple times, only one allowed", resource), this);
		}
	}

	String classificationDescription(ClassAndIdDescription desc, String cScheme) {
		return "Classification(" + cScheme + ")(" + desc.names.get(cScheme) + ")";
	}
	
	String externalIdentifierDescription(ClassAndIdDescription desc, String eiScheme) {
		return "ExternalIdentifier(" + eiScheme + ")(" + desc.names.get(eiScheme) + ")";
	}

	public void validateRequiredClassificationsPresent(IAssertionGroup er, ValidationContext vc, ClassAndIdDescription desc, String resource) {
		if (!(vc.isXDM || vc.isXDRLimited)) {
			for (String cScheme : desc.requiredSchemes) {
				List<Classification> cs = getClassificationsByClassificationScheme(cScheme);
				if (cs.size() == 0) 
					er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + ": " + classificationDescription(desc, cScheme) + " is required but missing", resource), this);
			}
		}
	}

	public void validateClassificationsCodedCorrectly(IAssertionGroup er, ValidationContext vc) {
		for (Classification c : getClassifications()) 
			c.validateStructure(er, vc);

		for (Author a : getAuthors()) 
			a.validateStructure(er, vc);
	}

	public void validateClassifications(IAssertionGroup er, ValidationContext vc, ClassAndIdDescription desc, String resource)  {
		er.challenge("Validating Classifications present are legal");
		validateClassificationsLegal(er, desc, resource);
		er.challenge("Validating Required Classifications present");
		validateRequiredClassificationsPresent(er, vc, desc, resource);
		er.challenge("Validating Classifications coded correctly");
		validateClassificationsCodedCorrectly(er, vc);
	}

	public void validateExternalIdentifiers(IAssertionGroup er, ValidationContext vc, ClassAndIdDescription desc, String resource) {
		er.challenge("Validating ExternalIdentifiers present are legal");
		validateExternalIdentifiersLegal(er, desc, resource);
		er.challenge("Validating Required ExternalIdentifiers present");
		validateRequiredExternalIdentifiersPresent(er, vc, desc, resource);
		er.challenge("Validating ExternalIdentifiers coded correctly");
		validateExternalIdentifiersCodedCorrectly(er, vc, desc, resource);
	}
	
	public void validateExternalIdentifiersCodedCorrectly(IAssertionGroup er, ValidationContext vc, ClassAndIdDescription desc, String resource) {
		for (ExternalIdentifier ei : getExternalIdentifiers()) {
			ei.validateStructure(er, vc);
			if (MetadataSupport.XDSDocumentEntry_uniqueid_uuid.equals(ei.getIdentificationScheme())) {
				String[] parts = ei.getValue().split("\\^");
				new OidFormat(er, identifyingString() + ": " + ei.identifyingString(), externalIdentifierDescription(desc, ei.getIdentificationScheme()))
				.validate(parts[0]);
				if (parts[0].length() > 64)
					er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + ": " + ei.identifyingString() + " OID part of DocumentEntry uniqueID is limited to 64 digits", resource), this);
				if (parts.length > 1 && parts[1].length() > 16) {
					er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + ": " + ei.identifyingString() + " extension part of DocumentEntry uniqueID is limited to 16 characters", resource), this);
				}

			} else if (MetadataSupport.XDSDocumentEntry_patientid_uuid.equals(ei.getIdentificationScheme())){
				new CxFormat(er, identifyingString() + ": " + ei.identifyingString(), "ITI TF-3: Table 4.1.7")
				.validate(ei.getValue());
			}
		}
	}


	
	public void validateRequiredExternalIdentifiersPresent(IAssertionGroup er, ValidationContext vc, ClassAndIdDescription desc, String resource)  {
		for (String idScheme : desc.requiredSchemes) {
			List<ExternalIdentifier> eis = getExternalIdentifiers(idScheme);
			if (eis.size() == 0)
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + ": " + externalIdentifierDescription(desc, idScheme) + " is required but missing", resource), this);
			if (eis.size() > 1)
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + ": " + externalIdentifierDescription(desc, idScheme) + " is specified multiple times, only one allowed", resource), this);
		}
	}


	public void validateExternalIdentifiersLegal(IAssertionGroup er, ClassAndIdDescription desc, String resource) {
		for (ExternalIdentifier ei : getExternalIdentifiers()) {
			String idScheme = ei.getIdentificationScheme();
			if (idScheme == null || idScheme.equals("") || !desc.definedSchemes.contains(idScheme)) 
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + ": " + ei.identifyingString() + " has an unknown identificationScheme attribute value: " + idScheme, resource), this);
		}
	}

	public void validateSlots(IAssertionGroup er, ValidationContext vc) {
		er.challenge("Validating that Slots present are legal");
		validateSlotsLegal(er);
		er.challenge("Validating required Slots present");
		validateRequiredSlotsPresent(er, vc);
		er.challenge("Validating Slots are coded correctly");
		validateSlotsCodedCorrectly(er, vc);
	}


}
