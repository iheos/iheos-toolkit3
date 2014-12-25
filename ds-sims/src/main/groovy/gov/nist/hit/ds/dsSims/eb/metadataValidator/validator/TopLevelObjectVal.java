package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator;

import gov.nist.hit.ds.dsSims.eb.client.ValidationContext;
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder;

import java.util.Set;

public interface TopLevelObjectVal {
	public void validate(ErrorRecorder er, ValidationContext vc, Set<String> knownIds);
}
