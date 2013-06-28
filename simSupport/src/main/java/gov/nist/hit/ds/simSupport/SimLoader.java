package gov.nist.hit.ds.simSupport;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Loads simulators if necessary and launches them.
 * @author bill
 *
 */
public class SimLoader {
	String simName;
	static Properties simConfig = null;
	// map from simName to implementing simulator Class
	static Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
	
	/**
	 *  
	 * @param simName - short name for simulator
	 */
	public SimLoader(String simName) {
		this.simName = simName;
	}

	/**
	 * return the simName => class mapping. This is synchronized since all 
	 * threads share this data structure.
	 * @return
	 * @throws IOException
	 */
	 synchronized Properties getSimConfig() throws IOException {
		if (simConfig != null)
			return simConfig;
		loadSimConfig();
		return simConfig;
	}

	 void loadSimConfig() throws IOException {
		InputStream is = ClassLoader.getSystemResourceAsStream("simconfig.properties");
		simConfig = new Properties();
		try {
			simConfig.load(is);
		}
		finally {
			is.close();
		}
	}
	
	/**
	 * Load a simulator class.  This method is synchronized because of common access
	 * to the static classMap variable. 
	 * @return
	 * @throws SimConfigurationException
	 * @throws IOException
	 */
	synchronized Class<?> loadSimClass() throws SimConfigurationException, IOException {
		if (classMap.get(simName) != null)
			return classMap.get(simName);
		String simClassName = getSimConfig().getProperty(simName, null);
		if (simClassName == null) {
			throw new SimConfigurationException("Simulator " + simName + " is not configured in toolkit");
		}
		try {
			Class<?> clas = ClassLoader.getSystemClassLoader().loadClass(simClassName);
			classMap.put(simName, clas);
			return clas;
		} catch (ClassNotFoundException e) {
			throw new SimConfigurationException("Cannot load simulator " + simName + 
					" - the simulator definition class " + simClassName + " is not loaded in toolkit");
		}
	}
	
	public GenericSim getSimulator() throws SimConfigurationException, IOException, InstantiationException, IllegalAccessException {
		Class<?> clas = loadSimClass();
		Object obj = clas.newInstance();
		if (obj instanceof GenericSim)
			return (GenericSim) obj;
		throw new SimConfigurationException("Simulator " + simName + " which is implemnted by class " + 
			clas.getName() + " does not implement the GenericSimulator Interface");
	}
	
}
