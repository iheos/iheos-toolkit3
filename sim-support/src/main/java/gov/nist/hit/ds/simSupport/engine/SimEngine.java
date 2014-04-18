package gov.nist.hit.ds.simSupport.engine;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.EventDAO;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.annotations.SimComponentInject;
import gov.nist.hit.ds.simSupport.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.simSupport.exception.SimChainLoaderException;
import gov.nist.hit.ds.simSupport.exception.SimEngineException;
import gov.nist.hit.ds.simSupport.exception.SimEngineExecutionException;
import gov.nist.hit.ds.simSupport.exception.SimEngineSubscriptionException;
import gov.nist.hit.ds.soapSupport.SoapFault;
import gov.nist.hit.ds.soapSupport.SoapFaultException;
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment;
import gov.nist.hit.ds.xdsException.ExceptionUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * Simulator Engine.
 * TODO: Test addMessageValidator
 * @author bmajur
 *
 */
public class SimEngine /* implements MessageValidatorEngine */ {
	SimChain simChain;
	// This is of Object instead of ValSim since it includes the base type
	// which may not be of type of ValSim.
	List<Object> combinedInputs;
	List<PubSubMatch> pubSubMatches = new ArrayList<PubSubMatch>();
	int simsRun = 0;
	List<SimStep> stepsCompleted = new ArrayList<SimStep>();
	SoapEnvironment soapEnvironment;
	Event event;
	static Logger logger = Logger.getLogger(SimEngine.class);
	
	public SimEngine(String simChainResource, Event event) throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, SecurityException, IllegalArgumentException, SimChainLoaderException, SimEngineException, RepositoryException {
		this(new SimChainLoader(simChainResource).load(), event);
	}
	
	/**
	 * 
	 * @param valChain - list of validator instances
	 */
	public SimEngine(SimChain simChain, Event event) {
		this.simChain = simChain;
		this.event = event;
	}

	public SimEngine() {
		this.simChain = new SimChain();
	}
	
	void simStepCompleted(SimStep step) {
		if (stepsCompleted.contains(step))
			return;
		stepsCompleted.add(step);
	}
	
	public boolean isStepCompleted(SimStep step) {
		return stepsCompleted.contains(step);
	}
	
	public SimChain getSimChain() {
		return simChain;
	}
	
//	public MessageValidatorEngine getMessageValidatorEngine() {
//		return this;
//	}

	/**
	 * Run the current Simulator Chain.  This method is not re-entrant and
	 * should only be called once to execute a chain, even if the chain grows
	 * during its execution.
	 * If new SimSteps are added to the Chain after execution starts then the
	 * outer loop (while(!isComplete()) will catch the added SimSteps. 
	 * @throws RepositoryException 
	 * @throws gov.nist.hit.ds.simSupport.exception.SimEngineSubscriptionException
	 */
	public void run() throws SimEngineException, RepositoryException {
		if (simChain.getBase() != null && (simChain.getBase() instanceof SoapEnvironment))
			soapEnvironment = (SoapEnvironment) simChain.getBase();
		
		logger.debug("---------------------------------------------------------------");
		logger.debug("Run");
		boolean errorsFound = false;
//		SystemErrorRecorderBuilder erBuilder = new SystemErrorRecorderBuilder();
		while(!errorsFound && !isComplete()) {
			buildCombinedInputs();
			for (Iterator<SimStep> it=simChain.iterator(); it.hasNext(); ) {
				SimStep simStep = it.next();
				if (isStepCompleted(simStep))
					continue;
				simStepCompleted(simStep);
				SimComponent simComponent = simStep.getSimComponent();
				if (simStep.getAssertionGroup() == null) {
					AssertionGroup ag = event.getAssertionGroup();
                    ag.setValidatorName(simComponent.getClass().getName());
					simStep.setEvent(event);
					simStep.setAssertionGroup(ag);
				}
//				simStep.getAssertionGroup().setSaveInLog(simComponent.showOutputInLogs());
//				if (simComponent.getName() == null)
//					simStep.getAssertionGroup().sectionHeading("Validator");
//				else
//					simStep.getAssertionGroup().sectionHeading(simComponent.getName());
//				if (simComponent.getDescription() != null) 
//					simStep.getAssertionGroup().sectionHeading(simComponent.getDescription());
				matchPubSub(simComponent);
				simsRun++;
				try {
					simComponent.run((MessageValidatorEngine) this);
				} catch (SoapFaultException e) {
					SoapFault soapFault = new SoapFault(soapEnvironment, e);
					try {
                        simStep.getEvent().setFault(e.getMessage());
						soapFault.send();
					} catch (Exception e1) {
						logger.error(ExceptionUtil.exception_details(e1));
					}
                    new EventDAO().save(event);
					return;
				}
                new EventDAO().save(event);
				if (simChain.hasErrors()) {
					logger.error("Engine Error: " + simChain.getErrors());
					errorsFound = true;
				}
				break;  // this gets around concurrent modification errors to SimChain
				// in the case where dynamic sims are running (one sim
				// schedules the next.
			}
		}
		logger.debug("Engine: " + simsRun + " sims injectAll");
	}

	/**
	 * Has the Simulator Chain injectAll to completion?  If not, maybe a SimStep added
	 * a new SimStep to the chain.  Either way, injectAll() should be called in a loop
	 * until isComplete() returns true;
	 * @return
	 */
	public boolean isComplete() {
		for (Iterator<SimStep> it=simChain.iterator(); it.hasNext(); ) {
			SimStep simStep = it.next();
			if (!isStepCompleted(simStep))
				return false;
		}
		return true;
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

	List<String>  getClassNames(Class<?>[] classes) {
		List<String> names = new ArrayList<String>();

		for (int i=0; i<classes.length; i++)
			names.add("<" + classes[i].getName() + ">");

		return names;
	}

	/**
	 * CombinedInputs is made up of
	 *   - simChain in reverse order
	 *   - inputs
	 * This establishes the search order for finding publishers
	 */
	void buildCombinedInputs() {
		combinedInputs = new ArrayList<Object>();
		for (Iterator<SimStep> it=simChain.iterator(); it.hasNext(); ) {
			SimStep simStep = it.next();
			combinedInputs.add(simStep.getSimComponent());
		}
		Collections.reverse(combinedInputs);
		combinedInputs.add(simChain.getBase());
	}

	/**
	 * Given validator to be injectAll, identify its subscriptions and find
	 * publishers to provide them. Only consider pubilshers that come
	 * earlier in the valchain. Execute the getter/setter combination
	 * to inject the necessary parameter.
	 * TODO: Why is subscriptionObject of type Object instead of ValSim????
	 * @param subObject
	 * @return
	 * @throws gov.nist.hit.ds.simSupport.exception.SimEngineSubscriptionException
	 */
	void matchPubSub(SimComponent subscriptionObject) throws SimEngineException {
		Class<?> valClass = subscriptionObject.getClass();
		String className = valClass.getName();
		if (className.startsWith("gov.nist.hit.ds.simSupport"))
			logger.debug(className);
		else
			logger.debug("======================= " + className + "  ==========================");
		Method[] valMethods = valClass.getMethods();
		// For all setters in this subscriptionObject
		for (int subMethI=0; subMethI<valMethods.length; subMethI++) {
			Method subMethod = valMethods[subMethI];
			if (!subMethod.isAnnotationPresent(SimComponentInject.class))
				continue;
			String subMethName = subMethod.getName();
			if (!subMethName.startsWith("set"))
				continue;
			if (subMethName.equals("setErrorRecorder"))
				continue;
			if (subMethName.equals("setName"))
				continue;
			Class<?>[] subParamTypes = subMethod.getParameterTypes();
			if (subParamTypes == null || subParamTypes.length != 1)
				continue;  // must be single input param, input must be an object
			Class<?> subClass = subParamTypes[0];  // subscription class
			if (subClass.getName().startsWith("java."))
				throw new SimEngineSubscriptionException("Illegal subscription type: java.*: <" + subClass.getClass().getName() + "> " + "<#" + subMethName + ">");
			PubSubMatch match = findMatch(subscriptionObject, subClass, subMethod);
			pubSubMatches.add(match);
			logger.debug(match);
			executePubSub(match);
		}
	}

	/**
	 * find a publisher for class subClass
	 * @param subscriptionObject
	 * @throws SimEngineSubscriptionException 
	 */
	PubSubMatch findMatch(SimComponent subscriptionObject, Class<?> subClass, Method subMethod) throws SimEngineSubscriptionException {
		Object pubObject;  // type aligns with combinedInputs
		Method pubMethod;
		List<Object> priorInputs = combinedInputs.subList(combinedInputs.indexOf(subscriptionObject)+1, combinedInputs.size());
		for (Object pObject : priorInputs) {
			pubObject = pObject;
			if (pubObject == null)
				break;
			if (pubObject == subscriptionObject)
				break;  // only look at sims that come before
			Class<?> pubClass = pubObject.getClass();
			//					System.out.println("....Examining class <" + pubClass.getName() + ">");
			Method[] pubMethods = pubClass.getMethods();
			for (int pubMethI=0; pubMethI<pubMethods.length; pubMethI++) {
				pubMethod = pubMethods[pubMethI];
				String pubMethName = pubMethod.getName();
				if (!pubMethName.startsWith("get")) 
					continue;
				Class<?> returnType = pubMethod.getReturnType();
				if (returnType == null)
					continue;
				//						System.out.println("........offers type <" + returnType.getName() +">");
				if (returnType.equals(subClass)) {
					// found a matching publisher
					// Do something with it.....
					PubSubMatch match = new PubSubMatch();
                    match.setPubMethod(pubMethod);
                    match.setPubObject(pubObject);
                    match.setSubMethod(subMethod);
                    match.setSubObject(subscriptionObject);
					return match;
				}
			}
		}
		throw new SimEngineSubscriptionException(
				"Input of type <" + subClass.getName() + 
				" cannot be found to satisfy <" + subscriptionObject.getClass().getName()  + ">" + "\n" +
				documentSimsUpTo(subscriptionObject)
				);

	}

	StringBuffer documentSimsUpTo(SimComponent targetSim) {
		StringBuffer buf = new StringBuffer();
		if (simChain.getBase() != null) {
			buf.
			append(simChain.getBase().getClass().getName()).
			append(" offers types\n");
			getPublishedTypesDescription(buf, simChain.getBase());
		}

		for (Iterator<SimStep> it=simChain.iterator(); it.hasNext(); ) {
			SimStep simStep = it.next();
			SimComponent pubSim = simStep.getSimComponent();
			if (pubSim == targetSim)
				break;
			// Name, output types
			buf.
			append(pubSim.getName()).
			append(" offers types\n");
			getPublishedTypesDescription(buf, pubSim);
		}

		return buf;
	}

	void getPublishedTypesDescription(StringBuffer buf,
			Object pubSim) {
		Method[] pubMethods = pubSim.getClass().getMethods();
		for (int i=0; i<pubMethods.length; i++) {
			Method pubMethod = pubMethods[i];
			String methodName = pubMethod.getName();
			if (!methodName.startsWith("get"))
				continue;
			String typeName = pubMethod.getReturnType().getName();
			if (!typeName.startsWith("java."))
				buf.
				append("....").
				append(typeName).
				append("\n");
		}
	}

	List<PubSubMatch> getPubSubMatches() { return pubSubMatches; }

	/**
	 * Execute getter/setters to installRepositoryLinkage the current ValSim with information
	 * produced by an earlier step.
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

	public int getSimsRun() {
		return simsRun;
	}

}
