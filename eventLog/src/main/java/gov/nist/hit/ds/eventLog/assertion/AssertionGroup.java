package gov.nist.hit.ds.eventLog.assertion;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.client.ValidatorErrorItem;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode.Code;
import gov.nist.hit.ds.errorRecording.factories.ErrorRecorderBuilder;
import gov.nist.hit.ds.utilities.csv.CSVEntry;
import gov.nist.hit.ds.utilities.csv.CSVTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Collection of Assertion outputs (execution status of assertion)
 * that are managed together usually because they come from the
 * same validator.
 * @author bmajur
 *
 */
public class AssertionGroup implements IAssertionGroup, Enumeration<Assertion> {
    List<Assertion> assertionList = new ArrayList<Assertion>();
    AssertionStatus maxStatus = AssertionStatus.SUCCESS;
    String validatorName = "AssertionGroup";
    boolean saveInLog = true;

    public boolean isSaveInLog() {
        return saveInLog;
    }

    public void setSaveInLog(boolean saveInLog) {
        this.saveInLog = saveInLog;
    }

    static Logger logger = Logger.getLogger(AssertionGroup.class);
    final static String dashes = "---";

    public AssertionGroup() {
    }

    public String getValidatorName() {
        return validatorName;
    }

    public AssertionGroup setValidatorName(String name) {
        this.validatorName = name;
        logger.debug("New AssertionGroup: " + name);
        return this;
    }

    public String toString() {
        return "AssertionGroup(" + maxStatus + ")";
    }

    // Doesn't get called until the end to flush out
    public CSVTable getTable() {
        CSVTable assertionTable = new CSVTable();
        addRow(assertionTable, Arrays.asList(Assertion.columnNames));
        int cnt = 0;
        for (Assertion a : assertionList) {
            assertionTable.add(a.getEntry());
            cnt++;
        }
        logger.debug("AssertionGroup table contains " + cnt + " entries");
        return assertionTable;
    }

    public AssertionStatus getMaxStatus() { return maxStatus; }

    public int size() { return assertionList.size(); }

    public AssertionGroup addAssertion(Assertion asser) {
//		logger.debug("Add Assertion to AssertionGroup: " + asser);
        if (asser.getStatus().ordinal() > maxStatus.ordinal())
            maxStatus = asser.getStatus();
        assertionList.add(asser);
        return this;
    }

    void addRow(CSVTable assertionTable, List<String> values) {
        CSVEntry entry = new CSVEntry(values.size());
        for (int i=0; i<values.size(); i++) {
            entry.set(i, values.get(i));
        }
        assertionTable.add(entry);
    }

    public Assertion getFirstFailedAssertion() {
        for (Assertion a : assertionList) {
            if (a.failed())
                return a;
        }
        return null;
    }

    public Assertion getAssertion(int n) {
        if (n < assertionList.size())
            return assertionList.get(n);
        return null;
    }

    /************************************************************
     *
     * Assertions
     *
     * Each assert method in SimComponentBase is backed by an
     * assert method below.
     *
     *
     *************************************************************/

    public Assertion assertIn(String[] expecteds, String value) {
        Assertion as = new Assertion();
        as.setExpected(expecteds.toString()).setFound(value);
        addAssertion(as);
        for (int i=0; i<expecteds.length; i++) {
            String exp = expecteds[i];
            if (exp.equals(value)) {
                as.setStatus(AssertionStatus.SUCCESS);
                return as;
            }
        }
        as.setStatus(AssertionStatus.ERROR);
        return as;
    }

    public Assertion fail(String expected) {
        Assertion as = new Assertion();
        as.setExpected(expected).setFound("").setStatus(AssertionStatus.ERROR);
        addAssertion(as);
        return as;
    }

    public Assertion assertEquals(String expected, String found) {
        Assertion as = new Assertion();
        if (expected == null) {
            as.setStatus(AssertionStatus.INTERNALERROR);
            return as;
        }
        if (expected.equals(found)) {
            as.setExpected(expected).setFound(found).setStatus(AssertionStatus.SUCCESS);
        } else {
            as.setExpected(expected).setFound(found).setStatus(AssertionStatus.ERROR);
        }
        addAssertion(as);
        return as;
    }

    public Assertion assertEquals(int expected, int found) {
        Assertion as = new Assertion();
        if (expected == found) {
            as.setExpected(expected).setFound(found).setStatus(AssertionStatus.SUCCESS);
        } else {
            as.setExpected(expected).setFound(found).setStatus(AssertionStatus.ERROR);
        }
        addAssertion(as);
        return as;
    }

    public Assertion assertTrue(boolean value) {
        Assertion as = new Assertion();
        if (value) {
            as.setExpected("True").setFound("True").setStatus(AssertionStatus.SUCCESS);
        } else {
            as.setExpected("True").setFound("False").setStatus(AssertionStatus.ERROR);
        }
        addAssertion(as);
        return as;
    }

    public Assertion assertNotNull(Object value) {
        Assertion as = new Assertion();
        if (value == null)
            as.setExpected("Present").setFound("Missing").setStatus(AssertionStatus.ERROR);
        else
            as.setExpected("Present").setFound("Found").setStatus(AssertionStatus.SUCCESS);
        addAssertion(as);
        return as;
    }

    public Assertion infoFound(boolean found) {
        Assertion as = new Assertion();
        if (found)
            as.setExpected(dashes).setFound("Present").setStatus(AssertionStatus.INFO);
        else
            as.setExpected(dashes).setFound("Not Present").setStatus(AssertionStatus.INFO);
        addAssertion(as);
        return as;
    }

    public Assertion infoFound(String found) {
        Assertion as = new Assertion();
        as.setExpected(dashes).setFound(found).setStatus(AssertionStatus.INFO);
        addAssertion(as);
        return as;
    }

    /************************************************************
     *
     * Enumeration implementation
     *
     *************************************************************/
    int tableIndex = 0;

    public void resetEnumeration() {
        tableIndex = 0;
    }

    @Override
    public boolean hasMoreElements() {
        return tableIndex < assertionList.size();
    }

    @Override
    public Assertion nextElement() {
        tableIndex++;
        return assertionList.get(tableIndex - 1);
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
                setReference(Assertion.parseSemiDivided(context.getResource())).
                setMsg(context.getMsg()).
                setCode(code.name()).
                setLocation((location == null) ? "" : location.getClass().getName());

        addAssertion(as);
        hasErrors = true;
    }

    @Override
    public void err(Code code, Exception e) {
        Assertion as = new Assertion();
        String[] empty = { "" };

        as.setId("").
                setName("").
                setStatus(AssertionStatus.ERROR).
                setFound("").
                setExpected("").
                setReference(empty).
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
                setReference(Assertion.parseSemiDivided(context.getResource())).
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
                setReference(Assertion.parseSemiDivided(context.getResource())).
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

    }

    @Override
    public void error(String dts, String name, String found, String expected,
                      String RFC) {

    }

    @Override
    public void warning(String dts, String name, String found, String expected,
                        String RFC) {

    }

    @Override
    public void info(String dts, String name, String found, String expected,
                     String RFC) {

    }

    @Override
    public void summary(String msg, boolean success, boolean part) {

    }

    @Override
    public void finish() {

    }

    @Override
    public void showErrorInfo() {

    }

    @Override
    public boolean hasErrors() {
        return hasErrors;
    }

    @Override
    public int getNbErrors() {
        return 0;
    }

    @Override
    public List<ValidatorErrorItem> getErrMsgs() {
        return null;
    }

    @Override
    public ErrorRecorder buildNewErrorRecorder() {
        return null;
    }

    @Override
    public ErrorRecorderBuilder getErrorRecorderBuilder() {
        return null;
    }

    @Override
    public void concat(IAssertionGroup er) {

    }
}
