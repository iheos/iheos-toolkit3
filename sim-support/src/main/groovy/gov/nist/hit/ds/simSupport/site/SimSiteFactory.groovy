package gov.nist.hit.ds.simSupport.site
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.EndpointType
import gov.nist.hit.ds.actorTransaction.TransactionType
import gov.nist.hit.ds.simSupport.client.ActorSimConfig
import gov.nist.hit.ds.simSupport.client.configElementTypes.TransactionSimConfigElement
import gov.nist.hit.ds.simSupport.client.configElementTypes.RepositoryUniqueIdSimConfigElement
import gov.nist.hit.ds.siteManagement.client.Site
import gov.nist.hit.ds.siteManagement.client.TransactionBean
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
import groovy.util.logging.Log4j

/**
 * Created by bmajur on 6/8/14.
 */
@Log4j
class SimSiteFactory {

    Site buildSite(ActorSimConfig actorSimConfig, String name) {
        println(actorSimConfig)
        Site site = new Site(name)

        // This code depends on the rule that a Site can have only a single Repository

        def transactions = actorSimConfig.getElements().findAll { it instanceof TransactionSimConfigElement }
        def retrieveTransactions = transactions.findAll { println "TransTypeId is ${it.getTransactionType().getId()}"; it.getTransactionType().getId() == TransactionType.retrieveTransactionTypeCode }

        // Add transactions

        transactions.each { TransactionSimConfigElement transaction ->
            String transactionName = transaction.getTransactionName()
            String endpoint = transaction.endpointValue.value
            EndpointType endpointLabel = transaction.getEndpointType()
            TransactionType ttype = new ActorTransactionTypeFactory().getTransactionType(transactionName);
            site.addTransaction(new TransactionBean(ttype, TransactionBean.RepositoryType.NONE, endpoint, endpointLabel.isTls(), endpointLabel.isAsync()))
        }

        // add repositories

        if (retrieveTransactions == null) return site
        def repUIDElements = actorSimConfig.getElements().findAll { it instanceof RepositoryUniqueIdSimConfigElement }
        if (retrieveTransactions && !repUIDElements) throw new ToolkitRuntimeException("Site ${name} defines a Retrieve transaction but no repositoryUniqueId")

        if (repUIDElements.size() > 0) {
            def repuid = repUIDElements.first().value

            retrieveTransactions.each { retrieveTransaction ->
                String endpoint = retrieveTransaction.endpointValue.value
                EndpointType endpointType = retrieveTransaction.getEndpointType()
                site.addRepository(repuid, TransactionBean.RepositoryType.REPOSITORY, endpoint, endpointType.isTls(), endpointType.isAsync())
            }
        }
        return site
    }
}
