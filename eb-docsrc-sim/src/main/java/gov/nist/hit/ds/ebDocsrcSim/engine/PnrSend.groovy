package gov.nist.hit.ds.ebDocsrcSim.engine
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.AsyncType
import gov.nist.hit.ds.actorTransaction.TlsType
import gov.nist.hit.ds.ebDocsrcSim.transactions.AbstractClientTransaction
import gov.nist.hit.ds.dsSims.eb.transactionSupport.EbSendRequest
import gov.nist.hit.ds.ebDocsrcSim.transaction.ProvideAndRegisterTransaction
import gov.nist.hit.ds.ebMetadata.MetadataSupport
import gov.nist.hit.ds.eventLog.Fault
import gov.nist.hit.ds.simSupport.endpoint.EndpointValue
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.utilities.xml.Util
import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException
import gov.nist.hit.ds.xdsExceptions.XdsException
import gov.nist.hit.ds.xdsExceptions.XdsInternalException
import groovy.util.logging.Log4j
import org.apache.axiom.om.OMElement
/**
 * Created by bmajur on 1/13/15.
 *
 */
@Log4j
class PnrSend  {
//    OMElement metadata_element;
//    Map<String, DocumentHandler> documents;
//    String endpoint;
//    EnvironmentAccess environmentAccess
    OMElement logOutput = MetadataSupport.om_factory.createOMElement("Log", null);
    SimHandle simHandle;
    EbSendRequest request

//    PnrSend(OMElement _metadata_element, Map<String, DocumentHandler> _documents, String _endpoint, EnvironmentAccess _environmentAccess) {
//        metadata_element = _metadata_element
//        documents = _documents
//        endpoint = _endpoint
//        environmentAccess = _environmentAccess
//    }

//    private PnrSend(SimHandle simHandle, String transactionName, boolean tls, String metadata, Map<String, DocumentHandler> _documents) {
//        this.simHandle = simHandle
//        metadata_element = Util.parse_xml(metadata)
//        documents = _documents
//        EndpointValue endpointValue = simHandle.actorSimConfig.getEndpoint(
//                ActorTransactionTypeFactory.getTransactionType(transactionName),
//                (tls) ? TlsType.TLS : TlsType.NOTLS,
//                AsyncType.SYNC)
//        if (!endpointValue) throw new ToolkitRuntimeException("Transaction ${transactionName} with TLS ${tls} not configured")
//        endpoint = endpointValue.value
//        environmentAccess = simHandle.actorSimConfig.environmentAccess
//    }

    PnrSend(SimHandle _simHandle, EbSendRequest _request) {
        simHandle = _simHandle; request = _request
        log.info "PnrSend: ${request}"
    }

    def endpoint() {
        EndpointValue endpointValue = simHandle.actorSimConfig.getEndpoint(
                ActorTransactionTypeFactory.getTransactionType(request.transactionName),
                (request.tls) ? TlsType.TLS : TlsType.NOTLS,
                AsyncType.SYNC)
        if (!endpointValue) throw new ToolkitRuntimeException("Transaction ${request.transactionName} with TLS ${request.tls} not configured")
        return endpointValue.value
    }

    def addExtraHeaders(ProvideAndRegisterTransaction trans) {
        if (!request.extraHeaders) return
        OMElement headers = Util.parse_xml(request.extraHeaders)
        headers.childElements.each { trans.additionalHeaders.add(it) }
    }

    // return is [result, logOutput]
    List<OMElement> run() throws XdsException {
        String endpoint = endpoint()

        log.info "Transaction being sent to ${endpoint}"

        ProvideAndRegisterTransaction trans = new ProvideAndRegisterTransaction(simHandle);
        trans.no_convert = false;
        trans.nameUuidMap = null;
        trans.instruction_output = logOutput;
        trans.endpoint = endpoint;
        trans.messageId = (request.messageId) ? request.messageId : null
        trans.xds_version = AbstractClientTransaction.xds_b;
        addExtraHeaders(trans)

        TransactionSettings ts = new TransactionSettings();
        ts.securityParams = simHandle.actorSimConfig.environmentAccess
        trans.transactionSettings = ts;
        StepContext step = new StepContext();
        trans.s_ctx = step;
        step.transactionSettings = ts;

        OMElement endpointEle = MetadataSupport.om_factory.createOMElement("Endpoint", null)
        endpointEle.setText(endpoint)
        logOutput.addChild(endpointEle)

        List<OMElement> results = new ArrayList<>();
        try {
            OMElement result = trans.run(Util.parse_xml(request.metadata), request.documents);
            results.add(result)
        } catch (XdsInternalException e) {
            simHandle.event.fault = new Fault(e.message, 'CODE', "PNR", e.details)
            OMElement err_ele = MetadataSupport.om_factory.createOMElement("Fault", null);
            err_ele.setText(e.getMessage());
            results.add(err_ele);
        }
        results.add(logOutput);
        return results;
    }

}
