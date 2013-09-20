package gov.nist.toolkit.wsseTool.api;

import gov.nist.toolkit.wsseTool.validation.AssertionSignatureVal;
import gov.nist.toolkit.wsseTool.validation.AssertionVal;
import gov.nist.toolkit.wsseTool.validation.AttributeStatementVal;
import gov.nist.toolkit.wsseTool.validation.AuthnStatementVal;
import gov.nist.toolkit.wsseTool.validation.AuthzDecisionStatementVal;
import gov.nist.toolkit.wsseTool.validation.ParsingVal;
import gov.nist.toolkit.wsseTool.validation.SignatureVerificationVal;
import gov.nist.toolkit.wsseTool.validation.TimestampVal;

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
