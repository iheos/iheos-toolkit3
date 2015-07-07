package gov.nist.hit.ds.dsSims.eb.metadataValidator.model

import gov.nist.hit.ds.ebMetadata.Metadata
import gov.nist.hit.ds.ebMetadata.MetadataSupport
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.xdsExceptions.XdsInternalException
import gov.nist.toolkit.valsupport.client.ValidationContext
import org.apache.axiom.om.OMElement

public class InternalClassificationModel extends AbstractRegistryObjectModel {
	String classifiedObjectId;
	String classificationNode;
	
	public InternalClassificationModel(String id, String classifiedObject, String classificationNode) {
		super(id);
		this.classifiedObjectId = classifiedObject;
		this.classificationNode = classificationNode;
	}
	
	public boolean equals(InternalClassificationModel ic) {
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
	
	public String getClassificationNode() {
		return classificationNode;
	}
	
	public InternalClassificationModel(Metadata m, OMElement ro) throws XdsInternalException {
		super(m, ro);
		classifiedObjectId = ro.getAttributeValue(MetadataSupport.classified_object_qname);
		classificationNode = ro.getAttributeValue(MetadataSupport.classificationnode_qname);
	}
	
	static public boolean isInternalClassification(OMElement ele) {
		return ele.getAttribute(MetadataSupport.classificationnode_qname) != null;
	}

	public String identifyingString() {
		return "Classification(" + getId() + ")";
	}

	public OMElement toXml() throws XdsInternalException  {
		ro = MetadataSupport.om_factory.createOMElement(MetadataSupport.classification_qnamens);
		ro.addAttribute("id", id, null);
		ro.addAttribute("classifiedObject", classifiedObjectId, null);
		ro.addAttribute("classificationNode", classificationNode, null);
 
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

	public void validateRequiredSlotsPresent(ErrorRecorder er,
			ValidationContext vc) {
	}

	public void validateSlotsCodedCorrectly(ErrorRecorder er,
			ValidationContext vc) {
	}

	public void validateSlotsLegal(ErrorRecorder er) {
	}

}
