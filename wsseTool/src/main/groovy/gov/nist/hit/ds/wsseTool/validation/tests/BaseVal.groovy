package gov.nist.hit.ds.wsseTool.validation.tests;

import gov.nist.hit.ds.wsseTool.parsing.Message
import gov.nist.hit.ds.wsseTool.parsing.groovyXML.GroovyHeader
import gov.nist.hit.ds.wsseTool.validation.engine.TestData
import gov.nist.hit.ds.wsseTool.validation.engine.annotations.Data

import org.junit.Before
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
	
}
