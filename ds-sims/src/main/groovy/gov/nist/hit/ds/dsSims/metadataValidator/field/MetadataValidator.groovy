package gov.nist.hit.ds.dsSims.metadataValidator.field

import gov.nist.hit.ds.dsSims.client.ValidationContext
import gov.nist.hit.ds.dsSims.metadataValidator.RegistryValidationInterface
import gov.nist.hit.ds.dsSims.metadataValidator.object.Association
import gov.nist.hit.ds.dsSims.metadataValidator.object.DocumentEntry
import gov.nist.hit.ds.dsSims.metadataValidator.object.Folder
import gov.nist.hit.ds.dsSims.metadataValidator.object.SubmissionSet
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.metadata.Metadata
import gov.nist.hit.ds.metadata.MetadataSupport
import gov.nist.hit.ds.xdsException.XdsInternalException
import org.apache.axiom.om.OMElement

public class MetadataValidator {
	Metadata m;
	ValidationContext vc;
	RegistryValidationInterface rvi;

	public MetadataValidator(Metadata m, ValidationContext vc, RegistryValidationInterface rvi) {
		this.m = m;
		this.vc = vc;
		this.rvi = rvi;
	}
	
	public void run(ErrorRecorder er) {
		runObjectStructureValidation(er);
		runCodeValidation(er);
		runSubmissionStructureValidation(er);
	}
	
	public void runCodeValidation(ErrorRecorder er)   {
		CodeValidation cv = new CodeValidation(m);
		try {
			cv.setValidationContext(vc);
		} catch (XdsInternalException e) {
			er.err(XdsErrorCode.Code.XDSRegistryError, e);
			return;
		}
		cv.run(er);
	}

	public void runObjectStructureValidation(ErrorRecorder er)   {
		
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
			if (m.getSubmissionSetIds().contains(id)) continue
			else if (m.getFolderIds().contains(id)) continue
			else
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, "RegistryPackage(" + id + ") : is not classified as SubmissionSet or Folder", this, "ITI TF-3: 4.1.9.1");
		}
	}

	public void runSubmissionStructureValidation(ErrorRecorder er) {
		new SubmissionStructure(m, rvi).run(er, vc);
	}
	

}
