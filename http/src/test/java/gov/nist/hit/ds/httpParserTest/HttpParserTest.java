package gov.nist.hit.ds.httpParserTest;

public class HttpParserTest {

//	@Test
//	public void existsTest() {
//		File f = new File("src/test/resources/gov/nist/hit/ds/http/boundaryTest");
//		assertTrue(f.exists());
//		assertTrue(f.isDirectory());
//	}
//
//	void assemble() {
//		try {
//			File f = new File("src/test/resources/gov/nist/hit/ds/http/boundaryTest");
//			File hdrFile = new File(f, "request_hdr.txt");
//			assertTrue(hdrFile.exists());
//			byte[] hdr = Io.stringFromFile(hdrFile).getBytes();
//			File bodyFile = new File(f, "request_body.bin");
//			assertTrue(bodyFile.exists());
//			byte[] body = Io.bytesFromFile(bodyFile);
//
//			byte[] msg = new byte[hdr.length + body.length];
//			System.arraycopy(hdr, 0, msg, 0, hdr.length);
//			System.arraycopy(body, 0, msg, hdr.length, body.length);
//
//			IAssertionGroup er = new AssertionGroup();
//			new HttpParser(msg, er);
//			System.out.println(er.toString());
//		} catch (IOException e) {
//			e.printStackTrace();
//			Assert.fail();
//		} catch (HttpParseException e) {
//			e.printStackTrace();
//			Assert.fail();
//		} catch (HttpHeaderParseException e) {
//			e.printStackTrace();
//			Assert.fail();
//		} catch (ParseException e) {
//			e.printStackTrace();
//			Assert.fail();
//		}
//	}
//
//	@Test
//	public void assembleTest() {
//		assemble();
//	}
}
