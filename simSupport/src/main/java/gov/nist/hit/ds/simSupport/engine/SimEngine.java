package gov.nist.hit.ds.simSupport.engine;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Base type for this simEngine is Foo.
 * TODO: Add ErrorRecorder management.
 * @author bmajur
 *
 */
public class SimEngine {
	List<Object> inputs = new ArrayList<Object>();
	List<ValSim> valChain;
	List<Object> combinedInputs = new ArrayList<Object>();
	List<PubSubMatch> pubSubMatches = new ArrayList<PubSubMatch>();
	private Object subObject;
	private Method subMethod;
	private Object pubObject;
	private Method pubMethod;
	/**
	 * 
	 * @param valChain - list of validator instances
	 * @param foo - base type of simulator engine
	 */
	public SimEngine(Object base, List<ValSim> valChain) {
		this.valChain = valChain;
		this.inputs.add(base);
	}

	public void run() {
//		System.out.println("Base is:");
//		for (Object o : inputs)
//			System.out.println("....<" + o.getClass().getName() + ">\n........implements " + getClassNames(o.getClass().getInterfaces()));
//		System.out.println("Val Chain is:");
//		for (Object o : valChain)
//			System.out.println("....<" + o.getClass().getName() + ">\n........implements " + getClassNames(o.getClass().getInterfaces()));

		buildCombinedInputs();
		for (ValSim val : valChain) {
			matchPubSub(val);
			val.run();
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
	 *   - valChain in reverse order
	 *   - inputs
	 * This establishes the search order for finding publishers
	 */
	void buildCombinedInputs() {
		for (int i=valChain.size()-1; i>=0; i--) 
			combinedInputs.add(valChain.get(i));
		for (Object o : inputs)
			combinedInputs.add(o);
	}

	/**
	 * Given validator to be run, identify its subscriptions and find
	 * publishers to provide them. Execute the getter/setter combination
	 * to inject the necessary parameter.
	 * @param subObject
	 * @return
	 */
	void matchPubSub(Object subscriptionObject) {
		subObject = subscriptionObject;
		Class<?> valClass = subObject.getClass();
		Method[] valMethods = valClass.getMethods();
		for (int subMethI=0; subMethI<valMethods.length; subMethI++) {
			subMethod = valMethods[subMethI];
			String subMethName = subMethod.getName();
			if (!subMethName.startsWith("set"))
				continue;
			if (subMethName.equals("setErrorRecorder"))
				continue;
			Class<?>[] subParamTypes = subMethod.getParameterTypes();
			if (subParamTypes == null || subParamTypes.length != 1)
				continue;  // must be single input param, input must be an object
			Class<?> subClass = subParamTypes[0];  // subscription class

			// find a publisher for class subClass
			// First look at sim engine inputs (base type)
			outerloop:
				for (Object pObject : combinedInputs) {
					pubObject = pObject;
					Class<?> pubClass = pubObject.getClass();
					Method[] pubMethods = pubClass.getMethods();
					for (int pubMethI=0; pubMethI<pubMethods.length; pubMethI++) {
						pubMethod = pubMethods[pubMethI];
						String pubMethName = pubMethod.getName();
						if (!pubMethName.startsWith("get")) 
							continue;
						Class<?> returnType = pubMethod.getReturnType();
						if (returnType == null)
							continue;
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
							executePubSub(match);
							break outerloop;   // done with current subscriber - stop searching publishers 
						}
					}
				}
		}
	}
	
	public List<PubSubMatch> getPubSubMatches() { return pubSubMatches; }

	void executePubSub(PubSubMatch match) {
		try {
			Object o = match.pubMethod.invoke(match.pubObject, (Object[]) null);
			System.out.println("........value is <" + o + ">");
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

}
