package gov.nist.hit.ds.utilities.csv;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class SimpleTest {

	@Test
	public void oneLineTest() {
		String csvLine = "a,b,c";
		CSVParser parser = new CSVParser(csvLine);
		assertTrue(parser.size() == 1);
		CSVEntry entry = parser.get(0);
		List<String> items = entry.getItems();
		assertTrue(items.size() == 3);
		assertTrue("a".equals(items.get(0)));
		assertTrue("b".equals(items.get(1)));
		assertTrue("c".equals(items.get(2)));
	}

	@Test
	public void twoLineTest() {
		String csvLine = "a,b,c\nd,e,f";
		CSVParser parser = new CSVParser(csvLine);
		assertTrue(parser.size() == 2);
		CSVEntry entry = parser.get(1);
		List<String> items = entry.getItems();
		assertTrue(items.size() == 3);
		assertTrue("d".equals(items.get(0)));
		assertTrue("e".equals(items.get(1)));
		assertTrue("f".equals(items.get(2)));
	}

	@Test
	public void quotedElementTest() {
		String csvLine = "\"a\",\"b\",\"c\"";
		CSVParser parser = new CSVParser(csvLine);
		assertTrue(parser.size() == 1);
		CSVEntry entry = parser.get(0);
		List<String> items = entry.getItems();
		assertTrue(items.size() == 3);
		assertTrue("a".equals(items.get(0)));
		assertTrue("b".equals(items.get(1)));
		assertTrue("c".equals(items.get(2)));
	}

}
