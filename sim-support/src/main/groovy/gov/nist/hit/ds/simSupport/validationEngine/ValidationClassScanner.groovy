package gov.nist.hit.ds.simSupport.validationEngine
import gov.nist.hit.ds.simSupport.validationEngine.annotation.*
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
import org.apache.log4j.Logger

import java.lang.reflect.Method
/**
 * Analyze target class and the annotations on its methods and construct
 * and execution order for the methods based on the annotations
 * and the explicit dependencies coded in the annotations.
 * @author bmajur
 *
 */
public class ValidationClassScanner {
    List<ValidationMethod> validationMethods = new ArrayList<ValidationMethod>();
    static Logger logger = Logger.getLogger(ValidationClassScanner)
    Class<?> targetClass

    public ValidationClassScanner(Class<?> targetClass) {
        this.targetClass = targetClass
    }

    public List<ValidationMethod> scan() throws Exception {
        def validationIds = getAllValidationIds()
        targetClass.methods.each { addValidationMethod(it, validationIds) }
        return validationMethods
    }

    // Any method can be passed in.  If no appropriate annotation then nothing will be scheduled.
    def addValidationMethod(Method method, validationIds) {
        Validation validationAnnotation = method.getAnnotation(Validation)
        if (validationAnnotation) {
            ValidationMethod vMethod = new ValidationMethod(method, validationAnnotation)
            validationMethods.add(vMethod)

            Fault fault = method.getAnnotation(Fault)
            if (fault) {
                vMethod.type = RunType.FAULT
                vMethod.faultCode = fault.code()
            }

            Optional optional = method.getAnnotation(Optional)
            if (optional) {
                def guardNames = guard.methodNames() as List
                guardNames.each {
                    if (!getMethodByName(it))
                        throw new ToolkitRuntimeException("Validation method: ${method.name} depends on guard method ${it} which is not defined.")
                }
                vMethod.addOptionalGuardMethod(guard.methodNames())
            }

            def errorCode = method.getAnnotation(ErrorCode)
            if (errorCode) vMethod.errorCode = errorCode.code()

            if (method.getAnnotation(Setup)) vMethod.setup = true

            DependsOn dependsOn = method.getAnnotation(DependsOn)
            if (dependsOn) {
                def dependsOnIds = dependsOn.ids() as List
                dependsOnIds.each {
                    if (!validationIds.contains(it))
                        throw new ToolkitRuntimeException("Validation method: ${method.name} depends on id ${it} which is not defined.")
                }
                vMethod.addDependsOn(dependsOn.ids())
            }

            Guard guard = method.getAnnotation(Guard)
            if (guard) {
                def guardNames = guard.methodNames() as List
                guardNames.each {
                    if (!getMethodByName(it))
                        throw new ToolkitRuntimeException("Validation method: ${method.name} depends on guard method ${it} which is not defined.")
                }
                vMethod.addGuardMethod(guard.methodNames())
            }
        }
    }

    Method getMethodByName(String methodName) {
        return targetClass.getMethod(methodName)
    }

    def getAllValidationIds() {
        def ids = []
        targetClass.methods.each {
            Validation val = it.getAnnotation(Validation)
            if (val) ids << val.id()
        }
        return ids
    }
}
