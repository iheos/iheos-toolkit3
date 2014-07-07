package gov.nist.hit.ds.simSupport.transaction
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.simSupport.endpoint.EndpointBuilder
import gov.nist.hit.ds.simSupport.utilities.SimUtils
/**
 * Created by bmajur on 7/5/14.
 */
class TransactionRunner {

    def run(String endpoint, String header, byte[] body) {
        def builder = new EndpointBuilder()
        builder.parse(endpoint)
        def actorCode = builder.actorCode
        def transCode = builder.transCode
        def simId = builder.simId

        def simHandle = SimUtils.handle(simId)
        def event = new Event(simHandle.eventLogAsset)

        // Registry inputs
        event.inOut.reqHdr = header
        event.inOut.reqBody = body

        // Lookup transaction implementation class
        def transactionType = new ActorTransactionTypeFactory().getTransactionType(transCode)
        def implClassName = transactionType.implementationClassName

        // build implementation
        Class<?> clazz = this.class.classLoader.loadClass(implClassName)

//        // Verify impl class implements ValComponent interface
//        Method[] methods = clazz.methods
//        Method runMethod = null
//        for (int i=0; i<methods.length; i++) {
//            Method method = methods[i]
//            if (method.name == 'run') {
//                runMethod = method
//                break
//             }
//        }
//        if (runMethod == null) throw new ToolkitRuntimeException("class ${implClassName} does implement a run() method.")

        // call run() method
        clazz.invokeMethod('run', null)
    }
}
