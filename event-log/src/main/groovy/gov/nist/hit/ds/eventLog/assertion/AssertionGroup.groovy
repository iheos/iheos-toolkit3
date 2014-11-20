package gov.nist.hit.ds.eventLog.assertion

import gov.nist.hit.ds.repository.api.Asset
import groovy.util.logging.Log4j
import org.apache.log4j.Logger
/**
 * Collection of Assertion outputs (execution status of assertion)
 * that are managed together usually because they come from the
 * same validator.
 * @author bmajur
 *
 */
@Log4j
public class AssertionGroup  {
    List<Assertion> assertions = []
    AssertionStatus worstStatus = AssertionStatus.SUCCESS;
    def validatorName = "AssertionGroup";
    def saveInLog = true
    def saved = false
    def assertionIds = [ ]  // this is used by unit tests to detect that an assertion has executed
                    // doesnt seem to work
    Asset asset = null   // this gets set when saved - changes behaviour from create to update

    private static Logger logger = Logger.getLogger(AssertionGroup);
    private final static String dashes = "---";

    AssertionGroup() {}

    boolean hasAssertion(id) { assertions.find { it.id == id}}
    def getAssertions(id) { assertions.findAll { it.id == id}}

    boolean hasContent() { return assertions.size() > 0 }

    boolean needsFlushing() { (hasContent() || hasWarnings()) && !saved }

    def setErrorStatus(AssertionStatus status) { if (status > worstStatus) worstStatus = status }
    AssertionStatus status() { return worstStatus }

    public String toString() { return "AssertionGroup(${validatorName} - ${worstStatus})" }

    public int size() { return assertions.size(); }

    Assertion addAssertion(Assertion asser, boolean required) {
        log.debug("New Assertion is ${asser} (required=${required})")
        if (required && asser.getStatus().ordinal() > worstStatus.ordinal())
            worstStatus = asser.getStatus();
        if (!asser.defaultMsg) removeDefaultMsg()
        log.debug("...adding assertion to ${toString()}")
        log.debug("...worstStatus now ${worstStatus}")
        assertions.add(asser);
        assertionIds << asser.id
        if (asser.failed()) log.debug("...Failed (${(required) ? '' : 'not '} required)")
        asser
    }

    def removeDefaultMsg() {
        def lastIndex = assertions.size() - 1
        if (lastIndex == -1) return
        if (assertions[lastIndex].defaultMsg) assertions.remove(lastIndex)
    }

    boolean hasAssertion(String id) {
        return assertions.find { it.id == id }
    }

    Assertion getFirstFailedAssertion() { return assertions.find { it.failed() } }

    List<Assertion> getFailedAssertions() {
        def failedAssertions = []
        assertions.each { assertion ->
            if (assertion.failed()) failedAssertions << assertion
        }
        failedAssertions
    }

    boolean assertionFailed(String id) { return getFailedAssertions().find { it.failed() } }

    List<String> getErrorMessages() {
        def msgs = []
        getFailedAssertions().each { msgs << it.msg }
        msgs
    }

    boolean hasWarnings() { return worstStatus >= AssertionStatus.WARNING }

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

    public Assertion assertIn(List<String> expecteds, String found, boolean required) {
        Assertion a = new Assertion();
        a.expected = expecteds.toString();
        a.found = found;
        for (String value : expecteds) if (value == found) return addAssertion(a, required)
        a.status = AssertionStatus.ERROR
        addAssertion(a, required);
    }

    public Assertion assertIn(String[] expecteds, String found, boolean required) {
        Assertion a = new Assertion();
        a.expected = expecteds.toString();
        a.found = found;
        for (int i=0; i<expecteds.length; i++) if (expecteds[i] == found) return addAssertion(a, required)
        a.status = AssertionStatus.ERROR
        addAssertion(a, required);
    }

    public Assertion fail(String failureMsg, boolean required) {
        Assertion a = new Assertion();
        a.with {
            expected = ''
            found = failureMsg
            status = AssertionStatus.ERROR
            //msg = failureMsg
        }
        addAssertion(a, required);
        return a;
    }

    public Assertion fail(String failureMsg, String _found, boolean required) {
        Assertion a = new Assertion();
        a.with {
            expected = failureMsg
            found = _found
            status = AssertionStatus.ERROR
            //msg = failureMsg
        }
        addAssertion(a, required);
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
        addAssertion(a, true);
        return a;
    }

    public Assertion assertEquals(boolean expectedVal, boolean foundVal, boolean required) {
        Assertion a = new Assertion();
        a.with {
            expected = expectedVal
            found = foundVal
        }
        a.status = (expectedVal == foundVal) ? AssertionStatus.SUCCESS : AssertionStatus.ERROR
        addAssertion(a, required);
        return a;
    }

    public Assertion assertEquals(String expectedVal, String foundVal, boolean required) {
        Assertion a = new Assertion();
        a.with {
            expected = expectedVal
            found = foundVal
        }
        a.status = (expectedVal == foundVal) ? AssertionStatus.SUCCESS : AssertionStatus.ERROR
        addAssertion(a, required);
        return a;
    }

    public Assertion assertEquals(int expectedVal, int foundVal, boolean required) {
        Assertion a = new Assertion();
        a.with {
            expected = expectedVal
            found = foundVal
        }
        a.status = (expectedVal == foundVal) ? AssertionStatus.SUCCESS : AssertionStatus.ERROR
        addAssertion(a, required);
        return a;
    }

    public Assertion assertHasValue(String value, boolean required) {
        Assertion a = new Assertion();
        a.with {
            expected = 'Non-empty string'
            found = value
        }
        a.status = (value != null && value.size() > 0) ? AssertionStatus.SUCCESS : AssertionStatus.ERROR
        addAssertion(a, required);
        return a;
    }

    public Assertion assertStartsWith(String value, String prefix, boolean required) {
        Assertion a = new Assertion();
        a.with {
            expected = "Has prefix ${prefix}"
            found = value
        }
        a.status = (value != null && value.startsWith(prefix)) ? AssertionStatus.SUCCESS : AssertionStatus.ERROR
        addAssertion(a, required);
        return a;
    }

    public Assertion assertTrue(boolean ok, boolean required) {
        Assertion a = new Assertion();
        if (ok) {
            a.expected = dashes
            a.found = 'Ok'
        } else {
            a.expected = 'True'
            a.found = (ok) ? 'True' : 'False'
        }
        a.status = (ok) ? AssertionStatus.SUCCESS : AssertionStatus.ERROR
        addAssertion(a, required);
        return a;
    }

    public Assertion assertTrue(boolean ok, String found, boolean required) {
        Assertion a = new Assertion();
        if (ok) {
            a.expected = dashes
            a.found = found
        } else {
            a.expected = 'True'
            a.found = (ok) ? 'True' : 'False'
        }
        a.status = (ok) ? AssertionStatus.SUCCESS : AssertionStatus.ERROR
        addAssertion(a, required);
        return a;
    }

    public Assertion assertNotNull(Object value, boolean required) {
        Assertion a = new Assertion();
        a.with {
            expected = 'Present'
            found = (value) ? 'Found' : 'Missing'
            status = (value) ? AssertionStatus.SUCCESS : AssertionStatus.ERROR
        }
        addAssertion(a, required);
        return a;
    }

    public Assertion infoFound(boolean foundVal) {
        Assertion a = new Assertion();
        a.with {
            expected = ''
            found = (foundVal) ? 'True' : 'False'
            status = AssertionStatus.INFO
        }
        addAssertion(a, true);
        return a;
    }

    public Assertion infoFound(String foundVal) {
        Assertion a = new Assertion();
        a.with {
            expected = ''
            found = foundVal
            status = AssertionStatus.INFO
        }
        addAssertion(a, false);
        return a;
    }

    public Assertion msg(String msg) {
        Assertion a = new Assertion();
        a.with {
            expected = ''
            found = msg
            status = AssertionStatus.SUCCESS
        }
        addAssertion(a, false);
        return a;
    }

    public Assertion defaultMsg() {
        Assertion a = new Assertion();
        a.with {
            expected = ''
            found = 'Ok'
            status = AssertionStatus.SUCCESS
            defaultMsg = true
        }
        addAssertion(a, false);
        return a;
    }
}
