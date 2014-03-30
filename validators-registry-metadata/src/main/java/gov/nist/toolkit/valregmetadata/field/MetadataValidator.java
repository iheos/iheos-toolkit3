package gov.nist.toolkit.valregmetadata.field;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.xdsException.XdsInternalException;
import gov.nist.toolkit.registrymetadata.Metadata;
import gov.nist.toolkit.registrysupport.MetadataSupport;
import gov.nist.toolkit.valregmetadata.object.Association;
import gov.nist.toolkit.valregmetadata.object.DocumentEntry;
import gov.nist.toolkit.valregmetadata.object.Folder;
import gov.nist.toolkit.valregmetadata.object.SubmissionSet;
import gov.nist.toolkit.valsupport.client.ValidationContext;
import gov.nist.toolkit.valsupport.registry.RegistryValidationInterface;
import org.apache.axiom.om.OMElement;

import java.util.HashSet;
import java.util.Set;

public class MetadataValidator {
	Metadata m;
	ValidationContext vc;
	RegistryValidationInterface rvi;

	public MetadataValidator(Metadata m, ValidationContext vc, RegistryValidationInterface rvi) {
		this.m = m;
		this.vc = vc;
		this.rvi = rvi;
	}
	
	public void run(IAssertionGroup er) {
		runObjectStructureValidation(er);
		runCodeValidation(er);
		runSubmissionStructureValidation(er);
	}
	
	public void runCodeValidation(IAssertionGroup er)   {
		CodeValidation cv = new CodeValidation(m);
		try {
			cv.setValidationContext(vc);
		} catch (XdsInternalException e) {
			er.err(XdsErrorCode.Code.XDSRegistryError, e);
			return;
		}
		cv.run(er);
	}

	public void runObjectStructureValidation(IAssertionGroup er)   {
		
		if (vc.skipInternalStructure)
			return;
		
		er.sectionHeading("Evaluating metadata object structure");

		Set<String> knownIds = new HashSet<String>();

		for (OMElement ssEle : m.getSubmissionSets()) {
			er.sectionHeading("SubmissionSet(" + ssEle.getAttributeValue(MetadataSupport.id_qname) + ")");
			SubmissionSet s = null;
			try {
				s = new SubmissionSet(m, ssEle);
			} catch (XdsInternalException e) {
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, e);
				continue;
			}
			s.validate(er, vc, knownIds);
		}
		
		for (OMElement deEle : m.getExtrinsicObjects() ) {
			er.sectionHeading("DocumentEntry(" + deEle.getAttributeValue(MetadataSupport.id_qname) + ")");
			DocumentEntry de = null;
			try {
				de = new DocumentEntry(m, deEle);
			} catch (XdsInternalException e) {
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, e);
				continue;
			}
			de.validate(er, vc, knownIds);
		}
		
		for (OMElement fEle : m.getFolders()) {
			er.sectionHeading("Folder(" + fEle.getAttributeValue(MetadataSupport.id_qname) + ")");
			Folder f = null;
			try {
				f = new Folder(m, fEle);
			} catch (XdsInternalException e) {
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, e);
				continue;
			}
			f.validate(er, vc, knownIds);
		}
		
		for (OMElement aEle : m.getAssociations()) {
			er.sectionHeading("Association(" + aEle.getAttributeValue(MetadataSupport.id_qname) + ")");
			Association a = null;
			try {
				a = new Association(m, aEle, vc);
			} catch (XdsInternalException e) {
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, e);
				continue;
			}
			a.validate(er, vc, knownIds);
		}
		
		er.sectionHeading("Other metadata objects");
		
		for (String id : m.getRegistryPackageIds()) {
			if (m.getSubmissionSetIds().contains(id))
				;
			else if (m.getFolderIds().contains(id))
				;
			else
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext("RegistryPackage(" + id + ") : is not classified as SubmissionSet or Folder", "ITI TF-3: 4.1.9.1"), this);
		}
	}

	public void runSubmissionStructureValidation(IAssertionGroup er) {
		new SubmissionStructure(m, rvi).run(er, vc);
	}
	

}
