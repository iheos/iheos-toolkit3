package gov.nist.hit.ds.httpParserTest;

import gov.nist.hit.ds.http.parser.HeaderToken;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class HeaderTokenTest {

	@Test
	public void equalsTest() {
		assertTrue(HeaderToken.LT.equals(HeaderToken.LT));
	}
}
