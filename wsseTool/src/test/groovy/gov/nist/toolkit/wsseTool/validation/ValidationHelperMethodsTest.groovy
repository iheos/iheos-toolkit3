package gov.nist.toolkit.wsseTool.validation;

import static org.junit.Assert.*
import gov.nist.hit.ds.wsseTool.time.TimeUtil
import gov.nist.hit.ds.wsseTool.validation.tests.CommonVal
import gov.nist.toolkit.wsseTool.BaseTest

import java.util.regex.Matcher
import java.util.regex.Pattern

import org.joda.time.*
import org.joda.time.format.*
import org.junit.Test

class ValidationHelperMethodsTest extends BaseTest  {

	@Test void getAllIds(){
		def wsse = new XmlSlurper().parse("src/test/resources/validHeader.xml");
		def nodes = wsse.'**'.grep { it.@Id.text()} // all ids
		nodes.each {println it.name()}
		wsse.'**'.grep { it.@Id.text()}
		println nodes.findAll{ it.@Id == '_1' }.size() //how many ids with _1
	}
	
	@Test void test1026positive(){
		String id = "aasdas12" //standard alphanumeric
		String id2 = "_12aasdas" //common escape character
		String id3 = "#aasdas" //special character
		
		// '^\\D*$' means should start with a non digit!		
		Pattern pattern = Pattern.compile('^\\D', Pattern.UNICODE_CASE);
		Matcher m = pattern.matcher(id);
		assertTrue( m.find() );
		m = pattern.matcher(id2);
		assertTrue( m.find() );
		m = pattern.matcher(id3);
		assertTrue( m.find() );
	}
	
	@Test void test1026negative(){
		String id = "123aasdas"
		// '^\\D*$' means should start with a non digit!
		Pattern pattern = Pattern.compile('^\\D*$', Pattern.UNICODE_CASE);
		Matcher m = pattern.matcher(id);
		assertFalse( m.find() );
	}
	
	@Test void testValidEmail(){
		assertTrue( CommonVal.validEmail("tt@nist.gov"));
	}
	
	@Test void testInvalidEmail(){
		assertFalse( CommonVal.validEmail("tt@nist.govern"));
	}
	
	@Test void testValidDistinguishedName(){	
		assertTrue( CommonVal.validDistinguishedName("CN=abcd,CN=abcd,O=abcd,C=us"))
		assertTrue( CommonVal.validDistinguishedName("CN=abcd,CN=abcd,O=ab cd,C=us"))
		assertTrue( CommonVal.validDistinguishedName("C=us"))
		assertTrue( CommonVal.validDistinguishedName(" CN=E12.hs-fla.org"))
		assertTrue( CommonVal.validDistinguishedName("C=usdsa###d"))
		
		assertFalse( CommonVal.validDistinguishedName("C=usdsa,d"))
		assertFalse( CommonVal.validDistinguishedName("#C=usdsa,d"))
	}
	
	
	@Test void testValidURN(){
		assertTrue( CommonVal.validURN("urn:foo:A123456"))
		assertTrue( CommonVal.validURN("urn:foo:A12.345.6"))
		assertTrue( CommonVal.validURN("urn:oid:1.2.3.4"))
		assertFalse( CommonVal.validURN("urn:1.2.3"))
		assertFalse( CommonVal.validURN("oid:1.2.3."))
		assertFalse( CommonVal.validURN("urn:oid:1.2.3.4#3"))
	}
	
	@Test void testValidDateTime(){
		String d = "2002-10-10T12:00:00-05:00"
		String d2 = "2002-10-10T17:00:00Z"
		String d3 = "2013-04-25T19:47:18.772Z"
		
		String d4 = "2013-04-25T19:47:18"
		
		
		assertTrue( TimeUtil.isDateInUTCFormat(d) )
		assertTrue( TimeUtil.isDateInUTCFormat(d2) )
		assertTrue( TimeUtil.isDateInUTCFormat(d3) )
		assertTrue( TimeUtil.isDateInUTCFormat(d4) )
		
	}
}
