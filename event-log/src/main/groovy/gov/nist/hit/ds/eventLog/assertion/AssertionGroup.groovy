package gov.nist.hit.ds.eventLog.assertion

import org.apache.log4j.Logger
/**
 * Collection of Assertion outputs (execution status of assertion)
 * that are managed together usually because they come from the
 * same validator.
 * @author bmajur
 *
 */
public class AssertionGroup  {
    List<Assertion> assertions = []
    AssertionStatus worstStatus = AssertionStatus.SUCCESS;
    def validatorName = "AssertionGroup";
    def saveInLog = true
    def saved = false
    def assertionIds = [ ]  // this is used by unit tests to detect that an assertion has executed

    private static Logger logger = Logger.getLogger(AssertionGroup);
    private final static String dashes = "---";

    AssertionGroup() {}

    boolean hasContent() { return assertions.size() > 0 }

    boolean needsFlushing() { hasContent() && !saved }

    public String toString() { return "AssertionGroup(${worstStatus})" }

    public int size() { return assertions.size(); }

    Assertion addAssertion(Assertion asser) {
        if (asser.getStatus().ordinal() > worstStatus.ordinal())
            worstStatus = asser.getStatus();
        if (!asser.defaultMsg) removeDefaultMsg()
        assertions.add(asser);
        asser
    }

    def removeDefaultMsg() {
        def lastIndex = assertions.size() - 1
        if (lastIndex == -1) return
        if (assertions[lastIndex].defaultMsg) assertions.remove(lastIndex)
    }

    boolean hasAssertion(String id) { assertionIds.contains(id)}
    def assertionId(String id) {
        assertionIds << id
    }

    Assertion getFirstFailedAssertion() { return assertions.find { it.failed() } }

    List<Assertion> getFailedAssertions() {
        def failedAssertions = []
        assertions.each { assertion ->
            if (assertion.failed()) failedAssertions << assertion
        }
        failedAssertions
    }

    List<String> getErrorMessages() {
        def msgs = []
        getFailedAssertions().each { msgs << it.msg }
        msgs
    }

    boolean hasErrors() { return worstStatus >= AssertionStatus.ERROR }

    boolean hasInternalError() { return worstStatus >= AssertionStatus.INTERNALERROR }

    String getInternalError() {
        def failure = assertions.find { assertion ->
            assertion.internalError()
        }
        return failure?.msg
    }

    boolean hasUnsaved() {
        assertions.find { !it.saved }
    }

    public Assertion getAssertion(int i) { assertions[i] }

    /************************************************************
     *
     * AssertionGroupDAO
     *
     * Each assert method in SimComponentBase is backed by an
     * assert method below.
     *
     *
     *************************************************************/

    public Assertion assertIn(String[] expecteds, String found) {
        Assertion a = new Assertion();
        a.expected = expecteds.toString();
        a.found = found;
        for (int i=0; i<expecteds.length; i++) if (expecteds[i] == found) return addAssertion(a)
        a.status = AssertionStatus.ERROR
        addAssertion(a);
    }

    public Assertion fail(String failureMsg) {
        Assertion a = new Assertion();
        a.with {
            expected = ''
            found = failureMsg
            status = AssertionStatus.ERROR
            //msg = failureMsg
        }
        addAssertion(a);
        return a;
    }

    public Assertion internalError(String failureMsg) {
        Assertion a = new Assertion();
        a.with {
            expected = ""
            found = ""
            status = AssertionStatus.INTERNALERROR
            msg = failureMsg
        }
        addAssertion(a);
        return a;
    }

    public Assertion assertEquals(boolean expectedVal, boolean foundVal) {
        Assertion a = new Assertion();
        a.with {
            expected = expectedVal
            found = foundVal
        }
        a.status = (expectedVal == foundVal) ? AssertionStatus.SUCCESS : AssertionStatus.ERROR
        addAssertion(a);
        return a;
    }

    public Assertion assertEquals(String expectedVal, String foundVal) {
        Assertion a = new Assertion();
        a.with {
            expected = expectedVal
            found = foundVal
        }
        a.status = (expectedVal == foundVal) ? AssertionStatus.SUCCESS : AssertionStatus.ERROR
        addAssertion(a);
        return a;
    }

    public Assertion assertEquals(int expectedVal, int foundVal) {
        Assertion a = new Assertion();
        a.with {
            expected = expectedVal
            found = foundVal
        }
        a.status = (expectedVal == foundVal) ? AssertionStatus.SUCCESS : AssertionStatus.ERROR
        addAssertion(a);
        return a;
    }

    public Assertion assertTrue(boolean ok) {
        Assertion a = new Assertion();
        if (ok) {
            a.expected = dashes
            a.found = 'Ok'
        } else {
            a.expected = 'True'
            a.found = (ok) ? 'True' : 'False'
        }
        a.status = (ok) ? AssertionStatus.SUCCESS : AssertionStatus.ERROR
        addAssertion(a);
        return a;
    }

    public Assertion assertNotNull(Object value) {
        Assertion a = new Assertion();
        a.with {
            expected = 'Present'
            found = (value) ? 'Found' : 'Missing'
            status = (value) ? AssertionStatus.SUCCESS : AssertionStatus.ERROR
        }
        addAssertion(a);
        return a;
    }

    public Assertion infoFound(boolean foundVal) {
        Assertion a = new Assertion();
        a.with {
            expected = dashes
            found = (foundVal) ? 'True' : 'False'
            status = AssertionStatus.INFO
        }
        addAssertion(a);
        return a;
    }

    public Assertion infoFound(String foundVal) {
        Assertion a = new Assertion();
        a.with {
            expected = dashes
            found = foundVal
            status = AssertionStatus.INFO
        }
        addAssertion(a);
        return a;
    }

    public Assertion msg(String msg) {
        Assertion a = new Assertion();
        a.with {
            expected = dashes
            found = msg
            status = AssertionStatus.SUCCESS
        }
        addAssertion(a);
        return a;
    }

    public Assertion defaultMsg() {
        Assertion a = new Assertion();
        a.with {
            expected = dashes
            found = 'Ok'
            status = AssertionStatus.SUCCESS
            defaultMsg = true
        }
        addAssertion(a);
        return a;
    }
}
