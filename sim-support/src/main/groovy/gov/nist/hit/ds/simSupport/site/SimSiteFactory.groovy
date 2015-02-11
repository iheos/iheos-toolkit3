package gov.nist.hit.ds.simSupport.site
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.EndpointType
import gov.nist.hit.ds.actorTransaction.TransactionType
import gov.nist.hit.ds.simSupport.config.SimConfig
import gov.nist.hit.ds.simSupport.config.RetrieveTransactionSimConfigElement
import gov.nist.hit.ds.simSupport.config.TransactionSimConfigElement
import gov.nist.hit.ds.siteManagement.client.Site
import gov.nist.hit.ds.siteManagement.client.TransactionBean
import groovy.util.logging.Log4j

/**
 * Created by bmajur on 6/8/14.
 */
@Log4j
class SimSiteFactory {

    Site buildSite(SimConfig actorSimConfig, String name) {
        println(actorSimConfig)
        Site site = new Site(name)

        // This code depends on the rule that a Site can have only a single Repository

        def allTransactions = actorSimConfig.getTransactions().findAll { it instanceof TransactionSimConfigElement }
        def retrieveTransactions = allTransactions.findAll { it instanceof  RetrieveTransactionSimConfigElement }
        def notRetrieveTransactions = allTransactions.findAll { !(it instanceof  RetrieveTransactionSimConfigElement) }

        // Add transactions

        notRetrieveTransactions.each { TransactionSimConfigElement transaction ->
            String transactionName = transaction.getTransactionName()
            String endpoint = transaction.endpointValue.value
            EndpointType endpointLabel = transaction.getEndpointType()
            TransactionType ttype = new ActorTransactionTypeFactory().getTransactionType(transactionName);
            site.addTransaction(new TransactionBean(ttype, TransactionBean.RepositoryType.NONE, endpoint, endpointLabel.isTls(), endpointLabel.isAsync()))
        }

        // add repositories
            retrieveTransactions.each { retrieveTransaction ->
                def repuid = retrieveTransaction.repositoryUniqueId
                String endpoint = retrieveTransaction.endpointValue.value
                EndpointType endpointType = retrieveTransaction.getEndpointType()
                site.addRepository(repuid, TransactionBean.RepositoryType.REPOSITORY, endpoint, endpointType.isTls(), endpointType.isAsync())
            }
        return site
    }
}
