package gov.nist.hit.ds.httpSoapValidator;

import static org.junit.Assert.assertTrue;
import gov.nist.hit.ds.http.environment.HttpEnvironment;
import gov.nist.hit.ds.httpSoapValidator.testSupport.HttpServletResponseMock;
import gov.nist.hit.ds.simSupport.engine.SimChain;
import gov.nist.hit.ds.simSupport.engine.SimChainLoader;
import gov.nist.hit.ds.simSupport.engine.SimChainLoaderException;
import gov.nist.hit.ds.simSupport.engine.SimEngine;
import gov.nist.hit.ds.simSupport.engine.SimEngineException;
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;

public class Launcher {
	static Logger logger = Logger.getLogger(Launcher.class);

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
	 * @throws SimChainLoaderException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws SimEngineException 
	 */
	public Launcher launch(String chainDef) throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, SecurityException, IllegalArgumentException, SimChainLoaderException, SimEngineException {
		SimChain simChain = new SimChainLoader(chainDef).load();
		
		simChain.setBase(new SoapEnvironment(new HttpEnvironment().setResponse(new HttpServletResponseMock())));
		
		assertTrue(simChain.getSteps().size() > 0);

		SimEngine engine = new SimEngine(simChain);
		
		logger.info(engine.getDescription(simChain));

		engine.run();

		logger.info(simChain.getLog());
		
		return this;

	}
}
