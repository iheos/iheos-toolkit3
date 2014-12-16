package gov.nist.hit.ds.simSupport.api
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.repoSupport.RepoUtils
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.simulator.SimSystemConfig
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import groovy.util.logging.Log4j
/**
 * Created by bmajur on 12/4/14.
 */
@Log4j
class ValidationApi {
    def factory = new ActorTransactionTypeFactory()
    def config = new SimSystemConfig()

    Asset validateRequest(String transactionName, String content, String headers) {
        def simId = new SimId('ValidationApi')
        def simHandle = SimUtils.create(simId)
        def transactionType = factory.getTransactionType(transactionName)
        simHandle.transactionType = transactionType
        simHandle.repository = RepoUtils.getRepository(config.repoName)
        simHandle.event.inOut.reqHdr = headers
        simHandle.event.inOut.reqBody = content.getBytes()
        def runner = new TransactionRunner(simHandle)
        runner.validateRequest()
        return simHandle.event.eventAsset
    }
}
