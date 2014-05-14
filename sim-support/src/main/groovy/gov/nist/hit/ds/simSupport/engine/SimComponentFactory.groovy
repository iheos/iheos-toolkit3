package gov.nist.hit.ds.simSupport.engine
import gov.nist.hit.ds.simSupport.exception.SimChainLoaderException
import gov.nist.hit.ds.simSupport.exception.SimEngineException
import gov.nist.hit.ds.simSupport.exception.SimEngineSubscriptionException

import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
/**
 * Create an instance of a Sim Component and inject the supplied parameters. 
 * Also inject the component name;
 * @author bmajur
 *
 */
public class SimComponentFactory {
    String className;
    Map<String, String> parmMap;
    Class<?> clazz = null;
    SimComponent component = null;

    /**
     * Load a SimComponent and return a newly created instance.
     * @param className - fully qualified classname. This class must
     * implement the SimComponent interface or the loadFromPropertyBasedResource will fail.
     * @param name - name value to be injected (getName()/setName(String)
     * @param parmMap - String valued parameters to inject. A parameter named
     * "action" requires that the class contain the setter setAction(String action).
     * Typical calling sequence is:
     * new SimComponentFactory(class_name, component_name).loadFromPropertyBasedResource() which
     * returns the new instance
     */
    SimComponentFactory(String className, Properties parmMap) {
        this.className = className
        this.parmMap = asMap(parmMap)
    }

    SimComponentFactory(String className, Map<String, String> parms) {
        this.className = className
        this.parmMap = parms
    }

    SimComponent getComponent() throws Exception {
        if (!component) load()
        return component
    }

    private Map<String, String> asMap(Properties p) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        for (String propName : p.stringPropertyNames()) {
            parameterMap.put(propName, p.getProperty(propName));
        }
        return parameterMap;
    }

    def load() {
        mkInstance()
        injectParameters()
    }

    private def mkInstance() throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, SimEngineException, SimChainLoaderException {
        try {
            clazz = getClass().getClassLoader().loadClass(className)
        } catch (ClassNotFoundException e) {
            def msg = "Component class not found: <${className}>"
            throw new SimChainLoaderException(msg)
        } catch (NoClassDefFoundError e) {
            def msg = "Component class not found: <${className}>"
            throw new SimChainLoaderException(msg)
        }
        Constructor<?> cons = clazz.getConstructor((Class<?>[]) null)
        Object instance = cons.newInstance((Object[]) null)
        if (instance instanceof SimComponent)
            component = (SimComponent) instance
        else
            throw new SimEngineSubscriptionException("Component <${className}> does not implement the SimComponent interface")
    }

    private def injectParameters() throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        new Injector(component, parmMap).injectAll();
    }

}
