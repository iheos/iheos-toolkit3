package gov.nist.hit.ds.simSupport.engine;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.factories.SystemErrorRecorderBuilder;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidator;

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
public class SimEngine implements MessageValidatorEngine {
	//	List<Object> inputs = new ArrayList<Object>();
	//	List<ValSim> valChain;
	SimChain simChain;
	// This is of Object instead of ValSim since it includes the base type
	// which may not be of type of ValSim.
	List<Object> combinedInputs;
	List<PubSubMatch> pubSubMatches = new ArrayList<PubSubMatch>();
	int simsRun = 0;
	/**
	 * 
	 * @param valChain - list of validator instances
	 */
	public SimEngine(SimChain simChain) {
		this.simChain = simChain;
	}

	public SimEngine() {
		this.simChain = new SimChain();
	}

	/**
	 * Run the current Simulator Chain.  This method is not re-entrant and
	 * should only be called once to execute a chain, even if the chain grows
	 * during its execution.
	 * If new SimSteps are added to the Chain after execution starts then the
	 * outer loop (while(!isComplete()) will catch the added SimSteps. 
	 * @throws SimEngineSubscriptionException
	 */
	public void run() throws SimEngineSubscriptionException {
		boolean errorsFound = false;
		SystemErrorRecorderBuilder erBuilder = new SystemErrorRecorderBuilder();
		while(!errorsFound && !isComplete()) {
			buildCombinedInputs();
			for (Iterator<SimStep> it=simChain.iterator(); it.hasNext(); ) {
				SimStep simStep = it.next();
				if (simStep.hasRan())
					continue;
				simStep.hasRan(true);
				if (simStep.getErrorRecorder() == null)
					simStep.setErrorRecorder(erBuilder.buildNewErrorRecorder());
				ValSim valSim = simStep.getValSim();
				if (valSim.getName() != null)
					simStep.getErrorRecorder().sectionHeading(valSim.getName());
				matchPubSub(valSim);
				simsRun++;
				System.out.println("Engine Running: " + valSim.getName());
				valSim.run((MessageValidatorEngine) this);
				if (simChain.hasErrors()) {
					System.out.println("Engine Error: " + simChain.getError());
					errorsFound = true;
				}
				break;  // this gets around concurrent modification errors to SimChain
				// in the case where dynamic sims are running (one sim
				// schedules the next.
			}
		}
		System.out.println("Engine: " + simsRun + " sims run");
	}

	/**
	 * Has the Simulator Chain run to completion?  If not, maybe a SimStep added 
	 * a new SimStep to the chain.  Either way, run() should be called in a loop
	 * until isComplete() returns true;
	 * @return
	 */
	boolean isComplete() {
		Iterator<SimStep> f = simChain.iterator();
		for (Iterator<SimStep> it=simChain.iterator(); it.hasNext(); ) {
			SimStep simStep = it.next();
			if (!simStep.hasRan())
				return false;
		}
		return true;
	}

	public StringBuffer getDescription(SimChain simChain) {
		StringBuffer buf = new StringBuffer();

		describe(simChain.getBase(), buf);
		for(Iterator<SimStep> it=simChain.iterator(); it.hasNext(); ) {
			SimStep step = it.next();
			describe(step.getValSim(), buf);
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
			String name = method.getName();
			if (name.startsWith("get") && !name.equals("getClass"))
				buf.append(".." + name).append("\n");
		}
		for (int i=0; i<methods.length; i++) {
			Method method = methods[i];
			String name = method.getName();
			if (name.startsWith("set") && !name.equals("setErrorRecorder"))
				buf.append(".." + name).append("\n");
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
			combinedInputs.add(simStep.getValSim());
		}
		Collections.reverse(combinedInputs);
		combinedInputs.add(simChain.getBase());
	}

	/**
	 * Given validator to be run, identify its subscriptions and find
	 * publishers to provide them. Only consider pubilshers that come
	 * earlier in the valchain. Execute the getter/setter combination
	 * to inject the necessary parameter.
	 * TODO: Why is subscriptionObject of type Object instead of ValSim????
	 * @param subObject
	 * @return
	 * @throws SimEngineSubscriptionException 
	 */
	void matchPubSub(ValSim subscriptionObject) throws SimEngineSubscriptionException {
		ValSim subObject;
		Method subMethod;
		Object pubObject;  // type aligns with combinedInputs
		Method pubMethod;
		subObject = subscriptionObject;
		Class<?> valClass = subObject.getClass();
		System.out.println("Subscriber <" + valClass.getName() + ">");
		Method[] valMethods = valClass.getMethods();
		// For all setters in this subscriptionObject
		for (int subMethI=0; subMethI<valMethods.length; subMethI++) {
			subMethod = valMethods[subMethI];
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
			//			System.out.println("....Needs input class <" + subClass.getName() + ">");

			// find a publisher for class subClass
			// First look at sim engine inputs (base type)
			boolean matchFound = false;
			List<Object> priorInputs = combinedInputs.subList(combinedInputs.indexOf(subObject)+1, combinedInputs.size());
			outerloop:
				for (Object pObject : priorInputs) {
					pubObject = pObject;
					if (pubObject == subObject)
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
							PubSubMatch match = new PubSubMatch()
							.setPubMethod(pubMethod)
							.setPubObject(pubObject)
							.setSubMethod(subMethod)
							.setSubObject(subObject);
							pubSubMatches.add(match);
							System.out.println(match);
							System.out.println(".Executing match");
							executePubSub(match);
							matchFound = true;
							break outerloop;   // done with current subscriber - stop searching publishers 
						}
					}
				}
			if (!matchFound)
				throw new SimEngineSubscriptionException(
						"Input of type <" + subClass.getName() + 
						" cannot be found to satisfy <" + valClass.getName()  + ">" + "\n" +
						documentSimsUpTo(subscriptionObject)
						);
		}
	}

	StringBuffer documentSimsUpTo(ValSim targetSim) {
		StringBuffer buf = new StringBuffer();
		buf.
		append(simChain.getBase().getClass().getName()).
		append(" offers types\n");
		getPublishedTypesDescription(buf, simChain.getBase());

		for (Iterator<SimStep> it=simChain.iterator(); it.hasNext(); ) {
			SimStep simStep = it.next();
			ValSim pubSim = simStep.getValSim();
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
			buf.
			append("....").
			append(pubMethod.getReturnType().getName()).
			append("\n");
		}
	}

	List<PubSubMatch> getPubSubMatches() { return pubSubMatches; }

	/**
	 * Execute getter/setters to initialize the current ValSim with information
	 * produced by an earlier step.
	 * @param match
	 */
	void executePubSub(PubSubMatch match) {
		try {
			Object o = match.pubMethod.invoke(match.pubObject, (Object[]) null);
			if (o == null)
				System.out.println(".Value is null");
			Object[] args = new Object[1];
			args[0] = o;
			match.subMethod.invoke(match.subObject, args);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * MessageValidator implements the ValSim interface. This method exists
	 * to accommodate the v2 approach of scheduling validators as the message
	 * is parsed - a dynamic model.
	 */
	@Override
	public void addMessageValidator(String stepName, MessageValidator v,
			ErrorRecorder er) {
		v.setName(stepName);
		ValSim vs = v;
		vs.setErrorRecorder(er);
		SimStep ss = new SimStep();
		ss.setErrorRecorder(er);
		ss.setName(stepName);
		ss.setValSim(vs);
		simChain.add(ss);
	}

	public int getSimsRun() {
		return simsRun;
	}

}
