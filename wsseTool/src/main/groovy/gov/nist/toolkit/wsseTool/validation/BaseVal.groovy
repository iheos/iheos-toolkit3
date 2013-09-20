package gov.nist.toolkit.wsseTool.validation;

import gov.nist.toolkit.wsseTool.context.SecurityContextImpl
import gov.nist.toolkit.wsseTool.engine.TestData
import gov.nist.toolkit.wsseTool.engine.annotations.Data
import gov.nist.toolkit.wsseTool.parsing.groovyXML.GroovyHeader

import org.junit.Before
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class BaseVal {

	@Data
	public TestData data;

	SecurityContextImpl context;
	GroovyHeader header;
	
	/*
	 * Test initialization
	 */
	@Before
	public final void attributesSetup() {
			context = (SecurityContextImpl)data;
			header = context.groovyHeader
	}
	
	protected final Logger log = LoggerFactory.getLogger(this.getClass())
	
}
