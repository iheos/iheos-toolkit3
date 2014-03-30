package gov.nist.hit.ds.simSupport.validationEngine;

import gov.nist.hit.ds.eventLog.assertion.annotations.Validation;
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ValidationFault;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Analyze target class and the annotations on its methods and construct
 * and execution order for the methods based on the annotations
 * and the explicit dependencies coded in the annotations.
 * @author bmajur
 *
 */
public class Scheduler {
	List<Runable> runables;
	static Logger logger = Logger.getLogger(Scheduler.class);
	Class<?> targetClass;

	public Scheduler(Class<?> targetClass, List<Runable> runables) {
		this.targetClass = targetClass;
		this.runables = runables;
	}

	public void run() throws Exception {
		logger.debug("Building run queue for ValidationEngine on " + targetClass.getName());
		Method[] valMethods = targetClass.getMethods();
		// Organize all validations
		for (int methI=0; methI<valMethods.length; methI++) {
			Method meth = valMethods[methI];
			addRunable(meth, runables);
		}
		logger.debug("    have " + runables.size() + " runables");
	}

	// Any method can be passed in.  If no appropriate annotation then nothing will be scheduled.
	void addRunable(Method method, List<Runable> runables) throws Exception {
//		logger.debug("Scheduler: Evaluating method " + method.getName());
		ValidationFault validationFaultAnnotation = method.getAnnotation(ValidationFault.class);
		Validation validationAnnotation = method.getAnnotation(Validation.class);
		if (validationFaultAnnotation != null) {
			logger.debug("    Has fault validation annotation ");
			Class<?>[] subParamTypes = method.getParameterTypes();
			if (subParamTypes != null && subParamTypes.length > 0) 
				throw new Exception("Validation <" + targetClass.getName() + "#" + method.getName() + "> : a validation method accepts no parameters");
			RunType type = RunType.FAULT;
			String[] dependsOnId = validationFaultAnnotation.dependsOn();
			logger.debug("    depends on " + dependsOnId);
				runables.add(new Runable(method, type, dependsOnId, validationFaultAnnotation));
		} else if (validationAnnotation != null) {
			logger.debug("    Has validation annotation ");
			Class<?>[] subParamTypes = method.getParameterTypes();
			if (subParamTypes != null && subParamTypes.length > 0) 
				throw new Exception("Validation <" + targetClass.getName() + "#" + method.getName() + "> : a validation method accepts no parameters");
			RunType type = RunType.ERROR;
			String dependsOnId[] = validationAnnotation.dependsOn();
			logger.debug("    depends on " + dependsOnId);
			runables.add(new Runable(method, type, dependsOnId, validationAnnotation));
		} else {
//			logger.debug("    Not of interest ");
		}
	}
}
