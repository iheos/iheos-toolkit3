package gov.nist.hit.ds.simSupport.utilities
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.ActorType
import gov.nist.hit.ds.actorTransaction.TransactionType
import gov.nist.hit.ds.repoSupport.RepoUtils
import gov.nist.hit.ds.repository.api.ArtifactId
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.api.Repository
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.repository.simple.SimpleAsset
import gov.nist.hit.ds.repository.simple.SimpleId
import gov.nist.hit.ds.repository.simple.SimpleType
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.client.SimIdentifier
import gov.nist.hit.ds.simSupport.config.SimConfig
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
import gov.nist.hit.ds.xdsExceptions.ToolkitException
import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException
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

    static final defaultActorType = null
    static final defaultRepoName = 'unknown'

    // TODO: if already exists - verify actorTypeName

    /**
     * Create simulator instance with no registered actor  (really just an isolated Event log)
     * @param simId - Simulator ID
     * @return SimHandle
     */

    static SimHandle create(SimIdentifier simId) {
        return create(defaultActorType, simId.simId, simId.repoName)
    }

    static SimHandle create(SimId simId, String repoName) {
        return create(defaultActorType, simId, repoName)
    }

    static SimHandle recreate(String actorTypeName, SimIdentifier simIdent) {
        delete(simIdent)
        create(actorTypeName, simIdent)
    }

    static SimHandle recreate(String actorTypeName, SimId simId, String repoName) {
        delete(new SimIdentifier(SimUtils.defaultRepoName, simId))
        create(actorTypeName, simId, repoName)
    }

    // Seems odd to do lookup on transaction instead of actor.  This is needed for
    // validation manager where transaction is all that is known.
    // Should not be used anywhere else
    static SimHandle create(TransactionType ttype, repository, simId) {
//        TransactionType ttype
//        if (transaction instanceof TransactionType)
//            ttype = transaction
//        else if (transaction instanceof String)
//            ttype = new ActorTransactionTypeFactory().getTransactionType(transaction)
//        else
//            throw new ToolkitRuntimeException("Cannot interpret transaction parameter - type is ${transaction?.class?.name}")

        Repository repo
        if (repository instanceof Repository)
            repo = repository
        else if (repository instanceof String)
            repo = RepoUtils.getRepository(repository)
        else
            throw new ToolkitRuntimeException("Cannot interpret repository parameter - type is ${repository?.class?.name}")
        SimId id
        if (simId instanceof SimId)
            id = simId
        else if (simId instanceof String)
            id = new SimId(simId)
        else
            throw new ToolkitRuntimeException("Cannot interpret simId parameter - type is ${simId?.class?.name}")
        return create(ttype, repo, id)
    }

    static SimHandle create(TransactionType transactionType, Repository repository, SimId simId) {
        ActorType actorType = transactionType.actorType
        if (!actorType) throw new ToolkitRuntimeException("Transaction Type ${transactionType.name} is not owned by an actorType")
        def simHandle = create(actorType.name, simId, repository.id.idString)
        simHandle.transactionType = transactionType
        return simHandle
    }

    static SimHandle createSimForUser(String actorTypeName, SimId simId, String userName) {
        create(actorTypeName, simId, userName)
    }

    static  SimHandle create(String actorTypeName, SimIdentifier simIdent) {
        create(actorTypeName, simIdent.simId, simIdent.repoName)
    }

    static SimHandle create(String actorTypeName, SimId simId, String repoName) {
//        assert actorTypeName
        assert simId
        assert repoName
        def user = repoName
        Repository repo = buildRepository(repoName)
        if (exists(simId, repoName)) { // exists but might not be initialized
            log.debug("Sim ${simId.id} exists.")
            //initialize(user, simId, actorTypeName, simAsset)
            return open(simId.id, repoName)
        }
        log.debug("Creating sim ${simId.id}")
        Asset simAsset = RepoUtils.mkAsset(simId.id, new SimpleType('sim'), repo)

        initialize(user, simId, actorTypeName, simAsset)

        return open(simId.id, repoName)
    }

    static initialize(String user, SimId simId, String actorTypeName, Asset simAsset) {
        RepoUtils.mkChild(eventsAssetName, simAsset)
        if (!actorTypeName) return
//        SimSystemConfig simSystemConfig = new SimSystemConfig()
//        log.debug(simSystemConfig.toString())
//        SimConfig actorSimConfig = new SimConfigFactory().buildSim(simSystemConfig.host, simSystemConfig.port, simSystemConfig.service, user, simId, new ActorTransactionTypeFactory().getActorType(actorTypeName))
        SimConfig actorSimConfig = new SimConfigFactory().buildSim(new SimSystemConfig(), user, simId, new ActorTransactionTypeFactory().getActorType(actorTypeName))
        storeConfig(new SimulatorDAO().toXML(actorSimConfig), simAsset)
        Site site = new SimSiteFactory().buildSite(actorSimConfig, simId.id)
        OMElement siteEle = new SeparateSiteLoader().siteToXML(site)
        storeSite(new OMFormatter(siteEle).toString(), simAsset)
    }

//    @Deprecated
//    static SimHandle open(SimId simId) {
//        return open(simId.id, SimSystemConfig.repoName)
//    }

    static SimHandle open(String simId, String repositoryName) {
        Repository repository = RepoUtils.getRepository(repositoryName)
        return open(new SimId(simId), repository)
    }

    static SimHandle open(SimIdentifier simIdent) {
        open(simIdent.simId, RepoUtils.getRepository(simIdent.repoName))
    }

    static SimHandle open(SimId simId, Repository repository) {
        assert repository
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
//        assert simHandle.siteAsset
        try {
            simHandle.configAsset = RepoUtils.child(configAssetName, simAsset)
        } catch (RepositoryException e) {}
//        assert simHandle.configAsset
        simHandle.eventLogAsset = RepoUtils.child(eventsAssetName, simAsset)
        simHandle.repository = repository
        if (simHandle.configAsset)
            simHandle.actorSimConfig = loadConfig(simHandle.configAsset)
        log.debug("Open ActorSimConfig: ${simHandle.actorSimConfig}")

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

    static def delete(SimIdentifier simIdent) {
        delete(simIdent.simId, simIdent.repoName)
    }

    static def delete(SimId simId, String repositoryName) {
        Repository repository = RepoUtils.getRepository(repositoryName)
        Asset simAsset = sim(simId, repository)
        if (!simAsset) return
        RepoUtils.rmAsset(simId.id, repository)
    }

    static Repository buildRepository(String repositoryName) {
        log.debug("Build repoName ${repositoryName}")
        return RepoUtils.getRepository(repositoryName, new SimpleType('simRepos'))
    }

    static boolean exists(SimId simId, String repositoryName) {
        Repository repository = RepoUtils.getRepository(repositoryName)
        try {
            if (!sim(simId, repository)) return false
            return true
        } catch (Exception e) { return false }
    }

    static runTransaction(SimHandle simHandle) { new TransactionRunner(simHandle).acceptRequest() }
    static sendTransactionRequest(SimHandle simHandle, request) { new TransactionRunner(simHandle).sendRequest(request) }
    static validateTransactionRequest(SimHandle simHandle) { new TransactionRunner(simHandle).validateRequest() }
    static validateTransactionResponse(SimHandle simHandle) { new TransactionRunner(simHandle).validateResponse() }

    // used in unit tests only
    static SimHandle runTransaction(Endpoint endpoint, String header, byte[] body, String repositoryName) {
        log.debug("Inside runTransaction")
        def endp = new EndpointBuilder().parse(endpoint)
        log.debug("runTransaction: endpoint = ${endp}")
        def transactionType = new ActorTransactionTypeFactory().getTransactionType(endp.transCode)
        SimId simId = endp.simId
        return new TransactionRunner(simId, repositoryName, transactionType, header, body).testRun()
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
            log.debug "Storing content ${xmlEle}"
            a = RepoUtils.mkChild(name, parent)
            a.setContent(xmlEle, 'text/xml')
        } else {
            log.debug "Updating content ${xmlEle}"
            a.setAutoFlush(true)
            a.updateContent(xmlEle)
        }
    }

    static SimConfig loadConfig(Asset a) { new SimulatorDAO().toModel(load(a))}
    static String load(Asset a) { new String(a.content) }

}
