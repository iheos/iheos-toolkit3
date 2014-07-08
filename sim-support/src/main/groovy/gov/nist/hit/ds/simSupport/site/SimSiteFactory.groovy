package gov.nist.hit.ds.simSupport.site
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.EndpointLabel
import gov.nist.hit.ds.actorTransaction.TransactionType
import gov.nist.hit.ds.simSupport.client.ActorSimConfig
import gov.nist.hit.ds.simSupport.client.configElementTypes.EndpointActorSimConfigElement
import gov.nist.hit.ds.simSupport.client.configElementTypes.RepositoryUniqueIdSimConfigElement
import gov.nist.hit.ds.siteManagement.client.Site
import gov.nist.hit.ds.siteManagement.client.TransactionBean
/**
 * Created by bmajur on 6/8/14.
 */
class SimSiteFactory {

    Site buildSite(ActorSimConfig actorSimConfig, String name) {
        println(actorSimConfig)
        Site site = new Site(name)
        actorSimConfig.getAll().each {
            if (!(it instanceof EndpointActorSimConfigElement)) return
            if (it instanceof RepositoryUniqueIdSimConfigElement) return

            EndpointActorSimConfigElement endpointASCE = (EndpointActorSimConfigElement)it
            String transactionName = endpointASCE.getTransactionName()
            String endpoint = endpointASCE.value
            EndpointLabel endpointLabel = endpointASCE.getEndpointLabel()
            TransactionType ttype = new ActorTransactionTypeFactory().getTransactionType(transactionName);
            site.addTransaction(new TransactionBean(ttype, TransactionBean.RepositoryType.NONE, endpoint, endpointLabel.isTls(), endpointLabel.isAsync()))
        }
        actorSimConfig.getAll().each {
            if (!(it instanceof RepositoryUniqueIdSimConfigElement)) return

            RepositoryUniqueIdSimConfigElement repUidEle = (RepositoryUniqueIdSimConfigElement) it
            EndpointActorSimConfigElement endpointASCE = (EndpointActorSimConfigElement)it
            def repuid = repUidEle.newValue
            String endpoint = endpointASCE.value
            EndpointLabel endpointLabel = endpointASCE.getEndpointLabel()
            site.addRepository(repuid, TransactionBean.RepositoryType.REPOSITORY, endpoint, endpointLabel.isTls(), endpointLabel.isAsync())
        }
        return site
    }
}
