package gov.nist.hit.ds.simSupport.transaction
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.TransactionType
import gov.nist.hit.ds.eventLog.EventFactory
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
    String header = ''
    byte[] body = ''
    SimId simId
    SimHandle simHandle
    def transactionType
    String implClassName
    def event
    def transCode
//    def actorCode
    def repoName

    ////////////////////////////////////////////////////////////////
    // Production support

    TransactionRunner(EndpointBuilder _endpointBuilder, String _repoName, String header, byte[] body) {
        endpointBuilder = _endpointBuilder
//        this.endpoint = endpoint
        this.transCode = endpointBuilder.transCode
        this.header = header
        this.body = body
        repoName = _repoName
        init()
    }

    TransactionRunner(SimId _simId, String repositoryName, TransactionType _transactionType, String _header, byte[] _body) {
        header = _header
        body = _body
        transactionType = _transactionType
        simId = _simId
        repoName = repositoryName
        implClassName = transactionType.implementationClassName
        log.debug("implClassName is ${implClassName}")
        init2(simId, repositoryName, transactionType.code)
    }
    ////////////////////////////////////////////////////////////////

    def init() {
//        def builder = new EndpointBuilder()
//        builder.parse(endpoint)
        transCode = endpointBuilder.transCode
//        actorCode = endpointBuilder.actorCode
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
        simHandle = SimUtils.open(simId, repositoryName)
        event = new EventFactory().buildEvent(simHandle.repository, simHandle.eventLogAsset)
        simHandle.event = event

        // Register inputs
        event.inOut.reqHdr = header
        event.inOut.reqBody = body

        event.init()

        // Lookup transaction implementation class
        transactionType = new ActorTransactionTypeFactory().getTransactionType(transactionCode)
        simHandle.transactionType = transactionType
    }

    def validateRequest() {runAMethod('validateRequest')}
    def validateResponse() {runAMethod('validateResponse')}
    def run() { runAMethod('run')}

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
            event.flushAll()
        } catch (Throwable t) {
            String actorTrans = transCode
            event.fault = new Fault('Exception', FaultCode.Receiver.toString(), actorTrans, ExceptionUtil.exception_details(t))
            event.flushAll()
            throw t
        }
    }

    ///////////////////////////////////////////////////////////////////
    // Unit Test support - run individual validator/simulator component

    Closure runner
    TransactionRunner(String transactionCode, SimId simId, repositoryName, Closure runner)  {
        header = 'x'
        body = 'x'.getBytes()
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
