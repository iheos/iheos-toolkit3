package gov.nist.hit.ds.httpSoapValidator;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import gov.nist.hit.ds.http.parser.HttpEnvironment;
import gov.nist.hit.ds.httpSoapValidator.testSupport.HttpServletResponseMock;
import gov.nist.hit.ds.simSupport.engine.SimChain;
import gov.nist.hit.ds.simSupport.engine.SimChainLoader;
import gov.nist.hit.ds.simSupport.engine.SimEngine;
import gov.nist.hit.ds.simSupport.engine.SimEngineSubscriptionException;
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment;

public class Launcher {

	/**
	 * 
	 * @param chainDef - resource location (something in the classpath) of a property file that 
	 * defines a SimChain. 
	 * @return Launcher object - after SimChain has been run
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws SimEngineSubscriptionException
	 */
	public Launcher launch(String chainDef) throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, SimEngineSubscriptionException {
		SimChain simChain = new SimChainLoader(chainDef).load();
		
		simChain.setBase(new SoapEnvironment(new HttpEnvironment().setResponse(new HttpServletResponseMock())));
		
		assertTrue(simChain.getSteps().size() > 0);

		SimEngine engine = new SimEngine(simChain);
		
		System.out.println(engine.getDescription(simChain));

		engine.run();

		System.out.println(simChain.getLog());
		
		return this;

	}
}
