package gov.nist.hit.ds.simSupport.utilities
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.eventLog.EventFactory
import gov.nist.hit.ds.eventLog.Fault
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
import gov.nist.hit.ds.soapSupport.FaultCode
import gov.nist.hit.ds.utilities.xml.OMFormatter
import gov.nist.hit.ds.xdsException.ExceptionUtil
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
    static eventsAssetName = 'Events'

    static Asset mkSim(String actorTypeName, SimId simId) {
        if (RepoUtils.assetIfAvailable(artifactId(simId), SimSupport.simRepo)) throw new SimulatorExistsException("Simulator ${simId.id} aleady exists.")
        Asset simAsset = RepoUtils.mkAsset(simId.id, new SimpleType('sim'), SimSupport.simRepo)

        def actorSimConfig = new SimConfigFactory().buildSim('localhost', '8080', 'simwar', simId, actorTypeName)
        storeConfig(actorSimConfig.toXML(), simAsset)

        Site site = new SimSiteFactory().buildSite(actorSimConfig, simId.id)
        OMElement siteEle = new SeparateSiteLoader().siteToXML(site)
        storeSite(siteEle, simAsset)

        return simAsset
    }

    static Asset sim(SimId simId) {
        return RepoUtils.assetIfAvailable(artifactId(simId), SimSupport.simRepo)
    }

    static SimHandle handle(SimId simId) {
        def simAsset = sim(simId)
        if (!simAsset) throw new NoSimulatorException("Simulator ${simId.id} does not exist.")
        def simHandle = new SimHandle()
        simHandle.simId = simId
        simHandle.simAsset = simAsset
        simHandle.siteAsset = simAsset.getChildByName(siteAssetName)
        simHandle.configAsset = simAsset.getChildByName(configAssetName)

        simHandle.eventLogAsset = simAsset.getChildByName(eventsAssetName)
        if (!simHandle.eventLogAsset) {
            Asset eventsAsset = RepoUtils.mkChild(eventsAssetName, simAsset)
            simAsset.addChild(eventsAsset)
            simHandle.eventLogAsset = eventsAsset
        }

        return simHandle
    }

    static runTransaction(String endpoint, String header, byte[] body) {
        def builder = new EndpointBuilder()
        builder.parse(endpoint)
        def transCode = builder.transCode
        def actorCode = builder.actorCode
        def simId = builder.simId

        SimHandle simHandle = handle(simId)
        def event = new EventFactory().buildEvent(SimSupport.simRepo, simHandle.eventLogAsset)
        simHandle.event = event

        // Registry inputs
        event.inOut.reqHdr = header
        event.inOut.reqBody = body
        event.init()

        // Lookup transaction implementation class
        def transactionType = new ActorTransactionTypeFactory().getTransactionType(transCode)
        simHandle.transactionType = transactionType
        def implClassName = transactionType.implementationClassName
        log.debug("implClassName is ${implClassName}")

        // build implementation
        Class<?> clazz = new SimUtils().class.classLoader.loadClass(implClassName)
        if (!clazz) throw new ToolkitRuntimeException("Class ${implClassName} cannot be loaded.")
        Object[] params = new Object[1]
        params[0] = simHandle
        Object<?> instance = clazz.newInstance(params)

        // call run() method
        try {
            instance.invokeMethod('run', null)
        } catch (Throwable t) {
            def actorTrans = actorCode + '/' + transCode
            event.fault = new Fault('Exception', FaultCode.Receiver.toString(), actorTrans, ExceptionUtil.exception_details(t))
            throw t
        }
    }

    static ArtifactId artifactId(SimId simId) { return new SimpleId(simId.id) }

    static def storeConfig(OMElement config, Asset simAsset) { store(config, configAssetName, simAsset) }
    static def storeSite(OMElement site, Asset simAsset) { store(site, siteAssetName, simAsset) }

    private static store(OMElement xmlEle, String name, Asset simAsset) {
        Asset configAsset = RepoUtils.mkChild(name, simAsset)
        configAsset.setContent(new OMFormatter(xmlEle).toString(), 'text/xml')
    }

}
