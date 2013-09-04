package gov.nist.hit.ds.eventLog.assertion;

import gov.nist.hit.ds.utilities.csv.CSVEntry;
import gov.nist.hit.ds.utilities.csv.CSVTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssertionGroup {
	String[] columnNames = new String[] { "Name", "ID", "STATUS", "EXPECTED", "FOUND", "REFERENCE" };
	CSVTable assertionTable = new CSVTable();
	AssertionStatus maxStatus = AssertionStatus.SUCCESS;
	
	public AssertionGroup() {
		addRow(Arrays.asList(columnNames));
	}
	
	public CSVTable getTable() { return assertionTable; }
	
	public AssertionStatus getMaxStatus() { return maxStatus; }
	
	public AssertionGroup addAssertion(Assertion asser) {
		if (asser.getStatus().ordinal() > maxStatus.ordinal())
			maxStatus = asser.getStatus();
			
		List<String> values = new ArrayList<String>();
		values.add(asser.getName());
		values.add(asser.getId());
		values.add(asser.getStatus().name());
		values.add(asser.getExpected());
		values.add(asser.getFound());
		values.add(asser.getReference());
		addRow(values);
		return this;
	}
	
	public AssertionGroup addAssertion(String name, AssertionId id, AssertionStatus status, String expected, String found, AssertionReference ref) {
		if (status.ordinal() > maxStatus.ordinal())
			maxStatus = status;
		List<String> values = new ArrayList<String>();
		values.add(name);
		values.add(id.getId());
		values.add(status.name());
		values.add(expected);
		values.add(found);
		values.add(ref.getReference());
		addRow(values);
		return this;
	}
	
	void addRow(List<String> values) {
		CSVEntry entry = new CSVEntry(values.size());
		for (int i=0; i<values.size(); i++) {
			entry.set(i, values.get(i));
		}
		assertionTable.add(entry);
	}
	
}
