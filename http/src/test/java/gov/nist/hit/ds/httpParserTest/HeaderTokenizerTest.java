package gov.nist.hit.ds.httpParserTest;

import static org.junit.Assert.assertTrue;
import gov.nist.hit.ds.http.parser.HeaderTokenizer;
import gov.nist.hit.ds.http.parser.ParseException;
import gov.nist.hit.ds.http.parser.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class HeaderTokenizerTest {

	@Test
	public void compareTest() {
		String[] answer = {"name", ":", "value"};
		//		String input = "name: value";

		List<String> t = new ArrayList<String>();
		t.add("name");
		t.add(":");
		t.add("value");

		List<String> a = Arrays.asList(answer);

		assertTrue(a.equals(t));

		//		HeaderTokenizer ht = new HeaderTokenizer(input);
		//		List<String> tokens = ht.getTokens();
	}

	@Test
	public void singleValueTest()  {
		String input = "name";
		String[] a = {"name"};
		List<String> answer = Arrays.asList(a);

		run(input, answer);
	}

	@Test
	public void twoValueTest()  {
		String input = "name:";
		String[] a = {"name", ":"};
		List<String> answer = Arrays.asList(a);

		run(input, answer);
	}

	@Test
	public void nameValueTest()  {
		String input = "name: value";
		String[] a = {"name", ":", "value"};
		List<String> answer = Arrays.asList(a);

		run(input, answer);
	}

	@Test
	public void nameValue2Test()  {
		String input = "name : value";
		String[] a = {"name", ":", "value"};
		List<String> answer = Arrays.asList(a);

		run(input, answer);
	}

	@Test
	public void nameValue3Test()  {
		String input = "name : value  ";
		String[] a = {"name", ":", "value"};
		List<String> answer = Arrays.asList(a);

		run(input, answer);
	}

	@Test
	public void nameValue4Test()  {
		String input = "name : value\r\n   ";
		String[] a = {"name", ":", "value"};
		List<String> answer = Arrays.asList(a);

		run(input, answer);
	}

	@Test
	public void oneParmTest()  {
		String input = "name : value; name=value2";
		String[] a = {"name", ":", "value", ";", "name", "=", "value2"};
		List<String> answer = Arrays.asList(a);

		run(input, answer);
	}

	@Test
	public void quotedStringTest()  {
		String input = "\"value2\"";
		String[] a = {"value2"};
		List<String> answer = Arrays.asList(a);

		run(input, answer);
	}

	@Test
	public void oneQuotedParmTest()  {
		String input = "name : value; name=\"value2\"";
		String[] a = {"name", ":", "value", ";", "name", "=", "value2"};
		List<String> answer = Arrays.asList(a);

		run(input, answer);
	}

	@Test
	public void twoQuotedParmTest()  {
		String input = "name : value; name=\"value2\"; name2=\"value3\"";
		String[] a = {"name", ":", "value", ";", "name", "=", "value2", ";", "name2", "=", "value3"};
		List<String> answer = Arrays.asList(a);

		run(input, answer);
	}

	@Test
	public void urlTest()  {
		String input = "start=\"<http:/a>\"";
		String[] a = {"start", "=",  "<http:/a>"};
		List<String> answer = Arrays.asList(a);

		run(input, answer);
	}
	
	@Test
	public void boundaryTest()  {
		String input = "content-type: multipart/related; type=\"application/xop+xml\";start=\"<http://tempuri.org/0>\";boundary=\"uuid:898b3ded-c057-48c5-9f07-43a15680b58d+id=5\";start-info=\"application/soap+xml\"";
		String[] a = {"content-type", ":",  "multipart/related", ";", 
				"type", "=", "application/xop+xml", ";", "start", "=", "<http://tempuri.org/0>",
				";", "boundary", "=", "uuid:898b3ded-c057-48c5-9f07-43a15680b58d+id=5", ";", "start-info", "=", "application/soap+xml"};
		List<String> answer = Arrays.asList(a);

		run(input, answer);
	}
	
	List<Token> getTokenList(List<String> str) {
		List<Token> ts = new ArrayList<Token>();
		
		for (String s : str) {
			ts.add(new Token(s));
		}
		
		return ts;
	}

	void run(String input, List<String> answer) {
		try {
			HeaderTokenizer ht = new HeaderTokenizer(input);
			List<Token> tokens = ht.getTokens();
			System.out.println(tokens);
			
			assertTrue(answer.size() == tokens.size());
			List<Token> al = getTokenList(answer); 
			for (int i=0; i< al.size(); i++)
				assertTrue(al.get(i).equals(tokens.get(i)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

}
