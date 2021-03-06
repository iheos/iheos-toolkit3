package gov.nist.hit.ds.httpSoapValidator;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.http.environment.HttpEnvironment;
import gov.nist.hit.ds.httpSoapValidator.testSupport.HttpServletResponseMock;
import gov.nist.hit.ds.initialization.installation.InitializationFailedException;
import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.engine.SimChain;
import gov.nist.hit.ds.simSupport.engine.SimChainLoader;
import gov.nist.hit.ds.simSupport.engine.SimChainLoaderException;
import gov.nist.hit.ds.simSupport.engine.SimEngine;
import gov.nist.hit.ds.simSupport.engine.SimEngineException;
import gov.nist.hit.ds.simSupport.engine.SimStep;
import gov.nist.hit.ds.simSupport.event.EventBuilder;
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;


public class ConfigFileTest {
	Event event = null;
	
	@Before
	public void init() throws InitializationFailedException, IOException, RepositoryException {
		Installation.reset();
		Installation.installation().initialize();
		Configuration.configuration();
		event = new EventBuilder().buildEvent(new SimId("1123"), "Foo", "FOO");
	}

	@Test
	public void configFileTest()  {		
		try {

			String chainDef = "httpSoapValidator.properties";

			SimChain simChain = new SimChainLoader(chainDef).load();
			simChain.setBase(new SoapEnvironment(new HttpEnvironment().setResponse(new HttpServletResponseMock())));
			
			assertTrue(simChain.getSteps().size() > 0);

			SimEngine engine = new SimEngine(simChain, event);
			
			System.out.println(engine.getDescription(simChain));

			engine.run();

			System.out.println(simChain.getLog());
									
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
