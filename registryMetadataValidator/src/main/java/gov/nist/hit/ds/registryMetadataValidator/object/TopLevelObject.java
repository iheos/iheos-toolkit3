package gov.nist.hit.ds.registryMetadataValidator.object;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.valSupport.client.ValidationContext;

import java.util.Set;

public interface TopLevelObject {
	public void validate(ErrorRecorder er, ValidationContext vc, Set<String> knownIds);
}
