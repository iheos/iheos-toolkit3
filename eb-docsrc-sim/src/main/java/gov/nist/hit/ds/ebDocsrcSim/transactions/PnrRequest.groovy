package gov.nist.hit.ds.ebDocsrcSim.transactions
import gov.nist.hit.ds.ebDocsrcSim.engine.DocumentHandler
/**
 * Created by bmajur on 1/24/15.
 */
class PnrRequest {
    String metadata = null
    String transactionName
    Map<String, DocumentHandler> documents = [ : ]
    boolean tls
}
