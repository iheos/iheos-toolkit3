package gov.nist.hit.ds.simSupport.api
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.repoSupport.RepoUtils
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.shared.data.AssetNode
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.simulator.SimSystemConfig
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import groovy.util.logging.Log4j
import org.apache.log4j.Logger

/**
 * Created by bmajur on 12/4/14.
 */

class ValidationApi {
    private static Logger log = Logger.getLogger(ValidationApi);
    def factory = new ActorTransactionTypeFactory()
//    def config = new SimSystemConfig()

    AssetNode validateRequest(String transactionName, String content, String headers) {
        validateRequest('unknown', transactionName, content, headers)
    }

    AssetNode validateRequest(String repoName, String transactionName, String content, String headers) {
        def simId = new SimId('ValidationApi')
        def transactionType = factory.getTransactionType(transactionName)
        def simHandle = SimUtils.create(transactionType, repoName, simId)
        simHandle.transactionType = transactionType
        simHandle.repository = RepoUtils.getRepository(repoName)
        simHandle.event.inOut.reqHdr = headers
        simHandle.event.inOut.reqBody = content.getBytes()
        def runner = new TransactionRunner(simHandle)
        runner.validateRequest()
        AssetNode assetNode = new AssetNode()
        Asset a = simHandle.event.eventAsset
        assetNode.assetId = a.id.idString
        assetNode.reposSrc = a.source
        assetNode.repId = a.repository.idString
        assetNode.type = "validators"
        return assetNode
    }

    AssetNode validateRequest(String transactionName, String content) {
        def header
        // fake the headers since upload only offers the body
        content = content.trim()
        if (content.size() > 1 && content.startsWith('{'))
            header = 'Content-Type: application/json+fhir'
        else
            header = 'Content-Type: application/atom+xml'
        return validateRequest(transactionName, content, header)
    }
}
