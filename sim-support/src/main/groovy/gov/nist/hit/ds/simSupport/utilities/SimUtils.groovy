package gov.nist.hit.ds.simSupport.utilities
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.repository.api.ArtifactId
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.api.Repository
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.repository.simple.SimpleAsset
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
import gov.nist.hit.ds.xdsException.ToolkitException
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

    /**
     * Create simulator instance with no registered actor  (really just an isolated Event log)
     * @param simId - Simulator ID
     * @return SimHandle
     */

    static SimHandle create(SimId simId) {
        return create(null, simId, SimSystemConfig.repoName)
    }

    static SimHandle create(String actorTypeName, SimId simId) {
        return create(actorTypeName, simId, new SimSystemConfig().repoName)
    }

    static SimHandle create(String actorTypeName, SimId simId, String repoName) {
        SimSystemConfig simSystemConfig = new SimSystemConfig()
        Repository repo = buildRepository(repoName)
        if (exists(simId, repoName)) {
            log.debug("Sim ${simId.id} exists.")
            return open(simId.id, repoName)
        }
        log.debug("Creating sim ${simId.id}")

        Asset simAsset = RepoUtils.mkAsset(simId.id, new SimpleType('sim'), repo)

        if (actorTypeName) {
            ActorSimConfig actorSimConfig = new SimConfigFactory().buildSim(simSystemConfig.host, simSystemConfig.port, simSystemConfig.service, simId, new ActorTransactionTypeFactory().getActorType(actorTypeName))
            storeConfig(new SimulatorDAO().toXML(actorSimConfig), simAsset)
            Site site = new SimSiteFactory().buildSite(actorSimConfig, simId.id)
            OMElement siteEle = new SeparateSiteLoader().siteToXML(site)
            storeSite(new OMFormatter(siteEle).toString(), simAsset)
        }

        RepoUtils.mkChild(eventsAssetName, simAsset)

        return open(simId.id, repoName)
    }

    static SimHandle open(SimId simId) {
        return open(simId.id, SimSystemConfig.repoName)
    }

    static SimHandle open(String simId, String repositoryName) {
        Repository repository = RepoUtils.getRepository(repositoryName)
        return open(new SimId(simId), repository)
    }


    static SimHandle open(SimId simId, Repository repository) {
        Asset simAsset = sim(simId, repository)
        if (!simAsset) return null
        def simHandle = new SimHandle()
        simHandle.simId = simId
        simHandle.simAsset = simAsset
        if (simHandle.open)
            throw new ToolkitException("Sim ${simId.id} already open.")
        simHandle.open = true
        try {
            simHandle.siteAsset = RepoUtils.child(siteAssetName, simAsset)
        } catch (RepositoryException e) {}
        try {
            simHandle.configAsset = RepoUtils.child(configAssetName, simAsset)
        } catch (RepositoryException e) {}
        simHandle.eventLogAsset = RepoUtils.child(eventsAssetName, simAsset)
        simHandle.repository = repository
        if (simHandle.configAsset)
            simHandle.actorSimConfig = loadConfig(simHandle.configAsset)
        println "Open ActorSimConfig: ${simHandle.actorSimConfig}"

        // Made event creation lazy so that creating Sim does not create an empty event
//        def event = new EventFactory().buildEvent(simHandle.repository, simHandle.eventLogAsset)
//        simHandle.event = event
//        event.init()
        return simHandle
    }

    static def close(SimHandle simHandle) {
        assert simHandle.open
        simHandle.open = false
        simHandle.event.flushAll()
    }

    static def delete(SimId simId) {
        delete(simId, new SimSystemConfig().repoName)
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

    // used in unit tests only
    static SimHandle runTransaction(Endpoint endpoint, String header, byte[] body, String repositoryName) {
        log.debug("Inside runTransaction")
        def endp = new EndpointBuilder().parse(endpoint)
        log.debug("runTransaction: endpoint = ${endp}")
        def transactionType = new ActorTransactionTypeFactory().getTransactionType(endp.transCode)
        SimId simId = endp.simId
        return new TransactionRunner(simId, repositoryName, transactionType, header, body).run()
    }

    private static Asset sim(SimId simId, Repository repository) {
        try {
            return RepoUtils.child(simId.id, repository)
        } catch (RepositoryException e) { return null }
    }

    private static ArtifactId artifactId(SimId simId) { return new SimpleId(simId.id) }

    static storeConfig(SimHandle simHandle, String config) { storeConfig(config, simHandle.simAsset)}

    static storeConfig(String config, Asset simAsset) { store(config, configAssetName, simAsset) }
    private static storeSite(String site, Asset simAsset) { store(site, siteAssetName, simAsset) }

    private static store(String xmlEle, String name, Asset parent) {
        SimpleAsset a = RepoUtils.getChildIfAvailable(name, parent)
        if (!a) {
            println "Storing content ${xmlEle}"
            a = RepoUtils.mkChild(name, parent)
            a.setContent(xmlEle, 'text/xml')
        } else {
            println "Updating content ${xmlEle}"
            a.setAutoFlush(true)
            a.updateContent(xmlEle)
        }
    }

    private static ActorSimConfig loadConfig(Asset a) { new SimulatorDAO().toModel(load(a))}
    private static String load(Asset a) { new String(a.content) }

}
