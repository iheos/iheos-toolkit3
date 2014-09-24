package gov.nist.hit.ds.simSupport.site
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.EndpointType
import gov.nist.hit.ds.actorTransaction.TransactionType
import gov.nist.hit.ds.simSupport.client.ActorSimConfig
import gov.nist.hit.ds.simSupport.client.configElementTypes.EndpointSimConfigElement
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
        actorSimConfig.getElements().each {
            if (!(it instanceof EndpointSimConfigElement)) return
            if (it instanceof RepositoryUniqueIdSimConfigElement) return

            EndpointSimConfigElement endpointASCE = (EndpointSimConfigElement)it
            String transactionName = endpointASCE.getTransactionName()
            String endpoint = endpointASCE.value
            EndpointType endpointLabel = endpointASCE.getEndpointType()
            TransactionType ttype = new ActorTransactionTypeFactory().getTransactionType(transactionName);
            site.addTransaction(new TransactionBean(ttype, TransactionBean.RepositoryType.NONE, endpoint, endpointLabel.isTls(), endpointLabel.isAsync()))
        }
        actorSimConfig.getElements().each {
            if (!(it instanceof RepositoryUniqueIdSimConfigElement)) return

            RepositoryUniqueIdSimConfigElement repUidEle = (RepositoryUniqueIdSimConfigElement) it
            EndpointSimConfigElement endpointASCE = (EndpointSimConfigElement)it
            def repuid = repUidEle.newValue
            String endpoint = endpointASCE.value
            EndpointType endpointLabel = endpointASCE.getEndpointType()
            site.addRepository(repuid, TransactionBean.RepositoryType.REPOSITORY, endpoint, endpointLabel.isTls(), endpointLabel.isAsync())
        }
        return site
    }
}
