package gov.nist.hit.ds.simSupport.validationEngine;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.eventLog.assertion.annotations.Validation;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.soapSupport.core.FaultCode;
import gov.nist.hit.ds.soapSupport.core.ValidationFault;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.xdsException.ExceptionUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ValidationEngine {
	SimComponentBase validationObject;
	public ValidationFault validationFaultAnnotation;
	public Validation validationAnnotation;
	static Logger logger = Logger.getLogger(ValidationEngine.class);

	public ValidationEngine(SimComponentBase validationObject) {
		this.validationObject = validationObject;
	}

	public void run() throws SoapFaultException {
		try {
			innerRun();
		} catch (SoapFaultException e) {
			throw e;  // pass it up
		} catch (Throwable e) {
			logger.fatal(ExceptionUtil.exception_details(e));
			throw new SoapFaultException(
					validationObject.ag,
					FaultCode.Receiver,
					new ErrorContext(ExceptionUtil.exception_details(e))
					);
		}
	}

	Class<?> targetClass;
	List<Runable> runables = new ArrayList<Runable>();
	int validationsRan = 0;
	
	public void reset() {
		runables = new ArrayList<Runable>();
		validationsRan = 0;
	}
	
	public int getValidationsRan() { return validationsRan; }
	public int getRunableCount() { return runables.size(); }
	public Runable getRunableById(String id) {
		for (Runable r : runables) {
			if (id.equals(r.id))
				return r;
		}
		return null;
	}
	
	// Any method can be passed in.  If no appropriate annotation then nothing will be scheduled.
	void addRunable(Method method) throws Exception {
		logger.debug("Evaluating method " + method.getName());
		ValidationFault validationFaultAnnotation = method.getAnnotation(ValidationFault.class);
		Validation validationAnnotation = method.getAnnotation(Validation.class);
		if (validationFaultAnnotation != null) {
			logger.debug("    Has fault validation annotation ");
			Class<?>[] subParamTypes = method.getParameterTypes();
			if (subParamTypes != null && subParamTypes.length > 0) 
				throw new Exception("Validation <" + targetClass.getName() + "#" + method.getName() + "> : a validation method accepts no parameters");
			RunType type = RunType.FAULT;
			String dependsOnId = validationFaultAnnotation.dependsOn();
			logger.debug("    depends on " + dependsOnId);
				runables.add(new Runable(method, type, dependsOnId, validationFaultAnnotation));
		} else if (validationAnnotation != null) {
			logger.debug("    Has validation annotation ");
			Class<?>[] subParamTypes = method.getParameterTypes();
			if (subParamTypes != null && subParamTypes.length > 0) 
				throw new Exception("Validation <" + targetClass.getName() + "#" + method.getName() + "> : a validation method accepts no parameters");
			RunType type = RunType.ERROR;
			String dependsOnId = validationAnnotation.dependsOn();
			logger.debug("    depends on " + dependsOnId);
			runables.add(new Runable(method, type, dependsOnId, validationAnnotation));
		} else {
			// oops - not one we are interested in.
		}
		logger.debug("    have " + runables.size() + " runables");
	}
	public Runable getNextRunable() {
		for (Runable r : runables) {
			if (r.runable()) {
				String dependsOnId = r.dependsOnId;
				Runable dependsOn = null;
				if (dependsOnId != null && !dependsOnId.equals("none"))
					dependsOn = getRunableById(dependsOnId);
				if (dependsOn != null) 
					if (!dependsOn.ran)
						continue;
				this.validationAnnotation = r.validationAnnotation;
				this.validationFaultAnnotation = r.validationFaultAnnotation;
				validationsRan++;
				r.ran();
				return r;
			}
		}
		return null;
	}

	public void innerRun() throws Exception {
		scheduler();
		runner();
	}

	public void runner() throws IllegalAccessException,
			InvocationTargetException, SoapFaultException {
		Runable runable = getNextRunable();
		while(runable != null) {
			Method meth = runable.method;
			logger.debug("Calling " + meth.getName());

			try {
				meth.invoke(validationObject);
			} catch (InvocationTargetException e) {
				Throwable t = e.getCause();
				if (t == null)
					throw e;
				if (t.getClass().equals(NullPointerException.class))
					throw (NullPointerException) t;
				if (t.getClass().equals(SoapFaultException.class)) 
					throw (SoapFaultException) t;
				throw e;
			}

			runable = getNextRunable();
		}
	}

	public void scheduler() throws Exception {
		targetClass = validationObject.getClass();
		logger.debug("Running ValidationEngine on " + targetClass.getName());
		Method[] valMethods = targetClass.getMethods();
		// Organize all validations
		for (int methI=0; methI<valMethods.length; methI++) {
			Method meth = valMethods[methI];
			addRunable(meth);
		}
	}

}
