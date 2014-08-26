package gov.nist.hit.ds.simSupport.transaction

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.eventLog.EventFactory
import gov.nist.hit.ds.eventLog.Fault
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.endpoint.EndpointBuilder
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.utilities.SimSupport
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
    String endpoint
    String header = ''
    byte[] body = ''
    SimHandle simHandle
    def transactionType
    String implClassName
    def event
    def transCode
    def actorCode

    ////////////////////////////////////////////////////////////////
    // Production support

    TransactionRunner(String endpoint, String header, byte[] body) {
        this.endpoint = endpoint
        this.header = header
        this.body = body
        init()
    }

    def init() {
        def builder = new EndpointBuilder()
        builder.parse(endpoint)
        transCode = builder.transCode
        actorCode = builder.actorCode
        def simId = builder.simId

        init(simId, transCode)

        implClassName = transactionType.implementationClassName
        log.debug("implClassName is ${implClassName}")
    }

    def init(simId, transCode) {
        log.debug 'testinit'

        simHandle = SimUtils.open(simId)
        event = new EventFactory().buildEvent(SimSupport.simRepo, simHandle.eventLogAsset)
        simHandle.event = event

        // Register inputs
        event.inOut.reqHdr = header
        event.inOut.reqBody = body

        event.init()

        // Lookup transaction implementation class
        transactionType = new ActorTransactionTypeFactory().getTransactionType(transCode)
        simHandle.transactionType = transactionType
    }


    def run() {
        // build implementation
        Class<?> clazz = new SimUtils().class.classLoader.loadClass(implClassName)
        if (!clazz) throw new ToolkitRuntimeException("Class ${implClassName} cannot be loaded.")
        Object[] params = new Object[1]
        params[0] = simHandle
        Object instance = clazz.newInstance(params)

        // call run() method
        try {
            instance.invokeMethod('run', null)
            event.flushAll()
        } catch (Throwable t) {
            String actorTrans = actorCode + '/' + transCode
            event.fault = new Fault('Exception', FaultCode.Receiver.toString(), actorTrans, ExceptionUtil.exception_details(t))
            event.flushAll()
            throw t
        }
    }

    ///////////////////////////////////////////////////////////////////
    // Unit Test support - run individual validator/simulator component

    Closure runner
    TransactionRunner(String transactionCode, SimId simId, Closure runner)  {
        header = 'x'
        body = 'x'
        init(simId, transactionCode)
        this.runner = runner
    }

    public void runTest() {
        try {
            runner(simHandle)
            event.flushAll()
        } catch (Throwable t) {
            String actorTrans = actorCode + '/' + transCode
            event.fault = new Fault('Exception', FaultCode.Receiver.toString(), actorTrans, ExceptionUtil.exception_details(t))
            event.flushAll()
            throw t
        }
    }
}
