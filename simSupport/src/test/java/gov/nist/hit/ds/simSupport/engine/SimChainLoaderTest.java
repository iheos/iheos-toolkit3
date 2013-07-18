package gov.nist.hit.ds.simSupport.engine;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

public class SimChainLoaderTest {

	@Test 
	public void isCompleteTest() {		

		try {

			String chainDef = "gov/nist/hit/ds/simSupport/engine/FooMaker.properties";

			SimChain simChain = new SimChainLoader(chainDef).load();

			SimEngine engine = new SimEngine(simChain);
			
			
			for (SimStep step : simChain.getSteps()) 
				assertFalse(step.hasRan());
			assertFalse(engine.isComplete());
			
			
			engine.run();
			
			
			for (SimStep step : simChain.getSteps()) 
				assertTrue(step.hasRan());
			assertTrue(engine.isComplete());


		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} catch (SecurityException e) {
			e.printStackTrace();
			fail();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		} catch (ClassNotFoundException e) {
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
		}
	}

	@Test 
	public void isCompleteTest2() {		

		try {

			String chainDef = "gov/nist/hit/ds/simSupport/engine/FooMaker.properties";

			SimEngine engine = new SimEngine(chainDef);
			
			
			for (SimStep step : engine.getSimChain().getSteps()) 
				assertFalse(step.hasRan());
			assertFalse(engine.isComplete());
			
			
			engine.run();
			
			
			for (SimStep step : engine.getSimChain().getSteps()) 
				assertTrue(step.hasRan());
			assertTrue(engine.isComplete());


		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} catch (SecurityException e) {
			e.printStackTrace();
			fail();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		} catch (ClassNotFoundException e) {
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
		}
	}



}
