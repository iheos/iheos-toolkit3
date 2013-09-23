package gov.nist.hit.ds.simSupport.engine;

import gov.nist.hit.ds.utilities.string.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Inject String type parameters into object. This is used by the
 * SimChainLoader to initialize components from the property file
 * based simulator configuration.
 * @author bmajur
 *
 */
public class Injector {
	Object object;
	Properties paramMap;
	static Logger logger = Logger.getLogger(Injector.class);

	/**
	 * Create an Injector
	 * @param object - object to inject to
	 * @param paramMap - mapping of parameter names to parameter values.
	 * Parameter names must align with available setters.  So a parameter
	 * map item of description ==> "My Object" will result in the execution
	 * of:
	 *     object.setDescription("My Object").
	 * 
	 */
	public Injector(Object object, Properties paramMap) {
		this.object = object;
		this.paramMap = paramMap;
	}

	public void run() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		logger.debug("Injecting on class <" + object.getClass().getName() + ">");
		for (Enumeration<?> it = paramMap.propertyNames(); it.hasMoreElements(); ) {
			String paramName = (String) it.nextElement();
			if ("class".equals(paramName))
				continue;
			String paramValue = paramMap.getProperty(paramName);
//			logger.debug("Parm name = <" + paramName + "> value = <" + paramValue + ">");
			Object[] params = new String[] { paramValue };
			Method setter = getSetter(paramName);
			logger.debug("Method is: <" + setter + "> Parm is: <" + paramValue + ">");
			setter.invoke(object, params);
		}
	}

	Method getSetter(String paramName) throws SecurityException, NoSuchMethodException  {
		Class<?>[] parmTypes = new Class<?>[] { String.class };
		Method method = object.getClass().getMethod("set" + StringUtil.capitalize(paramName), parmTypes);
		return method;
	}
}
