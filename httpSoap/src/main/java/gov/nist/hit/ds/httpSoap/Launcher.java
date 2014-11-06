package gov.nist.hit.ds.httpSoap;

import gov.nist.hit.ds.repository.api.RepositoryException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

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
	 * @throws RepositoryException 
	 */
//	public Launcher launch(String chainDef) throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, SecurityException, IllegalArgumentException, SimChainLoaderException, SimEngineException, RepositoryException {
//		SimChain simChain = new SimChainLoader(chainDef).load();
//
//		simChain.setBase(new SoapEnvironment(new HttpEnvironment().setResponse(new HttpServletResponseMock())));
//
//		assertTrue(simChain.getSteps().size() > 0);
//
//		SimEngine engine = new SimEngine(simChain, new Event(null));
//
//		logger.info(engine.getDescription(simChain));
//
//		engine.run();
//
//		logger.info(simChain.getLog());
//
//		return this;
//
//	}
}
