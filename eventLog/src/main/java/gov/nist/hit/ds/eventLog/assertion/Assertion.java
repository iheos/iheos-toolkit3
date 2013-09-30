package gov.nist.hit.ds.eventLog.assertion;

import gov.nist.hit.ds.utilities.csv.CSVEntry;
import gov.nist.hit.ds.utilities.datatypes.RequiredOptional;

public class Assertion {
	static final String[] columnNames = new String[] { "Name", "ID", "STATUS", "EXPECTED", "FOUND", "MSG", "CODE", "LOCATION", "REFERENCE" };
	String id = "";
	String name = "";
	AssertionStatus status = AssertionStatus.SUCCESS;
	String found = "";
	String expected = "";
	String[] reference = {};
	String msg = "";
	String code = "";
	String location = "";
	RequiredOptional requiredOptional = RequiredOptional.R;
	
	public RequiredOptional getRequiredOptional() {
		return requiredOptional;
	}

	public Assertion setRequiredOptional(RequiredOptional requiredOptional) {
		this.requiredOptional = requiredOptional;
		return this;
	}

	public Assertion() {
		
	}
	
	public Assertion(CSVEntry entry) {
		setEntry(entry);
	}
	
	public boolean failed() {
		return status.isError();
	}
	
	public CSVEntry getEntry() {
		CSVEntry entry = new CSVEntry();
		
		entry.
		add(name).
		add(id).
		add(status.name()).
		add(requiredOptional.name()).
		add(expected).
		add(found).
		add(msg).
		add(code).
		add(location).
		add(buildSemiDivided(reference));
		
		return entry;
	}
	
	Assertion setEntry(CSVEntry entry) {
		name = entry.get(0);
		id = entry.get(1);
		status = AssertionStatus.valueOf(entry.get(2));
		requiredOptional = RequiredOptional.valueOf(entry.get(3));
		expected = entry.get(4);
		found = entry.get(5);
		msg = entry.get(6);
		code = entry.get(7);
		location = entry.get(8);
		reference = parseSemiDivided(entry.get(9));
		
		return this;
	}
	
	static public String buildSemiDivided(String[] values) {
		StringBuffer buf = new StringBuffer();
		
		if (values.length > 0)
			buf.append(values[0]);
		for (int i=1; i<values.length; i++) {
			buf.append(";").append(values[i]);
		}
		
		return buf.toString();
	}
	
	static public String[] parseSemiDivided(String in) {
		if (in == null)
			return new String[] { };
		String[] values = in.split(";");
		if (values == null) {
			String[] val = { in } ;
			return  val;
		}
		return values;
	}
	
	public String toString() {
		return getEntry().toString();
	}
	
	public String[] getColumnNames() {
		return columnNames;
	}
	
	public Assertion setLocation(String location) {
		this.location = location;
		return this;
	}
	
	public String getLocation() {
		return location;
	}
	
	public Assertion setCode(String code) {
		this.code = code;
		return this;
	}
	
	public String getCode() {
		return code;
	}
	
	public Assertion setMsg(String msg) {
		this.msg = msg;
		return this;
	}
	
	public String getMsg() {
		return msg;
	}
	
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
	
	public Assertion setFound(int found) {
		this.found = Integer.toString(found);
		return this;
	}
	
	public String getExpected() {
		return expected;
	}
	public Assertion setExpected(String expected) {
		this.expected = expected;
		return this;
	}
	public Assertion setExpected(int expected) {
		this.expected = Integer.toString(expected);
		return this;
	}
	public String[] getReference() {
		return reference;
	}
	public Assertion setReference(String[] reference) {
		this.reference = reference;
		return this;
	}
}
