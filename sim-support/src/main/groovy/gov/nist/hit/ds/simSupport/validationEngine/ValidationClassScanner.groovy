package gov.nist.hit.ds.simSupport.validationEngine
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ValidationFault
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
        targetClass.methods.each { addValidationMethod(it) }
        return validationMethods
    }

    // Any method can be passed in.  If no appropriate annotation then nothing will be scheduled.
    void addValidationMethod(Method method) throws Exception {
        ValidationFault validationFaultAnnotation = method.getAnnotation(ValidationFault)
        if (validationFaultAnnotation) {
            Class<?>[] subParamTypes = method.parameterTypes
            if (subParamTypes?.length > 0)
                throw new Exception("Validation <" + targetClass.getName() + "#" + method.getName() + "> : a validation method accepts no parameters")
            RunType type = RunType.FAULT
            String[] dependsOnId = validationFaultAnnotation.dependsOn()
            validationMethods.add(new ValidationMethod(method, type, dependsOnId, validationFaultAnnotation))
        }

        Validation validationAnnotation = method.getAnnotation(Validation)
        if (validationAnnotation) {
            Class<?>[] subParamTypes = method.getParameterTypes()
            if (subParamTypes != null && subParamTypes.length > 0)
                throw new Exception("Validation <" + targetClass.getName() + "#" + method.getName() + "> : a validation method accepts no parameters")
            RunType type = RunType.ERROR
            String[] dependsOnId = validationAnnotation.dependsOn()
            String guardMethod = validationAnnotation.guard()
            if (guardMethod == 'null') guardMethod = null
            validationMethods.add(new ValidationMethod(method, type, dependsOnId, validationAnnotation, guardMethod))
        }
    }
}
