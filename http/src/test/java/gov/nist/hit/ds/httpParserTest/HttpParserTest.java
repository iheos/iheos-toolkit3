package gov.nist.hit.ds.httpParserTest;

import static org.junit.Assert.assertTrue;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.http.parser.HttpHeader.HttpHeaderParseException;
import gov.nist.hit.ds.http.parser.HttpParseException;
import gov.nist.hit.ds.http.parser.HttpParser;
import gov.nist.hit.ds.http.parser.ParseException;
import gov.nist.hit.ds.utilities.io.Io;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class HttpParserTest {

	@Test
	public void existsTest() {
		File f = new File("src/test/resources/gov/nist/hit/ds/http/boundaryTest");
		assertTrue(f.exists());
		assertTrue(f.isDirectory());
	}

	void assemble() {
		try {
			File f = new File("src/test/resources/gov/nist/hit/ds/http/boundaryTest");
			File hdrFile = new File(f, "request_hdr.txt");
			assertTrue(hdrFile.exists());
			byte[] hdr = Io.stringFromFile(hdrFile).getBytes();
			File bodyFile = new File(f, "request_body.bin");
			assertTrue(bodyFile.exists());
			byte[] body = Io.bytesFromFile(bodyFile);

			byte[] msg = new byte[hdr.length + body.length];
			System.arraycopy(hdr, 0, msg, 0, hdr.length);
			System.arraycopy(body, 0, msg, hdr.length, body.length);

			IAssertionGroup er = new AssertionGroup();
			new HttpParser(msg, er);
			System.out.println(er.toString());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (HttpParseException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (HttpHeaderParseException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void assembleTest() {
		assemble();
	}
}
