package gov.nist.hit.ds.eventLog.assertion;

import gov.nist.hit.ds.utilities.csv.CSVEntry;
import gov.nist.hit.ds.utilities.datatypes.RequiredOptional;

public class Assertion {
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

	/**
	 * The following elements are all sensitive to the order and identity of the fields defined.
	 */
	
	static final String[] columnNames = new String[] { "ID", "STATUS", "R/O", "EXPECTED", "FOUND", "MSG", "CODE", "LOCATION", "REFERENCE" };

	public CSVEntry getEntry() {
		CSVEntry entry = new CSVEntry();
		
		entry.
		add(id).    					// 0
		add(status.name()).				// 1
		add(requiredOptional.name()).	// 2
		add(nocomma(expected)).					// 3
		add(nocomma(found)).						// 4
		add(nocomma(msg)).						// 5
		add(code).						// 6
		add(location).					// 7
		add(nocomma(buildSemiDivided(reference))); // 8
		
		return entry;
	}
	
	Assertion setEntry(CSVEntry entry) {
		id = entry.get(0);
		status = AssertionStatus.valueOf(entry.get(1));
		requiredOptional = RequiredOptional.valueOf(entry.get(2));
		expected = entry.get(3);
		found = entry.get(4);
		msg = entry.get(5);
		code = entry.get(6);
		location = entry.get(7);
		reference = parseSemiDivided(entry.get(8));
		
		return this;
	}
	
	/**
	 * The above elements are all sensitive to the order and identity of the fields defined.
	 */

	String nocomma(String in) {
		if (in == null) return "";
		return in.replaceAll(",", ";");
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
