package gov.nist.hit.ds.simSupport.utilities

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.repository.api.ArtifactId
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.api.Repository
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.repository.simple.SimpleId
import gov.nist.hit.ds.repository.simple.SimpleType
import gov.nist.hit.ds.simSupport.client.ActorSimConfig
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.endpoint.EndpointBuilder
import gov.nist.hit.ds.simSupport.serializer.SimulatorDAO
import gov.nist.hit.ds.simSupport.simulator.SimConfigFactory
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.simulator.SimSystemConfig
import gov.nist.hit.ds.simSupport.site.SimSiteFactory
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.siteManagement.client.Site
import gov.nist.hit.ds.siteManagement.loader.SeparateSiteLoader
import gov.nist.hit.ds.soapSupport.core.Endpoint
import gov.nist.hit.ds.utilities.xml.OMFormatter
import groovy.util.logging.Log4j
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 7/4/14.
 */
@Log4j
class SimUtils {
    static final configAssetName = 'config'
    static final siteAssetName = 'site'
    static final eventsAssetName = 'Events'

    // TODO: if already exists - verify actorTypeName

    // Some tests rely on this - has hard coded values
    static SimHandle create(String actorTypeName, SimId simId) {
        def eConfig = new SimSystemConfig()
        eConfig.host = 'localhost'
        eConfig.port = '8080'
        eConfig.service = 'simwar'
        return create(actorTypeName, simId, eConfig)
    }

    static SimHandle create(String actorTypeName, SimId simId, SimSystemConfig simEndpointConfig) {
        Repository repo = buildRepository(simEndpointConfig.repoName)
        if (exists(simId, simEndpointConfig.repoName)) {
            log.debug("Sim ${simId.id} exists.")
            return open(simId, simEndpointConfig.repoName)
        }
        log.debug("Creating sim ${simId.id}")

        Asset simAsset = RepoUtils.mkAsset(simId.id, new SimpleType('sim'), repo)

        ActorSimConfig actorSimConfig = new SimConfigFactory().buildSim(simEndpointConfig.host, simEndpointConfig.port, simEndpointConfig.service, simId, actorTypeName)
        storeConfig(new SimulatorDAO().toXML(actorSimConfig), simAsset)

        Site site = new SimSiteFactory().buildSite(actorSimConfig, simId.id)
        OMElement siteEle = new SeparateSiteLoader().siteToXML(site)
        storeSite(new OMFormatter(siteEle).toString(), simAsset)

        RepoUtils.mkChild(eventsAssetName, simAsset)

        return open(simId, simEndpointConfig.repoName)
    }

    static SimHandle open(SimId simId, String repositoryName) {
        Repository repository = RepoUtils.getRepository(repositoryName)
        Asset simAsset = sim(simId, repository)
        if (!simAsset) return null
        def simHandle = new SimHandle()
        simHandle.simId = simId
        simHandle.simAsset = simAsset
        simHandle.siteAsset = RepoUtils.child(siteAssetName, simAsset)
        simHandle.configAsset = RepoUtils.child(configAssetName, simAsset)
        simHandle.eventLogAsset = RepoUtils.child(eventsAssetName, simAsset)
        simHandle.repository = repository

        return simHandle
    }

    static def delete(SimId simId, String repositoryName) {
        Repository repository = RepoUtils.getRepository(repositoryName)
        Asset simAsset = sim(simId, repository)
        if (!simAsset) return
        RepoUtils.rmAsset(simId.id, repository)
    }

    static Repository buildRepository(String repositoryName) {
        return RepoUtils.getRepository(repositoryName, new SimpleType('simRepos'))
    }

    static boolean exists(SimId simId, String repositoryName) {
        Repository repository = RepoUtils.getRepository(repositoryName)
        try {
            if (!sim(simId, repository)) return false
            return true
        } catch (Exception e) { return false }
    }

    static def runTransaction(Endpoint endpoint, String header, byte[] body, repositoryName) {
        def endp = new EndpointBuilder().parse(endpoint)
        def transactionType = new ActorTransactionTypeFactory().getTransactionType(endp.transCode)
        def simId = endp.simId
        new TransactionRunner(simId, repositoryName, transactionType, header, body).run()
    }

    private static Asset sim(SimId simId, Repository repository) {
        try {
            return RepoUtils.child(simId.id, repository)
        } catch (RepositoryException e) { return null }
    }

    private static ArtifactId artifactId(SimId simId) { return new SimpleId(simId.id) }

    private static storeConfig(String config, Asset simAsset) { store(config, configAssetName, simAsset) }
    private static storeSite(String site, Asset simAsset) { store(site, siteAssetName, simAsset) }

    private static store(String xmlEle, String name, Asset parent) {
        def a = RepoUtils.mkChild(name, parent)
        a.setContent(xmlEle, 'text/xml')
    }

}
