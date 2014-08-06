package gov.nist.hit.ds.simSupport.engine;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class InjectorTest {
	
	class TestObject {
		String name;
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
	
	@Test
	public void aTest() {
		TestObject to = new TestObject();
		Properties paramMap = new Properties();
		paramMap.put("name", "Bill");
		
		Injector inj = new Injector(to, paramMap);
		try {
			inj.run();
		} catch (SecurityException e) {
			e.printStackTrace();
			fail();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			fail();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			fail();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			fail();
		}
		assertTrue("Bill".equals(to.getName()));
	}

}
