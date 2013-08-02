package gov.nist.hit.ds.actorSimFactory;

import gov.nist.hit.ds.utilities.string.StringUtil;

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
sim1.name=name_of_sim_to_put_in_displays
sim2.class=full_class_name_of_sim_factory
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
	static final String configuredSimsFileName = "configuredSims.properties";
	static Properties simConfig = new Properties();
	static final Logger logger = Logger.getLogger(ActorSimFactory.class);

	static final Map<String /* ActorType.name */, ActorFactory> factories = new HashMap<String, ActorFactory>();
	
	static {
		try {
			loadSims();
		} catch (Exception e) {
			logger.error("Error loading Simulator definitions", e);
		} 
	}

	static void loadSims() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		simConfig.load(new ActorSimFactory().getClass().getResourceAsStream(configuredSimsFileName));

		Enumeration<?> pnames = simConfig.propertyNames();
		ActorSimFactory factory = new ActorSimFactory();
		while (pnames.hasMoreElements()) {
			String propName = (String) pnames.nextElement();
			if (!propName.endsWith("class"))
				continue;
			String className = simConfig.getProperty(propName);
			String simId = StringUtil.removePrefixEndingWith(propName, ".");
			String simName = simConfig.getProperty(simId + ".name");
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
			factories.put(simName, instance);
		}
	}

}
