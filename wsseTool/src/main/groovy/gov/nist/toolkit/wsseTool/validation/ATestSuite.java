package gov.nist.toolkit.wsseTool.validation;

import gov.nist.toolkit.wsseTool.validation.tests.run.AssertionSignatureVal;
import gov.nist.toolkit.wsseTool.validation.tests.run.AssertionVal;
import gov.nist.toolkit.wsseTool.validation.tests.run.AttributeStatementVal;
import gov.nist.toolkit.wsseTool.validation.tests.run.AuthnStatementVal;
import gov.nist.toolkit.wsseTool.validation.tests.run.AuthzDecisionStatementVal;
import gov.nist.toolkit.wsseTool.validation.tests.run.ParsingVal;
import gov.nist.toolkit.wsseTool.validation.tests.run.SignatureVerificationVal;
import gov.nist.toolkit.wsseTool.validation.tests.run.TimestampVal;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		ParsingVal.class,
        AssertionVal.class,
        AssertionSignatureVal.class,
        AttributeStatementVal.class,
        AuthnStatementVal.class,
        AuthzDecisionStatementVal.class,
        SignatureVerificationVal.class,
        TimestampVal.class
})
public class ATestSuite {
}
