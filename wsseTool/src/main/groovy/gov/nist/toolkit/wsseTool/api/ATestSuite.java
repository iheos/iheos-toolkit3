package gov.nist.toolkit.wsseTool.api;

import gov.nist.toolkit.wsseTool.validation.AssertionSignatureVal;
import gov.nist.toolkit.wsseTool.validation.AssertionVal;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AssertionVal.class,
        AssertionSignatureVal.class
})
public class ATestSuite {
}
