package gov.nist.hit.ds.simSupport.validationEngine
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.eventLog.assertion.Assertion
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup
import gov.nist.hit.ds.eventLog.assertion.AssertionStatus
import gov.nist.hit.ds.eventLog.errorRecording.ErrorContext
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.hit.ds.soapSupport.FaultCode
import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.xdsException.ExceptionUtil
import org.apache.commons.lang3.exception.ExceptionUtils
import org.apache.log4j.Logger

public class ValidationEngine {
    ValComponentBase validationObject
    List<ValidationMethod> validationMethods  // taken from validationObject
    ValidationMethod currentValidationMethod
        // only one of these is non-null indicating what kind of assertion is being tested
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
        currentValidationMethod = getARunableValidationMethod();
        while(currentValidationMethod) {
            logger.debug("Starting Validation ${currentValidationMethod.method.name} on ${validationObject.class.name}");
            validationObject.defaultMsg()
            invoke(currentValidationMethod)
            currentValidationMethod = getARunableValidationMethod();
        }
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

    private def logException(Exception e) {
        // Force a log entry under validators even if this validator would not normally generate one
        AssertionGroup ag = validationObject.ag
        Assertion a = new Assertion()
        a.setLocation(ExceptionUtils.getStackTrace(e.getCause()))
        a.setMsg(e.getMessage())
        a.setStatus(AssertionStatus.INTERNALERROR)
        ag.addAssertion(a, true)
    }

    private def logThrowable(Throwable t) {
        // Force a log entry under validators even if this validator would not normally generate one
        AssertionGroup ag = validationObject.ag
        Assertion a = new Assertion()
        a.setLocation(ExceptionUtils.getStackTrace(t.getCause()))
        a.setMsg(t.getMessage())
        a.setStatus(AssertionStatus.INTERNALERROR)
        ag.addAssertion(a, true)
    }

    private def getARunableValidationMethod() {
        def valMethod = validationMethods.find {
            it.runable() && dependenciesSatisfied(it.dependsOnId) && evalGuard(it)
        }
        if (valMethod) {
            validationAnnotation = valMethod.validationAnnotation
            assert validationAnnotation
            valMethod.markHasBeenRun()
            return valMethod
        }
        return null
    }

    private boolean evalGuard(ValidationMethod validationMethod) {
        validationMethod.guardMethodNames.findResult(true) { guardMethodName ->
            validationObject."${guardMethodName}"()
        }
//        validationMethod.guardMethodNames.each { guardMethodName ->
//            def ok = true
//            try {
//                ok = validationObject."${guardMethodName}"()
//            } catch (Exception e) {
//                println "Guard: ${e.class.name}: ${e.message}"
//            }
//        }
//        if (validationMethod.guardMethodName == null) return true
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
