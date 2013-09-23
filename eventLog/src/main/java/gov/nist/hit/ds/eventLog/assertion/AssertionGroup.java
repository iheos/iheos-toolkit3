package gov.nist.hit.ds.eventLog.assertion;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.client.ValidatorErrorItem;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode.Code;
import gov.nist.hit.ds.errorRecording.factories.ErrorRecorderBuilder;
import gov.nist.hit.ds.utilities.csv.CSVEntry;
import gov.nist.hit.ds.utilities.csv.CSVTable;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;

public class AssertionGroup implements IAssertionGroup, Enumeration<Assertion> {
	CSVTable assertionTable = new CSVTable();
	AssertionStatus maxStatus = AssertionStatus.SUCCESS;
	String validatorName;
	static Logger logger = Logger.getLogger(AssertionGroup.class);

	public AssertionGroup() {
		addRow(Arrays.asList(Assertion.columnNames));
	}

	public String getValidatorName() {
		return validatorName;
	}

	public AssertionGroup setValidatorName(String name) {
		this.validatorName = name;
		return this;
	}

	public CSVTable getTable() { return assertionTable; }

	public AssertionStatus getMaxStatus() { return maxStatus; }

	public AssertionGroup addAssertion(Assertion asser) {
		logger.debug(asser);
		if (asser.getStatus().ordinal() > maxStatus.ordinal())
			maxStatus = asser.getStatus();
		assertionTable.add(asser.getEntry());
		return this;
	}

	void addRow(List<String> values) {
		CSVEntry entry = new CSVEntry(values.size());
		for (int i=0; i<values.size(); i++) {
			entry.set(i, values.get(i));
		}
		assertionTable.add(entry);
	}

	/************************************************************
	 * 
	 * Assertions
	 * 
	 *************************************************************/
	
	public AssertionGroup assertEquals(String expected, String found, String msg) {
		Assertion as = new Assertion();
		if (expected.equals(found)) {
			as.setExpected(expected).setFound(found).setMsg(msg).setStatus(AssertionStatus.SUCCESS);
		} else {
			as.setExpected(expected).setFound(found).setMsg(msg).setStatus(AssertionStatus.ERROR);
		}
		addAssertion(as);
		return this;
	}
	
	public Assertion assertEquals(int expected, int found, String msg) {
		Assertion as = new Assertion();
		if (expected == found) {
			as.setExpected(expected).setFound(found).setMsg(msg).setStatus(AssertionStatus.SUCCESS);			
		} else {
			as.setExpected(expected).setFound(found).setMsg(msg).setStatus(AssertionStatus.ERROR);
		}
		addAssertion(as);
		return as;
	}
	
	/************************************************************
	 * 
	 * Enumeration implementation
	 * 
	 *************************************************************/
	int tableIndex = 1;  // 1 because first row is column headers

	public void resetEnumeration() {
		tableIndex = 1;
	}

	@Override
	public boolean hasMoreElements() {
		return tableIndex < assertionTable.size();
	}

	@Override
	public Assertion nextElement() {
		tableIndex++;
		return new Assertion().setEntry(assertionTable.get(tableIndex - 1));
	}


	/*************************************************************
	 * 
	 * Implementation of ErrorRecorder for backwards compatibility.
	 * 
	 * ***********************************************************/

	boolean hasErrors = false;

	@Override
	public void err(Code code, ErrorContext context, Object location) {
		Assertion as = new Assertion();

		as.setId("").
		setName("").
		setStatus(AssertionStatus.ERROR).
		setFound("").
		setExpected("").
		setReference(context.getResource()).
		setMsg(context.getMsg()).
		setCode(code.name()).
		setLocation((location == null) ? "" : location.getClass().getName());

		addAssertion(as);
		hasErrors = true;
	}

	@Override
	public void err(Code code, Exception e) {
		Assertion as = new Assertion();

		as.setId("").
		setName("").
		setStatus(AssertionStatus.ERROR).
		setFound("").
		setExpected("").
		setReference("").
		setMsg(e.getMessage()).
		setCode(code.name());

		addAssertion(as);
		hasErrors = true;
	}

	@Override
	public void warning(String code, ErrorContext context, String location) {
		Assertion as = new Assertion();

		as.setId("").
		setName("").
		setStatus(AssertionStatus.WARNING).
		setFound("").
		setExpected("").
		setReference(context.getResource()).
		setMsg(context.getMsg()).
		setCode(code).
		setLocation((location == null) ? "" : location.getClass().getName());

		addAssertion(as);
	}

	@Override
	public void warning(Code code, ErrorContext context, String location) {
		Assertion as = new Assertion();

		as.setId("").
		setName("").
		setStatus(AssertionStatus.WARNING).
		setFound("").
		setExpected("").
		setReference(context.getResource()).
		setMsg(context.getMsg()).
		setCode(code.name()).
		setLocation((location == null) ? "" : location.getClass().getName());

		addAssertion(as);
	}

	@Override
	public void sectionHeading(String msg) {
		Assertion as = new Assertion();

		as.setId("").
		setName(msg).
		setStatus(AssertionStatus.INFO).
		setFound("").
		setMsg(msg).
		setExpected("");

		addAssertion(as);
	}

	@Override
	public void challenge(String msg) {
		Assertion as = new Assertion();

		as.setId("").
		setName("").
		setStatus(AssertionStatus.INFO).
		setFound("").
		setExpected("").
		setMsg(msg);

		addAssertion(as);
	}

	@Override
	public void externalChallenge(String msg) {
		challenge(msg);
	}

	@Override
	public void detail(String msg) {
		challenge(msg);
	}

	@Override
	public void success(String dts, String name, String found, String expected,
			String RFC) {
		// TODO Auto-generated method stub

	}

	@Override
	public void error(String dts, String name, String found, String expected,
			String RFC) {
		// TODO Auto-generated method stub

	}

	@Override
	public void warning(String dts, String name, String found, String expected,
			String RFC) {
		// TODO Auto-generated method stub

	}

	@Override
	public void info(String dts, String name, String found, String expected,
			String RFC) {
		// TODO Auto-generated method stub

	}

	@Override
	public void summary(String msg, boolean success, boolean part) {
		// TODO Auto-generated method stub

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showErrorInfo() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasErrors() {
		return hasErrors;
	}

	@Override
	public int getNbErrors() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<ValidatorErrorItem> getErrMsgs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErrorRecorder buildNewErrorRecorder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErrorRecorderBuilder getErrorRecorderBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void concat(IAssertionGroup er) {
		// TODO Auto-generated method stub
		
	}



}
