package gov.nist.hit.ds.httpSoapValidator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.http.parser.HttpEnvironment;
import gov.nist.hit.ds.simSupport.engine.SimChain;
import gov.nist.hit.ds.simSupport.engine.SimChainLoader;
import gov.nist.hit.ds.simSupport.engine.SimEngine;
import gov.nist.hit.ds.simSupport.engine.SimEngineSubscriptionException;
import gov.nist.hit.ds.simSupport.engine.SimStep;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

public class ConfigFileTest {

	@Test
	public void configFileTest() {		
		try {

			String chainDef = "httpSoapValidator.properties";

			SimChain simChain = new SimChainLoader(chainDef).load();
			simChain.setBase(new SoapEnvironment(new HttpEnvironment().setResponse(new HttpServletResponseMock())));
			
			assertTrue(simChain.getSteps().size() > 0);

			SimEngine engine = new SimEngine(simChain);
			
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
