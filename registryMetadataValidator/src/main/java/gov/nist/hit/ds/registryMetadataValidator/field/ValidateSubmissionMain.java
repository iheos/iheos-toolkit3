package gov.nist.hit.ds.registryMetadataValidator.field;

import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.TextErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.registryMetadata.MetadataParser;
import gov.nist.hit.ds.valSupport.client.ValidationContext;

import java.io.File;

public class ValidateSubmissionMain  {

	static public void main(String[] args) {
		Metadata m;
		ValidateSubmissionMain main = new ValidateSubmissionMain();
		String sampleDir = "/Users/bill/dev/sampleSubmissions/";
		
		IAssertionGroup er = new TextErrorRecorder();

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
