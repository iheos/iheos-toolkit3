package gov.nist.hit.ds.simSupport.simulator
import gov.nist.hit.ds.actorTransaction.TransactionType
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.eventLog.EventFactory
import gov.nist.hit.ds.eventLog.Fault
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.api.Repository
import gov.nist.hit.ds.repository.shared.data.AssetNode
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.config.SimConfig
import gov.nist.hit.ds.simSupport.endpoint.EndpointBuilder
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment
import groovy.util.logging.Log4j

/**
 * Created by bmajur on 7/5/14.
 */

@Log4j
class SimHandle {
    SimId simId
    Asset simAsset
    Asset siteAsset
    Asset configAsset
    Asset eventLogAsset
    Event event
    TransactionType transactionType
    Repository repository
    SimConfig actorSimConfig
    EndpointBuilder endpointBuilder
    SoapEnvironment soapEnvironment
    boolean requestIsMultipart = false
    boolean open = false
    def options = []
    boolean complete = false

    SimIdentifier getSimIdentifier() {
        new SimIdentifier(repository.id.idString, simId)
    }

    // Event creation is lazy so create doesn't build unused event
    Event getEvent() {
        if (!event) {
            log.debug("Creating event for sim ${simId}")
            def event1 = new EventFactory().buildEvent(repository, eventLogAsset)
            event = event1
            event.init()
        }
        return event
    }

    String getOption(String option) { options.find { String opt -> opt.startsWith(option)}}

    String getOptionFromMap(String option) {
        def m = [ : ]
        for(i=0; i<options.size()-1; i++)
            m.put(options[i], options[i+1])
        return m.get(option) as String
    }

    String getOptionFromMap(String option, options) {
        def m = [ : ]
        for(int i=0; i<options.size()-1; i++)
            m.put(options[i], options[i+1])
        log.debug("option map is ${m}")
        return m.get(option) as String
    }

    SimHandle fault(Fault fault) {
        event.fault = fault
        complete = true
        return this
    }

    AssetNode getAssetNode(String assetType) {
        AssetNode assetNode = new AssetNode()
        Asset a = event.eventAsset
        assetNode.assetId = a.id.idString
        assetNode.reposSrc = a.source
        assetNode.repId = a.repository.idString
        assetNode.type = assetType
        return assetNode
    }
}
