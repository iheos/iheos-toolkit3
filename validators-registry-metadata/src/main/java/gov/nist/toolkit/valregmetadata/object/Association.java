package gov.nist.toolkit.valregmetadata.object;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.xdsException.XdsInternalException;
import gov.nist.toolkit.registrymetadata.Metadata;
import gov.nist.toolkit.registrysupport.MetadataSupport;
import gov.nist.toolkit.valsupport.client.ValidationContext;
import org.apache.axiom.om.OMElement;

import java.util.*;

public class Association extends AbstractRegistryObject implements TopLevelObject {
	String source = "";
	String target = "";
	String type = "";
	ValidationContext vc;

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


	public Association(Metadata m, OMElement ro, ValidationContext vc) throws XdsInternalException {
		super(m, ro);
		source = ro.getAttributeValue(MetadataSupport.source_object_qname);
		target = ro.getAttributeValue(MetadataSupport.target_object_qname);
		type = ro.getAttributeValue(MetadataSupport.association_type_qname);
		normalize();
		this.vc = vc;
	}
	
	public Association(String id, String type, String source, String target) {
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

	public boolean equals(Association a) {
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

	public OMElement toXml() throws XdsInternalException  {
		ro = MetadataSupport.om_factory.createOMElement(MetadataSupport.association_qnamens);
		ro.addAttribute("id", id, null);
		ro.addAttribute("sourceObject", source, null);
		ro.addAttribute("targetObject", target, null);
		ro.addAttribute("associationType", type, null);
		if (status != null)
			ro.addAttribute("status", status, null);
		if (home != null)
			ro.addAttribute("home", home, null);

		addSlotsXml(ro);
		addNameToXml(ro);
		addDescriptionXml(ro);
		addClassificationsXml(ro);
		addAuthorsXml(ro);
		addExternalIdentifiersXml(ro);

		return ro;
	}

	public void validate(IAssertionGroup er, ValidationContext vc,
			Set<String> knownIds) {
		if (vc.skipInternalStructure)
			return;
		
		validateTopAtts(er, vc);

		validateSlots(er, vc);

		validateClassifications(er, vc);

		validateExternalIdentifiers(er, vc, externalIdentifierDescription, "ITI TF-3 4.1.3");

		verifyIdsUnique(er, knownIds);
		
		verifyNotReferenceSelf(er);
	}
	
	void verifyNotReferenceSelf(IAssertionGroup er) {
		if (source.equals(id))
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + " sourceObject attribute references self", "???"), this);
		if (target.equals(id))
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + " targetObject attribute references self", "???"), this);
	}

	static List<String> assocs_with_documentation = 
		Arrays.asList(
				MetadataSupport.assoctype_rplc,
				MetadataSupport.assoctype_xfrm,
				MetadataSupport.assoctype_apnd,
				MetadataSupport.assoctype_xfrm_rplc
		);

	public void validateClassifications(IAssertionGroup er, ValidationContext vc) {

		er.challenge("Classifications present are legal");

		List<Classification> c = getClassificationsByClassificationScheme(MetadataSupport.XDSAssociationDocumentation_uuid);
		if (c.size() == 0)
			;
		else if (c.size() > 1)
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() +
					": may contain only a single documentation classification (classificationScheme=" + 
					MetadataSupport.XDSAssociationDocumentation_uuid + ")", "ITI TF-3 4.1.6.1"), this);
		else {
			if (!assocs_with_documentation.contains(type))
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() +
						": documentation classification (classificationScheme=" + 
						MetadataSupport.XDSAssociationDocumentation_uuid + 
						") may only be present on the following association types: " + 
						assocs_with_documentation, "ITI TF-3 4.1.6.1"), this);
		}
		er.challenge("Required Classifications present");
		er.challenge("Classifications coded correctly");
	}
	
	List<String> relationship_assocs = 
		Arrays.asList(
				MetadataSupport.assoctype_rplc,
				MetadataSupport.assoctype_apnd,
				MetadataSupport.assoctype_xfrm,
				MetadataSupport.assoctype_xfrm_rplc
				);

	public void validateTopAtts(IAssertionGroup er, ValidationContext vc) {
		validateId(er, vc, "entryUUID", id, null);

		validateId(er, vc, "sourceObject", source, null);
		validateId(er, vc, "targetObject", target, null);
		
		
		boolean muReq = vc.isMU && vc.isRequest;
		boolean basicType = assocTypes.contains(type);
		boolean muType = assocTypesMU.contains(type);
				
		if (muReq) {
			if (basicType == false && muType == false)
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + ": associationType " + type + " unknown. Known assocationTypes are " + assocTypes + " and " + assocTypesMU, "ITI TF-3 Table 4.1-2.1"), this);
		}
				
		else if (vc.isResponse) {
			if (!assocTypes.contains(type) && !assocTypesMU.contains(type))
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + ": associationType " + type + " unknown. Known assocationTypes are " + assocTypes + " and " + assocTypesMU, "ITI TF-3 Table 4.1-2.1"), this);
		}
		
		else if (!assocTypes.contains(type))
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + ": associationType " + type + " unknown. Known assocationTypes are " + assocTypes, "ITI TF-3 Table 4.1-2.1"), this);

		
	}

	public void validateRequiredSlotsPresent(IAssertionGroup er,
			ValidationContext vc) {
//		Metadata m = getMetadata();
//		if (type.equals(MetadataSupport.assoctype_has_member) &&
//				m.isSubmissionSet(source) &&
//				m.isDocument(target)) {
//			if (getSlot(MetadataSupport.assoc_slot_submission_set_status) == null)
//				er.err(identifyingString() + ": SubmissionSet to DocumentEntry HasMember association must have a SubmissionSetStatus Slot", "ITI TF-3: 4.1.4.1");
//		} else {
//			
//		}

	}

	public void validateSlotsCodedCorrectly(IAssertionGroup er,
			ValidationContext vc) {
		Slot s = getSlot(MetadataSupport.assoc_slot_submission_set_status);
		if (s == null)
			return;
		if (s.getValues().size() == 1) {
			String value = s.getValues().get(0);
			if ("Original".equals(value) || "Reference".equals(value))
				;
			else
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + ": SubmissionSetStatus Slot can only take value Original or Reference", "ITI TF-3: 4.1.4.1"), this);
		} else {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext(identifyingString() + ": SubmissionSetStatus Slot must have only single value", "ITI TF-3: 4.1.4.1"), this);
		}
	}

	public void validateSlotsLegal(IAssertionGroup er) {
		// work done by validateRequiredSlotsPresent
	}

}
