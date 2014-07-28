package gov.nist.hit.ds.dsSims.metadataValidator.field

import gov.nist.hit.ds.dsSims.client.ValidationContext
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.TextErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.metadata.Metadata
import gov.nist.hit.ds.metadata.MetadataParser

public class ValidateSubmissionMain  {

	static public void main(String[] args) {
		Metadata m;
		ValidateSubmissionMain main = new ValidateSubmissionMain();
		String sampleDir = "/Users/bill/dev/sampleSubmissions/";
		
		ErrorRecorder er = new TextErrorRecorder();

		try {
			m = MetadataParser.parseNonSubmission(new File(sampleDir + args[0]));
			ValidationContext vc = new ValidationContext();
			vc.isR = true;
			vc.skipInternalStructure = true;
			MetadataValidator mv = new MetadataValidator(m, vc, null);
			mv.runObjectStructureValidation(er);
			mv.runCodeValidation(er);
			mv.runSubmissionStructureValidation(er);
			
			er.finish();

		} catch (Exception e) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, e);
		}
		
		er.showErrorInfo();
	}
	

}
