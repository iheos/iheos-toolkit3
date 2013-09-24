package gov.nist.hit.ds.wsseTool.validation.tests;

import gov.nist.hit.ds.wsseTool.parsing.Message
import gov.nist.hit.ds.wsseTool.parsing.groovyXML.GroovyHeader
import gov.nist.hit.ds.wsseTool.validation.engine.*
import gov.nist.hit.ds.wsseTool.validation.engine.annotations.*
import static org.junit.Assert.*


import org.junit.After;
import org.junit.Before
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class BaseVal {

	@Data
	public TestData data;

	Message context;
	GroovyHeader header;
	
	/*
	 * Test initialization
	 */
	@Before
	public final void attributesSetup() {
			context = (Message)data;
			header = context.groovyHeader
	}
	
	protected final Logger log = LoggerFactory.getLogger(this.getClass())
	
	@Rule
	public TestName name = new TestName();

	/*
	 * Test initialization
	 */
	@Before
	public final void start2() {
		System.out.println("------------ Test start : " + name.getMethodName()
				+ " -------------------");
	}

	@After
	public final void end2() {
		System.out.println("------------ Test end : " + name.getMethodName()
				+ " -------------------");
	}
}
