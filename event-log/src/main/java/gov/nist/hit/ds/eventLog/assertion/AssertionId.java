package gov.nist.hit.ds.eventLog.assertion;

public class AssertionId {
	String id;
	
	public AssertionId(String id) {
		this.id = id;
	}
	
	public String toString() { return id; }
	
	public String getId() { return id; }
}
