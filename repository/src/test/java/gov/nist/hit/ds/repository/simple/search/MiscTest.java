package gov.nist.hit.ds.repository.simple.search;

import static org.junit.Assert.assertTrue;

import javax.validation.constraints.AssertTrue;

import gov.nist.hit.ds.repository.api.RepositorySource;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;

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
		
		
		
		String[][][] repos = new String[1][1][];
		
		
		repos[0][0] = (new String[]{"file1","file2"});
		
		System.out.println("---- " + repos[0][0][0]);
		
		assert(false); // If asserts are checked explicitly, this should fail at runtime 
		
	}
	
	@Test
	public void npeTest() {
		NullPointerException npe;
		
		try {
			String x = null;
			
			x.equals("");
			
			
			
		} catch (Exception ex) {
			;
		}
		
		RepositorySource src = new RepositorySource(null, Access.RO_RESIDENT);
		
		assertTrue( src.getAccess().equals(Access.RO_RESIDENT));
		
		boolean xTrue = false;
		Object xObj = (1==2);
		
		System.out.println(xObj.toString());
		
		assertTrue( new Boolean(xObj.toString()).booleanValue()==xTrue);
		
	}
}
