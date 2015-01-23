package gov.nist.hit.ds.testClient.metadataValidator.object;

import gov.nist.hit.ds.testClient.logging.ErrorRecorder;
import gov.nist.hit.ds.testClient.logging.ValidationContext;

import java.util.Set;

public interface TopLevelObject {
	public void validate(ErrorRecorder er, ValidationContext vc, Set<String> knownIds);
}
