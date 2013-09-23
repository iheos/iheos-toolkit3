package gov.nist.hit.ds.registryMetadataValidator.object;

import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.valSupport.client.ValidationContext;

import java.util.Set;

public interface TopLevelObject {
	public void validate(IAssertionGroup er, ValidationContext vc, Set<String> knownIds);
}
