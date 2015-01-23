package gov.nist.hit.ds.simSupport.validationEngine
import edu.emory.mathcs.backport.java.util.Arrays
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.hit.ds.soapSupport.FaultCode

import java.lang.reflect.Method

public class ValidationMethod {
	Method method;
	Validation validationAnnotation;
	RunType type = RunType.ERROR
    FaultCode faultCode = FaultCode.DataEncodingUnknown // irrelevant until type == Fault
	boolean hasRun;
	List<String> dependsOnId = []
	String id;
    List<String> guardMethodNames = []
    List<String> optionalGuardMethodNames = [] // optional determined by a set of guards
    boolean setup = false
    boolean required = true
    String errorCode

    public String toString() {
        return "${method} required ${required} depends on ${dependsOnId} guards ${guardMethodNames} optionals ${optionalGuardMethodNames}"
    }

    ValidationMethod(Method method, Validation validationAnnotation) {
        this.id = validationAnnotation.id()
        this.method = method
        this.validationAnnotation = validationAnnotation
    }

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

    def addDependsOn(String[] ids) {
        for (int i=0; i<ids.length; i++) { if (ids[i] != 'none') dependsOnId.add(ids[i]) }
    }

    def addGuardMethod(String[] methodNames) { for (int i=0; i<methodNames.length; i++) if (methodNames[i] != 'null') guardMethodNames.add(methodNames[i]) }
    def addOptionalGuardMethod(String[] methodNames) { for (int i=0; i<methodNames.length; i++) if (methodNames[i] != 'null') optionalGuardMethodNames.add(methodNames[i]) }

}
