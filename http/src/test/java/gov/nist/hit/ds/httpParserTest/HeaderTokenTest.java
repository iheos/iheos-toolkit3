package gov.nist.hit.ds.httpParserTest;

import static org.junit.Assert.assertTrue;
import gov.nist.hit.ds.http.parser.HeaderToken;

import org.junit.Test;

public class HeaderTokenTest {

	@Test
	public void equalsTest() {
		assertTrue(HeaderToken.LT.equals(HeaderToken.LT));
	}
}
