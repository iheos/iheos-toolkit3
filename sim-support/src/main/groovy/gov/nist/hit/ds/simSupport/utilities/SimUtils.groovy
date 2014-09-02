package gov.nist.hit.ds.simSupport.utilities
import gov.nist.hit.ds.repository.api.ArtifactId
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.api.Repository
import gov.nist.hit.ds.repository.simple.SimpleId
import gov.nist.hit.ds.repository.simple.SimpleType
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.simulator.SimConfigFactory
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.site.SimSiteFactory
import gov.nist.hit.ds.siteManagement.client.Site
import gov.nist.hit.ds.siteManagement.loader.SeparateSiteLoader
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
    static SimHandle create(String actorTypeName, SimId simId, String repositoryName) {
        if (exists(simId, repositoryName)) {
            log.debug("Sim ${simId.id} exists.")
            return open(simId, repositoryName)
        }
        log.debug("Creating sim ${simId.id}")
        Asset simAsset = RepoUtils.mkAsset(simId.id, new SimpleType('sim'), SimSupport.simRepo)

        def actorSimConfig = new SimConfigFactory().buildSim('localhost', '8080', 'simwar', simId, actorTypeName)
        storeConfig(actorSimConfig.toXML(), simAsset)

        Site site = new SimSiteFactory().buildSite(actorSimConfig, simId.id)
        OMElement siteEle = new SeparateSiteLoader().siteToXML(site)
        storeSite(siteEle, simAsset)

        RepoUtils.mkChild(eventsAssetName, simAsset)

        return open(simId, repositoryName)
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

    static boolean exists(SimId simId, String repositoryName) {
        Repository repository = RepoUtils.getRepository(repositoryName)
        try {
            if (!sim(simId, repository)) return false
            return true
        } catch (Exception e) { return false }
    }

//    private static Asset sim(SimId simId) {
//        return sim(simId, SimSupport.simRepo)
//    }

    private static Asset sim(SimId simId, Repository repository) {
        return RepoUtils.child(simId.id, repository)
    }

    private static ArtifactId artifactId(SimId simId) { return new SimpleId(simId.id) }

    private static storeConfig(OMElement config, Asset simAsset) { store(config, configAssetName, simAsset) }
    private static storeSite(OMElement site, Asset simAsset) { store(site, siteAssetName, simAsset) }

    private static store(OMElement xmlEle, String name, Asset parent) {
        def a = RepoUtils.mkChild(name, parent)
        a.setContent(new OMFormatter(xmlEle).toString(), 'text/xml')
    }

}
