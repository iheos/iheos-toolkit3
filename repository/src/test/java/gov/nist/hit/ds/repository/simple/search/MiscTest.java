package gov.nist.hit.ds.repository.simple.search;

import gov.nist.hit.ds.repository.api.RepositorySource;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.presentation.PresentationData;
import gov.nist.hit.ds.utilities.xml.XmlFormatter;
import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
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

        try {
            assert(false); // If asserts are checked explicitly, this should fail at runtime
            fail("assertion not checked");
        } catch (AssertionError ae) {
            // good!
        }

		
	}



	@Test
	public void npeTest() {
		NullPointerException npe =  new NullPointerException();
		
		try {
			// String x = null;
			
			// x.equals("");
			
			throw npe;
			
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
	
	@Test
	public void xmlFmtTest() {
		System.out.println(XmlFormatter.normalize("<html><head><title>Title</title></head><body><h1>Header</h1></body></html>"));
		
		System.out.println(XmlFormatter.htmlize("<html><head><title>Title</title></head><body><h1>Header</h1></body></html>"));
        
	}
	
	
	@Test
	public void listTest() {
		File reposDir =  new File("C:\\e\\artrep_test_resources\\Installation\\IHE-Testing\\xdstools2_environment\\new_repository_shell_prototype - 2");		

		File[] assetFileNames = reposDir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if (name!=null) {
					System.out.println(dir.toString() + "--" + name);
				}
				return true;
			}
		});		
		
		for (File f : assetFileNames) {
			System.out.println(f);
			System.out.println(f.getParentFile());
			System.out.println(f.getParentFile().getName());
		}
		
	}

	@Test
	public void fnRegTest1() {
		Pattern fileName = Pattern.compile(".+(?<!/)/(?!/)(.+?)\\..+?");
		String str = "hello string ?";
		Matcher matcher = fileName.matcher(str);
		if (matcher.find()) {
		    System.out.println(matcher.group(1));
		    String replacedURLWithRegex = str.replace(matcher.group(1), "new");
		    System.out.println(replacedURLWithRegex);
		}
	}
	
	
	@Test
	public void sortMapTest() {
		
		Properties p = new Properties();
		
		p.setProperty("x", "1");
		p.setProperty("a", "2");
		p.setProperty("y", "3");
		p.setProperty("l", "4");

		
		System.out.println("******" + PresentationData.getSortedMapString(p));

	}

    @Test
    public void propertyKeyValueTest() {

        Properties p = new Properties();

        try {
            p.setProperty(null, "null key");
            fail("null key");
        } catch (NullPointerException npe) {
            // good!
        }

        try {
            p.setProperty("goodKeyButNullValueTest",null);
            fail("goodKeyButNullValueTest");
        } catch (NullPointerException npe) {
            // good!
        }



        try {
            p.getProperty(null);
            fail("get prop by null key");
        } catch (NullPointerException npe) {
            // good!
        }



        p.setProperty("patientId","100");
            p.setProperty("PatientId","100");



        System.out.println("property key/value test: " + p.size());

    }




    @Test
	public void fnRegTest2() {
		
		System.out.println("::a3-b!c%zz_@#$ hello".replaceAll("[^@A-Za-z0-9-_. ]+", ""));
		
		// System.out.println(regex.Replace(fileName.Trim(), "[^A-Za-z0-9_. ]+", ""));
		/*
		Pattern fileName = Pattern.compile("[^A-Za-z0-9_. ]+");
		String str = "hello string ?\\";
		Matcher matcher = fileName.matcher(str);
		if (matcher.find()) {
		    System.out.println(matcher.group(1));
		    String replacedURLWithRegex = str.replace(matcher.group(1), "blah");
		    System.out.println(replacedURLWithRegex);
		}
		*/
	}

    @Test
    public void testList() {
        List<String> list = new ArrayList<String>();

        list.add("1");
        list.add("2");
        list.add("3");

        list.add(1,"1.1");

        for (String s : list) {
            System.out.println(s);
        }
    }
	
	@Test
	public void stringComp() {
		System.out.println("*** string comp");
		System.out.println("a".compareTo("z") + "");
		
//
//		List<String[]> unsortList = new ArrayList<String[]>();
// 
//		unsortList.add(new String[]{"CCC","x"});
//		unsortList.add(new String[]{"111","x"});
//		unsortList.add(new String[]{"AAA","x"});
//		unsortList.add(new String[]{"BBB","x"});
//		unsortList.add(new String[]{"222","x"});
// 
//		//before sort
//		System.out.println("ArrayList is unsort");
//		for(String[] temp: unsortList){
//			System.out.println(temp[0]);
//		}
// 
//		//sort the list
//		Collections.sort(unsortList);
// 
//		//after sorted
//		System.out.println("ArrayList is sorted");
//		for(String[] temp: unsortList){
//			System.out.println(temp[0]);
//		}
//	
	}

    @Test
    public void concMapTest() {

        System.out.println("parse out:" + Boolean.parseBoolean(null));

        ConcurrentHashMap<String, AtomicBoolean> reposIndexMap = new ConcurrentHashMap<String, AtomicBoolean>();
        try {
            reposIndexMap.put("repos1", new AtomicBoolean(Boolean.TRUE));
            reposIndexMap.put("repos1", new AtomicBoolean(Boolean.TRUE));

        } catch (Throwable t) {
            fail(t.toString());
        }
        System.out.println("concMapTest exit");
    }
	
}
