package gov.nist.hit.ds.simSupport.transaction
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.TransactionType
import gov.nist.hit.ds.eventLog.Fault
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.endpoint.EndpointBuilder
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import gov.nist.hit.ds.soapSupport.FaultCode
import gov.nist.hit.ds.xdsException.ExceptionUtil
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
import groovy.util.logging.Log4j
/**
 * Created by bmajur on 7/5/14.
 */
@Log4j
class TransactionRunner {
//    String endpoint
    EndpointBuilder endpointBuilder
    SimId simId
    SimHandle simHandle
    def transactionType
    String implClassName
    def event
    def transCode
    def repoName

    ////////////////////////////////////////////////////////////////
    // Production support

    TransactionRunner(EndpointBuilder _endpointBuilder, String _repoName) {
        endpointBuilder = _endpointBuilder
//        this.endpoint = endpoint
        this.transCode = endpointBuilder.transCode
        repoName = _repoName
        init()
    }

    TransactionRunner() {}

    TransactionRunner(SimId _simId, String repositoryName, TransactionType _transactionType) {
        log.debug("TransactionRunner: transactionType is ${_transactionType}")
        transactionType = _transactionType
        simId = _simId
        repoName = repositoryName
        implClassName = transactionType.implementationClassName
        log.debug("implClassName is ${implClassName}")
        init2(simId, transactionType.code, repositoryName)
    }

    TransactionRunner(SimHandle _simHandle) {
        log.debug("TransactionRunner: transactionType is ${transactionType}")
        simHandle = _simHandle
        event = simHandle.event
        implClassName = simHandle.transactionType.implementationClassName
    }
    ////////////////////////////////////////////////////////////////

    def init() {
        transCode = endpointBuilder.transCode
        def simId = endpointBuilder.simId

        assert repoName
        assert simId
        assert transCode
        init2(simId, transCode, repoName)

        implClassName = transactionType.implementationClassName
        log.debug("transactionClassName is ${implClassName}")
    }


    private init2(simId, transactionCode, repositoryName) {
        log.debug("TransactionRunner using repo ${repositoryName}")
        simHandle = SimUtils.open(simId.id, repositoryName)
        event = simHandle.event

        // Lookup transaction implementation class
        transactionType = new ActorTransactionTypeFactory().getTransactionType(transactionCode)
        simHandle.transactionType = transactionType
    }

    def validateRequest() {runAMethod('validateRequest')}
    def validateResponse() {runAMethod('validateResponse')}
    SimHandle run() { runAMethod('run'); return simHandle }  // used for unit tests
    SimHandle prun() { runPMethod('run'); return simHandle }  // used for production (from servlet)

    // TODO - may be closing sim too early - good for unit tests, bad for servlet access
    def runAMethod(methodName) {
        // build implementation
        Class<?> clazz = new SimUtils().getClass().classLoader.loadClass(implClassName)
        if (!clazz) throw new ToolkitRuntimeException("Class ${implClassName} cannot be loaded.")
        Object[] params = new Object[1]
        params[0] = simHandle
        Object instance = clazz.newInstance(params)

        // call run() method
        try {
            instance.invokeMethod(methodName, null)
            SimUtils.close(simHandle)
        } catch (Throwable t) {
            String actorTrans = transCode
            event.fault = new Fault('Exception', FaultCode.Receiver.toString(), actorTrans, ExceptionUtil.exception_details(t))
            SimUtils.close(simHandle)
            throw t
        }
    }

    def runPMethod(String methodName) {
        // build implementation
        Class<?> clazz
        try {
            clazz = new SimUtils().getClass().classLoader.loadClass(implClassName)
        } catch (Throwable t) {
            simHandle.event.fault = new Fault("Configuration Error - cannot load transaction class ${implClassName}", FaultCode.Receiver.toString(), simHandle.transactionType?.code, "Transaction implementation class ${implClassName} does not exist.")
            return
        }
        if (!clazz) {
            simHandle.event.fault = new Fault('Configuration Error', FaultCode.Receiver.toString(), simHandle.transactionType.code, "Transaction implementation class ${implClassName} does not exist.")
            return
        }
        Object[] params = new Object[1]
        params[0] = simHandle
        Object instance = clazz.newInstance(params)

        // call run() method
        try {
            instance.invokeMethod(methodName, null)
        } catch (Throwable t) {
            String actorTrans = transCode
            event.fault = new Fault('Exception running transaction', FaultCode.Receiver.toString(), actorTrans, ExceptionUtil.exception_details(t))
        }
    }

    ///////////////////////////////////////////////////////////////////
    // Unit Test support - run individual validator/simulator component

    Closure runner
    TransactionRunner(String transactionCode, SimId simId, repositoryName, Closure runner)  {
        repoName = repositoryName
        transactionType = new ActorTransactionTypeFactory().getTransactionType(transactionCode)
        init2(simId, transactionCode, repositoryName)
        this.runner = runner
    }

    public void runTest() {
        try {
            runner(simHandle)
            event.flushAll()
        } catch (Throwable t) {
            String actorTrans = transCode
            event.fault = new Fault('Exception', FaultCode.Receiver.toString(), actorTrans, ExceptionUtil.exception_details(t))
            event.flushAll()
            throw t
        }
    }
    ////////////////////////////////////////////////////////////////
}
