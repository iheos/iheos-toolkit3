package gov.nist.hit.ds.simSupport.validationEngine;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.eventLog.assertion.Assertion;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.eventLog.assertion.AssertionStatus;
import gov.nist.hit.ds.eventLog.assertion.annotations.Validation;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ValidationFault;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.soapSupport.soapFault.FaultCode;
import gov.nist.hit.ds.xdsException.ExceptionUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ValidationEngine {
	SimComponentBase validationObject;
	// only one of these is non-null indicating what kind of assertation is being tested
	public ValidationFault validationFaultAnnotation;
	public Validation validationAnnotation;
	
	Class<?> targetClass;
	static Logger logger = Logger.getLogger(ValidationEngine.class);

	public ValidationEngine(SimComponentBase validationObject) {
		this.validationObject = validationObject;
		this.targetClass = validationObject.getClass();
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

	List<Runable> runables = new ArrayList<Runable>();
	int validationsRan = 0;
	
	boolean allRun() {
		for (Runable r : runables) {
			if (!r.ran)
				return false;
		}
		return true;
	}
	
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
	
	
	boolean dependenciesSatisfied(String[] dependsOnIds) {
		for (int i=0; dependsOnIds != null && i<dependsOnIds.length; i++) {
			String dependsOnId = dependsOnIds[i];
			if ("none".equals(dependsOnId))
				continue;
			Runable dependsOn = getRunableById(dependsOnId);
			if (!dependsOn.ran)
				return false;
		}
		return true;
	}
	
	public Runable getNextRunable() throws ValidationEngineException {
		for (Runable r : runables) {
			if (r.runable() && dependenciesSatisfied(r.dependsOnId)) {
				this.validationAnnotation = r.validationAnnotation;
				this.validationFaultAnnotation = r.validationFaultAnnotation;
				validationsRan++;
				r.ran();
				return r;
			}
		}
		if (!allRun()) 
			throw new ValidationEngineException("Validator " + targetClass.getName() + " has circular dependencies");
		return null;
	}

	public void innerRun() throws Exception {
		scheduler();
		runner();
	}
	
	public void scheduler() throws Exception {
		new Scheduler(targetClass, runables).run();
	}

	public void runner() throws IllegalAccessException,
			InvocationTargetException, SoapFaultException, ValidationEngineException {
		Runable runable = getNextRunable();
		while(runable != null) {
			Method meth = runable.method;
			logger.debug("Running Validation " + meth.getName() + " on " + validationObject.getClass().getName());

			try {
				meth.invoke(validationObject);
			} catch (InvocationTargetException e) {
				Throwable t = e.getCause();
				
				// Force a log entry under validators even if this validator would not normally generate one
				AssertionGroup ag = validationObject.ag;
				Assertion a = new Assertion();
				a.setLocation(ExceptionUtils.getStackTrace(t)).setMsg(t.getMessage()).setStatus(AssertionStatus.INTERNALERROR);
				ag.addAssertion(a);
				ag.setSaveInLog(true);
				
				if (t.getClass().equals(NullPointerException.class)) {
					throw (NullPointerException) t;
				}
				if (t.getClass().equals(SoapFaultException.class)) {
					throw (SoapFaultException) t;
				}
				throw e;
			}

			runable = getNextRunable();
		}
	}
}
