package gov.nist.hit.ds.simSupport.validationEngine;

import gov.nist.hit.ds.eventLog.assertion.annotations.Validation;
import gov.nist.hit.ds.soapSupport.core.ValidationFault;

import java.lang.reflect.Method;

public class Runable {
	Method method;
	ValidationFault validationFaultAnnotation;
	Validation validationAnnotation;
	RunType type;
	boolean ran;
	String[] dependsOnId;
	String id;

	Runable(Method method, RunType type, String[] dependsOnId, ValidationFault validationFaultAnnotation) {
		this.id = validationFaultAnnotation.id();
		this.method = method;
		this.type = type;
		this.dependsOnId = dependsOnId;
		this.validationFaultAnnotation = validationFaultAnnotation;
		this.validationAnnotation = null;
	}

	Runable(Method method, RunType type, String[] dependsOnId, Validation validationAnnotation) {
		this.id = validationAnnotation.id();
		this.method = method;
		this.type = type;
		this.dependsOnId = dependsOnId;
		this.validationFaultAnnotation = null;
		this.validationAnnotation = validationAnnotation;
	}

	boolean runable() {
		if (ran == true)
			return false;
		return true;
	}

	void ran() {
		this.ran = true;
	}
}