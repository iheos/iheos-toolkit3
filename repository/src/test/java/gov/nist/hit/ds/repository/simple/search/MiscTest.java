package gov.nist.hit.ds.repository.simple.search;

import java.util.ArrayList;

import org.junit.Test;

public class MiscTest {

	@Test
	public void orTest() {
		System.out.println(  32768 & 32769);
		System.out.println(32768 | 1); // encode
		System.out.println(39999 ^ 32768);
		System.out.println(32769 ^ 32768); // decode
		
		System.out.println(  39999 & 32768); // node tag
		/*
		 * 32768
			32769
			7231

		 */
		
		
		System.out.println(">" +  ".32768".split("\\.")[1] );
		
		System.out.println("".equals(null));
		
		
		
		ArrayList<String> l = new ArrayList<String>();
		String x ="test";
		
		l.add(x);
		
		System.out.println("" + l.contains("test"));
	}
}
