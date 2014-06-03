package gov.nist.hit.ds.httpParserTest;

import gov.nist.hit.ds.http.parser.HttpHeaderParser;
import gov.nist.hit.ds.http.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class HttpHeaderParserTest {

	@Test
	public void nameValueTest() {
		String input = "name: value\r\n";
		HttpHeaderParser hp = new HttpHeaderParser(input);
		
		try {
			assertTrue(hp.getName().equals("name"));
			assertTrue(hp.getValue().equals("value"));
			assertTrue(hp.getUnnamedParams().size() == 0);
			assertTrue(hp.getParams().size() == 0);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.fail();
		}
		
	}
}
