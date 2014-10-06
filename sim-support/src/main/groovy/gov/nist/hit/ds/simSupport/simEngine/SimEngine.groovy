package gov.nist.hit.ds.simSupport.simEngine
import gov.nist.hit.ds.eventLog.EventDAO
import gov.nist.hit.ds.eventLog.Fault
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.simSupport.annotations.SimComponentInject
import gov.nist.hit.ds.simSupport.exception.SimEngineException
import gov.nist.hit.ds.simSupport.exception.SimEngineExecutionException
import gov.nist.hit.ds.simSupport.exception.SimEngineSubscriptionException
import gov.nist.hit.ds.simSupport.simChain.SimChain
import gov.nist.hit.ds.simSupport.validationEngine.ValComponent
import gov.nist.hit.ds.soapSupport.FaultCode
import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment
import gov.nist.hit.ds.soapSupport.core.SoapFault
import gov.nist.hit.ds.xdsException.ExceptionUtil
import org.apache.log4j.Logger

import java.lang.reflect.Method
/**
 * Simulator Engine.
 * TODO: Test addMessageValidator
 * @author bmajur
 *
 */
public class SimEngine  {
    SimChain simChain;
    SoapEnvironment soapEnvironment;
    static Logger logger = Logger.getLogger(SimEngine)

    def trace = false
    def componentNamesRun = []
    def stepsRun = []
    boolean hasError = false

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
        def eventDAO = new EventDAO()
        if (simChain.getBase() && (simChain.getBase() instanceof SoapEnvironment))
            soapEnvironment = (SoapEnvironment) simChain.getBase()
        else {
            SimStep simStep = simChain.getRunableStep()
            Fault fault = new Fault(faultMsg:'No Base Object defined in SimChain, must be instance of SoapEnvironment',
                    faultCode: FaultCode.Receiver.toString())
            simStep.event.fault = fault
            logger.error('No Base Object defined in SimChain, must be instance of SoapEnvironment')
            hasError = true
            SoapFaultException e = new SoapFaultException(null, FaultCode.Receiver,
                    'No Base Object defined in SimChain',
            '',''
            )
            SoapFault soapFault = new SoapFault(soapEnvironment, e);
            soapFault.send()
            eventDAO.save(simStep.event)
            return
        }

        while(!simChain.hasInternalErrors() && isRunable()) {
            SimStep simStep = simChain.getRunableStep()
            simStep.completed = true

            assert simStep.simComponent
            def priorComponents = getPriorComponents(simStep.simComponent)

            inject(simStep, priorComponents)
            runSimComponent(simStep)
            if (trace) componentNamesRun << simStep.simComponent.class.name
            if (trace) stepsRun << simStep
            eventDAO.save(simStep.event)
        }
    }

    /**
     * Has the Simulator Chain scan to completion?
     */
    public boolean isRunable() { !hasError && simChain.getRunableStep()  }

    public boolean isComplete() { !isRunable() }

    def inject(SimStep simStep, priorComponents) {
        try {
            injectInputs(simStep, priorComponents)
        } catch (SimEngineSubscriptionException e) {
            simStep.fail(e.getMessage())
        } catch (RuntimeException e) {
            logger.fatal(ExceptionUtil.exception_details())
        }
    }

    def runSimComponent(SimStep simStep) {
        try {
            simStep.simComponent.run()
        } catch (SoapFaultException e) {
            SoapFault soapFault = new SoapFault(soapEnvironment, e);
            try {
                simStep.event.fault = e.asFault();
                soapFault.send()
            } catch (Exception e1) {
                logger.error(ExceptionUtil.exception_details(e1))
            }
        } catch (RuntimeException e) {
            logger.fatal(ExceptionUtil.exception_details(e))
        }

    }

    def getPriorComponents(simComponent) {
        def allComponents = simChain.getComponents().reverse()
        def base = simChain.getBase()
        if (base) allComponents.add(base)
        def index = allComponents.indexOf(simComponent)
        // all components that preceed simComponent (remember, list is revered)
        allComponents[index+1..<allComponents.size()]
    }

    /**
     * Given a component to be scan, identify its subscriptions and getActorTypeIfAvailable
     * publishers to provide them. Only consider pubilshers that come
     * earlier in the valchain. Execute the getter/setter combination
     * to inject the necessary parameter. The combination of getter/setter is
     * refered to as pubSub.
     * TODO: Why is subscriptionObject of type Object instead of ValSim????
     * @param subObject
     * @return
     * @throws gov.nist.hit.ds.simSupport.exception.SimEngineSubscriptionException
     */
    void injectInputs(SimStep simStep, priorComponents) throws SimEngineException {
        ValComponent subscriptionObject = simStep.simComponent
        Class<?> componentClass = subscriptionObject.class
        String componentClassName = componentClass.name
        if (componentClassName.indexOf('simSupport') != -1)
            logger.debug(componentClassName);
        else
            logger.debug("======================= " + componentClassName + "  ==========================");
        Method[] componentMethods = componentClass.methods;
        // For all setters in this subscriptionObject, getActorTypeIfAvailable and
        // execute the getter/setter pair to inject the necessary
        // objects into subscriptionObject so it is ready to scan.  Caller
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
            if (subParamTypes?.length != 1) return
            Class<?> injectableClass = subParamTypes[0];  // subscription class

            if (injectableClass.name.startsWith('java.'))
                throw new SimEngineSubscriptionException("Illegal subscription type: java.*: <" + injectableClass.getClass().getName() + "> " + "<#" + subMethName + ">");
            PubSubMatch match = findInjectable(simStep, injectableClass, injectionMethod, priorComponents);

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
    PubSubMatch findInjectable(SimStep simStep, Class<?> subClass, Method subMethod, priorComponents) throws SimEngineSubscriptionException {
        ValComponent subscriptionObject = simStep.simComponent
        for (Object pubObject : priorComponents) {
            Method pubMethod = pubObject.class.methods.find { method ->
                method.name.startsWith("label") && method.returnType == subClass
            }
            if (!pubMethod) continue

            PubSubMatch match = new PubSubMatch()
            match.pubMethod = pubMethod
            match.pubObject = pubObject
            match.subMethod = subMethod
            match.subObject = subscriptionObject
            return match;
        }
        def err = '\n    Component execution error.\n' +
                "    Component <${subscriptionObject.class.name}> requires input of type ${subClass.name} which is not availble.\n" +
                '    Available inputs are:\n' +
                documentSimsUpTo(subscriptionObject, priorComponents)
        simStep.internalError(err)
        throw new SimEngineSubscriptionException(err)
    }

    /**
     * Perform single injection.  The supplier/consumer (getter/setter) has already
     * been identified and captured in the match object.
     * @param match
     * @throws gov.nist.hit.ds.simSupport.exception.SimEngineExecutionException
     */
    void executePubSub(PubSubMatch match) throws SimEngineExecutionException {
        try {
            Object o = match.getPubMethod().invoke(match.getPubObject(), (Object[]) null)
            if (o == null) System.out.println(".Value is null");
            Object[] args = new Object[1];
            args[0] = o;
            match.getSubMethod().invoke(match.getSubObject(), args)
        } catch (Exception e) {
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
            buf.append("Component ${simChain.getBase().class.name} offers types\n")
            getPublishedTypesDescription(buf, simChain.getBase());
        }

        def previousComponents = componentsPriorTo(targetComponent, priorComponents)
        previousComponents.each {
            buf.append("Component ${it.class.name} offers types\n")
            getPublishedTypesDescription(buf, it);
        }
        return buf;
    }

    void getPublishedTypesDescription(StringBuffer buf, Object pubSim) {
        Method[] pubMethods = pubSim.class.methods
        pubMethods.each { pubMethod ->
            def methodName = pubMethod.name
            if (!methodName.startsWith("label")) return
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
        StringBuffer buf = new StringBuffer()
        logger.debug("---------------------------------------------------------------\nSimChain Analyis\n")

        describe(simChain.getBase(), buf)
        simChain.steps.each { describe(it.getSimComponent(), buf) }
        return buf
    }

    void describe(Object o, StringBuffer buf) {
        if (o == null)
            return;
        Class<?> clas = o.getClass();
        buf.append(clas.getName()).append("\n");
        Method[] methods = clas.getMethods();

        for (int i=0; i<methods.length; i++) {
            Method method = methods[i];
            if (!method.isAnnotationPresent(SimComponentInject)) continue;
            String name = method.getName();
            if (name.equals("setErrorRecorder")) continue
            if (name.equals("setName")) continue
            if (!name.startsWith("set")) continue
            Class<?>[] parmTypes = method.getParameterTypes();
            if (parmTypes?.length == 1)
                buf.append("..Needs " + parmTypes[0].getSimpleName()).append(" <#").append(name).append(">").append("\n");
        }

        for (int i=0; i<methods.length; i++) {
            Method method = methods[i];
            String name = method.getName();
            if (name.startsWith("label") && !name.equals("getClass") && !name.equals("getName")) {
                Class<?> returnType = method.getReturnType();
                if (!returnType.getName().startsWith("java."))
                    buf.append("..Generates " + returnType.getSimpleName()).append(" <#").append(name).append(">").append("\n");
            }
        }
    }

    StringBuffer getExecutionTrace() {
        def buf = new StringBuffer()
        if (!trace) return buf
        buf.append('Engine trace:\n')
        //componentNamesRun.each { buf.append('  ').append(it).append('\n') }
        if (simChain.getBase()) buf.append('  ').append(simChain.getBase().class.name).append('\n')
        stepsRun.each { step ->
            buf.append('  ').append(step.simComponent.class.name)
            if (step.hasInternalError()) buf.append('  ').append(step.getInternalError())
            buf.append('\n')
        }
        return buf
    }


}
