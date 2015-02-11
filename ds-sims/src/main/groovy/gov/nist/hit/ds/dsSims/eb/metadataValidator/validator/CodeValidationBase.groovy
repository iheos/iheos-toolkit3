package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator;

import gov.nist.hit.ds.dsSims.eb.client.ValidationContext;
import gov.nist.hit.ds.dsSims.eb.metadataValidator.factory.CodesFactory;
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.ClassificationModel;
import gov.nist.hit.ds.ebMetadata.Metadata;
import gov.nist.hit.ds.ebMetadata.MetadataSupport;
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.toolkit.environment.Environment;
import gov.nist.hit.ds.utilities.xml.XmlUtil;
import gov.nist.hit.ds.xdsExceptions.MetadataException;
import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException;
import org.apache.axiom.om.OMContainer;
import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;

import javax.xml.namespace.QName;
import java.util.*;

public class CodeValidationBase {
	Metadata m;
	OMElement codes = null;
	List<String> assigning_authorities;
	HashMap<String, String> mime_map;  // mime => ext
	HashMap<String, String> ext_map;   // ext => mime
	Exception startUpError = null;
	ValidationContext vc = null;
    Environment environment;
	
	static String ADConfigError = "ITI TF-3: 4.1.10";
	static Logger logger = Logger.getLogger(CodeValidationBase.class);


	CodeValidationBase(Environment _environment) { environment = _environment }

	public void setValidationContext(ValidationContext vc) {
		this.vc = vc;
		loadCodes(environment);
	}

    void loadCodes(Environment environment) {
        codes = CodesFactory.getCodes(environment);

		assigning_authorities = new ArrayList<String>();
		for (OMElement aa_ele : XmlUtil.childrenWithLocalName(codes, "AssigningAuthority"))
		{
			this.assigning_authorities.add(aa_ele.getAttributeValue(MetadataSupport.id_qname));
		}

		build_mime_map();
	}

	void build_mime_map()  {
		QName name_att_qname = new QName("name");
		QName code_att_qname = new QName("code");
		QName ext_att_qname = new QName("ext");
		OMElement mime_type_section = null;
		for(@SuppressWarnings("unchecked")
		Iterator<OMElement> it=codes.getChildrenWithName(new QName("CodeType")); it.hasNext();  ) {
			OMElement ct = it.next();
			if (ct.getAttributeValue(name_att_qname).equals("mimeType")) {
				mime_type_section = ct;
				break;
			}
		}
		if (mime_type_section == null) throw new ToolkitRuntimeException("CodeValidation2.java: Configuration Error: Cannot findSimple mime type table");

		mime_map = new HashMap<String, String>();
		ext_map = new HashMap<String, String>();

		for(@SuppressWarnings("unchecked")
		Iterator<OMElement> it=mime_type_section.getChildElements(); it.hasNext();  ) {
			OMElement code_ele = it.next();
			String mime_type = code_ele.getAttributeValue(code_att_qname);
			String ext = code_ele.getAttributeValue(ext_att_qname);
			mime_map.put(mime_type, ext);
			ext_map.put(ext, mime_type);
		}
	}

	String[] assocClassifications = [
			"XFRM", "APND", "RPLC", "XFRM_RPLC"
	];

	public boolean isValidMimeType(String mime_type) {
		return mime_map.containsKey(mime_type);
	}

	public Collection<String> getKnownFileExtensions() {
		return ext_map.keySet();
	}

	public String getMimeTypeForExt(String ext) {
		return ext_map.get(ext);
	}

	public String getExtForMimeType(String mime_type) {
		return mime_map.get(mime_type);
	}

	// next 3 copied from SubmissionStructure.java
	String objectType(String id) {
		if (id == null)
			return "null";
		if (m.getSubmissionSetIds().contains(id))
			return "SubmissionSet";
		if (m.getExtrinsicObjectIds().contains(id))
			return "DocumentEntry";
		if (m.getFolderIds().contains(id))
			return "Folder";
		if (m.getAssociationIds().contains(id))
			return "Association";
		return "Unknown";
	}
	
	String objectDescription(String id) {
		return objectType(id) + "(" + id + ")";
	}
	
	String objectDescription(OMElement ele) {
		String objectType = objectType(m.getId(ele));
		if (objectType.equals("Unknown"))
			objectType = ele.getLocalName();
		return objectType + "(" + m.getId(ele) + ")";
	}
	
	String getObjectTypeById(ErrorRecorder er, String id) {
		try {
			return m.getObjectTypeById(id);
		} catch (MetadataException e) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, e);
			return null;
		}
	}
	
	OMElement getObjectById(ErrorRecorder er, String id) {
		try {
			return m.getObjectById(id);
		} catch (MetadataException e) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, e);
			return null;
		}
	}
	
	String getSimpleAssocType(ErrorRecorder er, OMElement assoc) {
		try {
			return m.getSimpleAssocType(assoc);
		} catch (MetadataException e) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, e);
			return null;
		}
	}

	void cannotValidate(ErrorRecorder er, ClassificationModel c) {
		er.err(XdsErrorCode.Code.XDSRegistryMetadataError, c.identifyingString() + ": cannot validate code - error parsing Classification", this, "ebRIM section 4.3");
	}

	public void run(ErrorRecorder er) {
		if (startUpError != null) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, startUpError);
			return;
		}
		
		er.sectionHeading("Evaluating use of Affinity Domain coding");
		
	
		List<String> all_object_ids = m.getObjectIds(m.getAllObjects());
	
		for (String obj_id : all_object_ids) {
			List<OMElement> classifications = null;
	
			try {
				classifications = m.getClassifications(obj_id);
			} catch (MetadataException e) {
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, e);
				continue;
			}
	
			for (OMElement cl_ele : classifications) {
	
				ClassificationModel cl = null;
//				try {
					cl = new ClassificationModel(m, cl_ele);
//				} catch (XdsInternalException e) {
//					er.err(XdsErrorCode.Code.XDSRegistryMetadataError, e);
//					continue;
//				}
				validate(er, cl);
	
				validateAssocClassifications(er, cl);
	
			}
		}
	
		for (OMElement doc_ele : m.getExtrinsicObjects()) {
			String mime_type = doc_ele.getAttributeValue(MetadataSupport.mime_type_qname);
			if ( !isValidMimeType(mime_type)) {
				if (vc.isXDM || vc.isXDR) 
					er.detail("Mime type, " + mime_type + ", is not available in this Affinity Domain");
				else
					er.err(XdsErrorCode.Code.XDSRegistryMetadataError, "Mime type, " + mime_type + ", is not available in this Affinity Domain", this, ADConfigError);
			} 
		}
	}

	// if classified object is an Association, only some types of Associations can
	// accept an associationDocumenation classification
	void validateAssocClassifications(ErrorRecorder er, ClassificationModel cl) {

		String classification_type = cl.getClassificationScheme();

		if (classification_type == null || !classification_type.equals(MetadataSupport.XDSAssociationDocumentation_uuid))
			return;  // not associationDocumenation classification

		String classified_object_id = cl.parent_id();
		String classified_object_type = getObjectTypeById(er, classified_object_id);
		if (classified_object_type == null)
			return;

		if ( !classified_object_type.equals("Association")) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, objectDescription(cl.getOwnerId()) + ": associationDocumentation Classification (" + MetadataSupport.XDSAssociationDocumentation_uuid + ") can only be used on Associations", this, "ITI TF-3: 4.1.6.1");
			return;
		}

		String assoc_id = classified_object_id;
		OMElement assoc_ele = getObjectById(er, assoc_id);
		if (assoc_ele == null) 
			return;
		String assoc_type = getSimpleAssocType(er, assoc_ele);
		for (int i=0; i<assocClassifications.length; i++) {
			String a = assocClassifications[i];
			if (a.equals(assoc_type))
				return;
		}
		er.err(XdsErrorCode.Code.XDSRegistryMetadataError, objectDescription(assoc_ele) + ": Association Type " + assoc_type + " cannot have an associationDocumentation classification", this, "ITI TF-3: 4.1.6.1");
	}
	
	void validate(ErrorRecorder er, ClassificationModel cl) {
		String classification_scheme = cl.getClassificationScheme();

		if (classification_scheme == null) {
			String classification_node = cl.getClassificationNode();
			if (classification_node == null || classification_node.equals("")) {
				cannotValidate(er, cl);
				return ;
			} else
				return;
		}
		if (classification_scheme.equals(MetadataSupport.XDSSubmissionSet_author_uuid))
			return;
		if (classification_scheme.equals(MetadataSupport.XDSDocumentEntry_author_uuid))
			return;
		String code = cl.getCodeValue();
		String coding_scheme = cl.getCodeScheme();

		if (code == null) {
			cannotValidate(er, cl);
			return ;
		}
		if (coding_scheme == null) {
			cannotValidate(er, cl);
			return;
		}
		for (OMElement code_type : XmlUtil.childrenWithLocalName(codes, "CodeType")) {
			String class_scheme = code_type.getAttributeValue(MetadataSupport.classscheme_qname);

			// some codes don't have classScheme in their definition
			if (class_scheme != null && !class_scheme.equals(classification_scheme))
				continue;

			for (OMElement code_ele : XmlUtil.childrenWithLocalName(code_type, "Code")) {
				String code_name = code_ele.getAttributeValue(MetadataSupport.code_qname);
				String code_scheme = code_ele.getAttributeValue(MetadataSupport.codingscheme_qname);
				if ( 	code_name.equals(code) && 
						(code_scheme == null || code_scheme.equals(coding_scheme) )
				) {
					return;
				}
			}
		}
		OMElement ele = cl.getElement();
		OMElement owner = null;
		if (ele != null) {
			OMContainer container = ele.getParent();
			if (container instanceof OMElement)
				owner = (OMElement) container;
		}
		if (vc.isXDM || vc.isXDR) 
			er.detail(objectDescription(owner) + ": the code " + coding_scheme + "(" + code + ") is not found in the Affinity Domain configuration");
		else
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, objectDescription(owner) + ": the code " + coding_scheme + "(" + code + ") is not found in the Affinity Domain configuration", this, "ITI TF-3: 4.1.10");
	}




}
