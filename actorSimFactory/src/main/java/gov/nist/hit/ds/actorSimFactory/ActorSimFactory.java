package gov.nist.hit.ds.actorSimFactory;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.simSupport.engine.SimChain;
import gov.nist.hit.ds.simSupport.engine.SimChainLoader;
import gov.nist.hit.ds.simSupport.engine.SimChainLoaderException;
import gov.nist.hit.ds.simSupport.engine.SimEngine;
import gov.nist.hit.ds.simSupport.engine.SimEngineSubscriptionException;
import gov.nist.hit.ds.soapSupport.core.FaultCode;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.utilities.io.Io;
import gov.nist.hit.ds.utilities.string.StringUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * This class is a super factory, loading all known simulator factories so they are available
 * later when simulators are created. To avoid nasty dependency looping within the Java
 * build process, this factory does not reference any of the simulator factories directly.
 * Instead, a property format configuration file is use to control the configuration. The format
 * of this property file looks like:
<pre>
sim1.simChain=full_class_name_of_sim_chain
sim1.actor=actor abbreviation
sim1.transaction = transaction abbreviation
sim1.name=name_of_sim_to_put_in_displays
</pre>
 * which is repeated for other simulators (sim2, sim3).
 * This config file is located in the classpath at configuredActorSims.properties.
 * It is loaded at start-up time via the classloader.
 * The referenced SimChains are loaded by the class SimChainLoader.
 * 
 * @author bmajur
 *
 */
public class ActorSimFactory {
	static String configuredSimsFileName = "configuredActorSims.properties";
	static File configuredSimsFile = null;  // only used during testing
	static Properties simConfig = new Properties();
	static final Logger logger = Logger.getLogger(ActorSimFactory.class);

	static final Map<String /* actor^trans code */, SimChain> factories = new HashMap<String, SimChain>();

	public void  setConfiguredSimsFile(File file) {
		configuredSimsFile = file;
	}

	public void run(String actorTransCode, Object base) throws SoapFaultException, SimEngineSubscriptionException {
		String actorCode = actorCode(actorTransCode);
		String transCode = transactionCode(actorTransCode);
		if (actorCode == null) {
			throw new SoapFaultException(
					null,
					FaultCode.EndpointUnavailable,
					new ErrorContext("Invalid Actor code in endpoint <" + actorCode + ">")
					);
		}
		if (transCode == null) {
			throw new SoapFaultException(
					null,
					FaultCode.EndpointUnavailable,
					new ErrorContext("Invalid Transaction code in endpoint <" + transCode + ">")
					);
		}


		SimChain simChain = factories.get(actorTransCode); 
		if (simChain == null) {
			String msg = "Do not have a Simulator for Actor Code <" + actorCode + "> and Transaction Code <" + transCode + ">";
			logger.error(msg);
			throw new SoapFaultException(
					null,
					FaultCode.EndpointUnavailable,
					new ErrorContext(msg)
					);
		}

		simChain.setBase(base);
		SimEngine engine = new SimEngine(simChain);
		System.out.println(engine.getDescription(simChain).toString());
		engine.run();
		System.out.println(simChain.getLog());
	}

	/**
	 * Initialize Simulator environment by loading the simulator definitions
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws SimEngineSubscriptionException
	 * @throws SimChainLoaderException
	 */
	public void loadSims() throws FileNotFoundException, IOException, SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, SimEngineSubscriptionException, SimChainLoaderException  {
		if (configuredSimsFile != null)
			simConfig.load(Io.getInputStreamFromFile(configuredSimsFile));
		else
			simConfig.load(new ActorSimFactory().getClass().getResourceAsStream(configuredSimsFileName));

		Enumeration<?> pnames = simConfig.propertyNames();
		while (pnames.hasMoreElements()) {
			String propName = (String) pnames.nextElement();
			if (!propName.endsWith("simChain"))
				continue;
			String simChainPath = simConfig.getProperty(propName);
			String itemName = StringUtil.firstPiece(propName, ".");
//			String simName = simConfig.getProperty(itemName + ".name");
			String actorCode = simConfig.getProperty(itemName + ".actor");
			String transCode = simConfig.getProperty(itemName + ".transaction");
			if (actorCode == null || actorCode.equals("") || transCode == null || transCode.equals("")) {
				logger.error("Parsing configuredSims.properties: item <" + itemName + "> must have an actor and a transaction property to be valid - simulator not loaded");
				continue;
			}
			SimChain simChain = new SimChainLoader(simChainPath).load();
			factories.put(actorCode + "^" + transCode, simChain);			
		}
	}

	String actorCode(String atCode) {
		try {
			return atCode.split("\\^")[0];
		} catch (NullPointerException e) {
			return null;
		}
	}

	String transactionCode(String atCode) {
		try {
			return atCode.split("\\^")[1];
		} catch (NullPointerException e) {
			return null;
		}
	}

}
