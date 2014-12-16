package gov.nist.hit.ds.dsSims.eb.metadataValidator
import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.metadataValidator.engine.MetadataValidator
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.dsSims.eb.metadata.Metadata
import gov.nist.hit.ds.dsSims.eb.metadata.MetadataContainer
import gov.nist.hit.ds.dsSims.eb.metadata.MetadataParser
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.v2compatibility.MessageValidatorEngine
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.hit.ds.soapSupport.SoapFaultException
import org.apache.axiom.om.OMElement

public class MetadataMessageValidator extends ValComponentBase {
	OMElement xml
	Metadata m = null
	RegistryValidationInterface rvi
    SimHandle handle
    ValidationContext vc

	public Metadata getMetadata() { return m; }

    MetadataMessageValidator(SimHandle handle, OMElement xml, ValidationContext vc, RegistryValidationInterface rvi) {
        super(handle.event)
        this.handle = handle
        this.xml = xml
        this.rvi = rvi;
        this.vc = vc
    }

    @Validation(id="MetaParse001", msg="Parse Metadata", ref="??")
    def metaParse001() throws SoapFaultException {
        try {
            m = MetadataParser.parseNonSubmission(xml);
            handle.event.addArtifact('Content Summary', contentSummary())
        } catch (Exception e) {
            fail(e.getMessage())
        }
    }

    void runAfter() {
        new MetadataValidator(handle, m, vc, rvi).asPeer().run()
    }

    public void run(ErrorRecorder er, MessageValidatorEngine mvc) {
		this.er = er;
		
		if (xml == null) {
			er.err(XdsErrorCode.Code.XDSRegistryError, "MetadataMessageValidator: top element null", this, "");
			return;
		}
		
		

		try {
			m = MetadataParser.parseNonSubmission(xml);
			
			// save on validation stack so others can find it if they need it
			mvc.addMessageValidator("MetadataContainer", new MetadataContainer(vc, m), erBuilder.buildNewErrorRecorder());

			
//			contentSummary(er, m);
			
			MetadataValidator mv = new MetadataValidator(handle, m, vc, rvi);
			mv.runObjectStructureValidation(er);
			mv.runCodeValidation(er);
			mv.runSubmissionStructureValidation(er);
			

		} catch (Exception e) {
			er.err(XdsErrorCode.Code.XDSRegistryError, e);
		}

		er.finish();

	}
	
	String contentSummary() {
        StringBuffer buf = new StringBuffer()
		buf.append(m.getSubmissionSetIds().size()).append(" SubmissionSets  ")
        buf.append(m.getExtrinsicObjectIds().size()).append(" DocumentEntries  ")
        buf.append(m.getFolderIds().size()).append(" Folders  ")
        buf.append(m.getAssociationIds().size()).append(" Associations  ")
		if (m.getSubmissionSetIds().size() == 0 &&
				m.getExtrinsicObjectIds().size() == 0 &&
				m.getFolderIds().size() == 0 &&
				m.getAssociationIds().size() == 0)
            buf.append(m.getObjectRefIds().size()).append(" ObjectRefs")
        return buf.toString()
	}

}
