package gov.nist.hit.ds.eventLog.assertion;

public class Assertion {
	String id = "";
	String name = "";
	AssertionStatus status = AssertionStatus.SUCCESS;
	String found = "";
	String expected = "";
	String reference = "";
	
	public String getId() {
		return id;
	}
	public Assertion setId(String id) {
		this.id = id;
		return this;
	}
	public String getName() {
		return name;
	}
	public Assertion setName(String name) {
		this.name = name;
		return this;
	}
	public AssertionStatus getStatus() {
		return status;
	}
	public Assertion setStatus(AssertionStatus status) {
		this.status = status;
		return this;
	}
	public String getFound() {
		return found;
	}
	public Assertion setFound(String found) {
		this.found = found;
		return this;
	}
	public String getExpected() {
		return expected;
	}
	public Assertion setExpected(String expected) {
		this.expected = expected;
		return this;
	}
	public String getReference() {
		return reference;
	}
	public Assertion setReference(String reference) {
		this.reference = reference;
		return this;
	}
}
