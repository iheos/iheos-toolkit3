package gov.nist.hit.ds.simSupport.validationEngine
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.eventLog.assertion.Assertion
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup
import gov.nist.hit.ds.eventLog.assertion.AssertionStatus
import gov.nist.hit.ds.eventLog.errorRecording.ErrorContext
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.hit.ds.soapSupport.FaultCode
import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.xdsExceptions.ExceptionUtil
import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException
import groovy.util.logging.Log4j
import org.apache.log4j.Logger

import java.lang.reflect.InvocationTargetException

@Log4j
public class ValidationEngine {
    ValComponentBase validationObject
    List<ValidationMethod> validationMethods  // taken from validationObject
    ValidationMethod currentValidationMethod
        // only one of these is non-null indicating what kind of assertion is being tested
    public Validation validationAnnotation
    static Logger logger = Logger.getLogger(ValidationEngine)
    boolean quit = false

    public ValidationEngine(ValComponentBase validationObject, Event event) {
        if (!(validationObject instanceof ValComponentBase))
            throw new ToolkitRuntimeException("Class ${validationObject.name} is registered as a validator but is not instance of ValComponentBase")
        this.validationObject = validationObject
        validationObject.ag = event.assertionGroup
        validationObject.validationEngine = this
    }

    // called (indirectly through ValComponentBase) by the validator method testRun()
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
            validationObject.defaultMsg()
            invoke(currentValidationMethod)
            if (quit) return
            currentValidationMethod = getARunableValidationMethod();
        }
    }

    def invoke(validationMethod) throws Exception {
        logger.debug("Running Validation ${currentValidationMethod.id} on ${validationObject.class.name}");
        log.debug("...Validation Method is ${validationMethod}")
        try {
            validationMethod.method.invoke(validationObject);
        } catch (InvocationTargetException ite) {
//            logException(ite)
            try {
                throw ite.targetException
            } catch (SoapFaultException sfe) {
                // already logged
                throw sfe
            } catch (Exception e) {
                logException(e)
                Throwable t = e.getCause();
                if (t) throw t; else throw e
            } catch (Throwable t) {
                logThrowable(t)
                throw t;
            }
        }
    }

    private def logException(Exception e) {
        // Force a log entry under validators even if this validator would not normally generate one
        AssertionGroup ag = validationObject.ag
        Assertion a = new Assertion()
        if (e.cause)
            a.setLocation(ExceptionUtil.exception_details(e.getCause()))
        else
            a.setLocation(ExceptionUtil.exception_details(e))
        a.setMsg(e.getMessage())
        a.setStatus(AssertionStatus.INTERNALERROR)
        ag.addAssertion(a, true)
    }

    private def logThrowable(Throwable t) {
        // Force a log entry under validators even if this validator would not normally generate one
        AssertionGroup ag = validationObject.ag
        Assertion a = new Assertion()
        if (t.cause)
            a.setLocation(ExceptionUtil.exception_details(t.getCause()))
        else
            a.setLocation(ExceptionUtil.exception_details(t))
        a.setMsg(t.getMessage())
        a.setStatus(AssertionStatus.INTERNALERROR)
        ag.addAssertion(a, true)
    }

    private def getARunableValidationMethod() {
        def runFirst = validationMethods.find { it.setup && it.runable() }
        if (runFirst) { setupRunable(runFirst); return runFirst }
        def runableMethods = validationMethods.findAll() { validationMethod ->
            validationMethod.runable() && dependenciesSatisfied(validationMethod.dependsOnId)
        }
        if (!runableMethods) return null
        def valMethod = runableMethods.sort { it.id }.find { validationMethod ->
            evalGuard(validationMethod)
        }

//        def valMethod = validationMethods.find { validationMethod ->
//            if (!(validationMethod.runable() && dependenciesSatisfied(validationMethod.dependsOnId))) return false
//            log.debug("For ${validationMethod.id}...")
//            evalGuard(validationMethod)// determines FOUND status
//        }
        if (valMethod) {
            setupRunable(valMethod)
            valMethod.required = !isOptional(valMethod)
            return valMethod
        }
        return null
    }

    private setupRunable(def valMethod) {
        validationAnnotation = valMethod.validationAnnotation
        assert validationAnnotation
        valMethod.markHasBeenRun()
    }

    private boolean evalGuard(ValidationMethod validationMethod) {
        for (def guardMethodName in validationMethod.guardMethodNames) {
            def guardValue = validationObject."${guardMethodName}"()
            log.debug("Guard ${guardMethodName}? ${guardValue}")
            if (!guardValue) return false
        }
        return true
    }

    // If any @Optional guard are true then the valiation is optional
    // and we return true here
    private boolean isOptional(ValidationMethod validationMethod) {
        for (def guardMethodName in validationMethod.optionalGuardMethodNames) {
            def guardValue = validationObject."${guardMethodName}"()
            log.debug("Optional Guard ${guardMethodName}? ${guardValue}")
            if (guardValue) return true
        }
        return false
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
