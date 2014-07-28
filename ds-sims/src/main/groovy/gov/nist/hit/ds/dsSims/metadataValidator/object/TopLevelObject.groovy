package gov.nist.hit.ds.dsSims.metadataValidator.object

import gov.nist.hit.ds.dsSims.client.ValidationContext
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder

public interface TopLevelObject {
	public void validate(ErrorRecorder er, ValidationContext vc, Set<String> knownIds);
}
