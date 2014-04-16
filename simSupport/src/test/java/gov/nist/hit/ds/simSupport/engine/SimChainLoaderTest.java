package gov.nist.hit.ds.simSupport.engine;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.utilities.initialization.installation.InitializationFailedException;
import gov.nist.hit.ds.utilities.initialization.installation.Installation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.event.EventBuilder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;

public class SimChainLoaderTest {

	@Before
	public void before() throws InitializationFailedException, IOException, RepositoryException {
		Installation.reset();
		Installation.installation().initialize();
		BasicConfigurator.configure();
		Configuration.configuration();
	}

	@Test 
	public void isCompleteTest() {		

		try {
			
			Event event = new EventBuilder().buildEvent(new SimId("123"), "Foo", "foo");

			String chainDef = "FooMaker.properties";

			SimChain simChain = new SimChainLoader(chainDef).load();

			SimEngine engine = new SimEngine(simChain, event);


			for (SimStep step : simChain.getSteps()) 
				assertFalse(engine.isStepCompleted(step));
			assertFalse(engine.isComplete());


			engine.run();


			for (SimStep step : simChain.getSteps()) 
				assertTrue(engine.isStepCompleted(step));
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
		} catch (SimEngineException e) {
			e.printStackTrace();
			fail();
		} catch (SimChainLoaderException e) {
			e.printStackTrace();
			fail();
		} catch (RepositoryException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test 
	public void isCompleteTest2() {		

		try {

			String chainDef = "FooMaker.properties";

			Event event = new EventBuilder().buildEvent(new SimId("IsCompleteTest2"), "Foo", "foo");

			SimEngine engine = new SimEngine(chainDef, event);


			for (SimStep step : engine.getSimChain().getSteps()) 
				assertFalse(engine.isStepCompleted(step));
			assertFalse(engine.isComplete());


			engine.run();


			for (SimStep step : engine.getSimChain().getSteps()) 
				assertTrue(engine.isStepCompleted(step));
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
		} catch (SimEngineException e) {
			e.printStackTrace();
			fail();
		} catch (SimChainLoaderException e) {
			e.printStackTrace();
			fail();
		} catch (RepositoryException e) {
			e.printStackTrace();
			fail();
		}
	}



}
