package gov.nist.hit.ds.simSupport.engine

import gov.nist.hit.ds.eventLog.EventDAO
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.simSupport.annotations.SimComponentInject
import gov.nist.hit.ds.simSupport.exception.SimEngineException
import gov.nist.hit.ds.simSupport.exception.SimEngineExecutionException
import gov.nist.hit.ds.simSupport.exception.SimEngineSubscriptionException
import gov.nist.hit.ds.soapSupport.SoapFault
import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment
import gov.nist.hit.ds.xdsException.ExceptionUtil
import org.apache.log4j.Logger

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
/**
 * Simulator Engine.
 * TODO: Test addMessageValidator
 * @author bmajur
 *
 */
public class SimEngine /* implements MessageValidatorEngine */ {
    SimChain simChain;
    int simsRun = 0;
    SoapEnvironment soapEnvironment;
    static Logger logger = Logger.getLogger(SimEngine)

//    public SimEngine(String simChainResource) throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, SecurityException, IllegalArgumentException, SimChainLoaderException, SimEngineException, RepositoryException {
//        this(new SimChainFactory(simChainResource).loadFromPropertyBasedResource(), event);
//    }

    public SimEngine(SimChain simChain) {
        this.simChain = simChain
    }

    /**
     * Run the current Simulator Chain.  This method is not re-entrant and
     * should only be called once to execute a chain, even if the chain grows
     * during its execution.
     * If new SimSteps are added to the Chain after execution starts then the
     * outer loop (while(!isRunable()) will catch the added SimSteps.
     * @throws RepositoryException
     * @throws gov.nist.hit.ds.simSupport.exception.SimEngineSubscriptionException
     */
    public void run() throws SimEngineException, RepositoryException {
        if (simChain.getBase() && (simChain.getBase() instanceof SoapEnvironment))
            soapEnvironment = (SoapEnvironment) simChain.getBase()

        while(!simChain.hasErrors() && isRunable()) {
            SimStep simStep = simChain.getRunableStep()
            simStep.completed = true

            SimComponent simComponent = simStep.simComponent
            assert simComponent
            def priorComponents = getPriorComponents(simComponent)

            inject(simComponent, priorComponents, simStep)
            simsRun++
            runSimComponent(simComponent, simStep)
            new EventDAO().save(simStep.event)
        }
    }

    def inject(simComponent, priorComponents, SimStep simStep) {
        try {
            injectInputs(simComponent, priorComponents)
        } catch (SimEngineSubscriptionException e) {
            simStep.fail(e.getMessage())
        } catch (RuntimeException e) {
            logger.fatal(ExceptionUtil.exception_details())
        }
    }

    def runSimComponent(def simComponent, def simStep) {
        try {
            simComponent.run(null)
        } catch (SoapFaultException e) {
            SoapFault soapFault = new SoapFault(soapEnvironment, e);
            try {
                simStep.event.fault = e.getMessage()  // ensure logging
                soapFault.send()
            } catch (Exception e1) {
                logger.error(ExceptionUtil.exception_details(e1))
            }
        } catch (RuntimeException e) {
            logger.fatal(ExceptionUtil.exception_details())
        }

    }
    /**
     * Has the Simulator Chain injectAll to completion?  If not, maybe a SimStep added
     * a new SimStep to the chain.  Either way, injectAll() should be called in a loop
     * until isRunable() returns true;
     * @return
     */
    public boolean isRunable() { simChain.getRunableStep()  }


    List<String>  getClassNames(Class<?>[] classes) {
        List<String> names = new ArrayList<String>();

        for (int i=0; i<classes.length; i++)
            names.add("<" + classes[i].getName() + ">");

        return names;
    }

    def getPriorComponents(simComponent) {
        def allComponents = simChain.getComponents()
        allComponents.reverse(true)  // reverse in place - look at most recent first
        def base = simChain.getBase()
        if (base) allComponents.add(base)
        def index = allComponents.indexOf(simComponent)
        allComponents[index+1..<allComponents.size()]
    }

    /**
     * Given a component to be run, identify its subscriptions and find
     * publishers to provide them. Only consider pubilshers that come
     * earlier in the valchain. Execute the getter/setter combination
     * to inject the necessary parameter. The combination of getter/setter is
     * refered to as pubSub.
     * TODO: Why is subscriptionObject of type Object instead of ValSim????
     * @param subObject
     * @return
     * @throws gov.nist.hit.ds.simSupport.exception.SimEngineSubscriptionException
     */
    void injectInputs(SimComponent subscriptionObject, priorComponents) throws SimEngineException {
        Class<?> componentClass = subscriptionObject.class
        String componentClassName = componentClass.name
        if (componentClassName.indexOf('simSupport') != -1)
            logger.debug(componentClassName);
        else
            logger.debug("======================= " + componentClassName + "  ==========================");
        Method[] componentMethods = componentClass.methods;
        // For all setters in this subscriptionObject, find and
        // execute the getter/setter pair to inject the necessary
        // objects into subscriptionObject so it is ready to run.  Caller
        //

        def injectionMethods
        injectionMethods = componentMethods.findAll { method ->
            method.isAnnotationPresent(SimComponentInject) &&
                    method.name.startsWith('set') &&
                    method.name != 'setErrorRecorder' &&
                    method.name != 'setName'
        }
        injectionMethods.each { injectionMethod ->
            Class<?>[] subParamTypes = injectionMethod.parameterTypes
            // must be single input param, input must be an object
            if (subParamTypes == null || subParamTypes.length != 1) return
            Class<?> injectableClass = subParamTypes[0];  // subscription class

            if (injectableClass.name.startsWith('java.'))
                throw new SimEngineSubscriptionException("Illegal subscription type: java.*: <" + injectableClass.getClass().getName() + "> " + "<#" + subMethName + ">");
            PubSubMatch match = findInjectable(subscriptionObject, injectableClass, injectionMethod, priorComponents);

            logger.debug(match);
            executePubSub(match);
        }
    }

    /**
     * Find a publisher for class subClass that comes from a prior component. This is
     * done by searching through priorComponents.
     * @param subscriptionObject
     * @throws SimEngineSubscriptionException
     */
    PubSubMatch findInjectable(SimComponent subscriptionObject, Class<?> subClass, Method subMethod, priorComponents) throws SimEngineSubscriptionException {
        for (Object pubObject : priorComponents) {
            Method pubMethod = pubObject.class.methods.find { method ->
                method.name.startsWith("get") && method.returnType == subClass
            }
            if (!pubMethod) continue

            PubSubMatch match = new PubSubMatch();
            match.pubMethod = pubMethod
            match.pubObject = pubObject
            match.subMethod = subMethod
            match.subObject = subscriptionObject
            return match;
        }
        throw new SimEngineSubscriptionException(
                'Component Chain execution error.\n' +
                "Component <${subscriptionObject.class.name}> requires input of type ${subClass.name} which is not availble.\n" +
                        'Available inputs are:\n' +
                        documentSimsUpTo(subscriptionObject, priorComponents)
        );
    }

    /**
     * Perform single injection.  The supplier/consumer (getter/setter) has already
     * been identified and captured in the match object.
     * @param match
     * @throws gov.nist.hit.ds.simSupport.exception.SimEngineExecutionException
     */
    void executePubSub(PubSubMatch match) throws SimEngineExecutionException {
        try {
            Object o = match.getPubMethod().invoke(match.getPubObject(), (Object[]) null);
            if (o == null)
                System.out.println(".Value is null");
            Object[] args = new Object[1];
            args[0] = o;
            match.getSubMethod().invoke(match.getSubObject(), args);
        } catch (IllegalArgumentException e) {
            throw new SimEngineExecutionException(e);
        } catch (IllegalAccessException e) {
            throw new SimEngineExecutionException(e);
        } catch (InvocationTargetException e) {
            throw new SimEngineExecutionException(e);
        }
    }

    /**
     * This is a little wierd.  priorComponents is in reverse order because
     * we always search for most recent publisher of a class.  So, first it
     * must be put in chrono order then searched. The targetComponent must be
     * present in priorComponents.  If not then return entire list.
     * @param targetComponent
     * @param priorComponents
     */
    def componentsPriorTo(def targetComponent, def priorComponents) {
        def components = priorComponents.reverse()
        if (!priorComponents.contains(targetComponent)) return components
        def result = []
        for (def component : components) {
            if (component == targetComponent) break
            result << component
        }
        return result
    }

    StringBuffer documentSimsUpTo(def targetComponent, def priorComponents) {
        StringBuffer buf = new StringBuffer();
        if (simChain.getBase()) {
            buf.
                    append("Component ").append(simChain.getBase().class.name).
                    append(" offers types\n")
            getPublishedTypesDescription(buf, simChain.getBase());
        }

        def previousComponents = componentsPriorTo(targetComponent, priorComponents)
        previousComponents.each {
            buf.append("Component ${it.class.name} offers types\n")
            getPublishedTypesDescription(buf, it);
        }
        return buf;
    }

    void getPublishedTypesDescription(StringBuffer buf,
                                      Object pubSim) {
        Method[] pubMethods = pubSim.class.methods;
        pubMethods.each { pubMethod ->
            def methodName = pubMethod.name
            if (!methodName.startsWith("get")) return
            String typeName = pubMethod.returnType.name
            if (!typeName.startsWith("java."))
                buf.
                        append("....").
                        append(typeName).
                        append("\n")
        }
    }

    public int getSimsRun() {
        return simsRun;
    }

    public StringBuffer getDescription(SimChain simChain) {
        StringBuffer buf = new StringBuffer();
        logger.debug("---------------------------------------------------------------\nSimChain Analyis\n");

        describe(simChain.getBase(), buf);
        for(Iterator<SimStep> it=simChain.iterator(); it.hasNext(); ) {
            SimStep step = it.next();
            describe(step.getSimComponent(), buf);
        }

        return buf;
    }

    void describe(Object o, StringBuffer buf) {
        if (o == null)
            return;
        Class<?> clas = o.getClass();
        buf.append(clas.getName()).append("\n");
        Method[] methods = clas.getMethods();

        for (int i=0; i<methods.length; i++) {
            Method method = methods[i];
            if (!method.isAnnotationPresent(SimComponentInject.class))
                continue;
            String name = method.getName();
            if (name.startsWith("set") && !name.equals("setErrorRecorder") && !name.equals("setName")) {
                Class<?>[] parmTypes = method.getParameterTypes();
                if (parmTypes != null && parmTypes.length ==1)
                    buf.append("..Needs " + parmTypes[0].getSimpleName()).append(" <#").append(name).append(">").append("\n");
            }
        }

        for (int i=0; i<methods.length; i++) {
            Method method = methods[i];
            String name = method.getName();
            if (name.startsWith("get") && !name.equals("getClass") && !name.equals("getName")) {
                Class<?> returnType = method.getReturnType();
                if (!returnType.getName().startsWith("java."))
                    buf.append("..Generates " + returnType.getSimpleName()).append(" <#").append(name).append(">").append("\n");
            }
        }
    }

}
