package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator
import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.metadata.Metadata
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.MetadataModel
import gov.nist.hit.ds.dsSims.eb.metadataValidator.parser.MetadataParser
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.xdsException.XdsInternalException

public class MetadataVal {
	Metadata m
	ValidationContext vc
	RegistryValidationInterface rvi
    MetadataModel model

	public MetadataVal(Metadata m, ValidationContext vc, RegistryValidationInterface rvi) {
		this.m = m
		this.vc = vc
		this.rvi = rvi
	}
	
	public void run(ErrorRecorder er) {
        model = new MetadataParser().run(m, vc)
		runObjectStructureValidation(er)
		runCodeValidation(er)
		runSubmissionStructureValidation(er)
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
		
        model.submissionSets.each { new SubmissionSetVal(it).validate(er, vc, model.knownIds) }

        model.docEntries.each { new DocumentEntryVal(it).validate(er, vc, model.knownIds) }

        model.folders.each { new FolderVal(it).validate(er, vc, model.knownIds) }

        model.assocs.each { new AssociationVal(it).validate(er, vc, model.knownIds) }

		for (String id : m.getRegistryPackageIds()) {
			if (m.getSubmissionSetIds().contains(id))
				;
			else if (m.getFolderIds().contains(id))
				;
			else
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, "RegistryPackage(" + id + ") : is not classified as SubmissionSet or Folder", this, "ITI TF-3: 4.1.9.1");
		}
	}

	public void runSubmissionStructureValidation(ErrorRecorder er) {
		new SubmissionStructureVal(m, rvi).run(er, vc);
	}
	

}
