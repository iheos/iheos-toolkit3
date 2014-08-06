package gov.nist.hit.ds.eventLog.assertion.annotations;

public class AssertionReference {
	String reference;
	
	public AssertionReference(String ref) {
		this.reference = ref;
	}
	
	public String getReference() { return reference; }
}
