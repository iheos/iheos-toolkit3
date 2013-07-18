package gov.nist.hit.ds.simSupport.engine;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class SimComponentParserTest {

	@Test
	public void parserTest() {
		Properties data = buildTestData();
		SimComponentParser parser = new SimComponentParser(data);
		assertTrue(2 == parser.size());
		List<Properties> cprops = parser.getComponentProperties();
		assertTrue(2 == cprops.size());
		
		Properties ele1 = cprops.get(0);
		assertTrue("java.lang.String".equals(ele1.getProperty("class")));
		assertTrue("String".equals(ele1.getProperty("name")));
		assertTrue("Description".equals(ele1.getProperty("description")));
		
		Properties ele2 = cprops.get(1);
		assertTrue("java.lang.StringA".equals(ele2.getProperty("class")));
		assertTrue("StringA".equals(ele2.getProperty("name")));
		assertTrue("DescriptionA".equals(ele2.getProperty("description")));
	}
	
	Properties buildTestData() {
		Properties props = new Properties();
		
		props.setProperty("element1.class", "java.lang.String");
		props.setProperty("element1.name", "String");
		props.setProperty("element1.description", "Description");
		
		props.setProperty("element2.class", "java.lang.StringA");
		props.setProperty("element2.name", "StringA");
		props.setProperty("element2.description", "DescriptionA");
		
		return props;
	}
	
}
