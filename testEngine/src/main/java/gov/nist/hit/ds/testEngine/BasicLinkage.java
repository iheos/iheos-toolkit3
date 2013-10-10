package gov.nist.hit.ds.testEngine;

import gov.nist.hit.ds.testEngine.mgmt.TestConfig;

public abstract class BasicLinkage {
	TestConfig testConfig;
	
	public BasicLinkage(TestConfig config) {
		testConfig = config;
	}
}
