package gov.nist.hit.ds.registryMsgFormats.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.registryMsgFormats.RegistryError;
import gov.nist.hit.ds.registryMsgFormats.RegistryResponse;
import gov.nist.hit.ds.registryMsgFormats.RegistryResponseGenerator;
import gov.nist.hit.ds.registryMsgFormats.RegistryResponseParser;
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;

import org.apache.axiom.om.OMElement;
import org.junit.Test;

public class RegistryResponseTest {

	@Test
	public void successTest() {
		RegistryResponseGenerator respGen = new RegistryResponseGenerator();
		
		assertTrue(respGen.has_errors() == false);
		
		OMElement respMsg = respGen.getResponse();
		
		RegistryResponseParser respParser = new RegistryResponseParser(respMsg);
		try {
			assertFalse(respParser.is_error());
		} catch (ToolkitRuntimeException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void errorTest() {
		RegistryResponseGenerator respGen = new RegistryResponseGenerator();
		
		respGen.add_error(null, new ErrorContext("msg", "resource"), "home");
		
		assertTrue(respGen.has_errors() == true);
		
		OMElement respMsg = respGen.getResponse();
		
		RegistryResponseParser respParser = new RegistryResponseParser(respMsg);
		try {
			assertTrue(respParser.is_error());
		} catch (ToolkitRuntimeException e) {
			fail(e.getMessage());
		}
		try {
			RegistryResponse rresp = respParser.getRegistryResponse();
			assertTrue(1 == rresp.size());
			RegistryError err = rresp.next();
			assertTrue(err.isError());
			assertTrue("msg".equals(err.getCodeContext().getMsg()));
			assertTrue("resource".equals(err.getCodeContext().getResource()));
			assertTrue("home".equals(err.getLocation()));
		} catch (ToolkitRuntimeException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void errorWarningTest() {
		RegistryResponseGenerator respGen = new RegistryResponseGenerator();
		
		respGen.add_error(null, new ErrorContext("msg", "resource"), "home");
		respGen.add_warning(null, new ErrorContext("warning msg", "warning resource"), "warning home");
		
		assertTrue(respGen.has_errors() == true);
		
		OMElement respMsg = respGen.getResponse();
		
		RegistryResponseParser respParser = new RegistryResponseParser(respMsg);
		try {
			assertTrue(respParser.is_error());
		} catch (ToolkitRuntimeException e) {
			fail(e.getMessage());
		}
		try {
			RegistryResponse rresp = respParser.getRegistryResponse();
			assertTrue(2 == rresp.size());
			RegistryError err = rresp.next();
			assertTrue(err.isError());
			assertTrue("msg".equals(err.getCodeContext().getMsg()));
			assertTrue("resource".equals(err.getCodeContext().getResource()));
			assertTrue("home".equals(err.getLocation()));

			err = rresp.next();
			assertTrue(err.isWarning());
			assertTrue("warning msg".equals(err.getCodeContext().getMsg()));
			assertTrue("warning resource".equals(err.getCodeContext().getResource()));
			assertTrue("warning home".equals(err.getLocation()));
		} catch (ToolkitRuntimeException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
