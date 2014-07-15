package gov.nist.hit.ds.simSupport.transaction

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.eventLog.EventFactory
import gov.nist.hit.ds.eventLog.Fault
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
    String header
    byte[] body
    SimHandle simHandle
    def transactionType
    def implClassName
    def event
    def transCode
    def actorCode

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

        simHandle = SimUtils.handle(simId)
        event = new EventFactory().buildEvent(SimSupport.simRepo, simHandle.eventLogAsset)
        simHandle.event = event

        // Registry inputs
        event.inOut.reqHdr = header
        event.inOut.reqBody = body
        event.init()
        // Lookup transaction implementation class
        transactionType = new ActorTransactionTypeFactory().getTransactionType(transCode)
        simHandle.transactionType = transactionType
        implClassName = transactionType.implementationClassName
        log.debug("implClassName is ${implClassName}")
    }

    def run() {
        // build implementation
        Class<?> clazz = new SimUtils().class.classLoader.loadClass(implClassName)
        if (!clazz) throw new ToolkitRuntimeException("Class ${implClassName} cannot be loaded.")
        Object[] params = new Object[1]
        params[0] = simHandle
        Object<?> instance = clazz.newInstance(params)

        // call run() method
        try {
            instance.invokeMethod('run', null)
            event.flush()
        } catch (Throwable t) {
            def actorTrans = actorCode + '/' + transCode
            event.fault = new Fault('Exception', FaultCode.Receiver.toString(), actorTrans, ExceptionUtil.exception_details(t))
            event.flush()
            throw t
        }
    }
}
