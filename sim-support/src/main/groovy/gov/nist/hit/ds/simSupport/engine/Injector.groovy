package gov.nist.hit.ds.simSupport.engine;

import gov.nist.hit.ds.utilities.string.StringUtil

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * SimComponentInject String type parameters into object. This is used by the
 * SimChainLoader to installRepositoryLinkage components from the property file
 * based simulator configuration.
 * @author bmajur
 *
 */
public class Injector {
	def object;
	def paramMap;
	static Logger logger = Logger.getLogger(Injector);

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
	Injector(Object object, Map<String, String> paramMap) {
		this.object = object;
		this.paramMap = paramMap;
	}

	void injectAll() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		logger.trace("Injecting on class <${object.class.name}>")
        paramMap.keys().each { paramName ->
            if ('class' == paramName) return;
            def paramValue = paramMap.getProperty(paramName);
			logger.trace("Parm name = <" + paramName + "> value = <" + paramValue + ">");
            Object[] params = [ paramValue ] as Array
            Method setter = getSetter(paramName);
            logger.trace("Method is: <" + setter + "> Parm is: <" + paramValue + ">");
            setter.invoke(object, params);
        }
	}

	def getSetter(String paramName) throws SecurityException, NoSuchMethodException  {
		Class<?>[] paramTypes = [ String ] as Array
		Method method = object.class.getMethod("set${StringUtil.capitalize(paramName)}", paramTypes);
		return method;
	}
}
