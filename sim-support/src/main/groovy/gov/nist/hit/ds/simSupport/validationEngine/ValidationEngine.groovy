package gov.nist.hit.ds.simSupport.validationEngine
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.eventLog.assertion.Assertion
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup
import gov.nist.hit.ds.eventLog.assertion.AssertionStatus
import gov.nist.hit.ds.eventLog.assertion.annotations.Validation
import gov.nist.hit.ds.eventLog.errorRecording.ErrorContext
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ValidationFault
import gov.nist.hit.ds.soapSupport.FaultCode
import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.xdsException.ExceptionUtil
import org.apache.commons.lang3.exception.ExceptionUtils
import org.apache.log4j.Logger

public class ValidationEngine {
    ValComponentBase validationObject
    List<ValidationMethod> validationMethods  // taken from validationObject
    // only one of these is non-null indicating what kind of assertion is being tested
    public ValidationFault validationFaultAnnotation
    public Validation validationAnnotation
    static Logger logger = Logger.getLogger(ValidationEngine)

    public ValidationEngine(ValComponentBase validationObject, Event event) {
        this.validationObject = validationObject
        validationObject.ag = event.assertionGroup
        validationObject.validationEngine = this
    }

    // called (indirectly through ValComponentBase) by the validator method run()
    public void run() throws SoapFaultException {
        try {
            scanForValidationMethods()
            runValidationMethods()
        } catch (SoapFaultException e) {
            throw e  // pass it up
        } catch (Throwable e) {
            logger.fatal(ExceptionUtil.exception_details(e))
            throw new SoapFaultException(
                    validationObject.ag,
                    FaultCode.Receiver,
                    new ErrorContext(ExceptionUtil.exception_details(e)))
        }
    }

    public void scanForValidationMethods() throws Exception {
        validationMethods = new ValidationClassScanner(validationObject.class).scan()
    }

    public void runValidationMethods() throws Exception {
        ValidationMethod validationMethod = getARunableValidationMethod();
        while(validationMethod) {
            logger.debug("Starting Validation ${validationMethod.method.name} on ${validationObject.class.name}");
            validationObject.defaultMsg()
            invoke(validationMethod)
            validationMethod = getARunableValidationMethod();
        }
        def moreToRun = validationMethods.find { it.runable() }
        if (moreToRun)
            throw new ValidationEngineException("Validator ${targetClass.name} has circular dependencies");
    }

    def invoke(validationMethod) throws Exception {
        try {
            validationMethod.method.invoke(validationObject);
        } catch (Exception e) {
            logException(e)
            Throwable t = e.getCause();
            throw t;
        } catch (Throwable t) {
            logThrowable(t)
            throw t;
        }
    }

//    def invoke(validationMethod) throws Exception {
//        try {
//            validationMethod.method.invoke(validationObject);
//        } catch (InvocationTargetException e) {
//            logException(e)
//            Throwable t = e.getCause();
//            throw t;
//        }
//    }

    private def logException(Exception e) {
        // Force a log entry under validators even if this validator would not normally generate one
        AssertionGroup ag = validationObject.ag
        Assertion a = new Assertion()
        a.setLocation(ExceptionUtils.getStackTrace(e.getCause()))
        a.setMsg(e.getMessage())
        a.setStatus(AssertionStatus.INTERNALERROR)
        ag.addAssertion(a)
    }

    private def logThrowable(Throwable t) {
        // Force a log entry under validators even if this validator would not normally generate one
        AssertionGroup ag = validationObject.ag
        Assertion a = new Assertion()
        a.setLocation(ExceptionUtils.getStackTrace(t.getCause()))
        a.setMsg(t.getMessage())
        a.setStatus(AssertionStatus.INTERNALERROR)
        ag.addAssertion(a)
    }

    private def getARunableValidationMethod() {
        def valMethod = validationMethods.find { it.runable() && dependenciesSatisfied(it.dependsOnId) }
        if (valMethod) {
            validationAnnotation = valMethod.validationAnnotation
            validationFaultAnnotation = valMethod.validationFaultAnnotation
            assert validationAnnotation || validationFaultAnnotation
            valMethod.markHasBeenRun()
            return valMethod
        }
        return null
    }

    private def dependenciesSatisfied(List<String> dependsOnIds) {
        for (String dependsOnId : dependsOnIds) {
            if ("none".equals(dependsOnId))
                continue
            ValidationMethod dependsOn = getValidationMethodById(dependsOnId)
            if (dependsOn && !dependsOn.hasRun)
                return false
        }
        return true
    }

    private def getValidationMethodById(String id) { validationMethods.find { it.id == id } }
}
