package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator

import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.MetadataModel
import gov.nist.hit.ds.dsSims.eb.metadataValidator.parser.MetadataParser
import gov.nist.hit.ds.ebMetadata.Metadata
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.toolkit.environment.Environment

public class MetadataVal extends ValComponentBase {
	Metadata m
	ValidationContext vc
    SimHandle simHandle
    Environment environment

	RegistryValidationInterface rvi
    MetadataModel model

	public MetadataVal(SimHandle _simHandle, Metadata _m, ValidationContext _vc, Environment _environment, RegistryValidationInterface _rvi) {
        simHandle = _simHandle
		m = _m
		vc = _vc
		rvi = _rvi
        environment = _environment
	}
	
	public void run() {
        model = new MetadataParser().run(m, vc)
        runSubmissionStructureValidation()
		runObjectStructureValidation()
		runCodeValidation()
	}
	
	public void runCodeValidation()   {
		CodeValidation cv = new CodeValidation(m, environment);
		cv.setValidationContext(vc);
		cv.run();
	}

	public void runObjectStructureValidation()   {
		
		if (vc.skipInternalStructure)
			return;

        // model.knownIds starts out empty.  Each validation updates it. This is used
        // to check uniqueness of the id attribute.
        // TODO - model.knownIds needs expansion to validate uniqueness of uniqueIds.
		
        model.submissionSets.each {
            SubmissionSetValidator val = new SubmissionSetValidator(simHandle, it, vc, model.knownIds)
            val.name = it.identifyingString()
            val.asPeer().run()
        }

        model.docEntries.each {
            def val = new DocumentEntryValidator(simHandle, it, vc, model.knownIds)
            val.name =  it.identifyingString()
            val.asPeer().run()
        }

        model.folders.each {
            def val = new FolderValidator(simHandle, it, vc, model.knownIds)
            val.name =  it.identifyingString()
            val.asPeer().run()
        }

        model.assocs.each {
            def val = new AssociationValidator(simHandle, it, vc, model.knownIds)
            val.name =  it.identifyingString()
            val.asPeer().run() }
	}

	public void runSubmissionStructureValidation() {
		def val = new SubmissionStructureValidator(simHandle, vc, m, rvi)
        val.name = 'SubmissionStructure'
        val.asPeer().run();
	}
	

}
