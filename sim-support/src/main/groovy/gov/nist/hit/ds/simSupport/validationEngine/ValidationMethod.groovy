package gov.nist.hit.ds.simSupport.validationEngine
import edu.emory.mathcs.backport.java.util.Arrays
import gov.nist.hit.ds.eventLog.assertion.annotations.Validation
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ValidationFault

import java.lang.reflect.Method

public class ValidationMethod {
	Method method;
	ValidationFault validationFaultAnnotation;
	Validation validationAnnotation;
	RunType type;
	boolean hasRun;
	List<String> dependsOnId;
	String id;
    String guardMethodName

	ValidationMethod(Method method, RunType type, String[] dependsOnId, ValidationFault validationFaultAnnotation) {
		this.id = validationFaultAnnotation.id();
		this.method = method;
		this.type = type;
		this.dependsOnId = Arrays.asList(dependsOnId);
		this.validationFaultAnnotation = validationFaultAnnotation;
		this.validationAnnotation = null;
	}

//	ValidationMethod(Method method, RunType type, String[] dependsOnId, Validation validationAnnotation) {
//		this.id = validationAnnotation.id();
//		this.method = method;
//		this.type = type;
//		this.dependsOnId = Arrays.asList(dependsOnId);
//		this.validationFaultAnnotation = null;
//		this.validationAnnotation = validationAnnotation;
//	}

    ValidationMethod(Method method, RunType type, String[] dependsOnId, Validation validationAnnotation, String guardMethodName) {
        this.id = validationAnnotation.id();
        this.method = method;
        this.type = type;
        this.dependsOnId = Arrays.asList(dependsOnId);
        this.validationFaultAnnotation = null;
        this.validationAnnotation = validationAnnotation;
        this.guardMethodName = guardMethodName
    }

    boolean hasRun() { return hasRun; }

	boolean runable() { return !hasRun; }

	void markHasBeenRun() {
		this.hasRun = true;
	}
}