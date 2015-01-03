package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator

import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.MetadataModel
import gov.nist.hit.ds.dsSims.eb.metadataValidator.parser.MetadataParser
import gov.nist.hit.ds.ebMetadata.Metadata
import gov.nist.hit.ds.simSupport.simulator.SimHandle

public class MetadataVal {
	Metadata m
	ValidationContext vc
    SimHandle simHandle

	RegistryValidationInterface rvi
    MetadataModel model

	public MetadataVal(SimHandle _simHandle, Metadata _m, ValidationContext _vc, RegistryValidationInterface _rvi) {
        simHandle = _simHandle
		m = _m
		vc = _vc
		rvi = _rvi
	}
	
	public void run() {
        model = new MetadataParser().run(m, vc)
		runObjectStructureValidation()
		runCodeValidation()
		runSubmissionStructureValidation()
	}
	
	public void runCodeValidation()   {
		CodeValidation cv = new CodeValidation(m);
		cv.setValidationContext(vc);
		cv.run();
	}

	public void runObjectStructureValidation()   {
		
		if (vc.skipInternalStructure)
			return;

        // model.knownIds starts out empty.  Each validation updates it. This is used
        // to check uniqueness of the id attribute.
        // TODO - model.knownIds needs expansion to validate uniqueness of uniqueIds.
		
        model.submissionSets.each { new SubmissionSetValidator(simHandle, it, vc, model.knownIds).asChild().run() }

        model.docEntries.each { new DocumentEntryValidator(simHandle, it, vc, model.knownIds).asChild().run() }

        model.folders.each { new FolderValidator(simHandle, it, vc, model.knownIds).asChild().run() }

        model.assocs.each { new AssociationValidator(simHandle, it, vc, model.knownIds).asChild().run() }
	}

	public void runSubmissionStructureValidation() {
		new SubmissionStructureValidator(simHandle, vc, m, rvi).asChild().run();
	}
	

}
