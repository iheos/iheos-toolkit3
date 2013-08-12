package gov.nist.hit.ds.actorSim.factory;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.soapSupport.core.FaultCode;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.utilities.io.Io;
import gov.nist.hit.ds.utilities.string.StringUtil;

import java.io.File;
import java.io.IOException;
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
sim1.class=full_class_name_of_sim_factory
sim1.at=actor_code^trans_code
sim1.name=name_of_sim_to_put_in_displays
sim2.class=full_class_name_of_sim_factory
sim2.at=actor_code^trans_code
sim2.name=name_of_sim_to_put_in_displays
</pre>
 * This config file is located in the classpath at configuredSims.properties.
 * It is loaded at start-up time via the classloader.
 * To be valid, a simulator factory must implement the interface gov.nist.hit.ds.actorSimFactory.ActorFactory
 * 
 * @author bmajur
 *
 */
public class ActorSimFactory {
	static String configuredSimsFileName = "configuredSims.properties";
	static File configuredSimsFile = null;  // only used during testing
	static Properties simConfig = new Properties();
	static final Logger logger = Logger.getLogger(ActorSimFactory.class);

	static final Map<String /* actor^trans code */, ActorFactory> factories = new HashMap<String, ActorFactory>();

	public void  setConfiguredSimsFile(File file) {
		configuredSimsFile = file;
	}

	public void run(String actorTransCode) throws SoapFaultException {
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


		ActorFactory factory = factories.get(actorTransCode); 
		if (factory == null) {
			String msg = "Do not have a Simulator for Actor Code <" + actorCode + "> and Transaction Code <" + transCode + ">";
			logger.error(msg);
			throw new SoapFaultException(
					null,
					FaultCode.EndpointUnavailable,
					new ErrorContext(msg)
					);
		}

		factory.run();
	}

	/**
	 * Initialize Simulator environment by loading the simulator definitions.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void loadSims() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (configuredSimsFile != null)
			simConfig.load(Io.getInputStreamFromFile(configuredSimsFile));
		else
			simConfig.load(new ActorSimFactory().getClass().getResourceAsStream(configuredSimsFileName));

		Enumeration<?> pnames = simConfig.propertyNames();
		ActorSimFactory factory = new ActorSimFactory();
		while (pnames.hasMoreElements()) {
			String propName = (String) pnames.nextElement();
			if (!propName.endsWith("class"))
				continue;
			String className = simConfig.getProperty(propName);
			String itemName = StringUtil.firstPiece(propName, ".");
			String simName = simConfig.getProperty(itemName + ".name");
			String actorTransCode = simConfig.getProperty(itemName + ".at");
			if (actorTransCode == null || actorTransCode.equals("")) {
				logger.error("Parsing configuredSims.properties: item <" + itemName + "> has no <at> property - simulator not loaded");
				continue;
			}
			if (!isValidActorTransactionCode(actorTransCode)) {
				logger.error("Parsing configuredSims.properties: item <" + itemName + "> has an invalid <at> property <" + actorTransCode +"> - simulator not loaded");
				continue;
			}
			if (simName == null || simName.equals(""))
				simName = StringUtil.lastPiece(className, "\\.");
			Class<?> clazz = factory.getClass().getClassLoader().loadClass(className);
			Class<?>[] interfaces = clazz.getInterfaces();
			boolean foundInterface = false;
			String requiredInterfaceName = ActorFactory.class.getName();
			if (interfaces != null ) {
				for (int i=0; i<interfaces.length; i++) {
					if (requiredInterfaceName.equals(interfaces[i].getName())) {
						foundInterface = true;
						break;
					}
				}
			}
			if (!foundInterface) {
				logger.error("Loadable Simulator <" + className + "> does not implement the required interface <" + requiredInterfaceName + "> and will not be loaded");
				continue;
			}
			ActorFactory instance = (ActorFactory) clazz.newInstance();
			factories.put(actorTransCode, instance);
		}
	}

	String actorCode(String atCode) {
		if (!isValidActorTransactionCode(atCode)) 
			return null;
		return atCode.split("\\^")[0];
	}

	String transactionCode(String atCode) {
		if (!isValidActorTransactionCode(atCode)) 
			return null;
		return atCode.split("\\^")[1];
	}

	boolean isValidActorTransactionCode(String atCode) {
		if (atCode == null) return false;
		if (atCode.equals("")) return false;
		String[] parts = atCode.split("\\^");
		if (parts == null) return false;
		if (parts.length != 2) return false;
		if (parts[0].length() == 0) return false;
		if (parts[1].length() == 0) return false;
		return true;
	}

}
