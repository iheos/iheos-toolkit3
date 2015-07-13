package xtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import java.util.Arrays;
import java.net.URLClassLoader;



public class ClassLoaderTest {


	public static void test() {
	
	
	System.out.println(Arrays.toString(
           ((URLClassLoader)ClassLoaderTest.class.getClassLoader()).getURLs()));
	
	
	URL resource =  ClassLoaderTest.class.getResource("/toolkit.properties");
	if (resource!=null)
		System.out.println ("*** r: " + resource.toString());
	else 
		System.out.println ("null");
	
	resource = ClassLoaderTest.class.getResource("toolkit.properties");
	if (resource!=null)
		System.out.println ("*** r: " + resource.toString());
	else 
		System.out.println ("null");
	
	resource =  ClassLoaderTest.class.getResource("toolkit.properties");
	if (resource!=null)
		System.out.println ("*** r: " + resource.toString());
	else 
		System.out.println ("null");	
	}
	
	

  public static void main(String[] args) throws Exception {

	
	test();
	
	
	
	
	}
}