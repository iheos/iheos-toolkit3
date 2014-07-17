package gov.nist.hit.ds.simSupport.utilities
import gov.nist.hit.ds.repository.api.ArtifactId
import gov.nist.hit.ds.repository.api.Asset
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
    static configAssetName = 'config'
    static siteAssetName = 'site'
    // TODO: These need to key off asset name instead of asset id
    static ArtifactId siteAssetId = new SimpleId(siteAssetName)
    static ArtifactId configAssetId = new SimpleId(configAssetName)
    static ArtifactId eventRootId = null

    // TODO: Make this work with already-exists logic in place
    static Asset mkSim(String actorTypeName, SimId simId) {
//        if (exists(simId)) throw new ToolkitRuntimeException("Sim ${simId.id} already exists.")

        Asset simAsset = RepoUtils.mkAsset(simId.id, new SimpleType('sim'), SimSupport.simRepo)
        Asset eventsAsset = RepoUtils.mkChild('Events', simAsset)
        simAsset.addAsset(eventsAsset)
        eventRootId = eventsAsset.id

        def actorSimConfig = new SimConfigFactory().buildSim('localhost', '8080', 'simwar', simId, actorTypeName)
        storeConfig(actorSimConfig.toXML(), simAsset)

        Site site = new SimSiteFactory().buildSite(actorSimConfig, simId.id)
        OMElement siteEle = new SeparateSiteLoader().siteToXML(site)
        storeSite(siteEle, simAsset)

        return simAsset
    }

    // needs to initialize eventRootId even if sim already exists
    static Asset mkSimConditional(String actorTypeName, SimId simId) {
//        if (exists(simId)) {
            // load Events asset under Sim
            // initialize eventRootId
//            return sim(simId)
//        }
        return mkSim(actorTypeName, simId)
    }

    static boolean exists(SimId simId) { return RepoUtils.assetIfAvailable(artifactId(simId), SimSupport.simRepo)}

    static Asset sim(SimId simId) {
        return RepoUtils.asset(artifactId(simId), SimSupport.simRepo)
    }

    static SimHandle handle(SimId simId) {
        def simAsset = sim(simId)
        assert simAsset
        def simHandle = new SimHandle()
        simHandle.simId = simId
        simHandle.simAsset = simAsset
        simHandle.siteAsset = RepoUtils.childWithId(simAsset, siteAssetId)
        simHandle.configAsset = RepoUtils.childWithId(simAsset, configAssetId)
        simHandle.eventLogAsset = RepoUtils.childWithId(simAsset, eventRootId)
        return simHandle
    }

    static ArtifactId artifactId(SimId simId) { return new SimpleId(simId.id) }

    static def storeConfig(OMElement config, Asset simAsset) { store(config, configAssetName, simAsset) }
    static def storeSite(OMElement site, Asset simAsset) { store(site, siteAssetName, simAsset) }

    private static store(OMElement xmlEle, String name, Asset simAsset) {
        Asset configAsset = RepoUtils.mkChild(name, simAsset)
        configAsset.updateContent(new OMFormatter(xmlEle).toString(), 'text/xml')
    }

}
