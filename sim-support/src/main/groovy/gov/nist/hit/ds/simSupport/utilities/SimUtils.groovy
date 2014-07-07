package gov.nist.hit.ds.simSupport.utilities

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.repository.api.ArtifactId
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.simple.SimpleId
import gov.nist.hit.ds.repository.simple.SimpleType
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.endpoint.EndpointBuilder
import gov.nist.hit.ds.simSupport.simulator.SimConfigFactory
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.site.SimSiteFactory
import gov.nist.hit.ds.siteManagement.client.Site
import gov.nist.hit.ds.siteManagement.loader.SeparateSiteLoader
import gov.nist.hit.ds.utilities.xml.OMFormatter
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
import groovy.util.logging.Log4j
import org.apache.axiom.om.OMElement
/**
 * Created by bmajur on 7/4/14.
 */
@Log4j
class SimUtils {
    static configAssetName = 'config'
    static siteAssetName = 'site'
    static ArtifactId siteAssetId = new SimpleId(siteAssetName)
    static ArtifactId configAssetId = new SimpleId(configAssetName)
    static ArtifactId eventRootId = null

    static Asset mkSim(String actorTypeName, SimId simId) {
        Asset simAsset = RepoUtils.mkAsset(simId.id, new SimpleType('sim'), SimSupport.simRepo)
        Asset eventAsset = RepoUtils.mkChild('Events', simAsset)
        simAsset.addAsset(eventAsset)
        eventRootId = eventAsset.id

        def actorSimConfig = new SimConfigFactory().buildSim('localhost', '8080', 'simwar', simId, actorTypeName)
        storeConfig(actorSimConfig.toXML(), simAsset)

        Site site = new SimSiteFactory().buildSite(actorSimConfig, simId.id)
        OMElement siteEle = new SeparateSiteLoader().siteToXML(site)
        storeSite(siteEle, simAsset)

        return simAsset
    }

    static Asset sim(SimId simId) {
        return RepoUtils.asset(artifactId(simId), SimSupport.simRepo)
    }

    static SimHandle handle(SimId simId) {
        def simAsset = sim(simId)
        def simHandle = new SimHandle()
        simHandle.simId = simId
        simHandle.simAsset = simAsset
        simHandle.siteAsset = RepoUtils.childWithId(simAsset, siteAssetId)
        simHandle.configAsset = RepoUtils.childWithId(simAsset, configAssetId)
        simHandle.eventLogAsset = RepoUtils.childWithId(simAsset, eventRootId)
        return simHandle
    }

    static runTransaction(String endpoint, String header, byte[] body) {
        def builder = new EndpointBuilder()
        builder.parse(endpoint)
        def transCode = builder.transCode
        def simId = builder.simId

        def simHandle = handle(simId)
        def event = new Event(simHandle.eventLogAsset)

        // Registry inputs
        event.inOut.reqHdr = header
        event.inOut.reqBody = body

        // Lookup transaction implementation class
        def transactionType = new ActorTransactionTypeFactory().getTransactionType(transCode)
        def implClassName = transactionType.implementationClassName
        log.debug("implClassName is ${implClassName}")

        // build implementation
        Class<?> clazz = new SimUtils().class.classLoader.loadClass(implClassName)
        if (!clazz) throw new ToolkitRuntimeException("Class ${implClassName} cannot be loaded.")
        Object<?> instance = clazz.newInstance()

//        // Verify impl class implements ValComponent interface
//        Method[] methods = clazz.methods
//        Method runMethod = null
//        for (int i=0; i<methods.length; i++) {
//            Method method = methods[i]
//            if (method.name == 'run') {
//                runMethod = method
//                break
//             }
//        }
//        if (runMethod == null) throw new ToolkitRuntimeException("class ${implClassName} does implement a run() method.")

        // call run() method
        Object[] params = new Object[1]
        params[0] = handle(simId)
        instance.invokeMethod('run', params)
    }

    static ArtifactId artifactId(SimId simId) { return new SimpleId(simId.id) }

    static def storeConfig(OMElement config, Asset simAsset) { store(config, configAssetName, simAsset) }
    static def storeSite(OMElement site, Asset simAsset) { store(site, siteAssetName, simAsset) }

    private static store(OMElement xmlEle, String name, Asset simAsset) {
        Asset configAsset = RepoUtils.mkChild(name, simAsset)
        configAsset.updateContent(new OMFormatter(xmlEle).toString(), 'text/xml')
    }

}
