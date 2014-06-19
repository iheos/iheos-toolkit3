package gov.nist.hit.ds.registryMsgFormats.test;

import static org.junit.Assert.assertEquals;
import gov.nist.hit.ds.errorRecording.ErrorContext;

import org.junit.Test;

public class ErrorContextTest {

	@Test
	public void setgetTest() {
		String msg = "hi";
		String resource = "me";
		ErrorContext ec = new ErrorContext(msg, resource);
		assertEquals(msg, ec.getMsg());
		assertEquals(resource, ec.getResource());
	}

	@Test
	public void parseTest() {
		String msg = "hi";
		String resource = "me";
		String in = msg + " [" + resource + "]";
		ErrorContext ec = new ErrorContext(in);
		assertEquals(msg, ec.getMsg());
		assertEquals(resource, ec.getResource());
	}

	@Test
	public void parseTestNoClose() {
		String msg = "hi";
		String resource = "me";
		String in = msg + " [" + resource;
		ErrorContext ec = new ErrorContext(in);
		assertEquals(msg, ec.getMsg());
		assertEquals(resource, ec.getResource());
	}

	@Test
	public void parseTestExtraSpaces() {
		String msg = "hi";
		String resource = "me";
		String in = msg + "  [   " + resource + "   ]   ";
		ErrorContext ec = new ErrorContext(in);
		assertEquals(msg, ec.getMsg());
		assertEquals(resource, ec.getResource());
	}

	@Test
	public void parseTestMsgWithOpenAndClose() {
		String msg = "hi (mom)";
		String resource = "me";
		String in = msg + " [" + resource + "]";
		ErrorContext ec = new ErrorContext(in);
		assertEquals(msg, ec.getMsg());
		assertEquals(resource, ec.getResource());
	}

	@Test
	public void parseTestResourceWithOpenAndClose() {
		String msg = "hi (mom)";
		String resource = "(me)";
		String in = msg + " [" + resource + "]";
		ErrorContext ec = new ErrorContext(in);
		assertEquals(msg, ec.getMsg());
		assertEquals(resource, ec.getResource());
	}

}
 