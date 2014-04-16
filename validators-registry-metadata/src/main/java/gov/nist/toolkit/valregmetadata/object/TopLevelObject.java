package gov.nist.toolkit.valregmetadata.object;

import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.toolkit.valsupport.client.ValidationContext;

import java.util.Set;

public interface TopLevelObject {
	public void validate(IAssertionGroup er, ValidationContext vc, Set<String> knownIds);
}
