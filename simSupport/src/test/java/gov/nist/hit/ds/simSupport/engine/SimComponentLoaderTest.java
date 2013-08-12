package gov.nist.hit.ds.simSupport.engine;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class SimComponentLoaderTest {
	
	@Test
	public void loaderWithParmsTest() {
		Properties parmMap = new Properties();
		parmMap.put("myStuff", "Stuff it");
		parmMap.put("name", "My Component");
		parmMap.put("description", "Describe yourself");
		SimComponentLoader loader = new SimComponentLoader(MyComponent.class.getName(), parmMap);
		SimComponent component = null;
		try {
			component = loader.load();
		} catch (SecurityException e) {
			e.printStackTrace();
			fail();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			fail();
		} catch (InstantiationException e) {
			e.printStackTrace();
			fail();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			fail();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			fail();
		} catch (SimEngineSubscriptionException e) {
			e.printStackTrace();
			fail();
		} catch (SimEngineClassLoaderException e) {
			e.printStackTrace();
			fail();
		}
		
		assertTrue(component instanceof MyComponent);
		MyComponent c = (MyComponent) component;
		assertTrue("Stuff it".equals(c.getMyStuff()));
		assertTrue("My Component".equals(component.getName()));
		assertTrue("Describe yourself".equals(component.getDescription()));
	}

	@Test
	public void loaderWithNoParmsTest() {
		Properties parmMap = new Properties();
		SimComponentLoader loader = new SimComponentLoader(MyComponent.class.getName(), parmMap);
		SimComponent component = null;
		try {
			component = loader.load();
		} catch (SecurityException e) {
			e.printStackTrace();
			fail();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			fail();
		} catch (InstantiationException e) {
			e.printStackTrace();
			fail();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			fail();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			fail();
		} catch (SimEngineSubscriptionException e) {
			e.printStackTrace();
			fail();
		} catch (SimEngineClassLoaderException e) {
			e.printStackTrace();
			fail();
		}
		
		assertTrue(component instanceof MyComponent);
		MyComponent c = (MyComponent) component;
		assertTrue(component.getName() == null);
	}
}
