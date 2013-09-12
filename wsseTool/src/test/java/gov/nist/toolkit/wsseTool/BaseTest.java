package gov.nist.toolkit.wsseTool;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTest {

	private static Logger log = LoggerFactory.getLogger("");

	@Rule
	public TestName name = new TestName();

	/*
	 * Test initialization
	 */
	@Before
	public final void start() {
		log.info("------------ Test start : " + name.getMethodName()
				+ " -------------------");
	}

	@After
	public final void end() {
		log.info("------------ Test end : " + name.getMethodName()
				+ " -------------------");
	}
}
