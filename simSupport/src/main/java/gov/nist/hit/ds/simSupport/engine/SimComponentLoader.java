package gov.nist.hit.ds.simSupport.engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * Create an instance of a Sim Component and inject the supplied parameters. 
 * Also inject the component name;
 * @author bmajur
 *
 */
public class SimComponentLoader {
	String className;
	Properties parmMap;
	Class<?> clazz = null;
	SimComponent component;

	/**
	 * Load a SimComponent and return a newly created instance. 
	 * @param className - fully qualified classname. This class must 
	 * implement the SimComponent interface or th load will fail.
	 * @param name - name value to be injected (getName()/setName(String)
	 * @param parmMap - String valued parameters to inject. A parameter named
	 * "action" requires that the class contain the setter setAction(String action).
	 * Typical calling sequence is:  
	 * new SimComponentLoader(class_name, component_name).load() which
	 * returns the new instance
	 */
	public SimComponentLoader(String className, Properties parmMap) {
		this.className = className;
		this.parmMap = parmMap;
	}

	void mkInstance() throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, SimEngineSubscriptionException, SimChainLoaderException {
		try {
			clazz = getClass().getClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new SimChainLoaderException("SimComponentLoader: Cannot load SimComponent <" + className + ">");
		} catch (NoClassDefFoundError e) {
			throw new SimChainLoaderException("SimComponentLoader: Cannot load SimComponent <" + className + ">");
		}
		Constructor<?> cons = clazz.getConstructor((Class<?>[]) null);
		Object instance = cons.newInstance((Object[]) null);
		if (instance instanceof SimComponent)
			component = (SimComponent) instance;
		else
			throw new SimEngineSubscriptionException("Component <" + className + "> does not implement the SimComponent interface");
	}

	void injectParameters(Properties parmeterMap) throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		new Injector(component, parmeterMap).run();
	}

	public SimComponent load() throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, SimEngineSubscriptionException, SimChainLoaderException {
		mkInstance();
		if (parmMap != null)
			injectParameters(parmMap);
		Properties nameMap = new Properties();
		injectParameters(nameMap);
		return component;
	}
}
