package gov.nist.hit.ds.simSupport.simEngine
import gov.nist.hit.ds.utilities.string.StringUtil
import org.apache.log4j.Logger

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
/**
 * SimComponentInject String type parameters into object. This is used by the
 * SimChainFactory to installRepositoryLinkage components from the property file
 * based simulator configuration.
 * @author bmajur
 *
 */
public class Injector {
	def object;
    Map<String, String> paramMap;
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
        paramMap.keySet().each { paramName ->
            if ('class' == paramName) return;
            def paramValue = paramMap[paramName];
			logger.trace("Parm name = <" + paramName + "> value = <" + paramValue + ">");
            Object[] params = [ paramValue ].toArray()
            Method setterMethod = getSetterMethod(paramName);
            logger.trace("Method is: <" + setterMethod + "> Parm is: <" + paramValue + ">");
            setterMethod.invoke(object, params);
        }
	}

	def getSetterMethod(String paramName) throws SecurityException, NoSuchMethodException  {
		Class<?>[] paramTypes = [ String ].toArray()
        try {
            Method method = object.class.getMethod("set${StringUtil.capitalize(paramName)}", paramTypes);
            return method;
        } catch (NoSuchMethodException e) {
            def msg = "SimChain loader: method does not exist: ${e.message}"
            logger.error(msg)
            throw new NoSuchMethodException(msg)
        }
	}
}
